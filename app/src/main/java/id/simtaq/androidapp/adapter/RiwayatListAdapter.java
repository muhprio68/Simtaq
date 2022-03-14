package id.simtaq.androidapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.RiwayatActivity;
import id.simtaq.androidapp.models.RiwayatKas;
import id.simtaq.androidapp.viewholder.RiwayatViewHolder;

public class RiwayatListAdapter extends RecyclerView.Adapter<RiwayatViewHolder> {

    ArrayList<RiwayatKas> riwayatKasList;
    Context context;
    int tipe;
    IRiwayatListAdapter iRiwayatListAdapter;

    public RiwayatListAdapter(ArrayList<RiwayatKas> riwayatKasList, Context context, int tipe, IRiwayatListAdapter iRiwayatListAdapter) {
        this.riwayatKasList = riwayatKasList;
        this.context = context;
        this.tipe = tipe;
        this.iRiwayatListAdapter = iRiwayatListAdapter;
    }

    public int getItemViewType(final int position){
        if (tipe == 1){
            return R.layout.list_riwayat;
        } else{
            return R.layout.list_infouangkas;
        }
    }

    @NonNull
    @Override
    public RiwayatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RiwayatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatViewHolder holder, int position) {
        final RiwayatKas riwayatKas = riwayatKasList.get(position);
        if (tipe == 1) {
            if (riwayatKas.isPemasukan() == true ){
                holder.cvIconRiwayat.getBackground().setTint(ContextCompat.getColor(context, R.color.jmlPemasukan));
                holder.ivIconRiwayat.setImageResource(R.drawable.ic_bullish);
                holder.tvJmlUang.setText("+ Rp. "+String.valueOf(riwayatKas.getNominal()));
                holder.tvJmlUang.setTextColor(ContextCompat.getColor(context, R.color.jmlPemasukan));
            } else {
                holder.cvIconRiwayat.getBackground().setTint(ContextCompat.getColor(context, R.color.jmlPengeluaran));
                holder.ivIconRiwayat.setImageResource(R.drawable.ic_bearish);
                holder.tvJmlUang.setText("- Rp. "+String.valueOf(riwayatKas.getNominal()));
                holder.tvJmlUang.setTextColor(ContextCompat.getColor(context, R.color.jmlPengeluaran));
            }

            holder.tvKeteranganRiwayat.setText(riwayatKas.getKeterangan());
            holder.tvTanggalRiwayat.setText(riwayatKas.getTanggal());

            if (position == getItemCount()-1){
                holder.vGaris.setVisibility(View.GONE);
            } else {
                holder.vGaris.setVisibility(View.VISIBLE);
            }

            holder.rlLisRiwayat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iRiwayatListAdapter.doClick(riwayatKas.getId());
                }
            });
        } else {
            if (riwayatKas.isPemasukan() == true){
                holder.tvJmlInfoKas.setText("+ Rp. "+String.valueOf(riwayatKas.getNominal()));
                holder.tvJmlInfoKas.setTextColor(ContextCompat.getColor(context, R.color.jmlPemasukan));
            } else {
                holder.tvJmlInfoKas.setText("- Rp. "+String.valueOf(riwayatKas.getNominal()));
                holder.tvJmlInfoKas.setTextColor(ContextCompat.getColor(context, R.color.jmlPengeluaran));
            }
            holder.tvKeteranganInfoKas.setText(riwayatKas.getKeterangan());
            holder.tvTglInfoKas.setText(riwayatKas.getTanggal());
        }
    }

    @Override
    public int getItemCount() {
        if (tipe == 1){
            return (riwayatKasList != null) ? riwayatKasList.size() : 0;
        } else {
            if (riwayatKasList.size() < 5){
                return (riwayatKasList != null) ? riwayatKasList.size() : 0;
            } else {
                return 5;
            }
        }
    }

    public interface IRiwayatListAdapter{
        void doClick(String id);
    }
}
