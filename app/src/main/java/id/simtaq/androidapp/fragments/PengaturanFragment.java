package id.simtaq.androidapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import id.simtaq.androidapp.JadwalKegiatanActivity;
import id.simtaq.androidapp.R;
import id.simtaq.androidapp.SplashScreenActivity;
import id.simtaq.androidapp.adapter.PengaturanListAdapter;
import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.models.Pengaturan;

public class PengaturanFragment extends Fragment implements PengaturanListAdapter.IPengaturanAdapter, View.OnClickListener {

    private RecyclerView rvPengaturan;
    private ArrayList<Pengaturan> pengaturanList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengaturan, container, false);
        addData();
        rvPengaturan = view.findViewById(R.id.rvPengaturan);
        rvPengaturan.setHasFixedSize(true);
        rvPengaturan.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvPengaturan.setAdapter(new PengaturanListAdapter(view.getContext(), pengaturanList, this));

        return view;
    }

    void addData(){
        pengaturanList = new ArrayList<>();
        pengaturanList.add(new Pengaturan(0, R.drawable.ic_lock_primarytext, "Ganti kata sandi"));
        pengaturanList.add(new Pengaturan(1, R.drawable.ic_document_bug, "Laporkan Bug"));
        pengaturanList.add(new Pengaturan(2, R.drawable.ic_info_squared, "Tentang"));
        pengaturanList.add(new Pengaturan(3, R.drawable.ic_shutdown, "Keluar"));
    }

    @Override
    public void doClick(int id) {
        if (id == 0){
            Toast.makeText(getContext(), "Ganti kata sandi", Toast.LENGTH_SHORT).show();
        } else if (id == 1) {
            Toast.makeText(getContext(), "Laporkan Bug", Toast.LENGTH_SHORT).show();
        } else if (id == 2) {
            Toast.makeText(getContext(), "Tentang Simtaq", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getContext(), SplashScreenActivity.class));
        }
    }

    @Override
    public void onClick(View view) {

    }
}