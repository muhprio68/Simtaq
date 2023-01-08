package id.simtaq.androidapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import id.simtaq.androidapp.R;

public class PenggunaViewHolder extends RecyclerView.ViewHolder {
    public TextView tvNamaPengguna;
    public TextView tvTipePengguna;
    public TextView tvEmailPengguna;
    public ImageView ivIconPengguna;
    public View vGaris;
    public ConstraintLayout clListPengguna;

    public PenggunaViewHolder(@NonNull View itemView) {
        super(itemView);
        tvNamaPengguna = itemView.findViewById(R.id.tvNamaPenggunaList);
        tvTipePengguna = itemView.findViewById(R.id.tvTipePenggunaList);
        tvEmailPengguna = itemView.findViewById(R.id.tvEmailPenggunaList);
        ivIconPengguna = itemView.findViewById(R.id.ivIconPengguna);
        vGaris = itemView.findViewById(R.id.garisListPengguna);
        clListPengguna = itemView.findViewById(R.id.clListPengguna);
    }
}
