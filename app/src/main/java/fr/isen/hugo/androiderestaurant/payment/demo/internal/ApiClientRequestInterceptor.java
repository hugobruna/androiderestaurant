package fr.isen.hugo.androiderestaurant.payment.demo.internal;

import retrofit.RequestInterceptor;

public class ApiClientRequestInterceptor implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade request) {
        //request.addHeader("User-Agent", "braintree/android-demo-app/");
        request.addHeader("Accept", "application/json");
    }
}
