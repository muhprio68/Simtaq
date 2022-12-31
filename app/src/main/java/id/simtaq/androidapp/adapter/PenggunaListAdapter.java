package id.simtaq.androidapp.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.models.Pengaturan;
import id.simtaq.androidapp.models.Pengguna;
import id.simtaq.androidapp.viewholder.PengaturanViewHolder;
import id.simtaq.androidapp.viewholder.PenggunaViewHolder;

public class PenggunaListAdapter extends RecyclerView.Adapter<PenggunaViewHolder>{
    ArrayList<Pengguna> penggunaList;
    Context context;
    int tipe;
    IPenggunaAdapter iPenggunaAdapter;
    RequestQueue queue;
    CoordinatorLayout clListPengguna;

    public PenggunaListAdapter(ArrayList<Pengguna> penggunaList, Context context, int tipe, IPenggunaAdapter iPenggunaAdapter, RequestQueue queue, CoordinatorLayout clListPengguna) {
        this.penggunaList = penggunaList;
        this.context = context;
        this.tipe = tipe;
        this.iPenggunaAdapter = iPenggunaAdapter;
        this.queue = queue;
        this.clListPengguna = clListPengguna;
    }

    public int getItemViewType(final int position){
        return R.layout.list_pengguna;
    }

    @NonNull
    @Override
    public PenggunaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new PenggunaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenggunaViewHolder holder, int position) {
        final Pengguna pengguna = penggunaList.get(position);
        final int idPengguna = pengguna.getId();
        holder.tvNamaPengguna.setText(pengguna.getNama());
        String tipePengguna;
        if (pengguna.getLevel().equals("1")){
            tipePengguna = "Jama'ah masjid";
        } else if (pengguna.getLevel().equals("2")){
            tipePengguna = "Bendahara takmir";
        } else if (pengguna.getLevel().equals("3")){
            tipePengguna = "Humas takmir";
        } else {
            tipePengguna = "Superadmin";
        }
        holder.tvTipePengguna.setText(tipePengguna);
        holder.tvEmailPengguna.setText(pengguna.getEmail());
        //holder.ivIconPengaturan.setImageResource(pengaturan.getIconPengaturan());
        if (position == penggunaList.size()-1){
            holder.vGaris.setVisibility(View.GONE);
        }
        holder.clListPengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iPenggunaAdapter.doClick(idPengguna);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (penggunaList != null) ? penggunaList.size() : 0;
    }

    public interface IPenggunaAdapter{
        void doClick(int id);
    }
}
