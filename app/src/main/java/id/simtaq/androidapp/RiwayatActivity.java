package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.models.RiwayatKas;

public class RiwayatActivity extends AppCompatActivity implements RiwayatListAdapter.IRiwayatListAdapter {

    private Toolbar toolbar;
    RecyclerView rvRiwayat;
    private ArrayList<RiwayatKas> riwayatKasArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        initViews();
        addData();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Riwayat Uang Kas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        rvRiwayat.setHasFixedSize(true);
        rvRiwayat.setLayoutManager(new LinearLayoutManager(RiwayatActivity.this));
        rvRiwayat.setAdapter(new RiwayatListAdapter(riwayatKasArrayList, RiwayatActivity.this, 1, this));
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbRiwayatKas);
        rvRiwayat = findViewById(R.id.rvRiwayat);
    }

    public void addData(){
        riwayatKasArrayList = new ArrayList<>();
        riwayatKasArrayList.add(new RiwayatKas("RK000001","Kotak Amal", true,"01/01/2022","Kotak Amal Sholat Jum'at",450000,8000000, 8450000));
        riwayatKasArrayList.add(new RiwayatKas("RK000002","Pembayaran Listrik", false,"05/01/2022","Pembayaran Listrik Bulan Desember ajdjhfhfkjhfj ajgdjhdgjhdgdhgd ajhsgjhdgdjhgdjh kjdgjhdgdjhgdjhgdjh",200000, 8450000, 8250000));
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

    @Override
    public void doClick(String id) {
        for (int i = 0 ; i < riwayatKasArrayList.size() ; i++){
            final RiwayatKas riwayatKas = riwayatKasArrayList.get(i);
            if (riwayatKas.getId().equals(id)){
                Bundle bundle = new Bundle();
                bundle.putString("noCatatan", riwayatKas.getId());
                bundle.putBoolean("tipe", riwayatKas.isPemasukan());
                bundle.putString("tgl", riwayatKas.getTanggal());
                bundle.putString("keterangan", riwayatKas.getKeterangan());
                bundle.putString("deskripsi", riwayatKas.getDeskripsi());
                bundle.putInt("totalKasAwal", riwayatKas.getJmlKasAwal());
                bundle.putInt("nominalCatatan", riwayatKas.getNominal());
                bundle.putInt("totalKasAkhir", riwayatKas.getJmlKasAkhir());
                Intent intent = new Intent(RiwayatActivity.this, DetailRiwayatKasActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
//        final RiwayatKas riwayatKas = riwayatKasArrayList.get(id);

    }
}