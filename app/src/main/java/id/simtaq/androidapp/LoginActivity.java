package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.helper.Preferences;
import id.simtaq.androidapp.helper.config;

import static id.simtaq.androidapp.helper.config.url;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView tvBelumPunyaAkun;
    private EditText etEmail;
    private EditText etPassword;

    private String email, password;
    private String token;

    private RequestQueue queue;
    private StringRequest request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        tvBelumPunyaAkun = findViewById(R.id.tvBelumPunyaAkun);
        etEmail = findViewById(R.id.inputEmail);
        etPassword = findViewById(R.id.inputPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    etEmail.requestFocus();
                    etEmail.setError("Masukkan email");
                } else if (!config.isEmailValid(email)) {
                    etEmail.requestFocus();
                    etEmail.setError("Email tidak valid");
                } else if (TextUtils.isEmpty(password)){
                    etPassword.requestFocus();
                    etPassword.setError("Masukkan password");
                } else {
                    login(email, password);
                }
            }
        });

        tvBelumPunyaAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home=new Intent(LoginActivity.this, DaftarActivity.class);
                startActivity(home);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Preferences.getLoggedInStatus(getBaseContext())){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    public void showHidePassLogin(View view) {

        if(view.getId()==R.id.show_passlogin_btn){
            if(etPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_invisible);
                //Show Password
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_visible);
                //Hide Password
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    private void login(String email, String password) {
        // url to post our data
        String url = config.url+"/login";
        //loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
//                loadingPB.setVisibility(View.GONE);
//                nameEdt.setText("");
//                jobEdt.setText("");

                // on below line we are displaying a success toast message.
                Toast.makeText(LoginActivity.this, "Berhasil masuk", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);

                    // below are the strings which we
                    // extract from our json object.
                    String name = respObj.getString("message");
                    token = respObj.getString("token");
                    Preferences.setLoggedInStatus(LoginActivity.this,true);
                    Preferences.setKeyToken(LoginActivity.this,token);


                    // on below line we are setting this string s to our text view.
                    //etEmail.setText(token+"");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("intentDari", "login");
                    intent.putExtra("token", token);
                    startActivity(intent);
                    finish();
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
                        Toast.makeText(LoginActivity.this, "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
                    } else if (error.networkResponse != null) {
                        if (error.networkResponse.data != null) {
                            body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject obj = new JSONObject(body);
                            JSONObject msg = obj.getJSONObject("messages");
                            String errorMsg = msg.getString("error");
                            if (errorMsg.equals("Email Not Found")) {
                                etEmail.requestFocus();
                                etEmail.setError("Email belum terdaftar");
                            } else {
                                etPassword.requestFocus();
                                etPassword.setError("Kata sandi salah");
                            }
                        }
                    }
                    //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("email", email);
                params.put("password", password);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}