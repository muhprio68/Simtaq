package id.simtaq.androidapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.models.CalendarModel;
import id.simtaq.androidapp.viewholder.JadwalKegiatanViewHolder;

public class JadwalKegiatanAdapter extends RecyclerView.Adapter<JadwalKegiatanViewHolder> {

    Context context;
    ArrayList <CalendarModel> mData;

    public JadwalKegiatanAdapter(Context context, ArrayList<CalendarModel> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public JadwalKegiatanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
        return new JadwalKegiatanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalKegiatanViewHolder holder, int position) {
        final CalendarModel tanggal = mData.get(position);
        holder.tvDate.setText(String.valueOf(tanggal.getDate()));

        holder.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.context, dateBuilder(tanggal.getDate(), tanggal.getMonth()+1, tanggal.getYear()), Toast.LENGTH_SHORT).show();
                //holder.tvValueDescTanggal.setText(String.valueOf(dateBuilder(tanggal.getDate(), tanggal.getMonth()+1, tanggal.getYear())));
            }
        });

        if (tanggal.getMonth() == tanggal.getCalendarCompare().get(Calendar.MONTH) && tanggal.getYear() == tanggal.getCalendarCompare().get(Calendar.YEAR)){
            holder.tvDate.setTextColor(ContextCompat.getColor(holder.context, R.color.date_true));
        }
        else holder.tvDate.setTextColor(ContextCompat.getColor(holder.context, R.color.date_false));

        String v = "hijau";
        if (String.valueOf(tanggal.getStatus()).equals(v)){
            holder.ivDot.setImageResource(R.drawable.dot);
        }
    }

    private String dateBuilder(int tanggal, int bulan, int tahun) {
        String tgl;
        String bln;
        if (String.valueOf(tanggal).length() == 1){
            tgl= "0"+tanggal;
        } else {
            tgl = ""+tanggal;
        }
        if (String.valueOf(bulan).length() == 1){
            bln = "0"+bulan;
        } else {
            bln = ""+bulan;
        }
        return tgl+"-"+bln+"-"+tahun;
    }

    @Override
    public int getItemCount() {
        return (mData != null) ? mData.size() : 0;
    }
}
