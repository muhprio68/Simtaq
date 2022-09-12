package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import id.simtaq.androidapp.adapter.JadwalKegiatanAdapter;
import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.models.CalendarModel;
import id.simtaq.androidapp.models.Kegiatan;
import id.simtaq.androidapp.models.RiwayatKas;

public class JadwalKegiatanActivity extends AppCompatActivity implements JadwalKegiatanAdapter.IJadwalKegiatanAdapter {

    private Toolbar toolbar;
    private ArrayList<Kegiatan> kegiatanList;
    private JadwalKegiatanAdapter adapter;

    private TextView tvFilterBulanKegiatan;
    private TextView tvFilterTahunKegiatan;
    private RecyclerView rvJadwalKegiatan;

    String url = "http://192.168.0.27:8080/restfulapi/public/kegiatan";
    //https://run.mocky.io/v3/3d965384-7078-4ee5-8209-a71a4dfc02c0
    private ProgressBar pbJadwalKegiatan;

    public JadwalKegiatanActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_kegiatan);
        initViews();
        //addData();
        kegiatanList = new ArrayList<>();
        getData();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Jadwal Kegiatan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        buildRecyclerView();
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbJadwalKegiatan);
        pbJadwalKegiatan = findViewById(R.id.pbJadwalKegiatan);
        tvFilterBulanKegiatan = findViewById(R.id.tvFilterBulanKegiatan);
        tvFilterTahunKegiatan = findViewById(R.id.tvFilterTahunKegiatan);
        rvJadwalKegiatan = findViewById(R.id.rvJadwalKegiatan);
    }

    public void addData(){
        kegiatanList = new ArrayList<>();
        kegiatanList.add(new Kegiatan("KEG00001", true, "Pengajian Rutin", "22/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","KH.Abdul Kholiq Hasan, M.HI."));
        kegiatanList.add(new Kegiatan("KEG00002", true, "Sholawat Diba' Rutin", "22/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. Suhardiman"));
        kegiatanList.add(new Kegiatan("KEG00003", false, "Rapat takmir", "18/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. H.M.Supeno"));
        kegiatanList.add(new Kegiatan("KEG00004", true, "Istighosah Rutin", "25/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. H.M.Supeno"));
        kegiatanList.add(new Kegiatan("KEG00005", true, "Sholawat Diba'", "29/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. M. Khoirul Huda"));
    }

    public void getData(){
        RequestQueue queue = Volley.newRequestQueue(JadwalKegiatanActivity.this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbJadwalKegiatan.setVisibility(View.GONE);
                rvJadwalKegiatan.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        String idKegiatan = responseObj.getString("id_kegiatan");
                        String namaKegiatan = responseObj.getString("nama_kegiatan");
                        String kegiatanUmum = responseObj.getString("kegiatan_umum");
                        boolean isUmum;
                        if (kegiatanUmum == "0"){
                            isUmum = false;
                        }else{
                            isUmum = true;
                        }
                        String tglKegiatan = responseObj.getString("tgl_kegiatan");
                        String waktuKegiatan = responseObj.getString("waktu_kegiatan");
                        String tempatKegiatan = responseObj.getString("tempat_kegiatan");
                        String pembicaraKegiatan = responseObj.getString("pembicara_kegiatan");
                        String deskripsiKegiatan = responseObj.getString("deskripsi_kegiatan");
                        kegiatanList.add(new Kegiatan(idKegiatan, isUmum, namaKegiatan, tglKegiatan, waktuKegiatan, tempatKegiatan, pembicaraKegiatan, deskripsiKegiatan));
                        buildRecyclerView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(JadwalKegiatanActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void buildRecyclerView(){
        adapter = new JadwalKegiatanAdapter(JadwalKegiatanActivity.this,kegiatanList, 1, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvJadwalKegiatan.setHasFixedSize(true);
        rvJadwalKegiatan.setLayoutManager(manager);
        rvJadwalKegiatan.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void doClick(String id) {
        Intent intent = new Intent(JadwalKegiatanActivity.this, DetailKegiatanActivity.class);
        intent.putExtra("idKegiatan", id);
        startActivity(intent);
    }
}