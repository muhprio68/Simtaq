package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LaporkanBugActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnKirim;
    private EditText etLaporanBug;
    private String textLaporan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporkan_bug);
        toolbar = findViewById(R.id.tbLaporkanBug);
        btnKirim = findViewById(R.id.btnKirimLaporanBug);
        etLaporanBug = findViewById(R.id.etLaporkanBug);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Laporkan Bug");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textLaporan = etLaporanBug.getText().toString();

                if (TextUtils.isEmpty(textLaporan)) {
                    etLaporanBug.setError("Laporan belum diisi");
                }  else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"simtaq9@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Laporkan Bug");
                    intent.putExtra(Intent.EXTRA_TEXT, textLaporan+"");

                    try {
                        startActivity(Intent.createChooser(intent, "Ingin Mengirim Email ?"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        //do something else
                    }
                }
            }
        });
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