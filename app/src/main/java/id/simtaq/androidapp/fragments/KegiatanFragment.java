package id.simtaq.androidapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import id.simtaq.androidapp.JadwalKegiatanActivity;
import id.simtaq.androidapp.R;
import id.simtaq.androidapp.RiwayatActivity;
import id.simtaq.androidapp.adapter.JadwalKegiatanAdapter;
import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.models.CalendarModel;
import id.simtaq.androidapp.models.Kegiatan;

import static id.simtaq.androidapp.helper.config.url;

public class KegiatanFragment extends Fragment implements View.OnClickListener, JadwalKegiatanAdapter.IJadwalKegiatanAdapter {

    private ArrayList<Kegiatan> kegiatanList;
    private RecyclerView rvKegiatan;
    private ProgressBar pbInfoKegiatan;
    private TextView tvLihatSemuaKegiatan;
    private JadwalKegiatanAdapter adapter;
    private RequestQueue queue;
    private RelativeLayout rlMenuKegiatan;

    public KegiatanFragment() {

    }

    public static KegiatanFragment newInstance(String param1, String param2) {
        KegiatanFragment fragment = new KegiatanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_kegiatan, container, false);
        initViews(view);
        tvLihatSemuaKegiatan.setOnClickListener(this);
        kegiatanList = new ArrayList<>();
        queue = Volley.newRequestQueue(view.getContext());
//        addData();
        getData(view);
        buildRecyclerView(view);
        return view;
    }

    public void initViews(View v){
        rlMenuKegiatan = v.findViewById(R.id.rlMenuKegiatan);
        pbInfoKegiatan = v.findViewById(R.id.pbInfoKegiatan);
        rvKegiatan = v.findViewById(R.id.rvKegiatan);
        tvLihatSemuaKegiatan = v.findViewById(R.id.tvLihatSemuaKegiatan);
    }

    public void addData(){
        kegiatanList.add(new Kegiatan("KEG00001", true, "Pengajian Rutin", "22/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","KH.Abdul Kholiq Hasan, M.HI."));
        kegiatanList.add(new Kegiatan("KEG00002", true, "Sholawat Diba' Rutin", "22/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. Suhardiman"));
        kegiatanList.add(new Kegiatan("KEG00003", false, "Rapat takmir", "18/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. H.M.Supeno"));
        kegiatanList.add(new Kegiatan("KEG00004", true, "Istighosah Rutin", "25/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. H.M.Supeno"));
        kegiatanList.add(new Kegiatan("KEG00005", true, "Sholawat Diba'", "29/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. M. Khoirul Huda"));
    }

    public void getData(View view){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/kegiatan", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbInfoKegiatan.setVisibility(View.GONE);
                rvKegiatan.setVisibility(View.VISIBLE);
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
                        buildRecyclerView(view);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void buildRecyclerView(View view) {
        adapter = new JadwalKegiatanAdapter(view.getContext(), kegiatanList, 2, this, queue, rlMenuKegiatan);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvKegiatan.setHasFixedSize(true);
        rvKegiatan.setLayoutManager(manager);
        rvKegiatan.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v == tvLihatSemuaKegiatan){
            startActivity(new Intent(v.getContext(), JadwalKegiatanActivity.class));
        }
    }

    @Override
    public void doClick(String id) {

    }
}

