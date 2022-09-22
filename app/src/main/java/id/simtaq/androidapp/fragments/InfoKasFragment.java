package id.simtaq.androidapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import id.simtaq.androidapp.CatatPengeluaranActivity;
import id.simtaq.androidapp.R;
import id.simtaq.androidapp.RiwayatActivity;
import id.simtaq.androidapp.adapter.JadwalKegiatanAdapter;
import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.models.Kegiatan;
import id.simtaq.androidapp.models.Keuangan;
import id.simtaq.androidapp.models.RiwayatKas;

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
    private Toolbar toolbar;
    private RiwayatListAdapter adapter;
    private RequestQueue queue;
    private RelativeLayout rlMenuKeuangan;

    private String jmlSaldo;
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
        tvSemuaRiwayat.setOnClickListener(this);
        rvRiwayatInfoKas.setHasFixedSize(true);
        queue = Volley.newRequestQueue(view.getContext());
        keuanganList = new ArrayList<>();
        //addData();
        getSaldo(view);
        getDataKeuangan(view);
        pemasukanBulanIni = 0;
        pengeluaranBulanIni = 0;

        //buildRecyclerView(view);
        return view;
    }

    private void initViews(View view){
        rvRiwayatInfoKas = view.findViewById(R.id.rvRiwayatInfoKas);
        rlMenuKeuangan = view.findViewById(R.id.rlMenuInfoKas);
        pbInfoKas = view.findViewById(R.id.pbInfoKas);
        tvJmlSaldo = view.findViewById(R.id.tvJmlSaldo);
        tvTanggalSaldo = view.findViewById(R.id.tvTanggalSaldo);
        tvSemuaRiwayat = view.findViewById(R.id.tvLihatSemuaRiwayat);
        toolbar = view.findViewById(R.id.tbInfoKas);
        tvPemasukanBlnIni = view.findViewById(R.id.tvPemasukanBlnIni);
        tvPengeluaranBlnIni = view.findViewById(R.id.tvPengeluaranBlnIni);
    }

//    public void addData(){
//        riwayatKasArrayList = new ArrayList<>();
//        riwayatKasArrayList.add(new RiwayatKas("RK000001","Kotak Amal", true,"01/01/2022","Kotak Amal Sholat Jum'at",450000,8000000, 8450000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000002","Pembayaran Listrik", false,"05/01/2022","Pembayaran Listrik Bulan Desember",200000, 8450000, 8250000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000003","Kotak Amal", true,"08/01/2022","Kotak Amal Sholat Jum'at",400000, 8250000, 8650000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000004","Renovasi Dinding", false,"13/01/2022","Pengecatan",500000, 8650000,8150000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000005","Kotak Amal", true,"16/01/2022","Kotak Amal Sholat Jum'at",900000, 8150000, 9050000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000006","Kotak Amal", true,"19/01/2022","Kotak Amal Sholat Jum'at",450000, 9050000, 9500000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000007","Pembayaran Listrik", false,"05/02/2022","Pembayaran Listrik Bulan Januari",200000, 9500000, 9300000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000008","Kotak Amal", true,"08/02/2022","Kotak Amal Sholat Jum'at",400000, 9300000, 9700000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000009","Renovasi Dinding", false,"11/02/2022","Pengecatan",500000, 9700000, 9200000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000010","Kotak Amal", true,"15/02/2022","Kotak Amal Sholat Jum'at",900000, 9200000, 10100000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000011","Kotak Amal", true,"27/02/2022","Kotak Amal Sholat Jum'at",450000, 10100000, 10550000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000012","Pembayaran Listrik", false,"05/03/2022","Pembayaran Listrik Bulan Februari",200000, 10550000, 10350000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000013","Kotak Amal", true,"06/03/2022","Kotak Amal Sholat Jum'at",400000, 10350000, 10750000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000014","Renovasi Dinding", false,"08/03/2022","Pengecatan",500000, 10750000, 10250000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000015","Kotak Amal", true,"13/03/2022","Kotak Amal Sholat Jum'at",90000, 10250000, 11150000));
//    }

    public void getDataKeuangan(View view){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/keuangan", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbInfoKas.setVisibility(View.GONE);
                rvRiwayatInfoKas.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        int idKeuangan = responseObj.getInt("id_keuangan");
                        String noKeuangan = responseObj.getString("no_keuangan");
                        String tipeKeuangan = responseObj.getString("tipe_keuangan");
                        String tglKeuangan = responseObj.getString("tgl_keuangan");
                        String ketKeuangan = responseObj.getString("keterangan_keuangan");
                        Long nominalKeuangan = responseObj.getLong("nominal_keuangan");
                        Long jmlKasAwal = responseObj.getLong("jml_kas_awal");
                        Long jmlKasAkhir = responseObj.getLong("jml_kas_akhir");
                        String deskripsiKeuangan = responseObj.getString("deskripsi_keuangan");
                        String createAt = responseObj.getString("create_at");
                        String updateAt = responseObj.getString("update_at");
                        if (tglKeuangan.contains(getCurentMonth())){
                            keuanganList.add(new Keuangan(idKeuangan, noKeuangan, tipeKeuangan, tglKeuangan, ketKeuangan, nominalKeuangan, jmlKasAwal, jmlKasAkhir, deskripsiKeuangan, createAt, updateAt));
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
                                pengeluaranBulanIni += nominalKeuangan;
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
        });
        queue.add(jsonArrayRequest);
    }

    public void getSaldo(View view){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/saldo", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //pbInfoKas.setVisibility(View.GONE);
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
        });
        queue.add(jsonArrayRequest);
    }

    public void buildRecyclerView(View view) {
        adapter = new RiwayatListAdapter(keuanganList, view.getContext(), 2, this, queue, rlMenuKeuangan);
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
        }
    }

    @Override
    public void doClick(int id) {

    }
}