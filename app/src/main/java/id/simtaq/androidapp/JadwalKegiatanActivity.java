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

public class JadwalKegiatanActivity extends AppCompatActivity {

    private final int DAYS_COUNT = 42;
    private ArrayList<CalendarModel> calendarList;
    private Calendar calendar;
    private int tahun = -1;
    private int monthOfYear = -1;
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
        calendarList = new ArrayList<>();
        calendar = Calendar.getInstance();
        adapter = new JadwalKegiatanAdapter(JadwalKegiatanActivity.this, calendarList);
        loadCalendar();
        recyclerView.setLayoutManager(new GridLayoutManager(JadwalKegiatanActivity.this,7));
        recyclerView.setAdapter(adapter);
    }

    private void loadCalendar() {
        //ubah val ke var
        //calendarList = new ArrayList<>();

        ArrayList<CalendarModel> cells = new ArrayList<>(); // inisialisasi variabel untuk setiap tanggal kalender
        if (tahun != -1 && monthOfYear != -1) {     // pengecekan bila varuiabel tahun dan monthOfYear kosong (-1 hanya pengecoh)
            //ubah obyek kalender ke tahun dan bulan yang diterima
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.YEAR, tahun);
        } else {
            // set variabel tahun dan monthOfYear ke tahun dan bulan sekarang
            tahun = calendar.get(Calendar.YEAR);
            monthOfYear = calendar.get(Calendar.MONTH);
        }
        Locale locale = new Locale("in", "ID");
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("MMMM,yyyy", locale);  // obyek untuk parse bulan dan tahun
        String[] dateToday = sdf.format(calendar.getTime()).split(","); //format obyek calendar lalu split berdasarkan ,
        tvMonth.setText(dateToday[0]+","); //settext bulan ke textview month
        tvYear.setText(dateToday[1]); //settext bulan ke textview year

        //calendarToday
        Calendar calendarCompare= Calendar.getInstance(); //instansiasi obyek calendar pembanding

        calendarCompare.set(Calendar.MONTH, monthOfYear); //set bulan pada calendar pembanding ke monthOfYear
        calendarCompare.set(Calendar.YEAR, tahun); //set tahun pada calendar pembanding ke tahun


        // memnentukan kapan tanggal dimulai pada bulan
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // pindah calendar ke awal minggu
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        //obyek untuk parse tanggal
        sdf = new SimpleDateFormat("dd-MM-yyyy", locale);

        // isi tanggal
        while (cells.size() < DAYS_COUNT) {
            if (sdf.format(calendar.getTime()).equals("19-03-2022")) {
                cells.add(new CalendarModel( calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), calendarCompare, "hijau"));
            } else {
                cells.add(new CalendarModel( calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), calendarCompare, null));
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendarList.clear();
        calendarList.addAll(cells);
        adapter.notifyDataSetChanged();
    }
}