package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.TextView;

public class DetailRiwayatKasActivity extends AppCompatActivity {

    TextView tvNominalCatatan;
    TextView tvNoCatatan;
    TextView tvTipeCatatan;
    TextView tvTglCatatan;
    TextView tvKeteranganCatatan;
    TextView tvDeskripsiCatatan;
    TextView tvJudulDetailPenjumlahan;
    TextView tvTotalKasAwal;
    TextView tvDetailNominalCatatan;
    TextView tvTotalKasAkhir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat_kas);
        initViews();
        Bundle bundle = getIntent().getExtras();
        tvNominalCatatan.setText("+ Rp. "+bundle.getInt("nominalCatatan"));
        tvNoCatatan.setText(bundle.getString("noCatatan"));
        if (bundle.getBoolean("tipe")==true){
            tvNominalCatatan.setTextColor(ContextCompat.getColor(this, R.color.jmlPemasukan));
            tvTipeCatatan.setText("Pemasukan");
            tvJudulDetailPenjumlahan.setText("Jumlah Pemasukan");
        } else{
            tvNominalCatatan.setTextColor(ContextCompat.getColor(this, R.color.jmlPengeluaran));
            tvTipeCatatan.setText("Pengeluaran");
            tvJudulDetailPenjumlahan.setText("Jumlah Pengeluaran");
        }
        tvTglCatatan.setText(bundle.getString("tgl"));
        tvKeteranganCatatan.setText(bundle.getString("keterangan"));
        tvDeskripsiCatatan.setText(bundle.getString("deskripsi"));
        tvTotalKasAwal.setText("Rp. "+bundle.getInt("totalKasAwal"));
        tvDetailNominalCatatan.setText("Rp. "+bundle.getInt("nominalCatatan"));
        tvTotalKasAkhir.setText("Rp. "+bundle.getInt("totalKasAkhir"));
    }

    public void initViews(){
        tvNominalCatatan = findViewById(R.id.tvValueNominalCatatanKas);
        tvNoCatatan = findViewById(R.id.tvValueNoCatatan);
        tvTipeCatatan = findViewById(R.id.tvValueTipeCatatanKas);
        tvTglCatatan = findViewById(R.id.tvValueTglCatatanKas);
        tvKeteranganCatatan = findViewById(R.id.tvValueKeteranganCatatanKas);
        tvDeskripsiCatatan = findViewById(R.id.tvValueDeskripsiCatanKas);
        tvJudulDetailPenjumlahan = findViewById(R.id.tvJudulDetailPenjumlahan);
        tvTotalKasAwal = findViewById(R.id.tvValueTotalKasAwal);
        tvDetailNominalCatatan = findViewById(R.id.tvValueNominalDetailPenjumlahan);
        tvTotalKasAkhir = findViewById(R.id.tvValueTotalKasAkhir);
    }
}