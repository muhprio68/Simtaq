package id.simtaq.androidapp.adapter;

import android.content.Context;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.simtaq.androidapp.JadwalKegiatanActivity;
import id.simtaq.androidapp.R;
import id.simtaq.androidapp.models.Kegiatan;
import id.simtaq.androidapp.viewholder.JadwalKegiatanViewHolder;

import static id.simtaq.androidapp.helper.config.url;

public class JadwalKegiatanAdapter extends RecyclerView.Adapter<JadwalKegiatanViewHolder> {

    private String token;
    private Context context;
    private ArrayList <Kegiatan> kegiatanList;
    private int tipe;
    private IJadwalKegiatanAdapter iJadwalKegiatanAdapter;
    private RequestQueue queue;
    private ConstraintLayout clJadwalKegiatan;

    public JadwalKegiatanAdapter(String token, Context context, ArrayList<Kegiatan> kegiatanList, int tipe, IJadwalKegiatanAdapter iJadwalKegiatanAdapter, RequestQueue queue, ConstraintLayout clJadwalKegiatan) {
        this.token = token;
        this.context = context;
        this.kegiatanList = kegiatanList;
        this.tipe = tipe;
        this.iJadwalKegiatanAdapter = iJadwalKegiatanAdapter;
        this.queue = queue;
        this.clJadwalKegiatan = clJadwalKegiatan;
    }

    public int getItemViewType(final int position){
        if (tipe == 1){
            return R.layout.list_kegiatan;
        } else{
            return R.layout.list_infokegiatan;
        }
    }

    @NonNull
    @Override
    public JadwalKegiatanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new JadwalKegiatanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalKegiatanViewHolder holder, int position) {
        final Kegiatan kegiatan = kegiatanList.get(position);
        if (tipe == 1) {
            holder.tvTanggalKegiatan.setText(fullDateToDate(kegiatan.getTglKegiatan()));
            holder.tvNamaKegiatan.setText(kegiatan.getNamaKegiatan());
            holder.tvJamKegiatan.setText(timeFormat(kegiatan.getWaktuKegiatan())+" WIB, "+kegiatan.getTempatKegiatan());
            holder.rlListKegiatan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iJadwalKegiatanAdapter.doClick(kegiatan.getIdKegiatan());
                }
            });
        } else {
            holder.tvKetInfoKegiatan.setText(kegiatan.getNamaKegiatan());
            holder.tvTglInfoKegiatan.setText(kegiatan.getTglKegiatan());
            holder.rlListInfoKegiatan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iJadwalKegiatanAdapter.doClick(kegiatan.getIdKegiatan());
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return (kegiatanList != null) ? kegiatanList.size() : 0;
    }

    private String fullDateToDate(String tanggal){
        String tgl = tanggal;
        Locale locale = new Locale("in", "ID");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", locale); //dd/MM/yyyy  yyyy-MM-dd
        Date date = null;
        try {
            date = (Date)formatter.parse(tgl);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("dd", locale);
        String tglBaru = newFormat.format(date);
        return tglBaru;
    }

    private String timeFormat(String waktu){
        String wkt = waktu;
        Locale locale = new Locale("in", "ID");
        DateFormat formatter = new SimpleDateFormat("hh:mm:ss", locale); //dd/MM/yyyy  yyyy-MM-dd
        Date date = null;
        try {
            date = (Date)formatter.parse(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm", locale);
        String wktBaru = newFormat.format(date);
        return wktBaru;
    }

    public interface IJadwalKegiatanAdapter{
        void doClick(int id);
    }

    public void removeItem(int position) {
        deleteData(kegiatanList.get(position).getIdKegiatan());
        kegiatanList.remove(position);
        //notifyItemRemoved(position);
    }

    public void deleteData(int id){
        StringRequest dr = new StringRequest(Request.Method.DELETE, url+"/kegiatan/"+id,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Snackbar snackbar = Snackbar
                                .make(clJadwalKegiatan, "Kegiatan berhasil dihapus", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar snackbar = Snackbar
                                .make(clJadwalKegiatan, "Gagal menghapus kegiatan", Snackbar.LENGTH_LONG);
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

    public void restoreItem(Kegiatan item, int position) {
        kegiatanList.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<Kegiatan> getData() {
        return kegiatanList;
    }
}
