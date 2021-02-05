package fr.isen.hugo.androiderestaurant.payment.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.models.CardNonce;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.cardinalcommerce.cardinalmobilesdk.models.Payment;

import fr.isen.hugo.androiderestaurant.HomeActivity;
import fr.isen.hugo.androiderestaurant.model.Cart;
import fr.isen.hugo.androiderestaurant.model.dataResultCode;
import fr.isen.hugo.androiderestaurant.payment.demo.models.Transaction;

import androidx.appcompat.app.AppCompatActivity;

import fr.isen.hugo.androiderestaurant.R;
import retrofit.Callback;
import retrofit.RetrofitError;

import static fr.isen.hugo.androiderestaurant.FileKt.cartReadFromFile;
import static fr.isen.hugo.androiderestaurant.FileKt.cartWriteToFile;
import static fr.isen.hugo.androiderestaurant.FileKt.getSharedPrefs;
import static fr.isen.hugo.androiderestaurant.FileKt.setSharedPrefs;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreateTransactionActivity extends AppCompatActivity {

    public static final String EXTRA_PAYMENT_METHOD_NONCE = "nonce";

    private ProgressBar mLoadingSpinner;
    private Button mButtonStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_transaction_activity);
        mLoadingSpinner = findViewById(R.id.loading_spinner);
        setTitle(R.string.processing_transaction);

        sendNonceToServer((PaymentMethodNonce) getIntent().getParcelableExtra(EXTRA_PAYMENT_METHOD_NONCE));

    }

    private void sendNonceToServer(PaymentMethodNonce nonce) {
        Callback<Transaction> callback = new Callback<Transaction>() {

            @Override
            public void success(Transaction transaction, retrofit.client.Response response) {
                if (transaction.getMessage() != null &&
                        transaction.getMessage().startsWith("created")) {
                    setStatus(R.string.transaction_complete);
                    setMessage(transaction.getMessage());
                    sendOrder(transaction);
                } else {
                    setStatus(R.string.transaction_failed);
                    if (TextUtils.isEmpty(transaction.getMessage())) {
                        setMessage("Server response was empty or malformed");
                    } else {
                        setMessage(transaction.getMessage());
                    }
                    mButtonStatus = findViewById(R.id.button_status);
                    mButtonStatus.setText(("retry").toString());
                    mButtonStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                            startActivity(intent);
                        }
                    });
                    mButtonStatus.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                setStatus(R.string.transaction_failed);
                setMessage("Unable to create a transaction. Response Code: " +
                        error.getResponse().getStatus() + " Response body: " +
                        error.getResponse().getBody());
                mButtonStatus = findViewById(R.id.button_status);
                mButtonStatus.setText(("retry").toString());
                mButtonStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                        startActivity(intent);
                    }
                });
                mButtonStatus.setVisibility(View.VISIBLE);
            }
        };

        if (Settings.isThreeDSecureEnabled(this) && Settings.isThreeDSecureRequired(this)) {
            DemoApplication.getApiClient(this).createTransaction(nonce.getNonce(),
                    Settings.getThreeDSecureMerchantAccountId(this), true, callback);
        } else if (Settings.isThreeDSecureEnabled(this)) {
            DemoApplication.getApiClient(this).createTransaction(nonce.getNonce(),
                    Settings.getThreeDSecureMerchantAccountId(this), callback);
        } else if (nonce instanceof CardNonce && ((CardNonce) nonce).getCardType().equals("UnionPay")) {
            DemoApplication.getApiClient(this).createTransaction(nonce.getNonce(),
                    Settings.getUnionPayMerchantAccountId(this), callback);
        } else {
            DemoApplication.getApiClient(this).createTransaction(nonce.getNonce(), Settings.getMerchantAccountId(this),
                    callback);
        }
    }

    private void setStatus(int message) {
        mLoadingSpinner.setVisibility(View.GONE);
        setTitle(message);
        TextView status = findViewById(R.id.transaction_status);
        status.setText(message);
        status.setVisibility(View.VISIBLE);
    }

    private void setMessage(String message) {
        mLoadingSpinner.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.transaction_message);
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }

    private void sendOrder(Transaction transaction){
        Context context = getApplicationContext();
        String postUrl = "http://test.api.catering.bluecodegames.com/" + "user" + "/" + "order";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject postData = new JSONObject();
        try {
            postData.put("id_shop", "1");
            postData.put("id_user", getSharedPrefs(context, "id_login"));
            postData.put("msg", new Gson().toJson(cartReadFromFile(context)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                dataResultCode gson = new Gson().fromJson(response.toString(), dataResultCode.class);
                if(gson.verifyResult()){
                    cartWriteToFile(new Cart(new ArrayList<>(), null, null), context);
                    setMessage(transaction.getMessage() + "\nOrder saved ! We will start to prepare your meals");
                    setSharedPrefs(context, "cart_item_number", "0");
                    mButtonStatus = findViewById(R.id.button_status);
                    mButtonStatus.setText(("continue").toString());
                    mButtonStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        }
                    });
                    mButtonStatus.setVisibility(View.VISIBLE);
                }
                else {
                    Log.e("PaymentActivity", "Noting found :(");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PaymentActivity", "Noting found :(");
                setMessage(transaction.getMessage() + "\nERROR ! Please contact Beaumanière and give your transaction number and what you ordered");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}

/*



 */
