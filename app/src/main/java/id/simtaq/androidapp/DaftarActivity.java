package id.simtaq.androidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.simtaq.androidapp.helper.config;

public class DaftarActivity extends AppCompatActivity {

    private TextView tvSudahPunyaAkun;
    private EditText etNama, etEmail, etPassword, etUlangiPassword;
    private Button btnDaftar;
    private String nama, email, password, ulangiPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        initViews();

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = etNama.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                ulangiPassword = etUlangiPassword.getText().toString();

                if (TextUtils.isEmpty(nama)) {
                    etNama.requestFocus();
                    etNama.setError("Masukkan nama");
                } else if (TextUtils.isEmpty(email)){
                    etEmail.requestFocus();
                    etEmail.setError("Masukkan email");
                }  else if (!config.isEmailValid(email)){
                    etEmail.requestFocus();
                    etEmail.setError("Email tidak valid");
                } else if (TextUtils.isEmpty(password)){
                    etPassword.requestFocus();
                    etPassword.setError("Masukkan kata sandi");
                } else if (!config.isValidPassword(password)){
                    etPassword.requestFocus();
                    etPassword.setError("Kata sandi setidaknya 6 karakter menggunakan 1 huruf besar dan 1 angka");
                } else if (TextUtils.isEmpty(ulangiPassword)){
                    etUlangiPassword.requestFocus();
                    etUlangiPassword.setError("Konfirmasi kata sandi harus diisi");
                } else if (!ulangiPassword.equals(password)){
                    etUlangiPassword.requestFocus();
                    etUlangiPassword.setError("Konfirmasi kata sandi tidak sama");
                } else {
                    daftar(nama, email, password, ulangiPassword);
                }
            }
        });

        tvSudahPunyaAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home=new Intent(DaftarActivity.this, LoginActivity.class);
                startActivity(home);
                finish();
            }
        });
    }

    private void initViews(){
        tvSudahPunyaAkun = findViewById(R.id.tvSudahPunyaAkun);
        etNama = findViewById(R.id.inputNama);
        etEmail = findViewById(R.id.inputEmail);
        etPassword = findViewById(R.id.inputPassword);
        etUlangiPassword = findViewById(R.id.inputUlangiPassword);
        btnDaftar = findViewById(R.id.btnDaftar);
    }

    public void showHidePassDaftar(View view) {

        if(view.getId()==R.id.show_passdaftar_btn){
            if(etPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_invisible);
                //Show Password
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_visible);
                //Hide Password
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void showHideKonfPassDaftar(View view) {

        if(view.getId()==R.id.show_konfpassdaftar_btn){
            if(etUlangiPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_invisible);
                //Show Password
                etUlangiPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_visible);
                //Hide Password
                etUlangiPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    private void daftar(String nama, String email, String password, String ulangiPassword) {
        // url to post our data
        String url = config.url+"/register";
        //loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(DaftarActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
//                loadingPB.setVisibility(View.GONE);
//                nameEdt.setText("");
//                jobEdt.setText("");

                showDialogSuksesDaftar(1);
                // on below line we are displaying a success toast message.
                //Toast.makeText(DaftarActivity.this, "Pendaftaran berhasil, silahkan login", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);

                    // below are the strings which we
                    // extract from our json object.
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(DaftarActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                showDialogSuksesDaftar(2);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("nama", nama);
                params.put("email", email);
                params.put("password", password);
                params.put("confpassword", ulangiPassword);
                params.put("level", 1+"");

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    public void showDialogSuksesDaftar(int a){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(DaftarActivity.this, R.style.DialogSlideAnim);
        View layoutView = getLayoutInflater().inflate(R.layout.dialogsukses, null);
        ImageView ivDialog = layoutView.findViewById(R.id.ivIconDialog);
        TextView tvJudulDialog = layoutView.findViewById(R.id.tvJudulDialog);
        TextView tvKetSuksesAdmin = layoutView.findViewById(R.id.tvKeteranganDialogSukses);
        Button btnDialog= layoutView.findViewById(R.id.btnOkDialogSukses);
        if (a == 1){
            ivDialog.setImageResource(R.drawable.ic_ok);
            tvJudulDialog.setText("Sukses");
            tvKetSuksesAdmin.setText("Anda berhasil mendaftar, silahkan login!");
            btnDialog.setBackgroundResource(R.drawable.rounded_bg_primary);
            btnDialog.setText("Login");
        } else {
            ivDialog.setImageResource(R.drawable.ic_fail);
            tvJudulDialog.setText("Gagal");
            tvKetSuksesAdmin.setText("Anda gagal mendaftar, pastikan anda terkoneksi dengan internet dan isi data anda dengan benar!");
            btnDialog.setBackgroundResource(R.drawable.rounded_bg_red);
            btnDialog.setText("Saya Mengerti");
        }
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (a == 1){
                    Intent intent = new Intent(DaftarActivity.this, LoginActivity.class);
                    intent.putExtra("intentDari", "daftar");
                    startActivity(intent);
                } else {
                    alertDialog.dismiss();
                }
            }
        });
    }
}