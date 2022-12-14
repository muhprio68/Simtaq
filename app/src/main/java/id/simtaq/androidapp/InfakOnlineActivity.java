package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import static android.provider.Telephony.BaseMmsColumns.TRANSACTION_ID;
import static id.simtaq.androidapp.helper.config.getCurentDate;
import static id.simtaq.androidapp.helper.config.getIdCurentDate;
import static id.simtaq.androidapp.helper.config.url;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import id.simtaq.androidapp.models.Keuangan;

public class InfakOnlineActivity extends AppCompatActivity implements TransactionFinishedCallback{

    private Toolbar toolbar;
    private ProgressBar pbInfakOnline;
    private EditText etInfakAtasNama;
    private EditText etNominalInfak;
    private EditText etDeskripsiInfak;
    private Button btnInfak;
    private Button btnBatalInfak;
    private RequestQueue queue;


    private String noKeuangan, ketInfak, nominalInfak, deskripInfak, jmlSaldo ;

    TransactionRequest transactionRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infak_online);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Infak Online");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        queue = Volley.newRequestQueue(InfakOnlineActivity.this);
        getNomorKeuangan();
        getSaldo();
        showPembayaran();
        btnInfak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etInfakAtasNama.getText().toString())) {
                    etInfakAtasNama.setError("Masukkan nama");
                } else if (TextUtils.isEmpty(etNominalInfak.getText().toString())){
                    etNominalInfak.setError("Masukkan nominal");
                }  else {
                    ketInfak = etInfakAtasNama.getText().toString();
                    nominalInfak = etNominalInfak.getText().toString();
                    deskripInfak = etDeskripsiInfak.getText().toString();
                    transactionRequest= new TransactionRequest(ketInfak, Integer.parseInt(nominalInfak));
                    clickPay();
                    setCustomer();
                }
            }
        });

    }

    public void getNomorKeuangan (){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/nomor", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //pbRiwayatKeuangan.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        int idKeuangan = responseObj.getInt("id_nomor");
                        String tglKeuangan = responseObj.getString("tgl_keuangan");
                        int noTerakhir = responseObj.getInt("no_terakhir");
                        if (tglKeuangan.equals(getCurentDate())){
                            noKeuangan = getIdCurentDate()+String.format("%04d",noTerakhir+1);
                        } else{
                            noKeuangan = getIdCurentDate()+String.format("%04d",1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InfakOnlineActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void setCustomer(){
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerIdentifier("budi-6789");
        customerDetails.setPhone("08123456789");
        customerDetails.setFirstName("Budi");
        customerDetails.setLastName("Utomo");
        customerDetails.setEmail("budi@utomo.com");

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress("Jalan Andalas Gang Sebelah No. 1");
        shippingAddress.setCity("Jakarta");
        shippingAddress.setPostalCode("10220");
        customerDetails.setShippingAddress(shippingAddress);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setAddress("Jalan Andalas Gang Sebelah No. 1");
        billingAddress.setCity("Jakarta");
        billingAddress.setPostalCode("10220");
        customerDetails.setBillingAddress(billingAddress);

        transactionRequest.setCustomerDetails(customerDetails);
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbInfakOnline);
        pbInfakOnline = findViewById(R.id.pbInfakOnline);
        etInfakAtasNama = findViewById(R.id.etInfakNama);
        etNominalInfak = findViewById(R.id.etNominalInfak);
        etDeskripsiInfak = findViewById(R.id.etDeskripsiInfak);
        btnInfak = findViewById(R.id.btnBayarInfak);
        btnBatalInfak = findViewById(R.id.btnBatalInfak);
    }

    public void showPembayaran(){
        SdkUIFlowBuilder.init()
                .setClientKey(BuildConfig.CLIENT_KEY) // client_key is mandatory
                .setContext(InfakOnlineActivity.this) // context is mandatory
                .setTransactionFinishedCallback(this)
                .setMerchantBaseUrl(BuildConfig.BASE_URL) //set merchant url (required)
                .enableLog(true) // enable sdk log (optional)
                .setColorTheme(new CustomColorTheme("#FF07b846", "#008715", "#FF5cec75")) // set theme. it will replace theme on snap theme on MAP ( optional)
                .setLanguage("id") //`en` for English and `id` for Bahasa
                .buildSDK();
    }

    private void clickPay(){
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
        MidtransSDK.getInstance().startPaymentUiFlow(InfakOnlineActivity.this);
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if(result.getResponse() != null){
            switch (result.getStatus()){
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Sukses " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    tambahDataPemasukan(getCurentDate(), "Infak atas nama "+ketInfak, "Selesai", nominalInfak, deskripInfak);
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    tambahDataPemasukan(getCurentDate(), "Infak atas nama "+ketInfak, "Tertunda", nominalInfak, deskripInfak);
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
        }else if(result.isTransactionCanceled()){
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show();
        }else{
            if(result.getStatus().equalsIgnoreCase((TransactionResult.STATUS_INVALID))){
                Toast.makeText(this, "Transaction Invalid" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void tambahDataPemasukan(String tglInfak, String ketInfak, String statusInfak, String nominalInfak, String deskripInfak) {
        StringRequest request = new StringRequest(Request.Method.POST, url+"/keuangan", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //pbCatatPemasukan.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    //snackbarWithAction();

                    //Toast.makeText(TambahKegiatanActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // and setting data to edit text as empty
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(InfakOnlineActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
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
                params.put("tgl_keuangan", tglInfak);
                params.put("keterangan_keuangan", ketInfak);
                params.put("status_keuangan", statusInfak);
                params.put("nominal_keuangan", nominalInfak);
                params.put("jml_kas_awal", jmlSaldo);
                int jmlKasAkhir = Integer.parseInt(jmlSaldo)+Integer.parseInt(nominalInfak);
                params.put("jml_kas_akhir", jmlKasAkhir+"");
                params.put("deskripsi_keuangan", deskripInfak);
                params.put("create_at", getCurentDate());
                params.put("update_at", getCurentDate());
                ubahSaldo(jmlKasAkhir+"");

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    public void getSaldo(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/saldo", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //pbJadwalKegiatan.setVisibility(View.GONE);
                //rvJadwalKegiatan.setVisibility(View.VISIBLE);
                try {
                    JSONObject responseObj = response.getJSONObject(0);
                    jmlSaldo = responseObj.getString("jml_saldo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InfakOnlineActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void ubahSaldo(String jmlSaldo) {
        StringRequest request = new StringRequest(Request.Method.PUT, url+"/saldo/1", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    //snackbarWithAction();

                    //Toast.makeText(TambahKegiatanActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(InfakOnlineActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
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
                params.put("jml_saldo", jmlSaldo);
                params.put("update_at", getCurentDate());

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}