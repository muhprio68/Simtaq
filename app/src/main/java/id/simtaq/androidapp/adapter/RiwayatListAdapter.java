package id.simtaq.androidapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.RiwayatActivity;
import id.simtaq.androidapp.models.Keuangan;
import id.simtaq.androidapp.viewholder.RiwayatViewHolder;

import static id.simtaq.androidapp.helper.config.formatLihatTanggal;
import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.toRupiah;
import static id.simtaq.androidapp.helper.config.url;

public class RiwayatListAdapter extends RecyclerView.Adapter<RiwayatViewHolder> {
    private String token;
    private ArrayList<Keuangan> keuanganList;
    private Context context;
    private int tipe;
    private IRiwayatListAdapter iRiwayatListAdapter;
    private RequestQueue queue;
    private ConstraintLayout clRiwayatKeuangan;

    public RiwayatListAdapter(String token, ArrayList<Keuangan> keuanganList, Context context, int tipe, IRiwayatListAdapter iRiwayatListAdapter, RequestQueue queue, ConstraintLayout clRiwayatKeuangan) {
        this.token = token;
        this.keuanganList = keuanganList;
        this.context = context;
        this.tipe = tipe;
        this.iRiwayatListAdapter = iRiwayatListAdapter;
        this.queue = queue;
        this.clRiwayatKeuangan = clRiwayatKeuangan;
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
        final Keuangan keuangan = keuanganList.get(position);
        if (tipe == 1) {
            if (keuangan.getTipeKeuangan().equals("Pemasukan") ){
                holder.cvIconRiwayat.getBackground().setTint(ContextCompat.getColor(context, R.color.jmlPemasukan));
                holder.ivIconRiwayat.setImageResource(R.drawable.ic_bullish);
                holder.tvJmlUang.setText("+ "+toRupiah(keuangan.getNominalKeuangan()+""));
                holder.tvJmlUang.setTextColor(ContextCompat.getColor(context, R.color.jmlPemasukan));
            } else {
                holder.cvIconRiwayat.getBackground().setTint(ContextCompat.getColor(context, R.color.jmlPengeluaran));
                holder.ivIconRiwayat.setImageResource(R.drawable.ic_bearish);
                holder.tvJmlUang.setText("- "+toRupiah(keuangan.getNominalKeuangan()+""));
                holder.tvJmlUang.setTextColor(ContextCompat.getColor(context, R.color.jmlPengeluaran));
            }

            holder.tvKeteranganRiwayat.setText(keuangan.getKetKeuangan());
            holder.tvTanggalRiwayat.setText(formatLihatTanggal(keuangan.getTglKeuangan()));

//            if (position == getItemCount()-1){
//                holder.vGaris.setVisibility(View.GONE);
//            } else {
//                holder.vGaris.setVisibility(View.VISIBLE);
//            }

            holder.rlLisRiwayat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iRiwayatListAdapter.doClick(keuangan.getIdKeuangan());
                }
            });
        } else {
            if (keuangan.getTipeKeuangan().equals("Pemasukan")){
                holder.tvJmlInfoKas.setText("+ "+toRupiah(keuangan.getNominalKeuangan()+""));
                holder.tvJmlInfoKas.setTextColor(ContextCompat.getColor(context, R.color.jmlPemasukan));
            } else {
                holder.tvJmlInfoKas.setText("- "+toRupiah(keuangan.getNominalKeuangan()+""));
                holder.tvJmlInfoKas.setTextColor(ContextCompat.getColor(context, R.color.jmlPengeluaran));
            }
            holder.tvKeteranganInfoKas.setText(keuangan.getKetKeuangan());
            holder.tvTglInfoKas.setText(formatLihatTanggal(keuangan.getTglKeuangan()));
            holder.rlListInfoKas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iRiwayatListAdapter.doClick(keuangan.getIdKeuangan());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (tipe == 1){
            return (keuanganList != null) ? keuanganList.size() : 0;
        } else {
            if (keuanganList.size() < 5){
                return (keuanganList != null) ? keuanganList.size() : 0;
            } else {
                return 5;
            }
        }
    }

    public interface IRiwayatListAdapter{
        void doClick(int id);
    }

    public void removeItem(int position) {
        deleteData(keuanganList.get(position).getIdKeuangan());
        keuanganList.remove(position);
        //notifyItemRemoved(position);
    }

    public void deleteData(int id){
        StringRequest dr = new StringRequest(Request.Method.DELETE, url+"/keuangan/"+id,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Snackbar snackbar = Snackbar
                                .make(clRiwayatKeuangan, "Data keuangan berhasil dihapus", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar snackbar = Snackbar
                                .make(clRiwayatKeuangan, "Gagal menghapus data keuangan", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(dr);
    }
}
