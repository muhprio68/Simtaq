package id.simtaq.androidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.simtaq.androidapp.helper.Preferences;

import static id.simtaq.androidapp.helper.config.formatLihatFullTanggal;
import static id.simtaq.androidapp.helper.config.formatLihatWaktu;
import static id.simtaq.androidapp.helper.config.url;

public class DetailKegiatanActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ScrollView svDetailKegiatan;
    TextView tvDetailNamaKegiatan;
    TextView tvNoKegiatan;
    TextView tvTipeKegiatan;
    TextView tvTglKegiatan;
    TextView tvWaktuKegiatan;
    TextView tvTempatKegiatan;
    TextView tvPembicaraKegiatan;
    TextView tvDeskripsiKegiatan;
    ProgressBar pbDetailKegiatan;
    private Button btnUbahKegiatan, btnHapusKegiatan;
    int idKegiatan;
    String intentDari;

    private RequestQueue queue;
    private String authToken;
    private String level;
    private String judulMessage, message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kegiatan);
        initViews();
        authToken = Preferences.getKeyToken(DetailKegiatanActivity.this);
        level = Preferences.getKeyLevel(DetailKegiatanActivity.this);
        queue = Volley.newRequestQueue(DetailKegiatanActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Kegiatan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        svDetailKegiatan.setVisibility(View.GONE);
        idKegiatan = getIntent().getExtras().getInt("idKegiatan", 0);
        intentDari = getIntent().getStringExtra("intentDari");
        initLevel(level);
        if (intentDari.equals("tambah kegiatan")){
            btnUbahKegiatan.setVisibility(View.GONE);
            btnHapusKegiatan.setVisibility(View.GONE);
            lihatTambah(authToken);
        } else if (intentDari.contains("ubah")){
            btnUbahKegiatan.setVisibility(View.GONE);
            btnHapusKegiatan.setVisibility(View.GONE);
            getData(authToken, idKegiatan);
        } else {
            getData(authToken, idKegiatan);
        }

        btnUbahKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailKegiatanActivity.this, UbahKegiatanActivity.class);
                intent.putExtra("idKegiatan", idKegiatan);
                intent.putExtra("intentDari", "ubah "+intentDari);
                startActivity(intent);
            }
        });

        btnHapusKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogHapusKegiatan();
            }
        });
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbDetailKegiatan);
        pbDetailKegiatan = findViewById(R.id.pbDetailKegiatan);
        svDetailKegiatan = findViewById(R.id.svDetailKegiatan);
        tvDetailNamaKegiatan = findViewById(R.id.tvValueNamaKegiatan);
        tvNoKegiatan = findViewById(R.id.tvValueNoKegiatan);
        tvTipeKegiatan = findViewById(R.id.tvValueDetailTipeKegiatan);
        tvTglKegiatan = findViewById(R.id.tvValueHariTglKegiatan);
        tvWaktuKegiatan = findViewById(R.id.tvValueDetailWaktuKegiatan);
        tvTempatKegiatan = findViewById(R.id.tvValueDetailTempatKegiatan);
        tvPembicaraKegiatan = findViewById(R.id.tvValueDetailPembicaraKegiatan);
        tvDeskripsiKegiatan =findViewById(R.id.tvValueDetailDeskripsiKegiatan);
        btnUbahKegiatan = findViewById(R.id.btnUbahKegiatan);
        btnHapusKegiatan = findViewById(R.id.btnHapusKegiatan);
    }

    public void getData(String token, int idKegiatan){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/kegiatan/"+idKegiatan, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbDetailKegiatan.setVisibility(View.GONE);
                svDetailKegiatan.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(0);
                        tvNoKegiatan.setText(responseObj.getString("no_kegiatan"));
                        tvDetailNamaKegiatan.setText(responseObj.getString("nama_kegiatan"));
                        tvTipeKegiatan.setText(responseObj.getString("tipe_kegiatan"));
                        tvTglKegiatan.setText(formatLihatFullTanggal(responseObj.getString("tgl_kegiatan")));
                        tvWaktuKegiatan.setText(formatLihatWaktu(responseObj.getString("waktu_kegiatan"))+" WIB");
                        tvTempatKegiatan.setText(responseObj.getString("tempat_kegiatan"));
                        tvPembicaraKegiatan.setText(responseObj.getString("pembicara_kegiatan"));
                        tvDeskripsiKegiatan.setText(responseObj.getString("deskripsi_kegiatan"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailKegiatanActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
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

    public void lihatTambah(String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/kegiatan", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbDetailKegiatan.setVisibility(View.GONE);
                svDetailKegiatan.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(response.length()-1);
                        tvNoKegiatan.setText(responseObj.getString("no_kegiatan"));
                        tvDetailNamaKegiatan.setText(responseObj.getString("nama_kegiatan"));
                        tvTipeKegiatan.setText(responseObj.getString("tipe_kegiatan"));
                        tvTglKegiatan.setText(formatLihatFullTanggal(responseObj.getString("tgl_kegiatan")));
                        tvWaktuKegiatan.setText(formatLihatWaktu(responseObj.getString("waktu_kegiatan"))+" WIB");
                        tvTempatKegiatan.setText(responseObj.getString("tempat_kegiatan"));
                        tvPembicaraKegiatan.setText(responseObj.getString("pembicara_kegiatan"));
                        tvDeskripsiKegiatan.setText(responseObj.getString("deskripsi_kegiatan"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailKegiatanActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
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

    private void initLevel(String level){
        if (level.equals("1") || level.equals("2")) {
            btnUbahKegiatan.setVisibility(View.GONE);
            btnHapusKegiatan.setVisibility(View.GONE);
        }
    }

    public void hapusKegiatan(){
        StringRequest dr = new StringRequest(Request.Method.DELETE, url+"/kegiatan/"+idKegiatan,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        judulMessage = "Sukses";
                        message = "Data kegiatan berhasil dihapus";
                        showDialogSuksesHapus(1);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String body = null;
                        try {
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(DetailKegiatanActivity.this, "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
                            } else if (error.networkResponse != null) {
                                if (error.networkResponse.data != null) {
                                    body = new String(error.networkResponse.data, "UTF-8");
                                    JSONObject obj = new JSONObject(body);
                                    JSONObject msg = obj.getJSONObject("messages");
                                    String errorMsg = msg.getString("error");
                                    judulMessage = "Gagal";
                                    message = errorMsg;
                                    showDialogSuksesHapus(2);
                                }
                            }
                            //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
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

    public void showDialogHapusKegiatan(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus Data Kegiatan");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Yakin menghapus kegiatan ini?")
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        hapusKegiatan();
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

    public void showDialogSuksesHapus(int i){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(DetailKegiatanActivity.this, R.style.DialogSlideAnim);
        View layoutView = getLayoutInflater().inflate(R.layout.dialogsukses, null);
        Button btnDialog = layoutView.findViewById(R.id.btnOkDialogSukses);
        ImageView ivDialog = layoutView.findViewById(R.id.ivIconDialog);
        TextView tvJudulDialog = layoutView.findViewById(R.id.tvJudulDialog);
        TextView tvKetSuksesAdmin = layoutView.findViewById(R.id.tvKeteranganDialogSukses);
        tvJudulDialog.setText(judulMessage);
        tvKetSuksesAdmin.setText(message);
        if (i == 1){
            ivDialog.setImageResource(R.drawable.ic_ok);
            btnDialog.setBackgroundResource(R.drawable.rounded_bg_primary);
            btnDialog.setText("Kembali");
        } else {
            ivDialog.setImageResource(R.drawable.ic_fail);
            btnDialog.setBackgroundResource(R.drawable.rounded_bg_red);
            btnDialog.setText("Saya Mengerti");
        }
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 2){
                    alertDialog.dismiss();
                } else {
                    intentDari = "hapus "+intentDari;
                    onBackPressed();
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
        if (intentDari.equals("jadwal kegiatan") || intentDari.equals("info kegiatan") || intentDari.equals("tambah kegiatan")){
            super.onBackPressed();
        } else if (intentDari.equals("ubah jadwal kegiatan")){
            Intent i = new Intent(DetailKegiatanActivity.this, JadwalKegiatanActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("intentDari", "detail kegiatan");
            startActivity(i);
        } else if (intentDari.equals("ubah info kegiatan") ){
            Intent i = new Intent(DetailKegiatanActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("intentDari", "detail kegiatan");
            startActivity(i);
        } else if (intentDari.equals("hapus info kegiatan") ) {
            Intent i = new Intent(DetailKegiatanActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("intentDari", "detail kegiatan");
            startActivity(i);
        } else {
            Intent i = new Intent(DetailKegiatanActivity.this, JadwalKegiatanActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("intentDari", "detail kegiatan");
            startActivity(i);
        }
    }
}