package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.helper.Preferences;

import static id.simtaq.androidapp.helper.config.formatLihatTanggal;
import static id.simtaq.androidapp.helper.config.formatLihatWaktu;
import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.url;

public class UbahKeuanganActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spTipeKeu, spJenisKeu;
    private EditText etTglKeu, etKetKeu, etNominalKeu, etDesKeu;
    private Button btnSimpanUbahKeu, btnBatalUbahKeu;

    private ConstraintLayout clUbahKeuangan;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TimePickerDialog timePickerDialog;

    private int idKeuangan;
    private String noKeuangan, tipeKeuangan, tglKeuangan, ketKeuangan, jenisKeuanagan, nominalKeuangan, deskripKeuangan;

    private RequestQueue queue;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_keuangan);
        initViews();
        authToken = Preferences.getKeyToken(UbahKeuanganActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ubah Kegiatan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        idKeuangan = getIntent().getIntExtra("idKeuangan",0);
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", locale);
        queue = Volley.newRequestQueue(UbahKeuanganActivity.this);
        getDataUbahKeuangan(authToken);
    }

    public void initViews(){
        clUbahKeuangan = findViewById(R.id.clUbahKeuangan);
        toolbar = findViewById(R.id.tbUbahKeuangan);
        spTipeKeu = findViewById(R.id.spUbahTipeKeuangan);
        spJenisKeu = findViewById(R.id.spUbahJenisKeuangan);
        etTglKeu = findViewById(R.id.etUbahTanggalKeuangan);
        etKetKeu = findViewById(R.id.etUbahKeteranganKeuangan);
        etNominalKeu = findViewById(R.id.etUbahNominalKeuangan);
        etDesKeu = findViewById(R.id.etUbahDeskripsiKeuangan);
        btnSimpanUbahKeu = findViewById(R.id.btnSimpanUbahKeuangan);
        btnBatalUbahKeu = findViewById(R.id.btnBatalUbahKeuangan);
    }

    public void getDataUbahKeuangan(String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/keuangan/"+idKeuangan, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //pbDetailKegiatan.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(0);
                        if (responseObj.getString("tipe_keuangan").equals("Pemasukan")){
                            spTipeKeu.setSelection(0);
                        } else{
                            spTipeKeu.setSelection(1);
                        }
                        etTglKeu.setText(formatLihatTanggal(responseObj.getString("tgl_keuangan")));
                        etKetKeu.setText(responseObj.getString("keterangan_keuangan"));
                        String b = responseObj.getString("jenis_keuangan");
                        for(int f=0; f<4 ;f++){
                            if(spJenisKeu.getItemAtPosition(f).toString().equals(b)){
                                spJenisKeu.setSelection(f);
                            }
                        }
                        etNominalKeu.setText(responseObj.getString("nominal_keuangan"));
                        etDesKeu.setText(responseObj.getString("deskripsi_keuangan"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UbahKeuanganActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
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