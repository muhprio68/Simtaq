package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import id.simtaq.androidapp.adapter.JadwalKegiatanAdapter;
import id.simtaq.androidapp.models.CalendarModel;
import id.simtaq.androidapp.models.Kegiatan;

public class JadwalKegiatanActivity extends AppCompatActivity {


    private ArrayList<Kegiatan> kegiatanList;
    private JadwalKegiatanAdapter adapter;

    private TextView tvMonth;
    private TextView tvYear;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_kegiatan);
        getSupportActionBar().hide();
        tvMonth = findViewById(R.id.month);
        tvYear = findViewById(R.id.year);
        recyclerView = findViewById(R.id.recyclerView);
//        addData();
//        adapter = new JadwalKegiatanAdapter(JadwalKegiatanActivity.this, kegiatanList, 2);
//        loadCalendar();
//        recyclerView.setLayoutManager(new GridLayoutManager(JadwalKegiatanActivity.this,7));
//        recyclerView.setAdapter(adapter);
    }

    public void addData(){
        kegiatanList = new ArrayList<>();
        kegiatanList.add(new Kegiatan("KEG00001", true, "Pengajian Rutin", "22/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","KH.Abdul Kholiq Hasan, M.HI."));
        kegiatanList.add(new Kegiatan("KEG00002", true, "Sholawat Diba' Rutin", "22/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. Suhardiman"));
        kegiatanList.add(new Kegiatan("KEG00003", false, "Rapat takmir", "18/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. H.M.Supeno"));
        kegiatanList.add(new Kegiatan("KEG00004", true, "Istighosah Rutin", "25/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. H.M.Supeno"));
        kegiatanList.add(new Kegiatan("KEG00005", true, "Sholawat Diba'", "29/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. M. Khoirul Huda"));
    }
}