package com.example.moofrosty.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.CheckInRequest;
import com.example.moofrosty.data.model.GeneralResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreInOutRepository {

    private ApiService apiService;

    public StoreInOutRepository() {
        // Initialize your Retrofit Client here or via Dependency Injection
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void enterStore(String token, CheckInRequest request, MutableLiveData<Resource<String>> liveData) {

        // 1. Emit Loading State
        liveData.setValue(Resource.loading(null));

        apiService.checkInStore("Bearer " + token, request).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GeneralResponse body = response.body();

                    if ("success".equalsIgnoreCase(body.getStatus())) {
                        liveData.setValue(Resource.success(body.getMessage()));
                    } else {
                        liveData.setValue(Resource.error(body.getMessage(), null));
                    }
                } else {
                    liveData.setValue(Resource.error("Server Error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Log.e("Failcheck", "API Failure: " + t.getMessage());
                liveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
            }
        });
    }
}
