package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahKegiatanActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etNamaKegiatan;
    private Spinner spTipeKegiatan;
    private EditText etTglKegiatan;
    private EditText etWaktuKegiatan;
    private EditText etTempatKegiatan;
    private EditText etPembicaraKegiatan;
    private EditText etDeskripsiKegiatan;
    private Button btnSimpanKegiatan;

    private String namaKegiatan, tipeKegiatan, tglKegiatan, wktKegiatan, tempatKegiatan, pembiacaraKegiatan, deskripsiKegiatan;

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

        btnSimpanKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbTambahKegiatan);
        etNamaKegiatan = findViewById(R.id.etNamaKegiatan);
        spTipeKegiatan = findViewById(R.id.spTipeKegiatan);
        etTglKegiatan = findViewById(R.id.etTglKegiatan);
        etWaktuKegiatan = findViewById(R.id.etWaktuKegiatan);
        etTempatKegiatan = findViewById(R.id.etTempatKegiatan);
        etPembicaraKegiatan = findViewById(R.id.etPembicaraKegiatan);
        etDeskripsiKegiatan = findViewById(R.id.etDeskripsiKegiatan);
        btnSimpanKegiatan = findViewById(R.id.btnSimpanKegiatan);
    }

    private void addDataToDatabase(String namaKegiatan, String tipeKegiatan, String tglKegiatan, String wktKegiatan, String tempatKegiatan, String pembiacaraKegiatan, String deskripsiKegiatan) {

        // url to post our data

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(TambahKegiatanActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    Toast.makeText(TambahKegiatanActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("waktu_kegiatan", wktKegiatan);
                params.put("tempat_kegiatan", tempatKegiatan);
                params.put("pembicara_kegiatan", pembiacaraKegiatan);
                params.put("deskripsi_kegiatan", deskripsiKegiatan);

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
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