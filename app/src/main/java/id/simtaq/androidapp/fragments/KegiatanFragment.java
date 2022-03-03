package id.simtaq.androidapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.adapter.JadwalKegiatanAdapter;
import id.simtaq.androidapp.models.CalendarModel;

public class KegiatanFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private int DAYS_COUNT = 42;
    private ArrayList<CalendarModel> calendarList;
    private Calendar calendar = Calendar.getInstance();
    private int tahun = -1;
    private int monthOfYear = -1;
    private JadwalKegiatanAdapter adapter;

    private TextView tvMonth;
    private TextView tvYear;

    public KegiatanFragment() {

    }

    public static KegiatanFragment newInstance(String param1, String param2) {
        KegiatanFragment fragment = new KegiatanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_kegiatan, container, false);
        tvMonth = view.findViewById(R.id.month);
        tvYear = view.findViewById(R.id.year);
        adapter = new JadwalKegiatanAdapter(getContext(), calendarList);

        return view;
    }


    private void loadCalendar() {
        //ubah val ke var
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
        tvMonth.setText(dateToday[0]); //settext bulan ke textview month
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
            if (sdf.format(calendar.getTime()).equals("13-05-2019")) {
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

