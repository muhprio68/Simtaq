package id.simtaq.androidapp.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    snackbarWithAction();

                    //Toast.makeText(TambahKegiatanActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // and setting data to edit text as empty
                etTglPemasukan.setText("");
                etKetPemasukan.setText("");
                etNominalPemasukan.setText("");
                etDeskripPemasukan.setText("");
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
                params.put("tipe_keuangan", "Pemasukan");
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

    public void snackbarWithAction(){
        Snackbar snackbar = Snackbar.make(flCatatPemasukan ,"Data berhasil disimpan",Snackbar.LENGTH_SHORT);
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
}