package id.simtaq.androidapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import id.simtaq.androidapp.R;

public class RiwayatViewHolder extends RecyclerView.ViewHolder {

    public ConstraintLayout clIconRiwayat;
    public ImageView ivIconRiwayat;
    public TextView tvKeteranganRiwayat;
    public TextView tvTanggalRiwayat;
    public TextView tvJmlUang;
    public View vGaris;
    public ProgressBar pbRiwayat;
    public TextView tvKeteranganInfoKas;
    public TextView tvJmlInfoKas;
    public TextView tvTglInfoKas;
    public RelativeLayout rlIcRiwayat;
    public RelativeLayout rlLisRiwayat;
    public RelativeLayout rlListInfoKas;

    public RiwayatViewHolder(@NonNull View itemView) {
        super(itemView);
        clIconRiwayat = itemView.findViewById(R.id.clIcRiwayat);
        ivIconRiwayat = itemView.findViewById(R.id.ivIcRiwayat);
        tvKeteranganRiwayat = itemView.findViewById(R.id.tvKeteranganRiwayat);
        tvTanggalRiwayat = itemView.findViewById(R.id.tvTanggalRiwayat);
        tvJmlUang = itemView.findViewById(R.id.tvJmlUang);
        vGaris = itemView.findViewById(R.id.garisListRiwayat);
        tvKeteranganInfoKas = itemView.findViewById(R.id.tvKeteranganInfoKas);
        tvJmlInfoKas = itemView.findViewById(R.id.tvJmlInfoKas);
        tvTglInfoKas = itemView.findViewById(R.id.tvTglInfoKas);
        rlIcRiwayat = itemView.findViewById(R.id.rlIcRiwayat);
        rlLisRiwayat = itemView.findViewById(R.id.rlListRiwayat);
        pbRiwayat = itemView.findViewById(R.id.pbRiwayatKeuangan);
        rlListInfoKas = itemView.findViewById(R.id.rlListInfoKas);
    }
}
