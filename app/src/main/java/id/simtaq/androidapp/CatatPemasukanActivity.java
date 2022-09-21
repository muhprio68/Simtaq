package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.urlKeuangan;
import static id.simtaq.androidapp.helper.config.urlSaldo;

public class CatatPemasukanActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RelativeLayout rlCatatPemasukan;
    private EditText etTglPemasukan;
    private EditText etKetPemasukan;
    private EditText etNominalPemasukan;
    private EditText etDeskripPemasukan;
    private Button btnSimpanPemasukan;
    private Button btnBatalPemasukan;

    private String tglPemasukan, ketPemasukan, nominalPemasukan, deskripPemasukan;

    private RequestQueue queue;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String jmlSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catat_pemasukan);
        initViews();
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Catat Pemasukan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        queue = Volley.newRequestQueue(CatatPemasukanActivity.this);
        getSaldo();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", locale);

        etTglPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        btnSimpanPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tglPemasukan = etTglPemasukan.getText().toString();
                ketPemasukan = etKetPemasukan.getText().toString();
                nominalPemasukan = etNominalPemasukan.getText().toString();
                deskripPemasukan = etDeskripPemasukan.getText().toString();

                if (TextUtils.isEmpty(tglPemasukan)) {
                    etTglPemasukan.setError("Masukkan nama kegiatan");
                } else if (TextUtils.isEmpty(ketPemasukan)){
                    etKetPemasukan.setError("Masukkan tanggal kegiatan");
                } else if (TextUtils.isEmpty(nominalPemasukan)){
                    etNominalPemasukan.setError("Masukkan waktu kegiatan");
                } else {
                    tambahDataPemasukan(tglPemasukan, ketPemasukan, nominalPemasukan, deskripPemasukan);
                }
            }
        });

        btnBatalPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etTglPemasukan.setText("");
                etKetPemasukan.setText("");
                etNominalPemasukan.setText("");
                etDeskripPemasukan.setText("");
                Toast.makeText(CatatPemasukanActivity.this, "Catat Pemasukan dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbCatatPemasukan);
        rlCatatPemasukan = findViewById(R.id.rlCatatPemasukan);
        etTglPemasukan = findViewById(R.id.etTanggalPemasukan);
        etKetPemasukan = findViewById(R.id.etKeteranganPemasukan);
        etNominalPemasukan = findViewById(R.id.etNominalPemasukan);
        etDeskripPemasukan = findViewById(R.id.etDeskripsiPemasukan);
        btnSimpanPemasukan = findViewById(R.id.btnSimpanPemasukan);
        btnBatalPemasukan = findViewById(R.id.btnBatalPemasukan);
    }

    private void tambahDataPemasukan(String tglPemasukan, String ketPemasukan, String nominalPemasukan, String deskripPemasukan) {
        StringRequest request = new StringRequest(Request.Method.POST, urlKeuangan, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    //snackbarWithAction();

                    //Toast.makeText(TambahKegiatanActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // and setting data to edit text as empty
                etTglPemasukan.setText("");
                etKetPemasukan.setText("");
                etNominalPemasukan.setText("");
                etDeskripPemasukan.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(CatatPemasukanActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("no_keuangan", "PEN-220921001");
                params.put("tipe_keuangan", "Pemasukan");
                params.put("tgl_keuangan", tglPemasukan);
                params.put("keterangan_keuangan", ketPemasukan);
                params.put("nominal_keuangan", nominalPemasukan);
                params.put("jml_kas_awal", jmlSaldo);
                int jmlKasAkhir = Integer.parseInt(jmlSaldo)+Integer.parseInt(nominalPemasukan);
                params.put("jml_kas_akhir", jmlKasAkhir+"");
                params.put("deskripsi_keuangan", deskripPemasukan);
                params.put("create_at", getCurentDate());
                params.put("update_at", getCurentDate());
                ubahSaldo(jmlKasAkhir+"");

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    public void getSaldo(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlSaldo, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //pbJadwalKegiatan.setVisibility(View.GONE);
                //rvJadwalKegiatan.setVisibility(View.VISIBLE);
                try {
                    JSONObject responseObj = response.getJSONObject(0);
                    jmlSaldo = responseObj.getString("jml_saldo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CatatPemasukanActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void ubahSaldo(String jmlSaldo) {
        StringRequest request = new StringRequest(Request.Method.PUT, urlSaldo+"/1", new com.android.volley.Response.Listener<String>() {
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
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(CatatPemasukanActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("jml_saldo", jmlSaldo);
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

                etTglPemasukan.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    public void snackbarWithAction(){
        Snackbar snackbar = Snackbar.make(rlCatatPemasukan ,"Data berhasil disimpan",Snackbar.LENGTH_SHORT);
        snackbar.setAction("Lihat", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lihatTambahData();
                //Toast.makeText(getApplicationContext(),"Lahhhh",Toast.LENGTH_SHORT).show();
            }
        });
        snackbar.show();
    }

    public String getCurentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //System.out.println(dateFormat.format(cal.getTime()));
        return dateFormat.format(cal.getTime());
    }

    public void lihatTambahData() {
        Intent intent = new Intent(CatatPemasukanActivity.this, DetailRiwayatKasActivity.class);
        intent.putExtra("intentDari", "catat Pemasukan");
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