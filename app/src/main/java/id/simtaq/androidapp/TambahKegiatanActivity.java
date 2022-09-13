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
    String url = "http://192.168.0.27:8080/restfulapi/public/kegiatan";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kegiatan);
        initViews();
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Tambah Kegiatan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
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
                if (valueSpinner.equals("Umum")){
                    tipeKegiatan = "1";
                } else {
                    tipeKegiatan = "0";
                }
                tglKegiatan = etTglKegiatan.getText().toString();
                wktKegiatan = etWaktuKegiatan.getText().toString();
                tempatKegiatan = etTempatKegiatan.getText().toString();
                pembicaraKegiatan = etPembicaraKegiatan.getText().toString();
                deskripsiKegiatan = etDeskripsiKegiatan.getText().toString();

                if (TextUtils.isEmpty(namaKegiatan)) {
                    etNamaKegiatan.setError("Masukkan nama kegiatan");
                } else if (TextUtils.isEmpty(tglKegiatan)){
                    etTglKegiatan.setError("Masukkan tanggal kegiatan");
                } else if (TextUtils.isEmpty(wktKegiatan)){
                    etWaktuKegiatan.setError("Masukkan waktu kegiatan");
                } else if (TextUtils.isEmpty(tempatKegiatan)){
                    etTempatKegiatan.setError("Masukkan tempat kegiatan");
                } else if (TextUtils.isEmpty(pembicaraKegiatan)){
                    etPembicaraKegiatan.setError("Masukkan pembicara kegiatan");
                } else {
                    addDataToDatabase(namaKegiatan, tipeKegiatan, tglKegiatan,wktKegiatan,tempatKegiatan,pembicaraKegiatan,deskripsiKegiatan);
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
                Toast.makeText(TambahKegiatanActivity.this, "Tambah keegiatan dibatalkan", Toast.LENGTH_SHORT).show();
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

    private void addDataToDatabase(String namaKegiatan, String tipeKegiatan, String tglKegiatan, String wktKegiatan, String tempatKegiatan, String pembicaraKegiatan, String deskripsiKegiatan) {
        RequestQueue queue = Volley.newRequestQueue(TambahKegiatanActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
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
                params.put("kegiatan_umum", tipeKegiatan);
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

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                etTglKegiatan.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    private void showTimeDialog() {

        /**
         * Calendar untuk mendapatkan waktu saat ini
         */
        Calendar calendar = Calendar.getInstance();

        /**
         * Initialize TimePicker Dialog
         */
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */

                etWaktuKegiatan.setText(timeFormat(hourOfDay+":"+minute));
            }
        },
                /**
                 * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
                 */
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),

                /**
                 * Cek apakah format waktu menggunakan 24-hour format
                 */
                DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }

    private String timeFormat(String waktu){
        String wkt = waktu;
        Locale locale = new Locale("in", "ID");
        java.text.DateFormat formatter = new SimpleDateFormat("hh:mm", locale); //dd/MM/yyyy  yyyy-MM-dd
        Date date = null;
        try {
            date = (Date)formatter.parse(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm", locale);
        String wktBaru = newFormat.format(date);
        return wktBaru;
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