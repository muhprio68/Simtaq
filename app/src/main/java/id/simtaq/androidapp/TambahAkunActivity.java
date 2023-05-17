package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class TambahAkunActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText etNama, etEmail, etPassword;
    private Spinner spLevelPengguna;
    private Button btnTambahAkun, btnBatalTambahAkun;
    private String nama, email, password, levelPengguna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_akun);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Akun");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        btnTambahAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = etNama.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                String valueSpinner = spLevelPengguna.getSelectedItemPosition()+1+"";
                levelPengguna = valueSpinner;

                if (TextUtils.isEmpty(nama)) {
                    etEmail.setError("Masukkan nama");
                } else if (TextUtils.isEmpty(email)){
                    etPassword.setError("Masukkan email");
                } else if (TextUtils.isEmpty(password)){
                    etPassword.setError("Masukkan password");
                }  else {
                    tambahAkun(nama, email, password, levelPengguna);
                }
            }
        });

        btnBatalTambahAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initViews(){
        toolbar = findViewById(R.id.tbTambahAkun);
        etNama = findViewById(R.id.etNamaTambahAkun);
        etEmail = findViewById(R.id.etEmailTambahAkun);
        etPassword = findViewById(R.id.etPasswordTambahAkun);
        spLevelPengguna = findViewById(R.id.spLvlTambahAkun);
        btnTambahAkun = findViewById(R.id.btnSimpanTambahAkun);
        btnBatalTambahAkun = findViewById(R.id.btnBatalTambahAkun);
    }

    private void tambahAkun(String nama, String email, String password, String levelPengguna) {
        // url to post our data
        String url = config.url+"/register";
        //loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(TambahAkunActivity.this);

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
                etNama.setText("");
                etEmail.setText("");
                etPassword.setText("");
                Toast.makeText(TambahAkunActivity.this, "Penambahan akun berhasil", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TambahAkunActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("confpassword", password);
                params.put("level", levelPengguna);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
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