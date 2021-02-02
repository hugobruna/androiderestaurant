package fr.isen.hugo.androiderestaurant.payment.api.internal;


import com.braintreepayments.api.internal.SignatureVerification;

public class SignatureVerificationOverrides {

    /**
     * WARNING: signature verification is disable based on a setting for testing in this demo app only. You should
     * never do this as it opens a security hole.
     */
    public static void disableAppSwitchSignatureVerification(boolean disable) {
        //SignatureVerification.sEnableSignatureVerification = !disable;
    }
}
