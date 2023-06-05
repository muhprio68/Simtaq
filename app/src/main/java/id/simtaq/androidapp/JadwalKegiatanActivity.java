package id.simtaq.androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.simtaq.androidapp.adapter.JadwalKegiatanAdapter;
import id.simtaq.androidapp.helper.Preferences;
import id.simtaq.androidapp.models.Kegiatan;

import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.url;

public class JadwalKegiatanActivity extends AppCompatActivity implements JadwalKegiatanAdapter.IJadwalKegiatanAdapter {

    private RelativeLayout rlJadwalKegiatan;
    private Toolbar toolbar;
    private ArrayList<Kegiatan> kegiatanList;
    private JadwalKegiatanAdapter adapter;
    private RequestQueue queue;
    private String authToken;
    private String sBulanTahun, sBulan, sTahun, sFilterBulanTahun;
    private SimpleDateFormat bulanTahun, tahun, bulan, filterBulanTahun;

    private TextView tvFilterBulanKegiatan;
    private TextView tvFilterTahunKegiatan;
    private ImageView ivNext;
    private ImageView ivPrev;
    private RecyclerView rvJadwalKegiatan;

    private Calendar c;

    private ProgressBar pbJadwalKegiatan;


    public JadwalKegiatanActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_kegiatan);
        initViews();
        authToken = Preferences.getKeyToken(JadwalKegiatanActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Jadwal Kegiatan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        //addData();
        kegiatanList = new ArrayList<>();
        queue = Volley.newRequestQueue(JadwalKegiatanActivity.this);
        c = Calendar.getInstance();
        bulanTahun = new SimpleDateFormat("yyyy-MM", locale);
        tahun = new SimpleDateFormat("yyyy", locale);
        bulan = new SimpleDateFormat("MMMM", locale);
        filterBulanTahun = new SimpleDateFormat("yyyy-MM", locale);

        sBulanTahun = bulanTahun.format(c.getTime());
        sTahun = tahun.format(c.getTime());
        sBulan = bulan.format(c.getTime());
        sFilterBulanTahun = filterBulanTahun.format(c.getTime());

        getData(sFilterBulanTahun, authToken);
        tvFilterBulanKegiatan.setText(sBulan);
        tvFilterTahunKegiatan.setText(sTahun);
        //doNextCurentTime();

        enableSwipeToDeleteAndUndo();
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNextPrev("N");
            }
        });

        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNextPrev("P");
            }
        });

    }

    public void initViews(){
        toolbar = findViewById(R.id.tbJadwalKegiatan);
        rlJadwalKegiatan = findViewById(R.id.rlJadwalKegiatan);
        pbJadwalKegiatan = findViewById(R.id.pbJadwalKegiatan);
        tvFilterBulanKegiatan = findViewById(R.id.tvFilterBulanKegiatan);
        tvFilterTahunKegiatan = findViewById(R.id.tvFilterTahunKegiatan);
        ivNext = findViewById(R.id.ivNext);
        ivPrev = findViewById(R.id.ivPrev);
        rvJadwalKegiatan = findViewById(R.id.rvJadwalKegiatan);
    }

//    public void addData(){
//        kegiatanList = new ArrayList<>();
//        kegiatanList.add(new Kegiatan("KEG00001", true, "Pengajian Rutin", "22/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","KH.Abdul Kholiq Hasan, M.HI."));
//        kegiatanList.add(new Kegiatan("KEG00002", true, "Sholawat Diba' Rutin", "22/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. Suhardiman"));
//        kegiatanList.add(new Kegiatan("KEG00003", false, "Rapat takmir", "18/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. H.M.Supeno"));
//        kegiatanList.add(new Kegiatan("KEG00004", true, "Istighosah Rutin", "25/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. H.M.Supeno"));
//        kegiatanList.add(new Kegiatan("KEG00005", true, "Sholawat Diba'", "29/03/2022","19.30", "Pengajuan Rutin Sabtu Pon","Masjid At-Taqwa","Bpk. M. Khoirul Huda"));
//    }

    public void getData(String filter, String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/kegiatan", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbJadwalKegiatan.setVisibility(View.GONE);
                rvJadwalKegiatan.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        String idKegiatan = responseObj.getString("id_kegiatan");
                        String noKegiatan = responseObj.getString("no_kegiatan");
                        String namaKegiatan = responseObj.getString("nama_kegiatan");
                        String tipeKegiatan = responseObj.getString("tipe_kegiatan");
                        String tglKegiatan = responseObj.getString("tgl_kegiatan");
                        String waktuKegiatan = responseObj.getString("waktu_kegiatan");
                        String tempatKegiatan = responseObj.getString("tempat_kegiatan");
                        String pembicaraKegiatan = responseObj.getString("pembicara_kegiatan");
                        String deskripsiKegiatan = responseObj.getString("deskripsi_kegiatan");
                        String createAt = responseObj.getString("create_at");
                        String updateAt = responseObj.getString("update_at");
                        if (tglKegiatan.contains(filter)){
                            kegiatanList.add(new Kegiatan(idKegiatan, noKegiatan, namaKegiatan, tipeKegiatan, tglKegiatan, waktuKegiatan, tempatKegiatan, pembicaraKegiatan, deskripsiKegiatan, createAt, updateAt));
                            Collections.sort(kegiatanList, new Comparator<Kegiatan>() {
                                @Override
                                public int compare(Kegiatan kegiatan, Kegiatan k1) {
                                    return kegiatan.getTglKegiatan().compareTo(k1.getTglKegiatan());
                                }
                            });
                            buildRecyclerView();
                            doNextCurentTime();
                        } else {
                            buildRecyclerView();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(JadwalKegiatanActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void buildRecyclerView(){
        adapter = new JadwalKegiatanAdapter(JadwalKegiatanActivity.this,kegiatanList, 1, this, queue, rlJadwalKegiatan);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvJadwalKegiatan.setHasFixedSize(true);
        rvJadwalKegiatan.setLayoutManager(manager);
        rvJadwalKegiatan.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                hapusDialog(position);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rvJadwalKegiatan);
    }

    public void hapusDialog(int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus Data Kegiatan");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Yakin menghapus kegiatan ini?")
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        adapter.removeItem(position);
                        buildRecyclerView();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                        buildRecyclerView();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void doNextPrev(String o) {
        try {
            int step = 8; //pindah sebelum-sesudah
            if (o.equals("N"))
                c.add(Calendar.MONTH, 1);
            else
                c.add(Calendar.MONTH, -1);

            sBulanTahun = bulanTahun.format(c.getTime());
            sTahun = tahun.format(c.getTime());
            sBulan = bulan.format(c.getTime());
            SimpleDateFormat sdfFilterBulanTahun = new SimpleDateFormat("yyyy-MM", locale);
            sFilterBulanTahun = sdfFilterBulanTahun.format(c.getTime());
            kegiatanList.clear();
            tvFilterBulanKegiatan.setText(sBulan);
            getData(sFilterBulanTahun, authToken);
            tvFilterTahunKegiatan.setText(sTahun);
            //adapter.notifyDataSetChanged();
            doNextCurentTime();
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(JadwalKegiatanActivity.this, "Gagal memuat list transaksi" , Toast.LENGTH_LONG).show();
        }
    }


    public void doNextCurentTime(){
        try {
            final Calendar b = Calendar.getInstance();
            b.add(Calendar.MONTH, 3);
            if (sBulanTahun.equals(tampilkanTanggalDanWaktu(b.getTime(),"yyyy-MM", locale))) {
                ivNext.setClickable(false);
                ivNext.setImageResource(R.drawable.ic_next_abu);
                ivNext.setEnabled(false);
            } else {
                ivNext.setEnabled(true);
                ivNext.setImageResource(R.drawable.ic_next);
                ivNext.setClickable(true);
            }
        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(JadwalKegiatanActivity.this, "Gagal memuat riwayat sebelum atau sesudah", Toast.LENGTH_LONG).show();
        }
    }

    private String tampilkanTanggalDanWaktu(Date tanggalDanWaktu, String pola, Locale lokal) {
        String tanggalStr;
        SimpleDateFormat formatter;
        if (lokal == null) {
            formatter = new SimpleDateFormat(pola);
        } else {
            formatter = new SimpleDateFormat(pola, lokal);
        }
        tanggalStr = formatter.format(tanggalDanWaktu);
        return tanggalStr;
    }

    @Override
    public void doClick(String id) {
        Intent intent = new Intent(JadwalKegiatanActivity.this, DetailKegiatanActivity.class);
        intent.putExtra("idKegiatan", id);
        startActivity(intent);
    }
}