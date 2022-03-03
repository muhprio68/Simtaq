package id.simtaq.androidapp.adapter;

import android.content.Context;
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalKegiatanViewHolder holder, int position) {
        final CalendarModel tanggal = mData.get(position);
        holder.tvDate.setText(tanggal.getDate());

        holder.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.context, dateBuilder(tanggal.getDate(), tanggal.getMonth()+1, tanggal.getYear()), Toast.LENGTH_SHORT).show();
            }
        });

        if (tanggal.getMonth() == tanggal.getCalendarCompare().get(Calendar.MONTH) && tanggal.getYear() == tanggal.getCalendarCompare().get(Calendar.YEAR)){
            holder.tvDate.setTextColor(ContextCompat.getColor(holder.context, R.color.date_true));
        }
        else holder.tvDate.setTextColor(ContextCompat.getColor(holder.context, R.color.date_false));

        if (tanggal.getStatus().equals("hijau")){
            holder.ivDot.setImageDrawable(ContextCompat.getDrawable(holder.context,R.drawable.dot));
        }
    }

    private String dateBuilder(int tanggal, int bulan, int tahun) {
        String tgl;
        String bln;
        String thn;
        if (String.valueOf(tanggal).length() == 1){
            tgl= "0$tanggal";
        } else {
            tgl = ""+tanggal;
        }
        if (String.valueOf(bulan).length() == 1){
            bln = "0$bulan";
        } else {
            bln = ""+bulan;
        }
        return "$tgl-$bln-$tahun";
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
