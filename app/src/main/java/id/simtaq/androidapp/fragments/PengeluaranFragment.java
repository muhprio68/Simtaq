package id.simtaq.androidapp.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.DetailKeuanganActivity;
import id.simtaq.androidapp.R;
import id.simtaq.androidapp.helper.Preferences;

import static id.simtaq.androidapp.helper.config.formatSimpanTanggal;
import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.url;

public class PengeluaranFragment extends Fragment {

    private FrameLayout flCatatPengeluaran;
    private ProgressBar pbCatatPengeluaran;
    private EditText etTglPengeluaran;
    private EditText etKetPengeluaran;
    private EditText etNominalPengeluaran;
    private EditText etDeskripPengeluaran;
    private Spinner spJenisPengeluaran;
    private Button btnSimpanPengeluaran;
    private Button btnBatalPengeluaran;

    private String tglPengeluaran, ketPengeluaran, jenisPengeluaran, nominalPengeluaran, deskripPengeluaran;
    private String authToken;
    private RequestQueue queue;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String jmlSaldo;

    public PengeluaranFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PengeluaranFragment newInstance() {
        PengeluaranFragment fragment = new PengeluaranFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengeluaran, container, false);
        initViews(view);
        authToken = Preferences.getKeyToken(getContext());
        queue = Volley.newRequestQueue(getContext());
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", locale);
        etTglPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        btnSimpanPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tglPengeluaran = etTglPengeluaran.getText().toString();
                ketPengeluaran = etKetPengeluaran.getText().toString();
                jenisPengeluaran = spJenisPengeluaran.getSelectedItem().toString();
                nominalPengeluaran = etNominalPengeluaran.getText().toString();
                deskripPengeluaran = etDeskripPengeluaran.getText().toString();

                if (TextUtils.isEmpty(tglPengeluaran)) {
                    etTglPengeluaran.setError("Masukkan nama kegiatan");
                } else if (TextUtils.isEmpty(ketPengeluaran)){
                    etKetPengeluaran.setError("Masukkan tanggal kegiatan");
                } else if (TextUtils.isEmpty(nominalPengeluaran)){
                    etNominalPengeluaran.setError("Masukkan waktu kegiatan");
                } else {
                    tambahDataPengeluaran(authToken, formatSimpanTanggal(tglPengeluaran), ketPengeluaran, jenisPengeluaran, nominalPengeluaran, deskripPengeluaran);
                }
            }
        });

        btnBatalPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etTglPengeluaran.setText("");
                etKetPengeluaran.setText("");
                etNominalPengeluaran.setText("");
                etDeskripPengeluaran.setText("");
                Toast.makeText(getContext(), "Catat pengeluaran dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void initViews(View v){
        flCatatPengeluaran = v.findViewById(R.id.flCatatPengeluaran);
        pbCatatPengeluaran = v.findViewById(R.id.pbCatatPengeluaran);
        etTglPengeluaran = v.findViewById(R.id.etTanggalPengeluaran);
        etKetPengeluaran = v.findViewById(R.id.etKeteranganPengeluaran);
        etNominalPengeluaran = v.findViewById(R.id.etNominalPengeluaran);
        etDeskripPengeluaran = v.findViewById(R.id.etDeskripsiPengeluaran);
        spJenisPengeluaran = v.findViewById(R.id.spJenisPengeluaran);
        btnSimpanPengeluaran = v.findViewById(R.id.btnSimpanPengeluaran);
        btnBatalPengeluaran = v.findViewById(R.id.btnBatalPengeluaran);
    }

    private void tambahDataPengeluaran(String token, String tglPengeluaran, String ketPengeluaran, String jenisPengeluaran, String nominalPengeluaran, String deskripPengeluaran) {
        StringRequest request = new StringRequest(Request.Method.POST, url+"/keuangan", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                pbCatatPengeluaran.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    //snackbarWithAction();
                    showDialogBerhasilTambah();

                    //Toast.makeText(TambahKegiatanActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // and setting data to edit text as empty
                etTglPengeluaran.setText("");
                etKetPengeluaran.setText("");
                etNominalPengeluaran.setText("");
                etDeskripPengeluaran.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(getContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("tipe_keuangan", "Pengeluaran");
                params.put("tgl_keuangan", tglPengeluaran);
                params.put("keterangan_keuangan", ketPengeluaran);
                params.put("jenis_keuangan", jenisPengeluaran);
                params.put("status_keuangan", "Selesai");
                params.put("nominal_keuangan", nominalPengeluaran);
                params.put("deskripsi_keuangan", deskripPengeluaran);

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
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                etTglPengeluaran.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    public void snackbarWithAction(){
        Snackbar snackbar = Snackbar.make(flCatatPengeluaran ,"Data berhasil disimpan",Snackbar.LENGTH_SHORT);
        snackbar.setAction("Lihat", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lihatTambahData();
                //Toast.makeText(getApplicationContext(),"Lahhhh",Toast.LENGTH_SHORT).show();
            }
        });
        snackbar.show();
    }

    public void lihatTambahData() {
        Intent intent = new Intent(getContext(), DetailKeuanganActivity.class);
        intent.putExtra("intentDari", "catat keuangan");
        startActivity(intent);
    }

    public void showDialogBerhasilTambah(){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(getContext(), R.style.DialogSlideAnim);
        View layoutView = getLayoutInflater().inflate(R.layout.dialogberhasiltambahdata, null);
        Button btnDialogYa = layoutView.findViewById(R.id.btnYaDialogBerhasil);
        Button btnDialogTidak = layoutView.findViewById(R.id.btnTidakDialogBerhasil);
        TextView tvKetBerhasil = layoutView.findViewById(R.id.tvKeteranganDialogBerhasil);
        btnDialogYa.setText("Ya");
        btnDialogTidak.setText("Tidak");
        tvKetBerhasil.setText("Data keuangan berhasil ditambahkan, apakah anda ingin melihat detailnya?");
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnDialogYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lihatTambahData();
                alertDialog.dismiss();
            }
        });

        btnDialogTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}