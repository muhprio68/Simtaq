package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.simtaq.androidapp.helper.Preferences;

import static id.simtaq.androidapp.helper.config.formatLihatFullTanggal;
import static id.simtaq.androidapp.helper.config.formatLihatWaktu;
import static id.simtaq.androidapp.helper.config.url;

public class DetailKegiatanActivity extends AppCompatActivity {

    private Toolbar toolbar;
    TextView tvDetailNamaKegiatan;
    TextView tvNoKegiatan;
    TextView tvTipeKegiatan;
    TextView tvTglKegiatan;
    TextView tvWaktuKegiatan;
    TextView tvTempatKegiatan;
    TextView tvPembicaraKegiatan;
    TextView tvDeskripsiKegiatan;
    ProgressBar pbDetailKegiatan;
    int idKegiatan;
    String intentDari;

    private RequestQueue queue;
    private String authToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kegiatan);
        initViews();
        authToken = Preferences.getKeyToken(DetailKegiatanActivity.this);
        queue = Volley.newRequestQueue(DetailKegiatanActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Kegiatan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        intentDari = String.valueOf(getIntent().getStringExtra("intentDari"));
        if (intentDari.equals("tambah kegiatan")){
            lihatTambah(authToken);
        } else {
            idKegiatan = Integer.valueOf(getIntent().getStringExtra("idKegiatan"));
            getData(authToken);
        }

    }

    public void initViews(){
        toolbar = findViewById(R.id.tbDetailKegiatan);
        pbDetailKegiatan = findViewById(R.id.pbDetailKegiatan);
        tvDetailNamaKegiatan = findViewById(R.id.tvValueNamaKegiatan);
        tvNoKegiatan = findViewById(R.id.tvValueNoKegiatan);
        tvTipeKegiatan = findViewById(R.id.tvValueDetailTipeKegiatan);
        tvTglKegiatan = findViewById(R.id.tvValueHariTglKegiatan);
        tvWaktuKegiatan = findViewById(R.id.tvValueDetailWaktuKegiatan);
        tvTempatKegiatan = findViewById(R.id.tvValueDetailTempatKegiatan);
        tvPembicaraKegiatan = findViewById(R.id.tvValueDetailPembicaraKegiatan);
        tvDeskripsiKegiatan =findViewById(R.id.tvValueDetailDeskripsiKegiatan);
    }

    public void getData(String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/kegiatan/"+idKegiatan, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbDetailKegiatan.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(0);
                        tvNoKegiatan.setText(responseObj.getString("no_kegiatan"));
                        tvDetailNamaKegiatan.setText(responseObj.getString("nama_kegiatan"));
                        tvTipeKegiatan.setText(responseObj.getString("tipe_kegiatan"));
                        tvTglKegiatan.setText(formatLihatFullTanggal(responseObj.getString("tgl_kegiatan")));
                        tvWaktuKegiatan.setText(formatLihatWaktu(responseObj.getString("waktu_kegiatan"))+" WIB");
                        tvTempatKegiatan.setText(responseObj.getString("tempat_kegiatan"));
                        tvPembicaraKegiatan.setText(responseObj.getString("pembicara_kegiatan"));
                        tvDeskripsiKegiatan.setText(responseObj.getString("deskripsi_kegiatan"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailKegiatanActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void lihatTambah(String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/kegiatan", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbDetailKegiatan.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(response.length()-1);
                        tvNoKegiatan.setText(responseObj.getString("no_kegiatan"));
                        tvDetailNamaKegiatan.setText(responseObj.getString("nama_kegiatan"));
                        tvTipeKegiatan.setText(responseObj.getString("tipe_kegiatan"));
                        tvTglKegiatan.setText(formatLihatFullTanggal(responseObj.getString("tgl_kegiatan")));
                        tvWaktuKegiatan.setText(formatLihatWaktu(responseObj.getString("waktu_kegiatan"))+" WIB");
                        tvTempatKegiatan.setText(responseObj.getString("tempat_kegiatan"));
                        tvPembicaraKegiatan.setText(responseObj.getString("pembicara_kegiatan"));
                        tvDeskripsiKegiatan.setText(responseObj.getString("deskripsi_kegiatan"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailKegiatanActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ubah_kegiatan:
                Intent intent = new Intent(DetailKegiatanActivity.this, UbahKegiatanActivity.class);
                intent.putExtra("idKegiatan", idKegiatan);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}