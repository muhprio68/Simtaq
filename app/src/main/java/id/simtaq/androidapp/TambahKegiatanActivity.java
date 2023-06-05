package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.simtaq.androidapp.helper.Preferences;

import static id.simtaq.androidapp.helper.config.formatSimpanTanggal;
import static id.simtaq.androidapp.helper.config.formatSimpanWaktu;
import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.url;

public class TambahKegiatanActivity extends AppCompatActivity {

    private RelativeLayout rlTambahKegiatan;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat timeFormatter;

    private Toolbar toolbar;
    private EditText etNamaKegiatan;
    private Spinner spTipeKegiatan;
    private EditText etTglKegiatan;
    private EditText etWaktuKegiatan;
    private EditText etTempatKegiatan;
    private EditText etPembicaraKegiatan;
    private EditText etDeskripsiKegiatan;
    private Button btnSimpanKegiatan;
    private Button btnBatal;

    private String namaKegiatan, tipeKegiatan, tglKegiatan, wktKegiatan, tempatKegiatan, pembicaraKegiatan, deskripsiKegiatan;
    private RequestQueue queue;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kegiatan);
        initViews();
        authToken = Preferences.getKeyToken(TambahKegiatanActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Kegiatan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        queue = Volley.newRequestQueue(TambahKegiatanActivity.this);
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", locale);
        etTglKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        etWaktuKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog();
            }
        });

        btnSimpanKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namaKegiatan = etNamaKegiatan.getText().toString();
                String valueSpinner = spTipeKegiatan.getSelectedItem().toString();
                tipeKegiatan = valueSpinner;
                tglKegiatan = etTglKegiatan.getText().toString();
                wktKegiatan = etWaktuKegiatan.getText().toString();
                tempatKegiatan = etTempatKegiatan.getText().toString();
                pembicaraKegiatan = etPembicaraKegiatan.getText().toString();
                deskripsiKegiatan = etDeskripsiKegiatan.getText().toString();

                if (TextUtils.isEmpty(namaKegiatan)) {
                    etNamaKegiatan.requestFocus();
                    etNamaKegiatan.setError("Masukkan nama kegiatan");
                } else if (TextUtils.isEmpty(tglKegiatan)){
                    etTglKegiatan.requestFocus();
                    etTglKegiatan.setError("Masukkan tanggal kegiatan");
                } else if (TextUtils.isEmpty(wktKegiatan)){
                    etWaktuKegiatan.requestFocus();
                    etWaktuKegiatan.setError("Masukkan waktu kegiatan");
                } else if (TextUtils.isEmpty(tempatKegiatan)){
                    etTempatKegiatan.requestFocus();
                    etTempatKegiatan.setError("Masukkan tempat kegiatan");
                } else if (TextUtils.isEmpty(pembicaraKegiatan)){
                    etPembicaraKegiatan.requestFocus();
                    etPembicaraKegiatan.setError("Masukkan pembicara kegiatan");
                } else {
                    tambahKegiatan(authToken, namaKegiatan, tipeKegiatan, formatSimpanTanggal(tglKegiatan),wktKegiatan,tempatKegiatan,pembicaraKegiatan,deskripsiKegiatan);
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNamaKegiatan.setText("");
                etTglKegiatan.setText("");
                etWaktuKegiatan.setText("");
                etTempatKegiatan.setText("");
                etPembicaraKegiatan.setText("");
                etDeskripsiKegiatan.setText("");
                Toast.makeText(TambahKegiatanActivity.this, "Tambah kegiatan dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbTambahKegiatan);
        rlTambahKegiatan = findViewById(R.id.rlTambahKegiatan);
        etNamaKegiatan = findViewById(R.id.etNamaKegiatan);
        spTipeKegiatan = findViewById(R.id.spTipeKegiatan);
        etTglKegiatan = findViewById(R.id.etTglKegiatan);
        etWaktuKegiatan = findViewById(R.id.etWaktuKegiatan);
        etTempatKegiatan = findViewById(R.id.etTempatKegiatan);
        etPembicaraKegiatan = findViewById(R.id.etPembicaraKegiatan);
        etDeskripsiKegiatan = findViewById(R.id.etDeskripsiKegiatan);
        btnSimpanKegiatan = findViewById(R.id.btnSimpanKegiatan);
        btnBatal = findViewById(R.id.btnBatalKegiatan);
    }

    private void tambahKegiatan(String token, String namaKegiatan, String tipeKegiatan, String tglKegiatan, String wktKegiatan, String tempatKegiatan, String pembicaraKegiatan, String deskripsiKegiatan) {
        StringRequest request = new StringRequest(Request.Method.POST, url+"/kegiatan", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    snackbarWithAction();

                    //Toast.makeText(TambahKegiatanActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // and setting data to edit text as empty
                etNamaKegiatan.setText("");
                etTglKegiatan.setText("");
                etWaktuKegiatan.setText("");
                etTempatKegiatan.setText("");
                etPembicaraKegiatan.setText("");
                etDeskripsiKegiatan.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(TambahKegiatanActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                // below line we are creating a map for storing
                // our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our
                // key and value pair to our parameters.
                params.put("nama_kegiatan", namaKegiatan);
                params.put("tipe_kegiatan", tipeKegiatan);
                params.put("tgl_kegiatan", tglKegiatan);
                params.put("waktu_kegiatan", wktKegiatan+":00");
                params.put("tempat_kegiatan", tempatKegiatan);
                params.put("pembicara_kegiatan", pembicaraKegiatan);
                params.put("deskripsi_kegiatan", deskripsiKegiatan);
                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void showDateDialog(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                etTglKegiatan.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    private void showTimeDialog() {


        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                etWaktuKegiatan.setText(formatSimpanWaktu(hourOfDay+":"+minute));
            }
        },

                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }

    public void snackbarWithAction(){
        Snackbar snackbar = Snackbar.make(rlTambahKegiatan ,"Data berhasil disimpan",Snackbar.LENGTH_SHORT);
        snackbar.setAction("Lihat", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lihatTambahData();
                //Toast.makeText(getApplicationContext(),"Lahhhh",Toast.LENGTH_SHORT).show();
            }
        });
        snackbar.show();
    }

    public void lihatTambahData() {
        Intent intent = new Intent(TambahKegiatanActivity.this, DetailKegiatanActivity.class);
        intent.putExtra("intentDari", "tambah kegiatan");
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}