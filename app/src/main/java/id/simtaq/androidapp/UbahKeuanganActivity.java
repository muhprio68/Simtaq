package id.simtaq.androidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.helper.Preferences;

import static id.simtaq.androidapp.helper.config.formatLihatTanggal;
import static id.simtaq.androidapp.helper.config.formatLihatWaktu;
import static id.simtaq.androidapp.helper.config.formatSimpanTanggal;
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
    private String tipeKeuangan, tglKeuangan, ketKeuangan, jenisKeuanagan, nominalKeuangan, deskripKeuangan;
    private String valueTipe;
    private String valueJenis = null;
    private RequestQueue queue;
    private String authToken;
    private String intentDari;
    private ArrayAdapter a1;
    private ArrayAdapter a2;
    private String[] arrayPemasukan;
    private String[] arrayPengeluaran;
    private String[] arrayKeuangan;
    private int selectJenis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_keuangan);
        initViews();
        authToken = Preferences.getKeyToken(UbahKeuanganActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ubah Keuangan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        selectJenis = 0;
        idKeuangan = getIntent().getIntExtra("idKeuangan",0);
        intentDari = getIntent().getStringExtra("intentDari");
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", locale);
        queue = Volley.newRequestQueue(UbahKeuanganActivity.this);
        arrayPemasukan = getResources().getStringArray(R.array.jenis_pemasukan);
        arrayPengeluaran = getResources().getStringArray(R.array.jenis_pengeluaran);
        getDataUbahKeuangan(authToken);
        etTglKeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        spTipeKeu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    arrayPemasukan = getResources().getStringArray(R.array.jenis_pemasukan);
                    a1 = new ArrayAdapter<String>(UbahKeuanganActivity.this, android.R.layout.simple_spinner_item,arrayPemasukan);
                    a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spJenisKeu.setAdapter(a1);
                    spJenisKeu.setSelection(selectJenis);
                    //for (int j =0; j<arrayPemasukan.length; j++){
//                        if (valueJenis.equals("Infaq")){
//                            spJenisKeu.setSelection(1);
//                        } else {
//                            spJenisKeu.setSelection(0);
//                        }
                    //}
                } else {
                    arrayPengeluaran = getResources().getStringArray(R.array.jenis_pengeluaran);
                    a2 = new ArrayAdapter<String>(UbahKeuanganActivity.this, android.R.layout.simple_spinner_item,arrayPengeluaran);
                    a2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spJenisKeu.setAdapter(a2);
                    spJenisKeu.setSelection(selectJenis);
                    //for (int k =0; k<arrayPengeluaran.length; k++){
//                        if (valueJenis.equals("Perawatan Masjid")){
//                            spJenisKeu.setSelection(1);
//                        } else {
//                            spJenisKeu.setSelection(0);
//                        }
                    //}
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSimpanUbahKeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipeKeuangan = spTipeKeu.getSelectedItem().toString();
                tglKeuangan = etTglKeu.getText().toString();
                ketKeuangan = etKetKeu.getText().toString();
                jenisKeuanagan = spJenisKeu.getSelectedItem().toString();
                nominalKeuangan = etNominalKeu.getText().toString();
                deskripKeuangan = etDesKeu.getText().toString();

                if (TextUtils.isEmpty(tglKeuangan)) {
                    etTglKeu.setError("Masukkan tanggal");
                } else if (TextUtils.isEmpty(ketKeuangan)){
                    etKetKeu.setError("Masukkan keterangan");
                } else if (TextUtils.isEmpty(nominalKeuangan)){
                    etNominalKeu.setError("Masukkan nominal");
                } else {
                   ubahKeuangan(authToken, tipeKeuangan, formatSimpanTanggal(tglKeuangan), ketKeuangan, jenisKeuanagan, nominalKeuangan, deskripKeuangan);
                }
            }
        });

        btnBatalUbahKeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
//                spTipeKeu.setSelection(0);
//                etTglKeu.setText("");
//                etKetKeu.setText("");
//                spJenisKeu.setSelection(0);
//                etNominalKeu.setText("");
//                etDesKeu.setText("");
//                Toast.makeText(UbahKeuanganActivity.this, "Perubahan keuangan dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });
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
                        valueTipe = responseObj.getString("tipe_keuangan");
                        valueJenis= responseObj.getString("jenis_keuangan");
                        if (valueTipe.equals("Pemasukan")){
                            spTipeKeu.setSelection(0);
                            for (int j =0; j<arrayPemasukan.length; j++){
                                if (valueJenis.contains(arrayPemasukan[j])){
                                    selectJenis = j;
                                }
                            }
                            spJenisKeu.setSelection(selectJenis);
                        } else{
                            spTipeKeu.setSelection(1);
                            for (int k =0; k<arrayPengeluaran.length; k++){
                                if (valueJenis.contains(arrayPengeluaran[k])){
                                    selectJenis = k;
                                }
                            }
                        }
                        etTglKeu.setText(formatLihatTanggal(responseObj.getString("tgl_keuangan")));
                        etKetKeu.setText(responseObj.getString("keterangan_keuangan"));
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

    private void ubahKeuangan(String token, String tipeKeuangan, String tglPemasukan, String ketPemasukan, String jenisPemasukan, String nominalPemasukan, String deskripPemasukan) {
        RequestQueue queue = Volley.newRequestQueue(UbahKeuanganActivity.this);

        StringRequest request = new StringRequest(Request.Method.PUT, url+"/keuangan/"+idKeuangan, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    //snackbarWithAction();
                    showDialogBerhasilUbah();

                    //Toast.makeText(TambahKegiatanActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // and setting data to edit text as empty
//                etNamaKegiatan.setText("");
//                etTglKegiatan.setText("");
//                etWaktuKegiatan.setText("");
//                etTempatKegiatan.setText("");
//                etPembicaraKegiatan.setText("");
//                etDeskripsiKegiatan.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(UbahKeuanganActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("tipe_keuangan", tipeKeuangan);
                params.put("tgl_keuangan", tglPemasukan);
                params.put("keterangan_keuangan", ketPemasukan);
                params.put("jenis_keuangan", jenisPemasukan);
                params.put("status_keuangan", "Selesai");
                params.put("nominal_keuangan", nominalPemasukan);
                params.put("deskripsi_keuangan", deskripPemasukan);
                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                etTglKeu.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void lihatUbahData() {
        Intent intent = new Intent(UbahKeuanganActivity.this, DetailKeuanganActivity.class);
        intent.putExtra("intentDari", intentDari);
        intent.putExtra("idKeuangan", idKeuangan);
        startActivity(intent);
        finish();
    }

    public void showDialogBerhasilUbah(){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(UbahKeuanganActivity.this, R.style.DialogSlideAnim);
        View layoutView = getLayoutInflater().inflate(R.layout.dialogberhasiltambahdata, null);
        Button btnDialogYa = layoutView.findViewById(R.id.btnYaDialogBerhasil);
        Button btnDialogTidak = layoutView.findViewById(R.id.btnTidakDialogBerhasil);
        TextView tvKetBerhasil = layoutView.findViewById(R.id.tvKeteranganDialogBerhasil);
        btnDialogYa.setText("Ya");
        btnDialogTidak.setText("Tidak");
        tvKetBerhasil.setText("Data keuangan berhasil diubah, apakah anda ingin melihat detailnya?");
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnDialogYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lihatUbahData();
            }
        });

        btnDialogTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (intentDari.equals("ubah jadwal kegiatan")){
                    Intent i = new Intent(UbahKeuanganActivity.this, JadwalKegiatanActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("intentDari", "detail keuangan");
                    startActivity(i);
                } else if (intentDari.equals("ubah info kegiatan") ){
                    Intent i = new Intent(UbahKeuanganActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("intentDari", "detail keuangan");
                    startActivity(i);
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
        super.onBackPressed();
    }
}