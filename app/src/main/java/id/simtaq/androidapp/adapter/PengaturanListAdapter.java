package id.simtaq.androidapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.models.Pengaturan;
import id.simtaq.androidapp.viewholder.PengaturanViewHolder;

public class PengaturanListAdapter extends RecyclerView.Adapter<PengaturanViewHolder> {

    Context mContext;
    ArrayList<Pengaturan> pengaturanList;
    IPengaturanAdapter iPengaturanAdapter;

    public PengaturanListAdapter(Context mContext, ArrayList<Pengaturan> pengaturanList) {
        this.mContext = mContext;
        this.pengaturanList = pengaturanList;
        //this.iPengaturanAdapter = iPengaturanAdapter;
    }

    public int getItemViewType(final int position){
        return R.layout.list_pengaturan;
    }

    @NonNull
    @Override
    public PengaturanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new PengaturanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PengaturanViewHolder holder, int position) {
        final Pengaturan pengaturan = pengaturanList.get(position);
        final int idPengaturan = pengaturan.getIdPengaturan();
        holder.ivIconPengaturan.setImageResource(pengaturan.getIconPengaturan());
        holder.tvNamaPengaturan.setText(pengaturan.getNamaPengaturan());
        if (position == pengaturanList.size()-1){
            holder.vGaris.setVisibility(View.GONE);
        }
//        holder.rlListPengaturan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                iPengaturanAdapter.doClick(idPengaturan);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (pengaturanList != null) ? pengaturanList.size() : 0;
    }

    public interface IPengaturanAdapter{
        void doClick(int id);
    }
}
