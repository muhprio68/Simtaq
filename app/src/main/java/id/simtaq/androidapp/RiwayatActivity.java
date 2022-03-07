package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.models.RiwayatKas;

public class RiwayatActivity extends AppCompatActivity {

    RecyclerView rvRiwayat;
    private ArrayList<RiwayatKas> riwayatKasArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        getSupportActionBar().hide();
        addData();
        rvRiwayat = findViewById(R.id.rvRiwayat);
        rvRiwayat.setHasFixedSize(true);
        rvRiwayat.setLayoutManager(new LinearLayoutManager(RiwayatActivity.this));
        rvRiwayat.setAdapter(new RiwayatListAdapter(riwayatKasArrayList, RiwayatActivity.this, 1));
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
        riwayatKasArrayList.add(new RiwayatKas("RK000001","Kotak Amal", true,"26/02/2022","Kotak Amal Sholat Jum'at",450000));
        riwayatKasArrayList.add(new RiwayatKas("RK000002","Pembayaran Listrik", false,"05/01/2022","Pembayaran Listrik Bulan Desember",200000));
        riwayatKasArrayList.add(new RiwayatKas("RK000003","Kotak Amal", true,"26/02/2022","Kotak Amal Sholat Jum'at",400000));
        riwayatKasArrayList.add(new RiwayatKas("RK000004","Renovasi Dinding", false,"26/02/2022","Pengecatan",500000));
        riwayatKasArrayList.add(new RiwayatKas("RK000005","Kotak Amal", true,"26/02/2022","Kotak Amal Sholat Jum'at",900000));
    }
}