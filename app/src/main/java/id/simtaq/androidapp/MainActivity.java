package id.simtaq.androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.fragments.HomeFragment;
import id.simtaq.androidapp.fragments.KegiatanFragment;
import id.simtaq.androidapp.fragments.PengaturanFragment;
import id.simtaq.androidapp.fragments.InfoKasFragment;
import id.simtaq.androidapp.helper.Preferences;
import id.simtaq.androidapp.helper.config;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String authToken;
    private String id, nama, email, level;
    private String intentDari;
    private String judulMessage, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //authToken = String.valueOf(getIntent().getStringExtra("token"));
        authToken = Preferences.getKeyToken(MainActivity.this);
        intentDari = String.valueOf(getIntent().getStringExtra("intentDari"));
        if (intentDari.equals("login")){
            auth(authToken);
        }
        setContentView(R.layout.activity_main);
        initView();
        if (intentDari.equals("detail keuangan")){
            bottomNavigationView.setSelectedItemId(R.id.menu_infokas);
            getSupportFragmentManager().beginTransaction().replace(R.id.flPageContainer, new InfoKasFragment()).commit();
        } else if (intentDari.equals("detail kegiatan")){
            bottomNavigationView.setSelectedItemId(R.id.menu_kegiatan);
            getSupportFragmentManager().beginTransaction().replace(R.id.flPageContainer, new KegiatanFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.flPageContainer, new HomeFragment()).commit();
        }
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.menu_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.menu_infokas:
                        selectedFragment = new InfoKasFragment();
                        break;
                    case R.id.menu_kegiatan:
                        selectedFragment = new KegiatanFragment();
                        break;
                    case R.id.menu_pengaturan:
                        selectedFragment = new PengaturanFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.flPageContainer, selectedFragment).commit();

                return true;

            }
        });
    }

    //Menampilkan halaman Fragment
    private boolean getFragmentPage(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flPageContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void initView(){
        bottomNavigationView = findViewById(R.id.bottomNav);
    }

    private void auth(String token) {
        // url to post our data
        String url = config.url+"/me";
        //loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

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

                    Preferences.setKeyId(MainActivity.this, id);
                    Preferences.setKeyNama(MainActivity.this, nama);
                    Preferences.setKeyEmail(MainActivity.this, email);
                    Preferences.setKeyLevel(MainActivity.this, level);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flPageContainer, new HomeFragment()).commit();


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
                        showDialogMsg(2);
                    } else if (error.networkResponse != null) {
                        if (error.networkResponse.data != null) {
                            body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject obj = new JSONObject(body);
                            JSONObject msg = obj.getJSONObject("messages");
                            String errorMsg = msg.getString("error");
                            judulMessage = "Gagal";
                            message = errorMsg;
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
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    public void showDialogMsg(int i){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.DialogSlideAnim);
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