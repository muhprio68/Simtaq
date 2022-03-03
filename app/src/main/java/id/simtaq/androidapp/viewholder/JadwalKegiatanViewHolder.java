package id.simtaq.androidapp.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.simtaq.androidapp.R;

public class JadwalKegiatanViewHolder extends RecyclerView.ViewHolder {
    public TextView tvDate;
    public ImageView ivDot;
    public TextView tvValueDescTanggal;
    public Context context;

    public JadwalKegiatanViewHolder(@NonNull View itemView) {
        super(itemView);
        tvDate = itemView.findViewById(R.id.date);
        ivDot = itemView.findViewById(R.id.dot);
        tvValueDescTanggal = itemView.findViewById(R.id.tvValueDescTanggal);
        context = itemView.getContext();
    }
}
