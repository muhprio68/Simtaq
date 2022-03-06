package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import id.simtaq.androidapp.adapter.RiwayatListAdapter;

public class RiwayatActivity extends AppCompatActivity {

    RecyclerView rvRiwayat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        getSupportActionBar().hide();
        rvRiwayat = findViewById(R.id.rvRiwayat);
        rvRiwayat.setHasFixedSize(true);
        rvRiwayat.setLayoutManager(new LinearLayoutManager(RiwayatActivity.this));
        rvRiwayat.setAdapter(new RiwayatListAdapter(1234, RiwayatActivity.this));
    }
}