package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    TextView tvBelumPunyaAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvBelumPunyaAkun = (TextView) findViewById(R.id.tvBelumPunyaAkun);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(home);
                finish();
            }
        });

        tvBelumPunyaAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home=new Intent(LoginActivity.this, DaftarActivity.class);
                startActivity(home);
                finish();
            }
        });
    }
}