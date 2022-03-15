package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        tvTglKegiatan.setText(fullDatePlusDay(bundle.getString("tgl")));
        tvWaktuKegiatan.setText(bundle.getString("waktu")+" WIB");
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

    private String fullDatePlusDay(String tanggal){
        String tgl = tanggal;
        Locale locale = new Locale("in", "ID");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", locale);
        Date date = null;
        try {
            date = (Date)formatter.parse(tgl);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", locale);
        String tglBaru = newFormat.format(date);
        return tglBaru;
    }
}