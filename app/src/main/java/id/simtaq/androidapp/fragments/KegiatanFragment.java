package id.simtaq.androidapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.simtaq.androidapp.CatatDonaturActivity;
import id.simtaq.androidapp.JadwalKegiatanActivity;
import id.simtaq.androidapp.R;
import id.simtaq.androidapp.RiwayatActivity;
import id.simtaq.androidapp.adapter.JadwalKegiatanAdapter;
import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.helper.Preferences;
import id.simtaq.androidapp.models.CalendarModel;
import id.simtaq.androidapp.models.Kegiatan;

import static id.simtaq.androidapp.helper.config.url;

public class KegiatanFragment extends Fragment implements View.OnClickListener, JadwalKegiatanAdapter.IJadwalKegiatanAdapter {

    private ArrayList<Kegiatan> kegiatanList;
    private RecyclerView rvKegiatan;
    private ProgressBar pbInfoKegiatan;
    private TextView tvLihatSemuaKegiatan;
    private TextView tvTambahData;
    private JadwalKegiatanAdapter adapter;
    private RequestQueue queue;
    private String authToken;
    private ConstraintLayout clViewInfoKegiatan, clMenuKegiatan, clTambahKegiatan;

    private String level;

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
        level = Preferences.getKeyLevel(view.getContext());
        authToken = Preferences.getKeyToken(view.getContext());
        clViewInfoKegiatan.setVisibility(View.GONE);
        tvLihatSemuaKegiatan.setOnClickListener(this);
        kegiatanList = new ArrayList<>();
        queue = Volley.newRequestQueue(view.getContext());
//        addData();
        getData(view, authToken);
        buildRecyclerView(view);
        aksesLevel(level);
        return view;
    }

    public void initViews(View v){
        clMenuKegiatan = v.findViewById(R.id.clMenuKegiatan);
        pbInfoKegiatan = v.findViewById(R.id.pbInfoKegiatan);
        rvKegiatan = v.findViewById(R.id.rvKegiatan);
        tvLihatSemuaKegiatan = v.findViewById(R.id.tvLihatSemuaKegiatan);
        tvTambahData = v.findViewById(R.id.tvTambahDataKegiatan);
        clTambahKegiatan = v.findViewById(R.id.clTambahKegiatan);
        clViewInfoKegiatan = v.findViewById(R.id.clViewInfoKegiatan);
    }

    public void getData(View view, String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/kegiatan", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbInfoKegiatan.setVisibility(View.GONE);
                clViewInfoKegiatan.setVisibility(View.VISIBLE);
                //rvKegiatan.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        String idKegiatan = responseObj.getString("id_kegiatan");
                        String noKegiatan = responseObj.getString("no_kegiatan");
                        String namaKegiatan = responseObj.getString("nama_kegiatan");
                        String tipeKegiatan = responseObj.getString("tipe_kegiatan");
                        String tglKegiatan = responseObj.getString("tgl_kegiatan");
                        String waktuKegiatan = responseObj.getString("waktu_kegiatan");
                        String tempatKegiatan = responseObj.getString("tempat_kegiatan");
                        String pembicaraKegiatan = responseObj.getString("pembicara_kegiatan");
                        String deskripsiKegiatan = responseObj.getString("deskripsi_kegiatan");
                        String createAt = responseObj.getString("create_at");
                        String updateAt = responseObj.getString("update_at");
                        kegiatanList.add(new Kegiatan(idKegiatan, noKegiatan, namaKegiatan, tipeKegiatan, tglKegiatan, waktuKegiatan, tempatKegiatan, pembicaraKegiatan, deskripsiKegiatan, createAt, updateAt));
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

    public void buildRecyclerView(View view) {
        adapter = new JadwalKegiatanAdapter(authToken, view.getContext(), kegiatanList, 2, this, queue, clMenuKegiatan);
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

    private void aksesLevel(String level){
        if (level.equals("1") || level.equals("2")) {
            clTambahKegiatan.setVisibility(View.GONE);
            tvTambahData.setVisibility(View.GONE);
        }
    }
}

