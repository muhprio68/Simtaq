package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DaftarActivity extends AppCompatActivity {

    TextView tvSudahPunyaAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        tvSudahPunyaAkun = (TextView) findViewById(R.id.tvSudahPunyaAkun);

        tvSudahPunyaAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home=new Intent(DaftarActivity.this, LoginActivity.class);
                startActivity(home);
                finish();
            }
        });
    }
}