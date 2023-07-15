package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import static id.simtaq.androidapp.helper.config.getCurentDate;
import static id.simtaq.androidapp.helper.config.getIdCurentDate;
import static id.simtaq.androidapp.helper.config.url;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

import id.simtaq.androidapp.helper.Preferences;

public class JariyahOnlineActivity extends AppCompatActivity implements TransactionFinishedCallback{

    private Toolbar toolbar;
    private ProgressBar pbJariyahOnline;
    private EditText etJariyahAtasNama;
    private EditText etNominalJariyah;
    private EditText etDeskripsiJariyah;
    private Button btnJariyah;
    private Button btnBatalJariyah;
    private RequestQueue queue;
    private String authToken;

    private String noKeuangan, ketJariyah, nominalJariyah, deskripJariyah;

    TransactionRequest transactionRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jariyah_online);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Jariyah Online");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        authToken = Preferences.getKeyToken(JariyahOnlineActivity.this);
        queue = Volley.newRequestQueue(JariyahOnlineActivity.this);
        getNomorKeuangan();
        showPembayaran();
        btnJariyah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etJariyahAtasNama.getText().toString())) {
                    etJariyahAtasNama.setError("Masukkan nama");
                } else if (TextUtils.isEmpty(etNominalJariyah.getText().toString())){
                    etNominalJariyah.setError("Masukkan nominal");
                } else if (TextUtils.isEmpty(etDeskripsiJariyah.getText().toString())){
                    etDeskripsiJariyah.setError("Masukkan deskripsi");
                } else {
                    ketJariyah = etJariyahAtasNama.getText().toString();
                    nominalJariyah = etNominalJariyah.getText().toString();
                    deskripJariyah = etDeskripsiJariyah.getText().toString();
                    transactionRequest= new TransactionRequest(noKeuangan, Integer.parseInt(nominalJariyah));
                    transactionRequest.setCustomField1(ketJariyah);
                    transactionRequest.setCustomField2(deskripJariyah+"");
                    transactionRequest.setCustomField3(authToken);

                    clickPay();
                    setCustomer();
                }
            }
        });
        btnBatalJariyah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etJariyahAtasNama.setText("");
                etNominalJariyah.setText("");
                etDeskripsiJariyah.setText("");
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
                            noKeuangan = "KEU-"+getIdCurentDate()+String.format("%04d",noTerakhir+1);
                        } else{
                            noKeuangan = "KEU-"+getIdCurentDate()+String.format("%04d",1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(JariyahOnlineActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
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
        toolbar = findViewById(R.id.tbJariyahOnline);
        pbJariyahOnline = findViewById(R.id.pbJariyahOnline);
        etJariyahAtasNama = findViewById(R.id.etJariyahNama);
        etNominalJariyah = findViewById(R.id.etNominalJariyah);
        etDeskripsiJariyah = findViewById(R.id.etDeskripsiJariyah);
        btnJariyah = findViewById(R.id.btnBayarJariyah);
        btnBatalJariyah = findViewById(R.id.btnBatalJariyah);
    }

    public void showPembayaran(){
        SdkUIFlowBuilder.init()
                .setClientKey(BuildConfig.CLIENT_KEY) // client_key is mandatory
                .setContext(JariyahOnlineActivity.this) // context is mandatory
                .setTransactionFinishedCallback(this)
                .setMerchantBaseUrl(BuildConfig.BASE_URL) //set merchant url (required)
                .enableLog(true) // enable sdk log (optional)
                .setColorTheme(new CustomColorTheme("#07b846", "#5cec75", "#0b897b")) // set theme. it will replace theme on snap theme on MAP ( optional)
                .setLanguage("id") //`en` for English and `id` for Bahasa
                .buildSDK();
    }

    private void clickPay(){
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
        MidtransSDK.getInstance().startPaymentUiFlow(JariyahOnlineActivity.this);
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if(result.getResponse() != null){
            switch (result.getStatus()){
                case TransactionResult.STATUS_SUCCESS:
                    getNomorKeuangan();
                    etJariyahAtasNama.setText("");
                    etNominalJariyah.setText("" );
                    etDeskripsiJariyah.setText("");
                    Toast.makeText(this, "Transaction Sukses " + result.getResponse().getOrderId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    getNomorKeuangan();
                    etJariyahAtasNama.setText("");
                    etNominalJariyah.setText("" );
                    etDeskripsiJariyah.setText("");
                    Toast.makeText(this, "Transaction Pending " + result.getResponse().getOrderId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed" + result.getResponse().getOrderId(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
        }else if(result.isTransactionCanceled()){
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show();
        }else{
            if(result.getStatus().equalsIgnoreCase((TransactionResult.STATUS_INVALID))){
                Toast.makeText(this, "Transaction Invalid" + result.getResponse().getOrderId(), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
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