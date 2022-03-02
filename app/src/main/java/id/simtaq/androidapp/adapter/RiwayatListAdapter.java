package id.simtaq.androidapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.viewholder.RiwayatViewHolder;

public class RiwayatListAdapter extends RecyclerView.Adapter<RiwayatViewHolder> {

    private Random random;
    public RiwayatListAdapter (int seed){
        this.random = new Random(seed);
    }

    public int getItemViewType(final int position){
        return R.layout.list_riwayat;
    }

    @NonNull
    @Override
    public RiwayatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RiwayatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatViewHolder holder, int position) {
        holder.tvJmlUang.setText(String.valueOf(random.nextInt()));
        holder.tvKeteranganRiwayat.setText("Kotak Amal "+String.valueOf((int)1+position));
        holder.tvTanggalRiwayat.setText(position+1+" Jan, 2022");
    }

    @Override
    public int getItemCount() {
        return 13;
    }
}
