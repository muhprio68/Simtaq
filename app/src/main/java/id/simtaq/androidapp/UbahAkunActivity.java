package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.helper.Preferences;

import static id.simtaq.androidapp.helper.config.url;

public class UbahAkunActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etUbahNama, etUbahEmail;
    private Spinner spLevel;
    private Button btnSimpanUbah, btnBatalUbah;
    private int id;

    private RequestQueue queue;
    private String authToken;
    private String ubahNama, ubahEmail;
    private int ubahLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_akun);
        initViews();
        authToken = Preferences.getKeyToken(UbahAkunActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ubah Akun");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        queue = Volley.newRequestQueue(UbahAkunActivity.this);
        id = getIntent().getIntExtra("id",0);
        getDataUbahAkun(authToken);
        btnBatalUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSimpanUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ubahNama = etUbahNama.getText().toString();
                ubahEmail = etUbahEmail.getText().toString();
                ubahLevel = spLevel.getSelectedItemPosition()+1;

                if (TextUtils.isEmpty(ubahNama)){
                    etUbahNama.setError("Masukkan nama pengguna");
                } else if (TextUtils.isEmpty(ubahEmail)){
                    etUbahNama.setError("Masukkan email pengguna");
                }  else {
                    ubahAkun(id, ubahNama, ubahEmail, ubahLevel, authToken);
                }
            }
        });
    }

    private void initViews(){
        toolbar = findViewById(R.id.tbUbahAkun);
        etUbahNama = findViewById(R.id.etNamaUbahAkun);
        etUbahEmail = findViewById(R.id.etEmailUbahAkun);
        spLevel = findViewById(R.id.spLvlUbahAkun);
        btnSimpanUbah = findViewById(R.id.btnSimpanUbahAkun);
        btnBatalUbah = findViewById(R.id.btnBatalUbahAkun);
    }

    public void getDataUbahAkun(String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/user/"+id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //pbDetailKegiatan.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(0);
                        etUbahNama.setText(responseObj.getString("nama"));
                        etUbahEmail.setText(responseObj.getString("email"));
                        spLevel.setSelection(responseObj.getInt("level")-1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UbahAkunActivity.this,    "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    private void ubahAkun(int id, String nama, String email, int level, String token) {
        RequestQueue queue = Volley.newRequestQueue(UbahAkunActivity.this);

        StringRequest request = new StringRequest(Request.Method.PUT, url+"/user/"+id, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    //snackbarWithAction();

                    Toast.makeText(UbahAkunActivity.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UbahAkunActivity.this, ListPenggunaActivity.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(UbahAkunActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("nama", nama);
                params.put("email", email);
                params.put("level", level+"");
                // at last we are returning our params.
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