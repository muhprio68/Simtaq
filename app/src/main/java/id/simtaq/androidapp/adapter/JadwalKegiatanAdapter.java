package id.simtaq.androidapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.models.CalendarModel;
import id.simtaq.androidapp.models.Kegiatan;
import id.simtaq.androidapp.viewholder.JadwalKegiatanViewHolder;

public class JadwalKegiatanAdapter extends RecyclerView.Adapter<JadwalKegiatanViewHolder> {

    Context context;
    ArrayList <Kegiatan> kegiatanList;
    int tipe;

    public JadwalKegiatanAdapter(Context context, ArrayList<Kegiatan> kegiatanList, int tipe) {
        this.context = context;
        this.kegiatanList = kegiatanList;
        this.tipe = tipe;
    }

    public int getItemViewType(final int position){
        if (tipe == 1){
            return R.layout.list_infokegiatan;
        } else{
            return R.layout.list_kegiatan;
        }
    }

    @NonNull
    @Override
    public JadwalKegiatanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new JadwalKegiatanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalKegiatanViewHolder holder, int position) {
        final Kegiatan kegiatan = kegiatanList.get(position);
        if (tipe == 1) {
            holder.tvKetInfoKegiatan.setText(kegiatan.getNamaKegiatan());
            holder.tvTglInfoKegiatan.setText(kegiatan.getTanggalKegiatan());
        } else {
            holder.tvTanggalKegiatan.setText(dateFormat(kegiatan.getTanggalKegiatan()));
            holder.tvNamaKegiatan.setText(kegiatan.getNamaKegiatan());
            holder.tvJamKegiatan.setText(kegiatan.getWaktuKegiatan());
        }
    }
    @Override
    public int getItemCount() {
        return (kegiatanList != null) ? kegiatanList.size() : 0;
    }

    private String dateFormat(String tanggal){
        String tgl = tanggal;
        Locale locale = new Locale("in", "ID");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", locale);
        Date date = null;
        try {
            date = (Date)formatter.parse(tgl);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("dd", locale);
        String tglBaru = newFormat.format(date);
        return tglBaru;
    }

}
