package id.simtaq.androidapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import id.simtaq.androidapp.CatatPemasukanActivity;
import id.simtaq.androidapp.CatatPengeluaranActivity;
import id.simtaq.androidapp.JadwalKegiatanActivity;
import id.simtaq.androidapp.R;
import id.simtaq.androidapp.RiwayatActivity;
import id.simtaq.androidapp.TambahKegiatanActivity;

public class HomeFragment extends Fragment implements View.OnClickListener {

    RelativeLayout rlRiwayatUangKas;
    RelativeLayout rlJadwalKegiatan;
    RelativeLayout rlCatatPemasukan;
    RelativeLayout rlCatatPengeluaran;
    RelativeLayout rlTambahKegiatan;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        rlRiwayatUangKas.setOnClickListener(this);
        rlJadwalKegiatan.setOnClickListener(this);
        rlCatatPemasukan.setOnClickListener(this);
        rlCatatPengeluaran.setOnClickListener(this);
        rlTambahKegiatan.setOnClickListener(this);
        return view;
    }

    public void initViews(View v){
        rlRiwayatUangKas = v.findViewById(R.id.rlButtonRiwayatUangKas);
        rlJadwalKegiatan = v.findViewById(R.id.rlButtonJadwalKegiatan);
        rlCatatPemasukan = v.findViewById(R.id.rlButtonCatatPemasukan);
        rlCatatPengeluaran = v.findViewById(R.id.rlButtonCatatPengeluaran);
        rlTambahKegiatan = v.findViewById(R.id.rlButtonTambahKegiatan);
    }

    @Override
    public void onClick(View v) {
        if (v == rlRiwayatUangKas){
            startActivity(new Intent(getContext(), RiwayatActivity.class));
        } else if (v== rlJadwalKegiatan){
            startActivity(new Intent(getContext(), JadwalKegiatanActivity.class));
        } else if (v== rlCatatPemasukan){
            startActivity(new Intent(getContext(), CatatPemasukanActivity.class));
        } else if (v== rlCatatPengeluaran){
            startActivity(new Intent(getContext(), CatatPengeluaranActivity.class));
        } else if (v== rlTambahKegiatan){
            startActivity(new Intent(getContext(), TambahKegiatanActivity.class));
        }
    }
}