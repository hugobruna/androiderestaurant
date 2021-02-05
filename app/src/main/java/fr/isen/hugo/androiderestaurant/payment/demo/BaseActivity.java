package fr.isen.hugo.androiderestaurant.payment.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.BuildConfig;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import fr.isen.hugo.androiderestaurant.payment.api.internal.SignatureVerificationOverrides;
import com.braintreepayments.api.models.PaymentMethodNonce;
import fr.isen.hugo.androiderestaurant.payment.demo.models.ClientToken;
import com.paypal.android.sdk.onetouch.core.PayPalOneTouchCore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;

import fr.isen.hugo.androiderestaurant.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

@SuppressWarnings("deprecation")
public abstract class BaseActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback,
        PaymentMethodNonceCreatedListener, BraintreeCancelListener, BraintreeErrorListener,
        ActionBar.OnNavigationListener {

    private static final String KEY_AUTHORIZATION = "com.braintreepayments.demo.KEY_AUTHORIZATION";

    protected String mAuthorization;
    protected String mCustomerId;
    protected BraintreeFragment mBraintreeFragment;

    private boolean mActionBarSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_AUTHORIZATION)) {
            mAuthorization = savedInstanceState.getString(KEY_AUTHORIZATION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mActionBarSetup) {
            mActionBarSetup = true;
        }

        SignatureVerificationOverrides.disableAppSwitchSignatureVerification(
                Settings.isPayPalSignatureVerificationDisabled(this));
        PayPalOneTouchCore.useHardcodedConfig(this, Settings.useHardcodedPayPalConfiguration(this));

        if (BuildConfig.DEBUG && ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ WRITE_EXTERNAL_STORAGE }, 1);
        } else {
            handleAuthorizationState();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handleAuthorizationState();
    }

    private void handleAuthorizationState() {
        if (mAuthorization == null ||
                (Settings.useTokenizationKey(this) && !mAuthorization.equals(Settings.getEnvironmentTokenizationKey(this))) ||
                !TextUtils.equals(mCustomerId, Settings.getCustomerId(this))) {
            performReset();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAuthorization != null) {
            outState.putString(KEY_AUTHORIZATION, mAuthorization);
        }
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        Log.d(getClass().getSimpleName(), "Payment Method Nonce received: " + paymentMethodNonce.getTypeLabel());
    }

    @Override
    public void onCancel(int requestCode) {
        Log.d(getClass().getSimpleName(), "Cancel received: " + requestCode);
    }

    @Override
    public void onError(Exception error) {
        Log.d(getClass().getSimpleName(), "Error received (" + error.getClass() + "): "  + error.getMessage());
        Log.d(getClass().getSimpleName(), error.toString());

        showDialog("An error occurred (" + error.getClass() + "): " + error.getMessage());
    }

    private void performReset() {
        mAuthorization = null;
        mCustomerId = Settings.getCustomerId(this);

        if (mBraintreeFragment == null) {
            mBraintreeFragment = (BraintreeFragment) getSupportFragmentManager()
                    .findFragmentByTag(BraintreeFragment.TAG);
        }

        if (mBraintreeFragment != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getSupportFragmentManager().beginTransaction().remove(mBraintreeFragment).commitNow();
            } else {
                getSupportFragmentManager().beginTransaction().remove(mBraintreeFragment).commit();
                getSupportFragmentManager().executePendingTransactions();
            }

            mBraintreeFragment = null;
        }

        reset();
        fetchAuthorization();
    }

    protected abstract void reset();

    protected abstract void onAuthorizationFetched();

    protected void fetchAuthorization() {
        if (mAuthorization != null) {
            onAuthorizationFetched();
        } else if (Settings.useTokenizationKey(this)) {
            mAuthorization = Settings.getEnvironmentTokenizationKey(this);
            onAuthorizationFetched();
        } else {
            DemoApplication.getApiClient(this).getClientToken(Settings.getCustomerId(this),
                    Settings.getMerchantAccountId(this), new Callback<ClientToken>() {
                        @Override
                        public void success(ClientToken clientToken, Response response) {
                            if (TextUtils.isEmpty(clientToken.getClientToken())) {
                                showDialog("Client token was empty");
                            } else {
                                mAuthorization = clientToken.getClientToken();
                                onAuthorizationFetched();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            showDialog("Unable to get a client token. Response Code: " +
                                    error.getResponse().getStatus() + " Response body: " +
                                    error.getResponse().getBody());
                        }
                    });
        }
    }

    protected void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    protected void setUpAsBack() {
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        if (Settings.getEnvironment(this) != itemPosition) {
            Settings.setEnvironment(this, itemPosition);
            performReset();
        }
        return true;
    }
}