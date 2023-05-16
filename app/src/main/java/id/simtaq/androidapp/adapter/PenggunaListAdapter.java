package id.simtaq.androidapp.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.models.Pengaturan;
import id.simtaq.androidapp.models.Pengguna;
import id.simtaq.androidapp.viewholder.PengaturanViewHolder;
import id.simtaq.androidapp.viewholder.PenggunaViewHolder;

import static id.simtaq.androidapp.helper.config.url;

public class PenggunaListAdapter extends RecyclerView.Adapter<PenggunaViewHolder>{
    ArrayList<Pengguna> penggunaList;
    Context context;
    IPenggunaAdapter iPenggunaAdapter;
    RequestQueue queue;
    ConstraintLayout clListPengguna;
    String token;

    public PenggunaListAdapter(ArrayList<Pengguna> penggunaList, Context context, IPenggunaAdapter iPenggunaAdapter, RequestQueue queue, ConstraintLayout clListPengguna, String token) {
        this.penggunaList = penggunaList;
        this.context = context;
        this.iPenggunaAdapter = iPenggunaAdapter;
        this.queue = queue;
        this.clListPengguna = clListPengguna;
        this.token = token;
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
        holder.ivIconPengguna.setImageResource(R.drawable.ic_ubah_primary);
//        if (position == penggunaList.size()-1){
//            holder.vGaris.setVisibility(View.INVISIBLE);
//        }
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

    public void removeItem(int position) {
        deleteData(penggunaList.get(position).getId());
        penggunaList.remove(position);
        //notifyItemRemoved(position);
    }

    public void deleteData(int id){
        StringRequest dr = new StringRequest(Request.Method.DELETE, url+"/user/"+id,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Snackbar snackbar = Snackbar
                                .make(clListPengguna, "Data pengguna berhasil dihapus", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar snackbar = Snackbar
                                .make(clListPengguna, "Gagal menghapus data pengguna", Snackbar.LENGTH_LONG);
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
