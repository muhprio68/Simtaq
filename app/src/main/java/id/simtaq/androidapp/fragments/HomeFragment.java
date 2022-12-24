package id.simtaq.androidapp.fragments;

import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import id.simtaq.androidapp.CatatDonaturActivity;
import id.simtaq.androidapp.CatatKeuanganActivity;
import id.simtaq.androidapp.InfakOnlineActivity;
import id.simtaq.androidapp.JadwalKegiatanActivity;
import id.simtaq.androidapp.LokasiKegiatanActivity;
import id.simtaq.androidapp.PengurusTakmirActivity;
import id.simtaq.androidapp.R;
import id.simtaq.androidapp.RiwayatActivity;
import id.simtaq.androidapp.TambahKegiatanActivity;
import id.simtaq.androidapp.TentangSimtaqActivity;
import id.simtaq.androidapp.helper.Preferences;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout rlRiwayatUangKas, rlJadwalKegiatan, rlInfakOnline;
    private RelativeLayout rlCatatKeuangan,rlCatatDonatur, rlTambahKegiatan;
    private RelativeLayout rlLokasiKegiatan, rlPengurusTakmir, rlTentangSimtaq;

    private TextView tvCatatKeuangan, tvCatatDonatur, tvTambahKegiatan;

    private String level;

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
        level = Preferences.getKeyLevel(getContext());
        rlRiwayatUangKas.setOnClickListener(this);
        rlJadwalKegiatan.setOnClickListener(this);
        rlInfakOnline.setOnClickListener(this);
        rlCatatKeuangan.setOnClickListener(this);
        rlCatatDonatur.setOnClickListener(this);
        rlTambahKegiatan.setOnClickListener(this);
        rlLokasiKegiatan.setOnClickListener(this);
        rlPengurusTakmir.setOnClickListener(this);
        rlTentangSimtaq.setOnClickListener(this);
        aksesLevel(level);
        return view;
    }

    public void initViews(View v){
        rlRiwayatUangKas = v.findViewById(R.id.rlButtonRiwayatUangKas);
        rlJadwalKegiatan = v.findViewById(R.id.rlButtonJadwalKegiatan);
        rlInfakOnline = v.findViewById(R.id.rlInfakOnline);
        rlCatatKeuangan = v.findViewById(R.id.rlCatatKeuangan);
        rlCatatDonatur = v.findViewById(R.id.rlCatatDonatur);
        rlTambahKegiatan = v.findViewById(R.id.rlTambahKegiatan);
        rlLokasiKegiatan = v.findViewById(R.id.rlLokasiKegiatan);
        rlPengurusTakmir = v.findViewById(R.id.rlPengurusTakmir);
        rlTentangSimtaq = v.findViewById(R.id.rlTentangSimtaq);
    }

    @Override
    public void onClick(View v) {
        if (v == rlRiwayatUangKas){
            startActivity(new Intent(v.getContext(), RiwayatActivity.class));
        } else if (v== rlJadwalKegiatan){
            startActivity(new Intent(v.getContext(), JadwalKegiatanActivity.class));
        } else if (v== rlInfakOnline){
            startActivity(new Intent(v.getContext(), InfakOnlineActivity.class));
        } else if (v== rlCatatKeuangan){
            if (level.equals("3")){
                Toast.makeText(getContext(), "Menu hanya untuk bendahara takmir", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(v.getContext(), CatatKeuanganActivity.class));
            }
        } else if (v== rlCatatDonatur){
            if (level.equals("3")){
                Toast.makeText(getContext(), "Menu hanya untuk bendahara takmir", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(v.getContext(), CatatDonaturActivity.class));
            }
        } else if (v== rlTambahKegiatan){
            if (level.equals("2")){
                Toast.makeText(getContext(), "Menu hanya untuk humas takmir", Toast.LENGTH_SHORT).show();
            } else{
                startActivity(new Intent(v.getContext(), TambahKegiatanActivity.class));
            }
        } else if (v== rlLokasiKegiatan){
            startActivity(new Intent(v.getContext(), LokasiKegiatanActivity.class));
        } else if (v== rlPengurusTakmir){
            startActivity(new Intent(v.getContext(), PengurusTakmirActivity.class));
        } else if (v== rlTentangSimtaq){
            startActivity(new Intent(v.getContext(), TentangSimtaqActivity.class));
        }
    }

    private void aksesLevel(String level){
        if (level.equals("1")) {
            rlCatatKeuangan.setVisibility(View.GONE);
            rlCatatDonatur.setVisibility(View.GONE);
            rlTambahKegiatan.setVisibility(View.GONE);
        }
    }
}