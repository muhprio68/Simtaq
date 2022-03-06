package id.simtaq.androidapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.viewholder.RiwayatViewHolder;

public class RiwayatListAdapter extends RecyclerView.Adapter<RiwayatViewHolder> {

    private Random random;
    Context context;
    public RiwayatListAdapter (int seed, Context context){
        this.random = new Random(seed);
        this.context = context;
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
        final int jmlUang = random.nextInt();
        if (jmlUang > 0 ){
            holder.ivIconRiwayat.setImageResource(R.drawable.ic_circled_up);
            holder.tvJmlUang.setText("+ Rp. "+String.valueOf(jmlUang));
            holder.tvJmlUang.setTextColor(ContextCompat.getColor(context, R.color.jmlPemasukan));
        } else {
            holder.ivIconRiwayat.setImageResource(R.drawable.ic_circled_down);
            holder.tvJmlUang.setText("- Rp. "+String.valueOf(jmlUang*-1));
            holder.tvJmlUang.setTextColor(ContextCompat.getColor(context, R.color.jmlPengeluaran));
        }

        holder.tvKeteranganRiwayat.setText("Kotak Amal "+String.valueOf((int)1+position));
        holder.tvTanggalRiwayat.setText(position+1+" Jan, 2022");

        if (position == getItemCount()-1){
            holder.vGaris.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 13;
    }
}
