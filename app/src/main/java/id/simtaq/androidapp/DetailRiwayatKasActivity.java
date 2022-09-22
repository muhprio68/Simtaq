package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.toRupiah;
import static id.simtaq.androidapp.helper.config.url;
import static id.simtaq.androidapp.helper.config.urlKeuangan;

public class DetailRiwayatKasActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressBar pbDetailKeuangan;
    private TextView tvNominalCatatan;
    private TextView tvNoCatatan;
    private TextView tvTipeCatatan;
    private TextView tvTglCatatan;
    private TextView tvKeteranganCatatan;
    private TextView tvDeskripsiCatatan;
    private TextView tvJudulDetailPenjumlahan;
    private TextView tvTotalKasAwal;
    private TextView tvTipeJumlah;
    private TextView tvDetailNominalCatatan;
    private TextView tvTotalKasAkhir;

    private RequestQueue queue;
    private int idKeuangan;
    private String intentDari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat_kas);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Catatan Kas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        queue = Volley.newRequestQueue(DetailRiwayatKasActivity.this);
        intentDari = String.valueOf(getIntent().getStringExtra("intentDari"));
        if (intentDari.equals("riwayat keuangan")){
            idKeuangan = getIntent().getExtras().getInt("idKeuangan");
            getData();
        } else {
            lihatTambah();
        }
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbDetailCatatanKas);
        pbDetailKeuangan = findViewById(R.id.pbDetailRiwayatKeuangan);
        tvNominalCatatan = findViewById(R.id.tvValueNominalCatatanKas);
        tvNoCatatan = findViewById(R.id.tvValueNoCatatan);
        tvTipeCatatan = findViewById(R.id.tvValueTipeCatatanKas);
        tvTglCatatan = findViewById(R.id.tvValueTglCatatanKas);
        tvKeteranganCatatan = findViewById(R.id.tvValueKeteranganCatatanKas);
        tvDeskripsiCatatan = findViewById(R.id.tvValueDeskripsiCatanKas);
        tvJudulDetailPenjumlahan = findViewById(R.id.tvJudulDetailPenjumlahan);
        tvTotalKasAwal = findViewById(R.id.tvValueTotalKasAwal);
        tvTipeJumlah = findViewById(R.id.tvTipeJumlah);
        tvDetailNominalCatatan = findViewById(R.id.tvValueNominalDetailPenjumlahan);
        tvTotalKasAkhir = findViewById(R.id.tvValueTotalKasAkhir);
    }

    public void getData(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlKeuangan+"/"+idKeuangan, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbDetailKeuangan.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(0);
                        tvNoCatatan.setText(responseObj.getString("no_keuangan"));
                        if (responseObj.getString("tipe_keuangan").equals("Pemasukan")){
                            tvNominalCatatan.setText("+ "+toRupiah(responseObj.getString("nominal_keuangan")));
                            tvNominalCatatan.setTextColor(ContextCompat.getColor(DetailRiwayatKasActivity.this, R.color.jmlPemasukan));
                            tvTipeCatatan.setText("Pemasukan");
                            tvJudulDetailPenjumlahan.setText("Detail Pemasukan");
                            tvTipeJumlah.setText("Jumlah Pemasukan");
                        } else{
                            tvNominalCatatan.setText("- "+toRupiah(responseObj.getString("nominal_keuangan")));
                            tvNominalCatatan.setTextColor(ContextCompat.getColor(DetailRiwayatKasActivity.this, R.color.jmlPengeluaran));
                            tvTipeCatatan.setText("Pengeluaran");
                            tvJudulDetailPenjumlahan.setText("Detail Pengeluaran");
                            tvTipeJumlah.setText("Jumlah Pengeluaran");
                        }
                        tvTglCatatan.setText(responseObj.getString("tgl_keuangan"));
                        tvKeteranganCatatan.setText(responseObj.getString("keterangan_keuangan"));
                        tvDeskripsiCatatan.setText(responseObj.getString("deskripsi_keuangan"));
                        tvTotalKasAwal.setText(toRupiah(responseObj.getString("jml_kas_awal")));
                        tvDetailNominalCatatan.setText(toRupiah(responseObj.getString("nominal_keuangan")));
                        tvTotalKasAkhir.setText(toRupiah(responseObj.getString("jml_kas_akhir")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailRiwayatKasActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void lihatTambah(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlKeuangan, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbDetailKeuangan.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(response.length()-1);
                        tvNoCatatan.setText(responseObj.getString("no_keuangan"));
                        if (responseObj.getString("tipe_keuangan").equals("Pemasukan")){
                            tvNominalCatatan.setText("+ "+toRupiah(responseObj.getString("nominal_keuangan")));
                            tvNominalCatatan.setTextColor(ContextCompat.getColor(DetailRiwayatKasActivity.this, R.color.jmlPemasukan));
                            tvTipeCatatan.setText("Pemasukan");
                            tvJudulDetailPenjumlahan.setText("Detail Pemasukan");
                            tvTipeJumlah.setText("Jumlah Pemasukan");
                        } else{
                            tvNominalCatatan.setText("- "+toRupiah(responseObj.getString("nominal_keuangan")));
                            tvNominalCatatan.setTextColor(ContextCompat.getColor(DetailRiwayatKasActivity.this, R.color.jmlPengeluaran));
                            tvTipeCatatan.setText("Pengeluaran");
                            tvJudulDetailPenjumlahan.setText("Detail Pengeluaran");
                            tvTipeJumlah.setText("Jumlah Pengeluaran");
                        }
                        tvTglCatatan.setText(responseObj.getString("tgl_keuangan"));
                        tvKeteranganCatatan.setText(responseObj.getString("keterangan_keuangan"));
                        tvDeskripsiCatatan.setText(responseObj.getString("deskripsi_keuangan"));
                        tvTotalKasAwal.setText(toRupiah(responseObj.getString("jml_kas_awal")));
                        tvDetailNominalCatatan.setText(toRupiah(responseObj.getString("nominal_keuangan")));
                        tvTotalKasAkhir.setText(toRupiah(responseObj.getString("jml_kas_akhir")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailRiwayatKasActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
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