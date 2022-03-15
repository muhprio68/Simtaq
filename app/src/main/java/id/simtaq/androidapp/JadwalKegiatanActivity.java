package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import id.simtaq.androidapp.adapter.JadwalKegiatanAdapter;
import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.models.CalendarModel;
import id.simtaq.androidapp.models.Kegiatan;
import id.simtaq.androidapp.models.RiwayatKas;

public class JadwalKegiatanActivity extends AppCompatActivity implements JadwalKegiatanAdapter.IJadwalKegiatanAdapter {


    private ArrayList<Kegiatan> kegiatanList;
    private JadwalKegiatanAdapter adapter;

    private TextView tvFilterBulanKegiatan;
    private TextView tvFilterTahunKegiatan;
    private RecyclerView rvJadwalKegiatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_kegiatan);
        getSupportActionBar().hide();
        initViews();
        addData();
        rvJadwalKegiatan.setHasFixedSize(true);
        rvJadwalKegiatan.setLayoutManager(new LinearLayoutManager(JadwalKegiatanActivity.this));
        rvJadwalKegiatan.setAdapter(new JadwalKegiatanAdapter(JadwalKegiatanActivity.this,kegiatanList, 2, this));
    }

    public void initViews(){
        tvFilterBulanKegiatan = findViewById(R.id.tvFilterBulanKegiatan);
        tvFilterTahunKegiatan = findViewById(R.id.tvFilterTahunKegiatan);
        rvJadwalKegiatan = findViewById(R.id.rvJadwalKegiatan);
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
    public void doClick(String id) {
        for (int i = 0 ; i < kegiatanList.size() ; i++){
            final Kegiatan kegiatan = kegiatanList.get(i);
            if (kegiatan.getIdKegiatan().equals(id)){
                Bundle bundle = new Bundle();
                bundle.putString("noKegiatan", kegiatan.getIdKegiatan());
                bundle.putString("namaKegiatan", kegiatan.getNamaKegiatan());
                bundle.putBoolean("tipe", kegiatan.isUmum());
                bundle.putString("tgl", kegiatan.getTanggalKegiatan());
                bundle.putString("waktu", kegiatan.getWaktuKegiatan());
                bundle.putString("tempat", kegiatan.getTempatKegiatan());
                bundle.putString("pembicara", kegiatan.getPembicaraKegiatan());
                bundle.putString("deskripsi", kegiatan.getDeskripsiKegiatan());
                Intent intent = new Intent(JadwalKegiatanActivity.this, DetailKegiatanActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }
}