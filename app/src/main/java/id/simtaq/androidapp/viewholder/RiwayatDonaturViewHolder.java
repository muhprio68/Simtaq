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

public class RiwayatDonaturViewHolder extends RecyclerView.ViewHolder {
    public CardView cvIconRiwayatDonatur;
    public ImageView ivIconRiwayatDonatur;
    public TextView tvWilRiwayatDonatur;
    public TextView tvPetugasRiwayatDonatur;
    public TextView tvNominalRiwayatDonatur;
    public View vGaris;
    public ProgressBar pbRiwayatDonatur;
    public ConstraintLayout clLisRiwayatDonatur;

    public RiwayatDonaturViewHolder(@NonNull View itemView) {
        super(itemView);
        cvIconRiwayatDonatur = itemView.findViewById(R.id.cvIcRiwayatDonatur);
        ivIconRiwayatDonatur = itemView.findViewById(R.id.ivIcRiwayatDonatur);
        tvWilRiwayatDonatur = itemView.findViewById(R.id.tvRiwayatWilDonatur);
        tvPetugasRiwayatDonatur = itemView.findViewById(R.id.tvRiwayatPetugasDonatur);
        tvNominalRiwayatDonatur = itemView.findViewById(R.id.tvRiwayatNominalDonatur);
        vGaris = itemView.findViewById(R.id.garisListRiwayatDonatur);
        clLisRiwayatDonatur = itemView.findViewById(R.id.clListRiwayatDonatur);
        pbRiwayatDonatur = itemView.findViewById(R.id.pbRiwayatDonatur);
    }
}
