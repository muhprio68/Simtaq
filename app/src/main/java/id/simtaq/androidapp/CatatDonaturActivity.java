package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.helper.Preferences;

import static id.simtaq.androidapp.helper.config.formatSimpanTanggal;
import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.url;

public class CatatDonaturActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RelativeLayout rlCatatDonatur;
    private ProgressBar pbCatatDonatur;
    private EditText etTglDonatur;
    private Spinner spWilayahDonatur;
    private EditText etPetugasDonatur;
    private EditText etNominalDonatur;
    private Button btnSimpanDonatur;
    private Button btnBatalDonatur;

    private String tglDonatur, wilDonatur, petugasDonatur, nominalDonatur;

    private RequestQueue queue;
    private String authToken;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String jmlSaldo;
    private String[] petugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catat_donatur);
        initViews();
        authToken = Preferences.getKeyToken(CatatDonaturActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Catat Donatur");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        queue = Volley.newRequestQueue(CatatDonaturActivity.this);
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", locale);
        initPetugas();


        etTglDonatur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        spWilayahDonatur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                etPetugasDonatur.setText(petugas[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSimpanDonatur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tglDonatur = etTglDonatur.getText().toString();
                String valueSpinner = spWilayahDonatur.getSelectedItem().toString();
                wilDonatur = valueSpinner;
                petugasDonatur = etPetugasDonatur.getText().toString();
                nominalDonatur = etNominalDonatur.getText().toString();

                if (TextUtils.isEmpty(tglDonatur)) {
                    etTglDonatur.requestFocus();
                    etTglDonatur.setError("Masukkan tanggal");
                }  else if (TextUtils.isEmpty(nominalDonatur)){
                    etNominalDonatur.requestFocus();
                    etNominalDonatur.setError("Masukkan nominal");
                } else {
                    tambahDataDonatur(authToken, formatSimpanTanggal(tglDonatur), wilDonatur, petugasDonatur, nominalDonatur);
                }
            }
        });

        btnBatalDonatur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etTglDonatur.setText("");
                etNominalDonatur.setText("");
                etPetugasDonatur.setText("");
                Toast.makeText(CatatDonaturActivity.this, "Catat donatur dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbCatatDonatur);
        rlCatatDonatur = findViewById(R.id.rlCatatDonatur);
        pbCatatDonatur = findViewById(R.id.pbCatatDonatur);
        etTglDonatur = findViewById(R.id.etTanggalDonatur);
        spWilayahDonatur = findViewById(R.id.spWilayahDonatur);
        etNominalDonatur = findViewById(R.id.etNominalDonatur);
        etPetugasDonatur = findViewById(R.id.etPetugasDonatur);
        btnSimpanDonatur = findViewById(R.id.btnSimpanDonatur);
        btnBatalDonatur = findViewById(R.id.btnBatalDonatur);
    }

    public void initPetugas(){
        petugas = new String[5];
        petugas[0] = "Bpk. Suhardiman";
        petugas[1] = "Bpk. Supadi";
        petugas[2] = "Bpk. a";
        petugas[3] = "Bpk. b";
        petugas[4] = "Bpk. c";
    }

    private void tambahDataDonatur(String token, String tglDonatur, String wilDonatur, String petugasDonatur, String nominalDonatur) {
        StringRequest request = new StringRequest(Request.Method.POST, url+"/donatur", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbCatatDonatur.setVisibility(View.GONE);
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
                etTglDonatur.setText("");
                etPetugasDonatur.setText("");
                etNominalDonatur.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(CatatDonaturActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("tgl_donatur", tglDonatur);
                params.put("wilayah_donatur", wilDonatur);
                params.put("petugas_donatur", petugasDonatur);
                params.put("nominal_donatur", nominalDonatur);

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

                etTglDonatur.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    public void snackbarWithAction(){
        Snackbar snackbar = Snackbar.make(rlCatatDonatur ,"Data berhasil disimpan",Snackbar.LENGTH_SHORT);
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
        Intent intent = new Intent(CatatDonaturActivity.this, DetailKeuanganActivity.class);
        intent.putExtra("intentDari", "catat keuangan");
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