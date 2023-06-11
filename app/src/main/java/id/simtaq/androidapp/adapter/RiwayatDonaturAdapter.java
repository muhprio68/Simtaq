package id.simtaq.androidapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.RiwayatDonaturActivity;
import id.simtaq.androidapp.models.Donatur;
import id.simtaq.androidapp.models.Keuangan;
import id.simtaq.androidapp.viewholder.RiwayatDonaturViewHolder;
import id.simtaq.androidapp.viewholder.RiwayatViewHolder;

import static id.simtaq.androidapp.helper.config.toRupiah;
import static id.simtaq.androidapp.helper.config.url;

public class RiwayatDonaturAdapter extends RecyclerView.Adapter<RiwayatDonaturViewHolder>{

    private String token;
    private ArrayList<Donatur> donaturList;
    private Context context;
    private RiwayatDonaturAdapter.IRiwayatDonaturAdapter iRiwayatDonaturAdapter;
    private RequestQueue queue;
    private ConstraintLayout clRiwayatDonatur;

    public RiwayatDonaturAdapter(String token, ArrayList<Donatur> donaturList, Context context, RiwayatDonaturAdapter.IRiwayatDonaturAdapter iRiwayatDonaturAdapter, RequestQueue queue, ConstraintLayout clRiwayatDonatur) {
        this.token = token;
        this.donaturList = donaturList;
        this.context = context;
        this.iRiwayatDonaturAdapter = iRiwayatDonaturAdapter;
        this.queue = queue;
        this.clRiwayatDonatur = clRiwayatDonatur;
    }

    public int getItemViewType(final int position){
        return R.layout.list_donatur;
    }

    @NonNull
    @Override
    public RiwayatDonaturViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RiwayatDonaturViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatDonaturViewHolder holder, int position) {
        final Donatur donatur = donaturList.get(position);
        holder.tvWilRiwayatDonatur.setText(donatur.getWilayahDonatur());
        holder.tvPetugasRiwayatDonatur.setText(donatur.getPetugasDonatur());
        holder.tvNominalRiwayatDonatur.setText(donatur.getNominaldonatur()+"");

        holder.clLisRiwayatDonatur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iRiwayatDonaturAdapter.doClick(donatur.getIdKeuangan());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (donaturList != null) ? donaturList.size() : 0;
    }

    public interface IRiwayatDonaturAdapter{
        void doClick(int id);
    }

    public void removeItem(int position) {
        deleteData(donaturList.get(position).getIdDonatur());
        donaturList.remove(position);
        //notifyItemRemoved(position);
    }

    public void deleteData(int id){
        StringRequest dr = new StringRequest(Request.Method.DELETE, url+"/donatur/"+id,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Snackbar snackbar = Snackbar
                                .make(clRiwayatDonatur, "Data keuangan berhasil dihapus", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar snackbar = Snackbar
                                .make(clRiwayatDonatur, "Gagal menghapus data keuangan", Snackbar.LENGTH_LONG);
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
