package id.simtaq.androidapp.fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.CatatDonaturActivity;
import id.simtaq.androidapp.CatatKeuanganActivity;
import id.simtaq.androidapp.JariyahOnlineActivity;
import id.simtaq.androidapp.JadwalKegiatanActivity;
import id.simtaq.androidapp.PengurusTakmirActivity;
import id.simtaq.androidapp.R;
import id.simtaq.androidapp.RiwayatActivity;
import id.simtaq.androidapp.RiwayatDonaturActivity;
import id.simtaq.androidapp.TambahKegiatanActivity;
import id.simtaq.androidapp.TentangSimtaqActivity;
import id.simtaq.androidapp.helper.Preferences;
import id.simtaq.androidapp.helper.config;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout rlRiwayatUangKas, rlJadwalKegiatan, rlJariyahOnline;
    private RelativeLayout rlCatatKeuangan,rlCatatDonatur, rlTambahKegiatan;
    private RelativeLayout rlLokasiKegiatan, rlPengurusTakmir, rlTentangSimtaq;

    private TextView tvCatatKeuangan, tvCatatDonatur, tvTambahKegiatan;

    private String id, nama, email, level, authToken;
    private String judulMessage, message;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        level = Preferences.getKeyLevel(view.getContext());

        //authToken = Preferences.getKeyToken(view.getContext());
        //auth(authToken, view);
        rlRiwayatUangKas.setOnClickListener(this);
        rlJadwalKegiatan.setOnClickListener(this);
        rlJariyahOnline.setOnClickListener(this);
        rlCatatKeuangan.setOnClickListener(this);
        rlCatatDonatur.setOnClickListener(this);
        rlTambahKegiatan.setOnClickListener(this);
        rlLokasiKegiatan.setOnClickListener(this);
        rlPengurusTakmir.setOnClickListener(this);
        rlTentangSimtaq.setOnClickListener(this);
        initLevel(level);
        return view;
    }

    public void initViews(View v){
        rlRiwayatUangKas = v.findViewById(R.id.rlButtonRiwayatUangKas);
        rlJadwalKegiatan = v.findViewById(R.id.rlButtonJadwalKegiatan);
        rlJariyahOnline = v.findViewById(R.id.rlJariyahOnline);
        rlCatatKeuangan = v.findViewById(R.id.rlCatatKeuangan);
        rlCatatDonatur = v.findViewById(R.id.rlCatatDonatur);
        rlTambahKegiatan = v.findViewById(R.id.rlTambahKegiatan);
        rlLokasiKegiatan = v.findViewById(R.id.rlLokasiKegiatan);
        rlPengurusTakmir = v.findViewById(R.id.rlPengurusTakmir);
        rlTentangSimtaq = v.findViewById(R.id.rlTentangSimtaq);
    }

    @Override
    public void onClick(View v) {
        if (v == rlRiwayatUangKas){
            startActivity(new Intent(v.getContext(), RiwayatActivity.class));
        } else if (v== rlJadwalKegiatan){
            startActivity(new Intent(v.getContext(), JadwalKegiatanActivity.class));
        } else if (v== rlJariyahOnline){
            startActivity(new Intent(v.getContext(), JariyahOnlineActivity.class));
        } else if (v== rlCatatKeuangan){
            if (level.equals("3")){
                Toast.makeText(getContext(), "Menu hanya untuk bendahara takmir", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(v.getContext(), CatatKeuanganActivity.class));
            }
        } else if (v== rlCatatDonatur){
            if (level.equals("3")){
                Toast.makeText(getContext(), "Menu hanya untuk bendahara takmir", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(v.getContext(), CatatDonaturActivity.class));
            }
        } else if (v== rlTambahKegiatan){
            if (level.equals("2")){
                Toast.makeText(getContext(), "Menu hanya untuk humas takmir", Toast.LENGTH_SHORT).show();
            } else{
                startActivity(new Intent(v.getContext(), TambahKegiatanActivity.class));
            }
        } else if (v== rlLokasiKegiatan){
            startActivity(new Intent(v.getContext(), RiwayatDonaturActivity.class));
        } else if (v== rlPengurusTakmir){
            startActivity(new Intent(v.getContext(), PengurusTakmirActivity.class));
        } else if (v== rlTentangSimtaq){
            startActivity(new Intent(v.getContext(), TentangSimtaqActivity.class));
        }
    }

    private void initLevel(String level){
        if (level.equals("1")) {
            rlCatatKeuangan.setVisibility(View.GONE);
            rlCatatDonatur.setVisibility(View.GONE);
            rlTambahKegiatan.setVisibility(View.GONE);
        }
    }

    private void auth(String token, View view) {
        // url to post our data
        String url = config.url+"/me";
        //loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
//                loadingPB.setVisibility(View.GONE);
//                nameEdt.setText("");
//                jobEdt.setText("");

                // on below line we are displaying a success toast message.
                //Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);

                    // below are the strings which we
                    // extract from our json object.
                    id = respObj.getString("id");
                    nama = respObj.getString("nama");
                    email = respObj.getString("email");
                    level = respObj.getString("level");

                    Preferences.setKeyId(view.getContext(), id);
                    Preferences.setKeyNama(view.getContext(), nama);
                    Preferences.setKeyEmail(view.getContext(), email);
                    Preferences.setKeyLevel(view.getContext(), level);

                    // on below line we are setting this string s to our text view.
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                Toast.makeText(MainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                intent.putExtra("intentDari", "main");
//                startActivity(intent);
//                finish();
                String body = null;
                try {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        judulMessage = "Gagal";
                        message = "Tidak ada koneksi internet, silahkan nyalakan data";
                        showDialogMsg(2, view);
                    } else if (error.networkResponse != null) {
                        if (error.networkResponse.data != null) {
                            body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject obj = new JSONObject(body);
                            JSONObject msg = obj.getJSONObject("messages");
                            String errorMsg = msg.getString("error");
                            judulMessage = "Gagal";
                            message = errorMsg;
                            showDialogMsg(2, view);
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
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    public void showDialogMsg(int i, View view){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(view.getContext(), R.style.DialogSlideAnim);
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
                }
            }
        });
    }
}