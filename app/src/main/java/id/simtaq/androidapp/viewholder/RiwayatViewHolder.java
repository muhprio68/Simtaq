package id.simtaq.androidapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import id.simtaq.androidapp.R;

public class RiwayatViewHolder extends RecyclerView.ViewHolder {

    public CardView cvIconRiwayat;
    public ImageView ivIconRiwayat;
    public TextView tvKeteranganRiwayat;
    public TextView tvTanggalRiwayat;
    public TextView tvJmlUang;
    public View vGaris;
    public TextView tvKeteranganInfoKas;
    public TextView tvJmlInfoKas;
    public TextView tvTglInfoKas;

    public RiwayatViewHolder(@NonNull View itemView) {
        super(itemView);
        cvIconRiwayat = itemView.findViewById(R.id.cvIcRiwayat);
        ivIconRiwayat = itemView.findViewById(R.id.ivIcRiwayat);
        tvKeteranganRiwayat = itemView.findViewById(R.id.tvKeteranganRiwayat);
        tvTanggalRiwayat = itemView.findViewById(R.id.tvTanggalRiwayat);
        tvJmlUang = itemView.findViewById(R.id.tvJmlUang);
        vGaris = itemView.findViewById(R.id.garisListRiwayat);
        tvKeteranganInfoKas = itemView.findViewById(R.id.tvKeteranganInfoKas);
        tvJmlInfoKas = itemView.findViewById(R.id.tvJmlInfoKas);
        tvTglInfoKas = itemView.findViewById(R.id.tvTglInfoKas);
    }
}
