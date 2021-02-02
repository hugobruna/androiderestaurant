package fr.isen.hugo.androiderestaurant.payment.demo.internal;

import fr.isen.hugo.androiderestaurant.payment.demo.models.ClientToken;
import fr.isen.hugo.androiderestaurant.payment.demo.models.Transaction;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface ApiClient {

    @GET("/client_token")
    void getClientToken(@Query("customer_id") String customerId, @Query("merchant_account_id") String merchantAccountId, Callback<ClientToken> callback);

    @FormUrlEncoded
    @POST("/nonce/transaction")
    void createTransaction(@Field("nonce") String nonce, Callback<Transaction> callback);

    @FormUrlEncoded
    @POST("/nonce/transaction")
    void createTransaction(@Field("nonce") String nonce, @Field("merchant_account_id") String merchantAccountId, Callback<Transaction> callback);

    @FormUrlEncoded
    @POST("/nonce/transaction")
    void createTransaction(@Field("nonce") String nonce, @Field("merchant_account_id") String merchantAccountId, @Field("three_d_secure_required") boolean requireThreeDSecure, Callback<Transaction> callback);
}
