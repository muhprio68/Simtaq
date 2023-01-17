package id.simtaq.androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.adapter.JadwalKegiatanAdapter;
import id.simtaq.androidapp.adapter.PengaturanListAdapter;
import id.simtaq.androidapp.adapter.PenggunaListAdapter;
import id.simtaq.androidapp.helper.Preferences;
import id.simtaq.androidapp.models.Keuangan;
import id.simtaq.androidapp.models.Pengguna;

import static id.simtaq.androidapp.helper.config.url;

public class ListPenggunaActivity extends AppCompatActivity implements PenggunaListAdapter.IPenggunaAdapter {

    private Toolbar toolbar;
    private RecyclerView rvPenggunaList;
    private ArrayList<Pengguna> penggunaList;
    private ConstraintLayout clPengggunaList;
    private RequestQueue queue;
    private PenggunaListAdapter adapter;
    private int tipe;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pengguna);
        initViews();
        tipe = getIntent().getIntExtra("tipe", 0);
        authToken = Preferences.getKeyToken(ListPenggunaActivity.this);
        setSupportActionBar(toolbar);
        if (tipe == 1){
            getSupportActionBar().setTitle("Ubah Pengguna");
        } else {
            getSupportActionBar().setTitle("Hapus Pengguna");
            enableSwipeToDeleteAndUndo();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        penggunaList = new ArrayList<>();
//        penggunaList.add(new Pengguna(1, "blabla1", "blabla1@gmail.com", "pppppp", "1"));
//        penggunaList.add(new Pengguna(2, "blabla2", "blabla2@gmail.com", "pppppp", "2"));
//        penggunaList.add(new Pengguna(3, "blabla3", "blabla3@gmail.com", "pppppp", "3"));
//        penggunaList.add(new Pengguna(4, "blabla4", "blabla4@gmail.com", "pppppp", "4"));
        queue = Volley.newRequestQueue(ListPenggunaActivity.this);
        getPengguna(authToken);
        buildRecyclerView();
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbListPengguna);
        rvPenggunaList = findViewById(R.id.rvPengguna);
        clPengggunaList = findViewById(R.id.clPengguna);
    }

    public void buildRecyclerView(){
        adapter = new PenggunaListAdapter(penggunaList, ListPenggunaActivity.this,tipe, this, queue, clPengggunaList, authToken);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvPenggunaList.setHasFixedSize(true);
        rvPenggunaList.setLayoutManager(manager);
        rvPenggunaList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void getPengguna (String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/user", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //pbRiwayatKeuangan.setVisibility(View.GONE);
                rvPenggunaList.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        int id = responseObj.getInt("id");
                        String nama = responseObj.getString("nama");
                        String email = responseObj.getString("email");
                        String password = responseObj.getString("password");
                        String level = responseObj.getString("level");
                        penggunaList.add(new Pengguna(id, nama, email, password, level));
                        buildRecyclerView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListPenggunaActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
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

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                hapusDialog(position);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rvPenggunaList);
    }

    public void hapusDialog(int position){
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
                        adapter.removeItem(position);
                        buildRecyclerView();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                        buildRecyclerView();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    @Override
    public void doClick(int id) {
        if (tipe == 1){
            Intent intent = new Intent(ListPenggunaActivity.this, UbahAkunActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        } else {
            hapusDialog(id);
        }
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