package com.example.moofrosty.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.CheckInRequest;
import com.example.moofrosty.data.model.GeneralResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeOrderActivityRepository {

    private final ApiService apiService;

    public TakeOrderActivityRepository() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

//    public void checkOutStore(String token, CheckInRequest request, MutableLiveData<Resource<GeneralResponse>> liveData) {
//        liveData.setValue(Resource.loading(null));
//
//        apiService.checkInStore("Bearer " + token, request).enqueue(new Callback<GeneralResponse>() {
//            @Override
//            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    liveData.setValue(Resource.success(response.body()));
//                } else {
//                    liveData.setValue(Resource.error("Server Error: " + response.code(), null));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GeneralResponse> call, Throwable t) {
//                liveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
//            }
//        });
//    }

    public void checkOutStore(String token, CheckInRequest request,
                              MutableLiveData<Resource<GeneralResponse>> liveData) {

        liveData.setValue(Resource.loading(null));

        // 🔥 REQUEST PAYLOAD LOG (BEFORE API CALL)
        Log.d("CHECKOUT_REQUEST", "Token: " + token);
        Log.d("CHECKOUT_REQUEST", "Request Payload: " + request.toString());

        apiService.checkInStore("Bearer " + token, request)
                .enqueue(new Callback<GeneralResponse>() {

                    @Override
                    public void onResponse(Call<GeneralResponse> call,
                                           Response<GeneralResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            GeneralResponse body = response.body();

                            // ✅ SUCCESS RESPONSE LOG
                            Log.d("CHECKOUT_SUCCESS", "Code: " + response.code());
                            Log.d("CHECKOUT_SUCCESS", "Message: " + body.getMessage());
//                            Log.d("CHECKOUT_SUCCESS", "Status: " + body.isStatus());
                            Log.d("CHECKOUT_SUCCESS::::;", "Response: " + new com.google.gson.Gson().toJson(body));

                            liveData.setValue(Resource.success(body));

                        } else {

                            Log.e("CHECKOUT_ERROR", "Server Error Code: " + response.code());

                            liveData.setValue(Resource.error(
                                    "Server Error: " + response.code(), null));
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                        Log.e("CHECKOUT_FAILURE", "Error: " + t.getMessage(), t);

                        liveData.setValue(Resource.error(
                                "Network Error: " + t.getMessage(), null));
                    }
                });
    }
}
