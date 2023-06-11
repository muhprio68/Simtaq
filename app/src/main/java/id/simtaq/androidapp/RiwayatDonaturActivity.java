package id.simtaq.androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import id.simtaq.androidapp.adapter.RiwayatDonaturAdapter;
import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.helper.Preferences;
import id.simtaq.androidapp.models.Donatur;
import id.simtaq.androidapp.models.Keuangan;

import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.url;

public class RiwayatDonaturActivity extends AppCompatActivity implements RiwayatDonaturAdapter.IRiwayatDonaturAdapter {

    private ConstraintLayout clRiwayatDonatur;
    private Toolbar toolbar;
    private ProgressBar pbRiwayatDonatur;
    private RecyclerView rvRiwayatDonatur;
    private ArrayList<Donatur> donaturList;
    public RiwayatDonaturAdapter adapter;
    private RequestQueue queue;
    private String authToken;
    private String level;
    private String sBulanTahun, sBulan, sTahun, sFilterBulanTahun;
    private SimpleDateFormat bulanTahun, tahun, bulan, filterBulanTahun;
    private Calendar c;
    private TextView tvFilterBulanKeuangan;
    private TextView tvFilterTahunKeuangan;
    private ImageView ivNext;
    private ImageView ivPrev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_donatur);
        initViews();

        authToken = Preferences.getKeyToken(RiwayatDonaturActivity.this);
        level = Preferences.getKeyLevel(RiwayatDonaturActivity.this);
        //addData();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Riwayat Donatur");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        donaturList = new ArrayList<>();
        queue = Volley.newRequestQueue(RiwayatDonaturActivity.this);
        c = Calendar.getInstance();
        bulanTahun = new SimpleDateFormat("yyyy-MM", locale);
        tahun = new SimpleDateFormat("yyyy", locale);
        bulan = new SimpleDateFormat("MMMM", locale);
        filterBulanTahun = new SimpleDateFormat("yyyy-MM", locale);

        sBulanTahun = bulanTahun.format(c.getTime());
        sTahun = tahun.format(c.getTime());
        sBulan = bulan.format(c.getTime());
        sFilterBulanTahun = filterBulanTahun.format(c.getTime());

        getDataDonatur(sFilterBulanTahun, authToken);
        tvFilterBulanKeuangan.setText(sBulan);
        tvFilterTahunKeuangan.setText(sTahun);
        initLevel(level);
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
        toolbar = findViewById(R.id.tbRiwayatDonatur);
        clRiwayatDonatur = findViewById(R.id.clRiwayatDonatur);
        rvRiwayatDonatur= findViewById(R.id.rvRiwayatDonatur);
        pbRiwayatDonatur = findViewById(R.id.pbRiwayatDonatur);
        tvFilterBulanKeuangan = findViewById(R.id.tvFilterBlnRiwayatDonatur);
        tvFilterTahunKeuangan = findViewById(R.id.tvFilterThnRiwayatDonatur);
        ivNext = findViewById(R.id.ivNextDonatur);
        ivPrev = findViewById(R.id.ivPrevDonatur);
    }

    private void initLevel(String level){
        if (level.equals("2") || level.equals("4")) {
            enableSwipeToDelete();
        }
    }

    public void getDataDonatur (String filter, String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/donatur", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbRiwayatDonatur.setVisibility(View.GONE);
                rvRiwayatDonatur.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        int idDonatur = responseObj.getInt("id_donatur");
                        int idKeuangan = responseObj.getInt("id_keuangan");
                        String tglDonatur = responseObj.getString("tgl_donatur");
                        String wilayahDonatur = responseObj.getString("wilayah_donatur");
                        String petugasDonatur = responseObj.getString("petugas_donatur");
                        Long nominalDonatur = responseObj.getLong("nominal_donatur");
                        String createAt = responseObj.getString("create_at");
                        String updateAt = responseObj.getString("update_at");
                        if (tglDonatur.contains(filter)){
                            donaturList.add(new Donatur(idDonatur, idKeuangan, tglDonatur, wilayahDonatur, petugasDonatur, nominalDonatur, createAt, updateAt));
                            Collections.sort(donaturList, new Comparator<Donatur>() {
                                @Override
                                public int compare(Donatur donatur, Donatur k1) {
                                    return donatur.getTglDonatur().compareTo(k1.getTglDonatur());
                                }
                            });
                            buildRecyclerView();
                            doNextCurentTime();
                        } else{
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
                Toast.makeText(RiwayatDonaturActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
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
        adapter = new RiwayatDonaturAdapter(authToken, donaturList, RiwayatDonaturActivity.this, this, queue, clRiwayatDonatur);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvRiwayatDonatur.setHasFixedSize(true);
        rvRiwayatDonatur.setLayoutManager(manager);
        rvRiwayatDonatur.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void enableSwipeToDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                hapusDialog(position);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rvRiwayatDonatur);
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
            donaturList.clear();
            getDataDonatur(sFilterBulanTahun, authToken);
            tvFilterBulanKeuangan.setText(sBulan);
            tvFilterTahunKeuangan.setText(sTahun);
            doNextCurentTime();
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(RiwayatDonaturActivity.this, "Gagal memuat list transaksi" , Toast.LENGTH_LONG).show();
        }
    }


    public void doNextCurentTime(){
        try {
            final Calendar b = Calendar.getInstance();
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
            Toast.makeText(RiwayatDonaturActivity.this, "Gagal memuat riwayat sebelum atau sesudah", Toast.LENGTH_LONG).show();
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
    public void doClick(int id) {
        Intent intent = new Intent(RiwayatDonaturActivity.this, DetailKeuanganActivity.class);
        intent.putExtra("intentDari", "riwayat keuangan");
        intent.putExtra("idKeuangan", id);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}