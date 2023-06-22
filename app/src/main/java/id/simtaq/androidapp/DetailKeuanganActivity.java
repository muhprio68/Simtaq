package id.simtaq.androidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.helper.Preferences;

import static id.simtaq.androidapp.helper.config.formatLihatFullTanggal;
import static id.simtaq.androidapp.helper.config.toRupiah;
import static id.simtaq.androidapp.helper.config.url;

public class DetailKeuanganActivity extends AppCompatActivity {

    private ConstraintLayout clDetailKeuangan;
    private Toolbar toolbar;
    private ProgressBar pbDetailKeuangan;
    private TextView tvNominalKeuangan;
    private TextView tvNoKeuangan;
    private TextView tvTipeKeungan;
    private TextView tvTglKeuangan;
    private TextView tvKeteranganKeuangan;
    private TextView tvJenisKeuangan;
    private TextView tvDeskripsiKeuangan;
    private TextView tvJudulDetailPenjumlahan;
    private TextView tvTotalKasAwal;
    private TextView tvTipeJumlah;
    private TextView tvDetailNominalKeuangan;
    private TextView tvTotalKasAkhir;
    private ScrollView svDetailKeuangan;
    private Button btnUbahKeuangan, btnHapusKeuangan;

    private RequestQueue queue;
    private String authToken;
    private String level;
    private int idKeuangan;
    private String intentDari;
    private String jenisKeuangan;
    private String judulMessage, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_keuangan);
        initViews();
        authToken = Preferences.getKeyToken(DetailKeuanganActivity.this);
        level = Preferences.getKeyLevel(DetailKeuanganActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Keuangan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        svDetailKeuangan.setVisibility(View.GONE);
        queue = Volley.newRequestQueue(DetailKeuanganActivity.this);
        intentDari = getIntent().getStringExtra("intentDari");
        idKeuangan = getIntent().getExtras().getInt("idKeuangan", 0);
        initLevel(level);
        if (intentDari.contains("catat")){
            btnUbahKeuangan.setVisibility(View.GONE);
            btnHapusKeuangan.setVisibility(View.GONE);
            lihatTambah(authToken);
        } else if (intentDari.contains("ubah")){
            btnUbahKeuangan.setVisibility(View.GONE);
            btnHapusKeuangan.setVisibility(View.GONE);
            getData(authToken, idKeuangan);
        } else {
            getData(authToken, idKeuangan);
        }

        btnUbahKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!jenisKeuangan.equals("Donatur")){
                    Intent intent = new Intent(DetailKeuanganActivity.this, UbahKeuanganActivity.class);
                    intent.putExtra("idKeuangan", idKeuangan);
                    intent.putExtra("intentDari", "ubah "+intentDari);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(DetailKeuanganActivity.this, UbahDonaturActivity.class);
                    intent.putExtra("idKeuangan", idKeuangan);
                    intent.putExtra("intentDari", "ubah "+intentDari);
                    startActivity(intent);
                }
            }
        });

        btnHapusKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogHapusKeuangan();
            }
        });
    }

    public void initViews(){
        clDetailKeuangan = findViewById(R.id.clDetailKeuangan);
        toolbar = findViewById(R.id.tbDetailKeuangan);
        svDetailKeuangan = findViewById(R.id.svDetailKeuangan);
        pbDetailKeuangan = findViewById(R.id.pbDetailRiwayatKeuangan);
        tvNominalKeuangan = findViewById(R.id.tvValueNominalKeuangan);
        tvNoKeuangan = findViewById(R.id.tvValueNoKeuangan);
        tvTipeKeungan = findViewById(R.id.tvValueTipeKeuangan);
        tvTglKeuangan = findViewById(R.id.tvValueTglKeuangan);
        tvKeteranganKeuangan = findViewById(R.id.tvValueKeteranganKeuangan);
        tvJenisKeuangan = findViewById(R.id.tvValueJenisKeuangan);
        tvDeskripsiKeuangan = findViewById(R.id.tvValueDeskripsiKeuangan);
        tvJudulDetailPenjumlahan = findViewById(R.id.tvJudulDetailPenjumlahan);
        tvTotalKasAwal = findViewById(R.id.tvValueTotalKasAwal);
        tvTipeJumlah = findViewById(R.id.tvTipeJumlah);
        tvDetailNominalKeuangan = findViewById(R.id.tvValueNominalDetailKeuangan);
        tvTotalKasAkhir = findViewById(R.id.tvValueTotalKasAkhir);
        btnUbahKeuangan = findViewById(R.id.btnUbahKeuangan);
        btnHapusKeuangan = findViewById(R.id.btnHapusKeuangan);
    }

    public void getData(String token, int idKeuangan){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/keuangan/"+idKeuangan, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbDetailKeuangan.setVisibility(View.GONE);
                svDetailKeuangan.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(0);
                        tvNoKeuangan.setText(responseObj.getString("no_keuangan"));
                        if (responseObj.getString("tipe_keuangan").equals("Pemasukan")){
                            tvNominalKeuangan.setText("+ "+toRupiah(responseObj.getString("nominal_keuangan")));
                            tvNominalKeuangan.setTextColor(ContextCompat.getColor(DetailKeuanganActivity.this, R.color.jmlPemasukan));
                            tvTipeKeungan.setText("Pemasukan");
                            tvJudulDetailPenjumlahan.setText("Detail Pemasukan");
                            tvTipeJumlah.setText("Jumlah Pemasukan");
                        } else{
                            tvNominalKeuangan.setText("- "+toRupiah(responseObj.getString("nominal_keuangan")));
                            tvNominalKeuangan.setTextColor(ContextCompat.getColor(DetailKeuanganActivity.this, R.color.jmlPengeluaran));
                            tvTipeKeungan.setText("Pengeluaran");
                            tvJudulDetailPenjumlahan.setText("Detail Pengeluaran");
                            tvTipeJumlah.setText("Jumlah Pengeluaran");
                        }
                        tvTglKeuangan.setText(formatLihatFullTanggal(responseObj.getString("tgl_keuangan")));
                        tvKeteranganKeuangan.setText(responseObj.getString("keterangan_keuangan"));
                        jenisKeuangan = responseObj.getString("jenis_keuangan");
                        tvJenisKeuangan.setText(jenisKeuangan);
                        tvDeskripsiKeuangan.setText(responseObj.getString("deskripsi_keuangan"));
                        tvTotalKasAwal.setText(toRupiah(responseObj.getString("jml_kas_awal")));
                        tvDetailNominalKeuangan.setText(toRupiah(responseObj.getString("nominal_keuangan")));
                        tvTotalKasAkhir.setText(toRupiah(responseObj.getString("jml_kas_akhir")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailKeuanganActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
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

    public void lihatTambah(String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/keuangan", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbDetailKeuangan.setVisibility(View.GONE);
                svDetailKeuangan.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(response.length()-1);
                        tvNoKeuangan.setText(responseObj.getString("no_keuangan"));
                        if (responseObj.getString("tipe_keuangan").equals("Pemasukan")){
                            tvNominalKeuangan.setText("+ "+toRupiah(responseObj.getString("nominal_keuangan")));
                            tvNominalKeuangan.setTextColor(ContextCompat.getColor(DetailKeuanganActivity.this, R.color.jmlPemasukan));
                            tvTipeKeungan.setText("Pemasukan");
                            tvJudulDetailPenjumlahan.setText("Detail Pemasukan");
                            tvTipeJumlah.setText("Jumlah Pemasukan");
                        } else{
                            tvNominalKeuangan.setText("- "+toRupiah(responseObj.getString("nominal_keuangan")));
                            tvNominalKeuangan.setTextColor(ContextCompat.getColor(DetailKeuanganActivity.this, R.color.jmlPengeluaran));
                            tvTipeKeungan.setText("Pengeluaran");
                            tvJudulDetailPenjumlahan.setText("Detail Pengeluaran");
                            tvTipeJumlah.setText("Jumlah Pengeluaran");
                        }
                        tvTglKeuangan.setText(formatLihatFullTanggal(responseObj.getString("tgl_keuangan")));
                        tvKeteranganKeuangan.setText(responseObj.getString("keterangan_keuangan"));
                        jenisKeuangan = responseObj.getString("jenis_keuangan");
                        tvJenisKeuangan.setText(jenisKeuangan);
                        tvDeskripsiKeuangan.setText(responseObj.getString("deskripsi_keuangan"));
                        tvTotalKasAwal.setText(toRupiah(responseObj.getString("jml_kas_awal")));
                        tvDetailNominalKeuangan.setText(toRupiah(responseObj.getString("nominal_keuangan")));
                        tvTotalKasAkhir.setText(toRupiah(responseObj.getString("jml_kas_akhir")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailKeuanganActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
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

    public void hapusKeuangan(){
        StringRequest dr = new StringRequest(Request.Method.DELETE, url+"/keuangan/"+idKeuangan,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        judulMessage = "Sukses";
                        message = "Data keuangan berhasil dihapus";
                        showDialogSuksesHapus(1);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String body = null;
                        try {
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(DetailKeuanganActivity.this, "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
                            } else if (error.networkResponse != null) {
                                if (error.networkResponse.data != null) {
                                    body = new String(error.networkResponse.data, "UTF-8");
                                    JSONObject obj = new JSONObject(body);
                                    JSONObject msg = obj.getJSONObject("messages");
                                    String errorMsg = msg.getString("error");
                                    judulMessage = "Gagal";
                                    message = errorMsg;
                                    showDialogSuksesHapus(2);
                                }
                            }
                            //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };
        queue.add(dr);
    }

    public void showDialogHapusKeuangan(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus Data Keuangan");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Yakin menghapus keuangan ini?")
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        hapusKeuangan();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    public void showDialogSuksesHapus(int i){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(DetailKeuanganActivity.this, R.style.DialogSlideAnim);
        View layoutView = getLayoutInflater().inflate(R.layout.dialogsukses, null);
        Button btnDialog = layoutView.findViewById(R.id.btnOkDialogSukses);
        ImageView ivDialog = layoutView.findViewById(R.id.ivIconDialog);
        TextView tvJudulDialog = layoutView.findViewById(R.id.tvJudulDialog);
        TextView tvKetSuksesAdmin = layoutView.findViewById(R.id.tvKeteranganDialogSukses);
        tvJudulDialog.setText(judulMessage);
        tvKetSuksesAdmin.setText(message);
        if (i == 1){
            ivDialog.setImageResource(R.drawable.ic_ok);
            btnDialog.setBackgroundResource(R.drawable.rounded_bg_primary);
            btnDialog.setText("Kembali");
        } else {
            ivDialog.setImageResource(R.drawable.ic_fail);
            btnDialog.setBackgroundResource(R.drawable.rounded_bg_red);
            btnDialog.setText("Saya Mengerti");
        }
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 2){
                    alertDialog.dismiss();
                } else {
                    intentDari = "hapus "+intentDari;
                    onBackPressed();
                }
            }
        });
    }

    private void initLevel(String level){
        if (level.equals("1") || level.equals("3")) {
            btnUbahKeuangan.setVisibility(View.GONE);
            btnHapusKeuangan.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (intentDari.equals("riwayat keuangan") || intentDari.equals("info kas")
                || intentDari.equals("catat keuangan") || intentDari.equals("catat donatur")){
            super.onBackPressed();
        } else if (intentDari.equals("ubah riwayat keuangan")){
            Intent i = new Intent(DetailKeuanganActivity.this, RiwayatActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("intentDari", "detail keuangan");
            startActivity(i);
        } else if (intentDari.equals("ubah info kas")){
            Intent i = new Intent(DetailKeuanganActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("intentDari", "detail keuangan");
            startActivity(i);
        } else if (intentDari.equals("ubah donatur")){
            Intent i = new Intent(DetailKeuanganActivity.this, RiwayatDonaturActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("intentDari", "detail keuangan");
            startActivity(i);
        } else if (intentDari.equals("hapus riwayat keuangan")){
            Intent i = new Intent(DetailKeuanganActivity.this, RiwayatActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("intentDari", "detail keuangan");
            startActivity(i);
        } else {
            Intent i = new Intent(DetailKeuanganActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("intentDari", "detail keuangan");
            startActivity(i);
        }
    }
}