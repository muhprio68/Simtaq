package id.simtaq.androidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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

public class UbahDonaturActivity extends AppCompatActivity {
    private RequestQueue queue;
    private String authToken;

    private Toolbar toolbar;
    private ConstraintLayout clUbahDonatur;
    private EditText etTglDonatur, etPetugasDonatur, etNominalDonatur;
    private Spinner spWilDonatur;
    private Button btnSimpanUbahDonatur;
    private Button btnBatalUbahDonatur;

    private int idDonatur, idKeuangan;
    private String tglDonatur, wilDonatur, petugasDonatur, nominalDonatur;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String[] petugas;
    private String message, judulMessage;
    private String pilih1, pilih2;
    private int iconDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_donatur);
        initViews();
        authToken = Preferences.getKeyToken(UbahDonaturActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ubah Donatur");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        idKeuangan = getIntent().getExtras().getInt("idKeuangan",0);
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", locale);
        queue = Volley.newRequestQueue(UbahDonaturActivity.this);
        initPetugas();
        getDataUbahDonatur(authToken);

        etTglDonatur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        spWilDonatur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                etPetugasDonatur.setText(petugas[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSimpanUbahDonatur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tglDonatur = etTglDonatur.getText().toString();
                String valueSpinner = spWilDonatur.getSelectedItem().toString();
                wilDonatur = valueSpinner;
                petugasDonatur = etPetugasDonatur.getText().toString();
                nominalDonatur = etNominalDonatur.getText().toString();

                if (TextUtils.isEmpty(tglDonatur)) {
                    etTglDonatur.requestFocus();
                    etTglDonatur.setError("Masukkan tanggal");
                }  else if (TextUtils.isEmpty(nominalDonatur)){
                    etNominalDonatur.requestFocus();
                    etNominalDonatur.setError("Masukkan nominal");
                } else {
                    ubahDataDonatur(authToken, formatSimpanTanggal(tglDonatur), wilDonatur, petugasDonatur, nominalDonatur);
                }
            }
        });

        btnBatalUbahDonatur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etTglDonatur.setText("");
                etNominalDonatur.setText("");
                etPetugasDonatur.setText("");
                Toast.makeText(UbahDonaturActivity.this, "Ubah donatur dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initViews(){
        clUbahDonatur = findViewById(R.id.clUbahDonatur);
        toolbar = findViewById(R.id.tbUbahDonatur);
        spWilDonatur = findViewById(R.id.spUbahWilayahDonatur);
        etTglDonatur = findViewById(R.id.etUbahTanggalDonatur);
        etPetugasDonatur = findViewById(R.id.etUbahPetugasDonatur);
        etNominalDonatur = findViewById(R.id.etUbahNominalDonatur);
        btnSimpanUbahDonatur = findViewById(R.id.btnSimpanUbahDonatur);
        btnBatalUbahDonatur = findViewById(R.id.btnBatalUbahDonatur);
    }

    public void initPetugas(){
        petugas = getResources().getStringArray(R.array.petugas_donatur);
    }

    public void getDataUbahDonatur(String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/donatur/"+idKeuangan, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //pbDetailKegiatan.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(0);
                        idDonatur = responseObj.getInt("id_donatur");
                        etTglDonatur.setText(formatLihatTanggal(responseObj.getString("tgl_donatur")));
                        for (int a = 0; a < 5; a++){
                            if (spWilDonatur.getItemAtPosition(a).toString().equals(responseObj.getString("wilayah_donatur"))){
                                spWilDonatur.setSelection(a);
                            }
                        }
                        etPetugasDonatur.setText(responseObj.getString("petugas_donatur"));;
                        etNominalDonatur.setText(responseObj.getString("nominal_donatur"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UbahDonaturActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
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

    private void ubahDataDonatur(String token, String tglDonatur, String wilDonatur, String petugasDonatur, String nominalDonatur) {
        RequestQueue queue = Volley.newRequestQueue(UbahDonaturActivity.this);

        StringRequest request = new StringRequest(Request.Method.PUT, url+"/donatur/"+idDonatur, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    //snackbarWithAction();
                    judulMessage = "Berhasil";
                    message = "Donatur berhasil ditambahkan, ingin lihat detailnya?";
                    pilih1 = "Lihat";
                    pilih2 = "Tidak";
                    iconDialog = R.drawable.ic_ok;
                    showDialogMsg(1);

                    //Toast.makeText(TambahKegiatanActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                String body = null;
                try {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        judulMessage = "Gagal";
                        message = "Tidak ada koneksi internet, silahkan nyalakan data";
                        pilih1 = "Saya Mengerti";
                        pilih2 = "Keluar";
                        iconDialog = R.drawable.ic_fail;
                        showDialogMsg(3);
                    } else if (error.networkResponse != null) {
                        if (error.networkResponse.data != null) {
                            body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject obj = new JSONObject(body);
                            JSONObject msg = obj.getJSONObject("messages");
                            String errorMsg = msg.getString("error");
                            judulMessage = "Gagal";
                            message = errorMsg;
                            pilih1 = "Saya Mengerti";
                            pilih2 = "Lihat Donatur";
                            iconDialog = R.drawable.ic_fail;
                            showDialogMsg(2);
                        }
                    }
                    //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
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
                params.put("tgl_donatur", tglDonatur);
                params.put("wilayah_donatur", wilDonatur);
                params.put("petugas_donatur", petugasDonatur);
                params.put("nominal_donatur", nominalDonatur);
                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void showDateDialog(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                etTglDonatur.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    public void lihatUbahData() {
        Intent intent = new Intent(UbahDonaturActivity.this, DetailKeuanganActivity.class);
        intent.putExtra("intentDari", "ubah donatur");
        intent.putExtra("idKeuangan", idKeuangan);
        startActivity(intent);
        finish();
    }

    public void showDialogMsg(int i){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(UbahDonaturActivity.this, R.style.DialogSlideAnim);
        View layoutView = getLayoutInflater().inflate(R.layout.dialogberhasiltambahdata, null);
        ImageView ivIconDialog = layoutView.findViewById(R.id.ivIconDialog);
        Button btnDialog1 = layoutView.findViewById(R.id.btnYaDialogBerhasil);
        Button btnDialog2 = layoutView.findViewById(R.id.btnTidakDialogBerhasil);
        TextView tvJdlMsg = layoutView.findViewById(R.id.tvJudulDialog);
        TextView tvKetMsg = layoutView.findViewById(R.id.tvKeteranganDialogBerhasil);
        ivIconDialog.setImageResource(iconDialog);
        btnDialog1.setText(pilih1);
        btnDialog2.setText(pilih2);
        tvJdlMsg.setText(judulMessage);
        tvKetMsg.setText(message);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (i==1){
            btnDialog1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    lihatUbahData();
                }
            });

            btnDialog2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

        } else if (i == 2){
            btnDialog1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            btnDialog2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    Intent i = new Intent(UbahDonaturActivity.this, RiwayatDonaturActivity.class);
                    i.putExtra("intentDari", "ubah donatur");
                    startActivity(i);
                }
            });
        } else{
            btnDialog1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            btnDialog2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    finish();
                    System.exit(0);
                }
            });
        }
    }

}