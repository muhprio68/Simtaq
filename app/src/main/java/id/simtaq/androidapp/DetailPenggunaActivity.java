package id.simtaq.androidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.helper.Preferences;

import static id.simtaq.androidapp.helper.config.url;

public class DetailPenggunaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ConstraintLayout clDetailPengguna, clDetailInfoPengguna;
    RelativeLayout rlInfoPengguna;
    private TextView tvInfoPengguna, tvIdPengguna, tvNamaPengguna, tvEmailPengguna, tvLevelPengguna;
    private Button btnUbahPengguna, btnGantiSandiPengguna, btnHapusPengguna;
    private ProgressBar pbDetailPengguna;
    private int id, level;
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
        btnUbahPengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPenggunaActivity.this, UbahAkunActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        btnGantiSandiPengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPenggunaActivity.this, UbahKataSandiActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        btnHapusPengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogHapusPengguna();
            }
        });
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbDetailPengguna);
        clDetailPengguna = findViewById(R.id.clDetailPengguna);
        rlInfoPengguna = findViewById(R.id.rlInfoPengguna);
        pbDetailPengguna = findViewById(R.id.pbDetailPengguna);
        tvInfoPengguna = findViewById(R.id.tvInfoPengguna);
        tvIdPengguna = findViewById(R.id.tvValueIdPengguna);
        tvNamaPengguna = findViewById(R.id.tvValueDetailNamaPengguna);
        tvEmailPengguna = findViewById(R.id.tvValueDetailEmailPengguna);
        tvLevelPengguna = findViewById(R.id.tvValueDetailLevelPengguna);
        btnUbahPengguna = findViewById(R.id.btnUbahProfilPengguna);
        btnGantiSandiPengguna = findViewById(R.id.btnGantiKataSandiPengguna);
        btnHapusPengguna = findViewById(R.id.btnHapusPengguna);
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

    public void showDialogHapusPengguna(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus Pengguna");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Yakin menghapus pengguna ini?")
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        hapusPengguna();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    public void hapusPengguna(){
        StringRequest dr = new StringRequest(Request.Method.DELETE, url+"/user/"+id,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        showDialogSuksesHapus();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar snackbar = Snackbar
                                .make(clDetailPengguna, "Gagal menghapus data pengguna", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };
        queue.add(dr);
    }

    public void showDialogSuksesHapus(){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(DetailPenggunaActivity.this, R.style.DialogSlideAnim);
        View layoutView = getLayoutInflater().inflate(R.layout.dialogsukses, null);
        Button dialogButton = layoutView.findViewById(R.id.btnOkDialogSukses);
        TextView tvKetSuksesAdmin = layoutView.findViewById(R.id.tvKeteranganDialogSukses);
        dialogButton.setText("Kembali");
        tvKetSuksesAdmin.setText("Pengguna berhasil dihapus");
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPenggunaActivity.this, ListPenggunaActivity.class);
                startActivity(intent);
                finish();
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