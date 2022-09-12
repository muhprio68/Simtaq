package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Locale;

import id.simtaq.androidapp.models.Kegiatan;

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
    String url = "http://192.168.0.27:8080/restfulapi/public/kegiatan";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kegiatan);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Kegiatan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        intentDari = String.valueOf(getIntent().getStringExtra("intentDari"));
        if (intentDari.equals("tambah kegiatan")){
            lihatTambah();
        } else {
            idKegiatan = Integer.valueOf(getIntent().getStringExtra("idKegiatan"));
            getData();
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

    public void getData(){
        RequestQueue queue = Volley.newRequestQueue(DetailKegiatanActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/"+idKegiatan, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbDetailKegiatan.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(0);
                        tvDetailNamaKegiatan.setText(responseObj.getString("nama_kegiatan"));
                        tvNoKegiatan.setText(responseObj.getString("id_kegiatan"));
                        if (responseObj.getString("kegiatan_umum").equals("0")){
                            tvTipeKegiatan.setText("Undangan");
                        } else{
                            tvTipeKegiatan.setText("Umum");
                        }
                        tvTglKegiatan.setText(fullDatePlusDay(responseObj.getString("tgl_kegiatan")));
                        tvWaktuKegiatan.setText(formatWaktu(responseObj.getString("waktu_kegiatan"))+" WIB");
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
        });
        queue.add(jsonArrayRequest);
    }

    public void lihatTambah(){
        RequestQueue queue = Volley.newRequestQueue(DetailKegiatanActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbDetailKegiatan.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(response.length()-1);
                        tvDetailNamaKegiatan.setText(responseObj.getString("nama_kegiatan"));
                        tvNoKegiatan.setText(responseObj.getString("id_kegiatan"));
                        if (responseObj.getString("kegiatan_umum").equals("0")){
                            tvTipeKegiatan.setText("Undangan");
                        } else{
                            tvTipeKegiatan.setText("Umum");
                        }
                        tvTglKegiatan.setText(fullDatePlusDay(responseObj.getString("tgl_kegiatan")));
                        tvWaktuKegiatan.setText(formatWaktu(responseObj.getString("waktu_kegiatan"))+" WIB");
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
        });
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

    private String fullDatePlusDay(String tanggal){
        String tgl = tanggal;
        Locale locale = new Locale("in", "ID");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", locale);//"dd/MM/yyyy" "yyyy-MM-dd"
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
        DateFormat formatter = new SimpleDateFormat("hh:mm:ss", locale);
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
}