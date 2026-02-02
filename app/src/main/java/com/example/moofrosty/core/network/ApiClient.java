package com.example.moofrosty.core.network;

import  retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//
public class ApiClient {
    private static final String BASE_URL = "https://moofrosty.ekatta.in/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
//public class ApiClient {
//
//    private static final String BASE_URL = "https://moofrosty.ekatta.in/";
//    private static Retrofit retrofit;
//    private static ApiService apiService;
//
//    public static Retrofit getRetrofitInstance() {
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//    public static ApiService getApi() {
//        if (apiService == null) {
//            apiService = getRetrofitInstance().create(ApiService.class);
//        }
//        return apiService;
//    }
//}
