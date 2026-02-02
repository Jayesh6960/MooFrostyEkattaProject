package com.example.moofrosty.data.repository;

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

    public void checkOutStore(String token, CheckInRequest request, MutableLiveData<Resource<GeneralResponse>> liveData) {
        liveData.setValue(Resource.loading(null));

        apiService.checkInStore("Bearer " + token, request).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server Error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
            }
        });
    }
}
