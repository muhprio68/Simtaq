package id.simtaq.androidapp.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.simtaq.androidapp.R;

public class JadwalKegiatanViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTanggalKegiatan;
    public TextView tvNamaKegiatan;
    public TextView tvJamKegiatan;
    public TextView tvKetInfoKegiatan;
    public TextView tvTglInfoKegiatan;
    public RelativeLayout rlListKegiatan;

    public JadwalKegiatanViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTanggalKegiatan = itemView.findViewById(R.id.tvTanggalKegiatan);
        tvNamaKegiatan = itemView.findViewById(R.id.tvNamaKegiatan);
        tvJamKegiatan = itemView.findViewById(R.id.tvJamKegiatan);
        tvKetInfoKegiatan = itemView.findViewById(R.id.tvKeteranganInfoKegiatan);
        tvTglInfoKegiatan = itemView.findViewById(R.id.tvTglInfoKegiatan);
        rlListKegiatan = itemView.findViewById(R.id.rlListKegiatan);
    }
}
