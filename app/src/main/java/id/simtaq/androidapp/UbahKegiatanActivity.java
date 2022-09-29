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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.url;

public class UbahKegiatanActivity extends AppCompatActivity {

    private RelativeLayout rlUbahKegiatan;

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
    private Button btnUbahKegiatan;
    private Button btnBatalUbah;

    private int idKegiatan;
    private String namaKegiatan, tipeKegiatan, tglKegiatan, wktKegiatan, tempatKegiatan, pembicaraKegiatan, deskripsiKegiatan;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_kegiatan);
        initViews();
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Ubah Kegiatan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        idKegiatan = getIntent().getIntExtra("idKegiatan",0);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", locale);
        queue = Volley.newRequestQueue(UbahKegiatanActivity.this);
        getDataUbahKegiatan();
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

        btnUbahKegiatan.setOnClickListener(new View.OnClickListener() {
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
                    ubahKegiatan(idKegiatan,namaKegiatan, tipeKegiatan, tglKegiatan,wktKegiatan,tempatKegiatan,pembicaraKegiatan,deskripsiKegiatan);
                }
            }
        });

        btnBatalUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNamaKegiatan.setText("");
                etTglKegiatan.setText("");
                etWaktuKegiatan.setText("");
                etTempatKegiatan.setText("");
                etPembicaraKegiatan.setText("");
                etDeskripsiKegiatan.setText("");
                Toast.makeText(UbahKegiatanActivity.this, "Perubahan kegiatan dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbUbahKegiatan);
        rlUbahKegiatan = findViewById(R.id.rlUbahKegiatan);
        etNamaKegiatan = findViewById(R.id.etNamaKegiatan);
        spTipeKegiatan = findViewById(R.id.spTipeKegiatan);
        etTglKegiatan = findViewById(R.id.etTglKegiatan);
        etWaktuKegiatan = findViewById(R.id.etWaktuKegiatan);
        etTempatKegiatan = findViewById(R.id.etTempatKegiatan);
        etPembicaraKegiatan = findViewById(R.id.etPembicaraKegiatan);
        etDeskripsiKegiatan = findViewById(R.id.etDeskripsiKegiatan);
        btnUbahKegiatan = findViewById(R.id.btnSimpanUbahKegiatan);
        btnBatalUbah = findViewById(R.id.btnBatalUbahKegiatan);
    }

    public void getDataUbahKegiatan(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/kegiatan/"+idKegiatan, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //pbDetailKegiatan.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(0);
                        etNamaKegiatan.setText(responseObj.getString("nama_kegiatan"));
                        if (responseObj.getString("tipe_kegiatan").equals("Umum")){
                            spTipeKegiatan.setSelection(0);
                        } else{
                            spTipeKegiatan.setSelection(1);
                        }
                        etTglKegiatan.setText(responseObj.getString("tgl_kegiatan"));
                        etWaktuKegiatan.setText(formatWaktu(responseObj.getString("waktu_kegiatan")));
                        etTempatKegiatan.setText(responseObj.getString("tempat_kegiatan"));
                        etPembicaraKegiatan.setText(responseObj.getString("pembicara_kegiatan"));
                        etDeskripsiKegiatan.setText(responseObj.getString("deskripsi_kegiatan"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UbahKegiatanActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void ubahKegiatan(int idKegiatan, String namaKegiatan, String tipeKegiatan, String tglKegiatan, String wktKegiatan, String tempatKegiatan, String pembicaraKegiatan, String deskripsiKegiatan) {
        RequestQueue queue = Volley.newRequestQueue(UbahKegiatanActivity.this);

        StringRequest request = new StringRequest(Request.Method.PUT, url+"/kegiatan/"+idKegiatan, new com.android.volley.Response.Listener<String>() {
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
//                etNamaKegiatan.setText("");
//                etTglKegiatan.setText("");
//                etWaktuKegiatan.setText("");
//                etTempatKegiatan.setText("");
//                etPembicaraKegiatan.setText("");
//                etDeskripsiKegiatan.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(UbahKegiatanActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("tipe_kegiatan", tipeKegiatan);
                params.put("tgl_kegiatan", tglKegiatan);
                params.put("waktu_kegiatan", wktKegiatan+":00");
                params.put("tempat_kegiatan", tempatKegiatan);
                params.put("pembicara_kegiatan", pembicaraKegiatan);
                params.put("deskripsi_kegiatan", deskripsiKegiatan);
                params.put("update_at", getCurentDate());
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

                etWaktuKegiatan.setText(timeFormat(hourOfDay+":"+minute));
            }
        },

                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
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
        Snackbar snackbar = Snackbar.make(rlUbahKegiatan ,"Data berhasil diubah",Snackbar.LENGTH_SHORT);
        snackbar.setAction("Lihat", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lihatUbahData();
                //Toast.makeText(getApplicationContext(),"Lahhhh",Toast.LENGTH_SHORT).show();
            }
        });
        snackbar.show();
    }

    private String inputDateFormat(String tanggal){
        String tgl = tanggal;
        Locale locale = new Locale("in", "ID");
        java.text.DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", locale);//"dd/MM/yyyy" "yyyy-MM-dd"
        Date date = null;
        try {
            date = (Date)formatter.parse(tgl);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", locale);
        String tglBaru = newFormat.format(date);
        return tglBaru;
    }

    private String formatWaktu(String waktu){
        String wkt = waktu;
        Locale locale = new Locale("in", "ID");
        java.text.DateFormat formatter = new SimpleDateFormat("hh:mm:ss", locale);
        Date date = null;
        try {
            date = (Date)formatter.parse(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("hh:mm", locale);
        String wktBaru = newFormat.format(date);
        return wktBaru;
    }

    public void lihatUbahData() {
        Intent intent = new Intent(UbahKegiatanActivity.this, DetailKegiatanActivity.class);
        intent.putExtra("intentDari", "tambah kegiatan");
        startActivity(intent);
        finish();
    }

    public String getCurentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //System.out.println(dateFormat.format(cal.getTime()));
        return dateFormat.format(cal.getTime());
    }

}