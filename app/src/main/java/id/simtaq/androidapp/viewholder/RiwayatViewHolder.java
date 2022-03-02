package id.simtaq.androidapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import id.simtaq.androidapp.R;

public class RiwayatViewHolder extends RecyclerView.ViewHolder {

    public TextView tvKeteranganRiwayat;
    public TextView tvTanggalRiwayat;
    public TextView tvJmlUang;
    public View vGaris;

    public RiwayatViewHolder(@NonNull View itemView) {
        super(itemView);
        tvKeteranganRiwayat = itemView.findViewById(R.id.tvKeteranganRiwayat);
        tvTanggalRiwayat = itemView.findViewById(R.id.tvTanggalRiwayat);
        tvJmlUang = itemView.findViewById(R.id.randomText);
        vGaris = itemView.findViewById(R.id.garisListRiwayat);
    }

    public TextView getView(){
        return tvJmlUang;
    }
}
