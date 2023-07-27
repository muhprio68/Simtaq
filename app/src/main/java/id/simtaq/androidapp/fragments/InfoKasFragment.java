package id.simtaq.androidapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.simtaq.androidapp.CatatDonaturActivity;
import id.simtaq.androidapp.CatatKeuanganActivity;
import id.simtaq.androidapp.DetailKeuanganActivity;
import id.simtaq.androidapp.R;
import id.simtaq.androidapp.RiwayatActivity;
import id.simtaq.androidapp.adapter.JadwalKegiatanAdapter;
import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.helper.Preferences;
import id.simtaq.androidapp.models.Kegiatan;
import id.simtaq.androidapp.models.Keuangan;

import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.toRupiah;
import static id.simtaq.androidapp.helper.config.url;


public class InfoKasFragment extends Fragment implements View.OnClickListener, RiwayatListAdapter.IRiwayatListAdapter {

    private RecyclerView rvRiwayatInfoKas;
    private ArrayList<Keuangan> keuanganList;
    private ProgressBar pbInfoKas;
    private TextView tvSemuaRiwayat;
    private TextView tvJmlSaldo;
    private TextView tvTanggalSaldo;
    private TextView tvPemasukanBlnIni;
    private TextView tvPengeluaranBlnIni;
    private TextView tvTambahData;
    private Toolbar toolbar;
    private RiwayatListAdapter adapter;
    private RequestQueue queue;
    private ConstraintLayout clViewInfoKas, clMenuKeuangan, clCatatKeuangan, clCatatDonatur;
    private String authToken;

    private String jmlSaldo, level;
    private int pemasukanBulanIni;
    private int pengeluaranBulanIni;

    public InfoKasFragment() {

    }

    public static InfoKasFragment newInstance(String param1, String param2) {
        InfoKasFragment fragment = new InfoKasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infokas, container, false);
        initViews(view);
        level = Preferences.getKeyLevel(view.getContext());
        authToken = Preferences.getKeyToken(view.getContext());
        tvSemuaRiwayat.setOnClickListener(this);
        clCatatKeuangan.setOnClickListener(this);
        clCatatDonatur.setOnClickListener(this);
        rvRiwayatInfoKas.setHasFixedSize(true);
        clViewInfoKas.setVisibility(View.GONE);
        queue = Volley.newRequestQueue(view.getContext());
        keuanganList = new ArrayList<>();
        getSaldo(view, authToken);
        getDataKeuangan(view, authToken);
        pemasukanBulanIni = 0;
        pengeluaranBulanIni = 0;
        initLevel(level);
        return view;
    }

    private void initViews(View view){
        rvRiwayatInfoKas = view.findViewById(R.id.rvRiwayatInfoKas);
        clMenuKeuangan = view.findViewById(R.id.clMenuInfoKas);
        pbInfoKas = view.findViewById(R.id.pbInfoKas);
        tvJmlSaldo = view.findViewById(R.id.tvJmlSaldo);
        tvTanggalSaldo = view.findViewById(R.id.tvTanggalSaldo);
        tvSemuaRiwayat = view.findViewById(R.id.tvLihatSemuaRiwayat);
        toolbar = view.findViewById(R.id.tbInfoKas);
        tvPemasukanBlnIni = view.findViewById(R.id.tvPemasukanBlnIni);
        tvPengeluaranBlnIni = view.findViewById(R.id.tvPengeluaranBlnIni);
        clCatatKeuangan = view.findViewById(R.id.clCatatKeuangan);
        clCatatDonatur = view.findViewById(R.id.clCatatDonatur);
        clViewInfoKas = view.findViewById(R.id.clViewInfoKas);
        tvTambahData = view.findViewById(R.id.tvTambahDataKeuangan);
    }

    public void getDataKeuangan(View view, String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/keuangan", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbInfoKas.setVisibility(View.GONE);
                clViewInfoKas.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        int idKeuangan = responseObj.getInt("id_keuangan");
                        String noKeuangan = responseObj.getString("no_keuangan");
                        String tipeKeuangan = responseObj.getString("tipe_keuangan");
                        String tglKeuangan = responseObj.getString("tgl_keuangan");
                        String ketKeuangan = responseObj.getString("keterangan_keuangan");
                        String jenisKeuangan = responseObj.getString("jenis_keuangan");
                        String statusKeuangan = responseObj.getString("status_keuangan");
                        Long nominalKeuangan = responseObj.getLong("nominal_keuangan");
                        Long jmlKasAwal = responseObj.getLong("jml_kas_awal");
                        Long jmlKasAkhir = responseObj.getLong("jml_kas_akhir");
                        String deskripsiKeuangan = responseObj.getString("deskripsi_keuangan");
                        String createAt = responseObj.getString("create_at");
                        String updateAt = responseObj.getString("update_at");
                        if (tglKeuangan.contains(getCurentMonth())){
                            keuanganList.add(new Keuangan(idKeuangan, noKeuangan, tipeKeuangan, tglKeuangan, ketKeuangan, jenisKeuangan, statusKeuangan, nominalKeuangan, jmlKasAwal, jmlKasAkhir, deskripsiKeuangan, createAt, updateAt));
                            Collections.sort(keuanganList, new Comparator<Keuangan>() {
                                @Override
                                public int compare(Keuangan keuangan, Keuangan k1) {
                                    return keuangan.getTglKeuangan().compareTo(k1.getTglKeuangan());
                                }
                            });
                            if (tipeKeuangan.equals("Pemasukan")){
                                pemasukanBulanIni += nominalKeuangan;
                                tvPemasukanBlnIni.setText("+ "+toRupiah(pemasukanBulanIni+""));
                            } else {
                                pengeluaranBulanIni -= nominalKeuangan;
                                tvPengeluaranBlnIni.setText("- "+toRupiah(pengeluaranBulanIni+""));
                            }
                            buildRecyclerView(view);
                        }
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

    public void getSaldo(View view, String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/saldo", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject responseObj = response.getJSONObject(0);
                    jmlSaldo = responseObj.getString("jml_saldo");
                    tvJmlSaldo.setText(toRupiah(jmlSaldo));
                    tvTanggalSaldo.setText(getCurentDate());
                } catch (JSONException e) {
                    e.printStackTrace();
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
        adapter = new RiwayatListAdapter(authToken, keuanganList, view.getContext(), 2, this, queue, clMenuKeuangan);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvRiwayatInfoKas.setHasFixedSize(true);
        rvRiwayatInfoKas.setLayoutManager(manager);
        rvRiwayatInfoKas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public String getCurentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public String getCurentMonth(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    @Override
    public void onClick(View v) {
        if (v == tvSemuaRiwayat){
            startActivity(new Intent(v.getContext(), RiwayatActivity.class));
        } else if (v == clCatatKeuangan){
            startActivity(new Intent(v.getContext(), CatatKeuanganActivity.class));
        } else if (v == clCatatDonatur){
            startActivity(new Intent(v.getContext(), CatatDonaturActivity.class));
        }
    }

    private void initLevel(String level){
        if (level.equals("1") || level.equals("3")) {
            clCatatKeuangan.setVisibility(View.GONE);
            clCatatDonatur.setVisibility(View.GONE);
            tvTambahData.setVisibility(View.GONE);
        }
    }

    @Override
    public void doClick(int id) {
        Intent intent = new Intent(getContext(), DetailKeuanganActivity.class);
        intent.putExtra("intentDari", "info kas");
        intent.putExtra("idKeuangan", id);
        startActivity(intent);
    }
}