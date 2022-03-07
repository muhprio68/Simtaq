package id.simtaq.androidapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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


public class InfoKasFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rvRiwayatInfoKas;
    private ArrayList<RiwayatKas> riwayatKasArrayList;
    private TextView tvSemuaRiwayat;

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
        rvRiwayatInfoKas.setAdapter(new RiwayatListAdapter(riwayatKasArrayList, view.getContext(), 2));
        return view;
    }

    private void initViews(View view){
        rvRiwayatInfoKas = view.findViewById(R.id.rvRiwayatInfoKas);
        tvSemuaRiwayat = view.findViewById(R.id.tvLihatSemuaRiwayat);
    }

    public void addData(){
        riwayatKasArrayList = new ArrayList<>();
        riwayatKasArrayList.add(new RiwayatKas("RK000001","Kotak Amal", true,"26/02/2022","Kotak Amal Sholat Jum'at",450000));
        riwayatKasArrayList.add(new RiwayatKas("RK000002","Pembayaran Listrik", false,"05/03/2022","Pembayaran Listrik Bulan Februari",200000));
        riwayatKasArrayList.add(new RiwayatKas("RK000003","Kotak Amal", true,"26/02/2022","Kotak Amal Sholat Jum'at",400000));
        riwayatKasArrayList.add(new RiwayatKas("RK000004","Renovasi Dinding", false,"26/02/2022","Pengecatan",500000));
        riwayatKasArrayList.add(new RiwayatKas("RK000005","Kotak Amal", true,"26/02/2022","Kotak Amal Sholat Jum'at",900000));
        riwayatKasArrayList.add(new RiwayatKas("RK000006","Kotak Amal", true,"26/02/2022","Kotak Amal Sholat Jum'at",450000));
        riwayatKasArrayList.add(new RiwayatKas("RK000007","Pembayaran Listrik", false,"05/02/2022","Pembayaran Listrik Bulan Januari",200000));
        riwayatKasArrayList.add(new RiwayatKas("RK000008","Kotak Amal", true,"26/02/2022","Kotak Amal Sholat Jum'at",400000));
        riwayatKasArrayList.add(new RiwayatKas("RK000009","Renovasi Dinding", false,"26/02/2022","Pengecatan",500000));
        riwayatKasArrayList.add(new RiwayatKas("RK000010","Kotak Amal", true,"26/02/2022","Kotak Amal Sholat Jum'at",900000));
        riwayatKasArrayList.add(new RiwayatKas("RK000011","Kotak Amal", true,"26/02/2022","Kotak Amal Sholat Jum'at",450000));
        riwayatKasArrayList.add(new RiwayatKas("RK000012","Pembayaran Listrik", false,"05/01/2022","Pembayaran Listrik Bulan Desember",200000));
        riwayatKasArrayList.add(new RiwayatKas("RK000013","Kotak Amal", true,"26/02/2022","Kotak Amal Sholat Jum'at",400000));
        riwayatKasArrayList.add(new RiwayatKas("RK000014","Renovasi Dinding", false,"26/02/2022","Pengecatan",500000));
        riwayatKasArrayList.add(new RiwayatKas("RK000015","Kotak Amal", true,"26/02/2022","Kotak Amal Sholat Jum'at",900000));
    }

    @Override
    public void onClick(View v) {
        if (v == tvSemuaRiwayat){
            startActivity(new Intent(v.getContext(), RiwayatActivity.class));
        }
    }
}