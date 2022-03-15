package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.TextView;

public class DetailKegiatanActivity extends AppCompatActivity {

    TextView tvDetailNamaKegiatan;
    TextView tvNoKegiatan;
    TextView tvTipeKegiatan;
    TextView tvTglKegiatan;
    TextView tvWaktuKegiatan;
    TextView tvTempatKegiatan;
    TextView tvPembicaraKegiatan;
    TextView tvDeskripsiKegiatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kegiatan);
        initViews();
        Bundle bundle = getIntent().getExtras();
        tvDetailNamaKegiatan.setText(bundle.getString("namaKegiatan"));
        tvNoKegiatan.setText(bundle.getString("noKegiatan"));
        if (bundle.getBoolean("tipe")==true){
            tvTipeKegiatan.setText("Umum");
        } else{
            tvTipeKegiatan.setText("Undangan");
        }
        tvTglKegiatan.setText(bundle.getString("tgl"));
        tvWaktuKegiatan.setText(bundle.getString("waktu"));
        tvTempatKegiatan.setText(bundle.getString("tempat"));
        tvPembicaraKegiatan.setText(bundle.getString("pembicara"));
        tvDeskripsiKegiatan.setText(bundle.getString("deskripsi"));
    }

    public void initViews(){
        tvDetailNamaKegiatan = findViewById(R.id.tvValueNamaKegiatan);
        tvNoKegiatan = findViewById(R.id.tvValueNoKegiatan);
        tvTipeKegiatan = findViewById(R.id.tvValueDetailTipeKegiatan);
        tvTglKegiatan = findViewById(R.id.tvValueHariTglKegiatan);
        tvWaktuKegiatan = findViewById(R.id.tvValueDetailWaktuKegiatan);
        tvTempatKegiatan = findViewById(R.id.tvValueDetailTempatKegiatan);
        tvPembicaraKegiatan = findViewById(R.id.tvValueDetailPembicaraKegiatan);
        tvDeskripsiKegiatan =findViewById(R.id.tvValueDetailDeskripsiKegiatan);
    }
}