package id.simtaq.androidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.helper.Preferences;

import static id.simtaq.androidapp.helper.config.url;

public class UbahKataSandiActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ConstraintLayout clPasswordSaatIni;
    private TextView tvPasswordSaatIni;
    private EditText etPasswordSaatIni, etPasswordBaru, etkonfirmasiPassword;
    private Button btnSimpanUbah, btnBatalUbah;
    private int id, level;

    private RequestQueue queue;
    private String authToken;
    private String passSaatIni, passBaru, passKonf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_kata_sandi);
        authToken = Preferences.getKeyToken(UbahKataSandiActivity.this);
        queue = Volley.newRequestQueue(UbahKataSandiActivity.this);
        id = getIntent().getIntExtra("id",0);
        level = Integer.parseInt(Preferences.getKeyLevel(UbahKataSandiActivity.this));
        initViews(level);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ubah Kata Sandi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);


        //id = getIntent().getIntExtra("id",0);
        btnSimpanUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passSaatIni = etPasswordSaatIni.getText().toString();
                passBaru = etPasswordBaru.getText().toString();
                passKonf= etkonfirmasiPassword.getText().toString();

                if (level!=4 && TextUtils.isEmpty(passSaatIni)){
                    etPasswordSaatIni.requestFocus();
                    etPasswordSaatIni.setError("Masukkan kata sandi saat ini");
                } else if (TextUtils.isEmpty(passBaru)){
                    etPasswordBaru.requestFocus();
                    etPasswordBaru.setError("Masukkan kata sandi baru");
                }  else if (TextUtils.isEmpty(passKonf)){
                    etkonfirmasiPassword.requestFocus();
                    etkonfirmasiPassword.setError("Masukkan konfirmasi kata sandi");
                } else if (!passKonf.equals(passBaru)){
                    etkonfirmasiPassword.requestFocus();
                    etkonfirmasiPassword.setError("Konfirmasi kata sandi tidak sama");
                } else {
                    ubahKataSandi(id, passSaatIni, passBaru, passKonf, authToken);
                }
            }
        });

        btnBatalUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initViews(int level){
        toolbar = findViewById(R.id.tbUbahKataSandi);
        clPasswordSaatIni = findViewById(R.id.clPasswordSaatIni);
        tvPasswordSaatIni = findViewById(R.id.tvPasswordSaatIni);
        etPasswordSaatIni = findViewById(R.id.etPasswordSaatIni);
        etPasswordBaru = findViewById(R.id.etPasswordBaru);
        etkonfirmasiPassword = findViewById(R.id.etKonfirmasiPasswordBaru);
        btnSimpanUbah = findViewById(R.id.btnSimpanUbahSandi);
        btnBatalUbah = findViewById(R.id.btnBatalUbahSandi);
        if (level == 4 ){
            tvPasswordSaatIni.setVisibility(View.GONE);
            clPasswordSaatIni.setVisibility(View.GONE);
        }
    }

    public void showHidePassSaatIni(View view) {

        if(view.getId()==R.id.show_passsaatini_btn){
            if(etPasswordSaatIni.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_invisible);
                //Show Password
                etPasswordSaatIni.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_visible);
                //Hide Password
                etPasswordSaatIni.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void showHidePassBaru(View view) {

        if(view.getId()==R.id.show_passbaru_btn){
            if(etPasswordBaru.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_invisible);
                //Show Password
                etPasswordBaru.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_visible);
                //Hide Password
                etPasswordBaru.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void showHideKonfPass(View view) {

        if(view.getId()==R.id.show_konfpass_btn){
            if(etkonfirmasiPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_invisible);
                //Show Password
                etkonfirmasiPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_visible);
                //Hide Password
                etkonfirmasiPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    private void ubahKataSandi(int id, String passwordSaatIni, String passwordBaru, String konfirmasiPassword, String token) {
        StringRequest request = new StringRequest(Request.Method.PUT, url+"/gantipassword/"+id, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    //snackbarWithAction();
                    if (level!=4){
                        showDialogSukses();
                    } else {
                        showDialogSuksesAdmin();
                    }
                    Log.e("TAG", "RESPONSE IS " + jsonObject.getString("messages"));
                    //Toast.makeText(UbahKataSandiActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(UbahKataSandiActivity.this, PengaturanFragment.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                etPasswordSaatIni.requestFocus();
                etPasswordSaatIni.setError("Kata sandi saat ini tidak sesuai");
                //Toast.makeText(UbahKataSandiActivity.this, "password saat ini salah", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                // below line we are creating a map for storing
                // our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our
                // key and value pair to our parameters.
                if (level != 4) {
                    params.put("passwordsaatini", passwordSaatIni);
                }
                params.put("passwordbaru", passwordBaru);
                params.put("konfirmasipassword", konfirmasiPassword);
                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    public void showDialogSukses(){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(UbahKataSandiActivity.this, R.style.DialogSlideAnim);
        View layoutView = getLayoutInflater().inflate(R.layout.dialogsukses, null);
        Button dialogButton = layoutView.findViewById(R.id.btnOkDialogSukses);
        TextView tvKetSuksesAdmin = layoutView.findViewById(R.id.tvKeteranganDialogSukses);
        dialogButton.setText("Login");
        tvKetSuksesAdmin.setText("Kata sandi anda berhasil diubah, silahkan masuk kembali");
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferences.setKeyToken(UbahKataSandiActivity.this,"");
                Preferences.setKeyId(UbahKataSandiActivity.this,"");
                Preferences.setKeyNama(UbahKataSandiActivity.this,"");
                Preferences.setKeyEmail(UbahKataSandiActivity.this,"");
                Preferences.setKeyLevel(UbahKataSandiActivity.this,"");
                Preferences.setLoggedInStatus(UbahKataSandiActivity.this,false);
                startActivity(new Intent(UbahKataSandiActivity.this, SplashScreenActivity.class));
                finish();
            }
        });
    }

    public void showDialogSuksesAdmin(){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(UbahKataSandiActivity.this, R.style.DialogSlideAnim);
        View layoutView = getLayoutInflater().inflate(R.layout.dialogsukses, null);
        Button dialogButton = layoutView.findViewById(R.id.btnOkDialogSukses);
        TextView tvKetSuksesAdmin = layoutView.findViewById(R.id.tvKeteranganDialogSukses);
        dialogButton.setText("Kembali");
        tvKetSuksesAdmin.setText("Kata sandi telah diubah");
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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