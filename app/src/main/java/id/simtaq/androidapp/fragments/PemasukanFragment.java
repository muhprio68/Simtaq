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
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.DetailKeuanganActivity;
import id.simtaq.androidapp.R;
import id.simtaq.androidapp.TambahKegiatanActivity;
import id.simtaq.androidapp.UbahKeuanganActivity;
import id.simtaq.androidapp.helper.Preferences;

import static id.simtaq.androidapp.helper.config.formatSimpanTanggal;
import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.url;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PemasukanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PemasukanFragment extends Fragment {

    private FrameLayout flCatatPemasukan;
    private ProgressBar pbCatatPemasukan;
    private EditText etTglPemasukan;
    private EditText etKetPemasukan;
    private EditText etNominalPemasukan;
    private EditText etDeskripPemasukan;
    private Spinner spJenisPemasukan;
    private Button btnSimpanPemasukan;
    private Button btnBatalPemasukan;

    private String tglPemasukan, ketPemasukan, jenisPemasukan, nominalPemasukan, deskripPemasukan;
    private String authToken;
    private RequestQueue queue;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String jmlSaldo;

    public PemasukanFragment() {
        // Required empty public constructor
    }

    public static PemasukanFragment newInstance() {
        PemasukanFragment fragment = new PemasukanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pemasukan, container, false);
        initViews(view);
        authToken = Preferences.getKeyToken(getContext());
        queue = Volley.newRequestQueue(getContext());
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", locale);

        etTglPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        btnSimpanPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tglPemasukan = etTglPemasukan.getText().toString();
                ketPemasukan = etKetPemasukan.getText().toString();
                jenisPemasukan = spJenisPemasukan.getSelectedItem().toString();
                nominalPemasukan = etNominalPemasukan.getText().toString();
                deskripPemasukan = etDeskripPemasukan.getText().toString();

                if (TextUtils.isEmpty(tglPemasukan)) {
                    etTglPemasukan.setError("Masukkan tanggal");
                } else if (TextUtils.isEmpty(ketPemasukan)){
                    etKetPemasukan.setError("Masukkan keterangan");
                } else if (TextUtils.isEmpty(nominalPemasukan)){
                    etNominalPemasukan.setError("Masukkan nominal");
                } else {
                    tambahDataPemasukan(authToken, formatSimpanTanggal(tglPemasukan), ketPemasukan, jenisPemasukan, nominalPemasukan, deskripPemasukan);
                }
            }
        });

        btnBatalPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etTglPemasukan.setText("");
                etKetPemasukan.setText("");
                etNominalPemasukan.setText("");
                etDeskripPemasukan.setText("");
                Toast.makeText(getContext(), "Catat Pemasukan dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void initViews(View v){
        flCatatPemasukan = v.findViewById(R.id.flCatatPemasukan);
        pbCatatPemasukan = v.findViewById(R.id.pbCatatPemasukan);
        etTglPemasukan = v.findViewById(R.id.etTanggalPemasukan);
        etKetPemasukan = v.findViewById(R.id.etKeteranganPemasukan);
        etNominalPemasukan = v.findViewById(R.id.etNominalPemasukan);
        etDeskripPemasukan = v.findViewById(R.id.etDeskripsiPemasukan);
        spJenisPemasukan = v.findViewById(R.id.spJenisPemasukan);
        btnSimpanPemasukan = v.findViewById(R.id.btnSimpanPemasukan);
        btnBatalPemasukan = v.findViewById(R.id.btnBatalPemasukan);
    }

    private void tambahDataPemasukan(String token, String tglPemasukan, String ketPemasukan, String jenisPemasukan, String nominalPemasukan, String deskripPemasukan) {
        StringRequest request = new StringRequest(Request.Method.POST, url+"/keuangan", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbCatatPemasukan.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                showDialogBerhasilTambah();
                etTglPemasukan.setText("");
                etKetPemasukan.setText("");
                etNominalPemasukan.setText("");
                etDeskripPemasukan.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body = null;
                try {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getContext(), "Tidak ada koneksi internet, silahkan nyalakan data", Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse != null) {
                        if (error.networkResponse.data != null) {
                            body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject obj = new JSONObject(body);
                            JSONObject msg = obj.getJSONObject("messages");
                            String errorMsg = msg.getString("error");
                            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tipe_keuangan", "Pemasukan");
                params.put("tgl_keuangan", tglPemasukan);
                params.put("keterangan_keuangan", ketPemasukan);
                params.put("jenis_keuangan", jenisPemasukan);
                params.put("status_keuangan", "Selesai");
                params.put("nominal_keuangan", nominalPemasukan);
                params.put("deskripsi_keuangan", deskripPemasukan);
                return params;
            }
        };
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

                etTglPemasukan.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
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