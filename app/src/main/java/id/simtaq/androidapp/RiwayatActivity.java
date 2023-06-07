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

import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.helper.Preferences;
import id.simtaq.androidapp.models.Keuangan;

import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.url;

public class RiwayatActivity extends AppCompatActivity implements RiwayatListAdapter.IRiwayatListAdapter {

    private ConstraintLayout clRiwayatKeuangan;
    private Toolbar toolbar;
    private ProgressBar pbRiwayatKeuangan;
    private RecyclerView rvRiwayat;
    private ArrayList<Keuangan> keuanganList;
    public RiwayatListAdapter adapter;
    private RequestQueue queue;
    private String authToken;
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
        setContentView(R.layout.activity_riwayat);
        initViews();
        authToken = Preferences.getKeyToken(RiwayatActivity.this);
        //addData();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Riwayat Uang Kas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        keuanganList = new ArrayList<>();
        queue = Volley.newRequestQueue(RiwayatActivity.this);
        c = Calendar.getInstance();
        bulanTahun = new SimpleDateFormat("yyyy-MM", locale);
        tahun = new SimpleDateFormat("yyyy", locale);
        bulan = new SimpleDateFormat("MMMM", locale);
        filterBulanTahun = new SimpleDateFormat("yyyy-MM", locale);

        sBulanTahun = bulanTahun.format(c.getTime());
        sTahun = tahun.format(c.getTime());
        sBulan = bulan.format(c.getTime());
        sFilterBulanTahun = filterBulanTahun.format(c.getTime());

        getDataKeuangan(sFilterBulanTahun, authToken);
        tvFilterBulanKeuangan.setText(sBulan);
        tvFilterTahunKeuangan.setText(sTahun);

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
        toolbar = findViewById(R.id.tbRiwayatKas);
        clRiwayatKeuangan = findViewById(R.id.clRiwayatKeuangan);
        rvRiwayat = findViewById(R.id.rvRiwayat);
        pbRiwayatKeuangan = findViewById(R.id.pbRiwayatKeuangan);
        tvFilterBulanKeuangan = findViewById(R.id.tvFilterBlnRiwayat);
        tvFilterTahunKeuangan = findViewById(R.id.tvFilterThnRiwayat);
        ivNext = findViewById(R.id.ivNextKeuangan);
        ivPrev = findViewById(R.id.ivPrevKeuangan);
    }

//    public void addData(){
//        riwayatKasArrayList = new ArrayList<>();
//        riwayatKasArrayList.add(new RiwayatKas("RK000001","Kotak Amal", true,"01/01/2022","Kotak Amal Sholat Jum'at",450000,8000000, 8450000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000002","Pembayaran Listrik", false,"05/01/2022","Pembayaran Listrik Bulan Desember ajdjhfhfkjhfj ajgdjhdgjhdgdhgd ajhsgjhdgdjhgdjh kjdgjhdgdjhgdjhgdjh",200000, 8450000, 8250000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000003","Kotak Amal", true,"08/01/2022","Kotak Amal Sholat Jum'at",400000, 8250000, 8650000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000004","Renovasi Dinding", false,"13/01/2022","Pengecatan",500000, 8650000,8150000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000005","Kotak Amal", true,"16/01/2022","Kotak Amal Sholat Jum'at",900000, 8150000, 9050000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000006","Kotak Amal", true,"19/01/2022","Kotak Amal Sholat Jum'at",450000, 9050000, 9500000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000007","Pembayaran Listrik", false,"05/02/2022","Pembayaran Listrik Bulan Januari",200000, 9500000, 9300000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000008","Kotak Amal", true,"08/02/2022","Kotak Amal Sholat Jum'at",400000, 9300000, 9700000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000009","Renovasi Dinding", false,"11/02/2022","Pengecatan",500000, 9700000, 9200000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000010","Kotak Amal", true,"15/02/2022","Kotak Amal Sholat Jum'at",900000, 9200000, 10100000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000011","Kotak Amal", true,"27/02/2022","Kotak Amal Sholat Jum'at",450000, 10100000, 10550000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000012","Pembayaran Listrik", false,"05/03/2022","Pembayaran Listrik Bulan Februari",200000, 10550000, 10350000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000013","Kotak Amal", true,"06/03/2022","Kotak Amal Sholat Jum'at",400000, 10350000, 10750000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000014","Renovasi Dinding", false,"08/03/2022","Pengecatan",500000, 10750000, 10250000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000015","Kotak Amal", true,"13/03/2022","Kotak Amal Sholat Jum'at",90000, 10250000, 11150000));
//    }

    public void getDataKeuangan (String filter, String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/keuangan", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbRiwayatKeuangan.setVisibility(View.GONE);
                rvRiwayat.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        int idKeuangan = responseObj.getInt("id_keuangan");
                        String noKeuangan = responseObj.getString("no_keuangan");
                        String tipeKeuangan = responseObj.getString("tipe_keuangan");
                        String tglKeuangan = responseObj.getString("tgl_keuangan");
                        String ketKeuangan = responseObj.getString("keterangan_keuangan");
                        String statusKeuangan = responseObj.getString("status_keuangan");
                        Long nominalKeuangan = responseObj.getLong("nominal_keuangan");
                        Long jmlKasAwal = responseObj.getLong("jml_kas_awal");
                        Long jmlKasAkhir = responseObj.getLong("jml_kas_akhir");
                        String deskripsiKeuangan = responseObj.getString("deskripsi_keuangan");
                        String createAt = responseObj.getString("create_at");
                        String updateAt = responseObj.getString("update_at");
                        if (tglKeuangan.contains(filter)){
                            keuanganList.add(new Keuangan(idKeuangan, noKeuangan, tipeKeuangan, tglKeuangan, ketKeuangan, statusKeuangan, nominalKeuangan, jmlKasAwal, jmlKasAkhir, deskripsiKeuangan, createAt, updateAt));
                            Collections.sort(keuanganList, new Comparator<Keuangan>() {
                                @Override
                                public int compare(Keuangan keuangan, Keuangan k1) {
                                    return keuangan.getTglKeuangan().compareTo(k1.getTglKeuangan());
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
                Toast.makeText(RiwayatActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
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
        adapter = new RiwayatListAdapter(authToken, keuanganList, RiwayatActivity.this, 1, this, queue, clRiwayatKeuangan);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvRiwayat.setHasFixedSize(true);
        rvRiwayat.setLayoutManager(manager);
        rvRiwayat.setAdapter(adapter);
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
        itemTouchhelper.attachToRecyclerView(rvRiwayat);
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
            keuanganList.clear();
            getDataKeuangan(sFilterBulanTahun, authToken);
            tvFilterBulanKeuangan.setText(sBulan);
            tvFilterTahunKeuangan.setText(sTahun);
            doNextCurentTime();
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(RiwayatActivity.this, "Gagal memuat list transaksi" , Toast.LENGTH_LONG).show();
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
            Toast.makeText(RiwayatActivity.this, "Gagal memuat riwayat sebelum atau sesudah", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(RiwayatActivity.this, DetailRiwayatKasActivity.class);
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