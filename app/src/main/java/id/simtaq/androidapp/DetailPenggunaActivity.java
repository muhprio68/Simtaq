package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.helper.Preferences;

import static id.simtaq.androidapp.helper.config.url;

public class DetailPenggunaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    TextView tvIdPengguna;
    TextView tvNamaPengguna;
    TextView tvEmailPengguna;
    TextView tvLevelPengguna;
    ProgressBar pbDetailPengguna;
    int id, level;
    private RequestQueue queue;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengguna);
        initViews();
        authToken = Preferences.getKeyToken(DetailPenggunaActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Pengguna");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        queue = Volley.newRequestQueue(DetailPenggunaActivity.this);
        id = getIntent().getIntExtra("id",0);
        getDataPengguna(authToken);
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbDetailPengguna);
        pbDetailPengguna = findViewById(R.id.pbDetailPengguna);
        tvIdPengguna = findViewById(R.id.tvValueIdPengguna);
        tvNamaPengguna = findViewById(R.id.tvValueDetailNamaPengguna);
        tvEmailPengguna = findViewById(R.id.tvValueDetailEmailPengguna);
        tvLevelPengguna = findViewById(R.id.tvValueDetailLevelPengguna);
    }

    public void getDataPengguna(String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/user/"+id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbDetailPengguna.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(0);
                        tvIdPengguna.setText(responseObj.getInt("id")+"");
                        tvNamaPengguna.setText(responseObj.getString("nama"));
                        tvEmailPengguna.setText(responseObj.getString("email"));
                        tvLevelPengguna.setText(getResources().getStringArray(R.array.lvl_pengguna)[responseObj.getInt("level")-1]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailPenggunaActivity.this,    "Fail to get the data..", Toast.LENGTH_SHORT).show();
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
}