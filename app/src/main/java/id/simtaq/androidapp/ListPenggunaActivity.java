package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import id.simtaq.androidapp.adapter.JadwalKegiatanAdapter;
import id.simtaq.androidapp.adapter.PengaturanListAdapter;
import id.simtaq.androidapp.adapter.PenggunaListAdapter;
import id.simtaq.androidapp.models.Pengguna;

public class ListPenggunaActivity extends AppCompatActivity implements PenggunaListAdapter.IPenggunaAdapter {

    private Toolbar toolbar;
    private RecyclerView rvPenggunaList;
    private ArrayList<Pengguna> penggunaList;
    private CoordinatorLayout clPengggunaList;
    private RequestQueue queue;
    private PenggunaListAdapter adapter;
    private int tipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pengguna);
        initViews();
        tipe = getIntent().getIntExtra("tipe", 0);
        setSupportActionBar(toolbar);
        if (tipe == 1){
            getSupportActionBar().setTitle("Ubah Pengguna");
        } else {
            getSupportActionBar().setTitle("Hapus Pengguna");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        penggunaList = new ArrayList<>();
        queue = Volley.newRequestQueue(ListPenggunaActivity.this);
        buildRecyclerView();
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbListPengguna);
        rvPenggunaList = findViewById(R.id.rvPengguna);
        clPengggunaList = findViewById(R.id.clPengguna);
    }

    public void buildRecyclerView(){
        adapter = new PenggunaListAdapter(penggunaList, ListPenggunaActivity.this,tipe, this, queue, clPengggunaList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvPenggunaList.setHasFixedSize(true);
        rvPenggunaList.setLayoutManager(manager);
        rvPenggunaList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void doClick(int id) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}