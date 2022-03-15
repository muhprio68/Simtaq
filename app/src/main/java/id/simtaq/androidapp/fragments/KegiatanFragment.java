package id.simtaq.androidapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import id.simtaq.androidapp.JadwalKegiatanActivity;
import id.simtaq.androidapp.R;
import id.simtaq.androidapp.RiwayatActivity;
import id.simtaq.androidapp.adapter.JadwalKegiatanAdapter;
import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.models.CalendarModel;
import id.simtaq.androidapp.models.Kegiatan;

public class KegiatanFragment extends Fragment implements View.OnClickListener, JadwalKegiatanAdapter.IJadwalKegiatanAdapter {

    private ArrayList<Kegiatan> kegiatanList;
    private RecyclerView rvKegiatan;
    private TextView tvLihatSemuaKegiatan;

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
        tvLihatSemuaKegiatan.setOnClickListener(this);
        rvKegiatan.setHasFixedSize(true);
        addData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvKegiatan.setLayoutManager(layoutManager);
        rvKegiatan.setAdapter(new JadwalKegiatanAdapter(view.getContext(), kegiatanList, 2, this));
        return view;
    }

    public void initViews(View v){
        rvKegiatan = v.findViewById(R.id.rvKegiatan);
        tvLihatSemuaKegiatan = v.findViewById(R.id.tvLihatSemuaKegiatan);
    }

    public void addData(){
        kegiatanList = new ArrayList<>();
        kegiatanList.add(new Kegiatan("KEG00001", true, "Pengajian Rutin", "22/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","KH.Abdul Kholiq Hasan, M.HI."));
        kegiatanList.add(new Kegiatan("KEG00002", true, "Sholawat Diba' Rutin", "22/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. Suhardiman"));
        kegiatanList.add(new Kegiatan("KEG00003", false, "Rapat takmir", "18/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. H.M.Supeno"));
        kegiatanList.add(new Kegiatan("KEG00004", true, "Istighosah Rutin", "25/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. H.M.Supeno"));
        kegiatanList.add(new Kegiatan("KEG00005", true, "Sholawat Diba'", "29/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. M. Khoirul Huda"));
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
}

