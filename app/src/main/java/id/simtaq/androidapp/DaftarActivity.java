package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
                    etEmail.setError("Masukkan nama");
                } else if (TextUtils.isEmpty(email)){
                    etPassword.setError("Masukkan email");
                } else if (TextUtils.isEmpty(password)){
                    etPassword.setError("Masukkan password");
                } else if (TextUtils.isEmpty(ulangiPassword)){
                    etPassword.setError("Ulangi password harus diisi");
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

                // on below line we are displaying a success toast message.
                Toast.makeText(DaftarActivity.this, "Pendaftaran berhasil, silahkan login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DaftarActivity.this, LoginActivity.class);
                intent.putExtra("intentDari", "catat pengeluaran");
                startActivity(intent);
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
                Toast.makeText(DaftarActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}