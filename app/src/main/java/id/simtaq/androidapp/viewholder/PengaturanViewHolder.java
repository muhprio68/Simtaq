package id.simtaq.androidapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.simtaq.androidapp.R;
public class PengaturanViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivIconPengaturan;
    public TextView tvNamaPengaturan;
    public RelativeLayout rlListPengaturan;

    public PengaturanViewHolder(@NonNull View itemView) {
        super(itemView);
        ivIconPengaturan = itemView.findViewById(R.id.ivIcPengaturan);
        tvNamaPengaturan = itemView.findViewById(R.id.tvNamaListPengaturan);
        rlListPengaturan = itemView.findViewById(R.id.rlListPengaturan);
    }
}
