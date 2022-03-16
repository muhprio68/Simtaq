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
import android.widget.TextView;

import java.util.ArrayList;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.RiwayatActivity;
import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.models.RiwayatKas;


public class InfoKasFragment extends Fragment implements View.OnClickListener, RiwayatListAdapter.IRiwayatListAdapter {

    private RecyclerView rvRiwayatInfoKas;
    private ArrayList<RiwayatKas> riwayatKasArrayList;
    private TextView tvSemuaRiwayat;
    private Toolbar toolbar;

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
        addData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvRiwayatInfoKas.setLayoutManager(layoutManager);
        rvRiwayatInfoKas.setAdapter(new RiwayatListAdapter(riwayatKasArrayList, view.getContext(), 2, this));
        return view;
    }

    private void initViews(View view){
        rvRiwayatInfoKas = view.findViewById(R.id.rvRiwayatInfoKas);
        tvSemuaRiwayat = view.findViewById(R.id.tvLihatSemuaRiwayat);
        toolbar = view.findViewById(R.id.tbInfoKas);
    }

    public void addData(){
        riwayatKasArrayList = new ArrayList<>();
        riwayatKasArrayList.add(new RiwayatKas("RK000001","Kotak Amal", true,"01/01/2022","Kotak Amal Sholat Jum'at",450000,8000000, 8450000));
        riwayatKasArrayList.add(new RiwayatKas("RK000002","Pembayaran Listrik", false,"05/01/2022","Pembayaran Listrik Bulan Desember",200000, 8450000, 8250000));
        riwayatKasArrayList.add(new RiwayatKas("RK000003","Kotak Amal", true,"08/01/2022","Kotak Amal Sholat Jum'at",400000, 8250000, 8650000));
        riwayatKasArrayList.add(new RiwayatKas("RK000004","Renovasi Dinding", false,"13/01/2022","Pengecatan",500000, 8650000,8150000));
        riwayatKasArrayList.add(new RiwayatKas("RK000005","Kotak Amal", true,"16/01/2022","Kotak Amal Sholat Jum'at",900000, 8150000, 9050000));
        riwayatKasArrayList.add(new RiwayatKas("RK000006","Kotak Amal", true,"19/01/2022","Kotak Amal Sholat Jum'at",450000, 9050000, 9500000));
        riwayatKasArrayList.add(new RiwayatKas("RK000007","Pembayaran Listrik", false,"05/02/2022","Pembayaran Listrik Bulan Januari",200000, 9500000, 9300000));
        riwayatKasArrayList.add(new RiwayatKas("RK000008","Kotak Amal", true,"08/02/2022","Kotak Amal Sholat Jum'at",400000, 9300000, 9700000));
        riwayatKasArrayList.add(new RiwayatKas("RK000009","Renovasi Dinding", false,"11/02/2022","Pengecatan",500000, 9700000, 9200000));
        riwayatKasArrayList.add(new RiwayatKas("RK000010","Kotak Amal", true,"15/02/2022","Kotak Amal Sholat Jum'at",900000, 9200000, 10100000));
        riwayatKasArrayList.add(new RiwayatKas("RK000011","Kotak Amal", true,"27/02/2022","Kotak Amal Sholat Jum'at",450000, 10100000, 10550000));
        riwayatKasArrayList.add(new RiwayatKas("RK000012","Pembayaran Listrik", false,"05/03/2022","Pembayaran Listrik Bulan Februari",200000, 10550000, 10350000));
        riwayatKasArrayList.add(new RiwayatKas("RK000013","Kotak Amal", true,"06/03/2022","Kotak Amal Sholat Jum'at",400000, 10350000, 10750000));
        riwayatKasArrayList.add(new RiwayatKas("RK000014","Renovasi Dinding", false,"08/03/2022","Pengecatan",500000, 10750000, 10250000));
        riwayatKasArrayList.add(new RiwayatKas("RK000015","Kotak Amal", true,"13/03/2022","Kotak Amal Sholat Jum'at",90000, 10250000, 11150000));
    }
//
    @Override
    public void onClick(View v) {
        if (v == tvSemuaRiwayat){
            startActivity(new Intent(v.getContext(), RiwayatActivity.class));
        }
    }

    @Override
    public void doClick(String id) {

    }
}