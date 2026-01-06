package com.example.moofrosty.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.StoreExistRequest;
import com.example.moofrosty.data.model.StoreExistResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreVerificationRepository {

    private ApiService apiService;

    public StoreVerificationRepository() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void checkStoreExistence(String token, String mobile, MutableLiveData<Resource<StoreExistResponse>> liveData) {
        liveData.postValue(Resource.loading(null));

        apiService.checkStoreExist("Bearer " + token, new StoreExistRequest(mobile)).enqueue(new Callback<StoreExistResponse>() {
            @Override
            public void onResponse(Call<StoreExistResponse> call, Response<StoreExistResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Check the 'success' flag from the API response
                    if (response.body().isSuccess()) {
                        liveData.postValue(Resource.success(response.body()));
                    } else {
                        // Even if HTTP is 200, if success is false, treat as error (optional depending on API logic)
                        // However, per your JSON, 'success' seems to be true even if 'exists' is false.
                        // So we pass success body mainly.
                        liveData.postValue(Resource.success(response.body()));
                    }
                } else {
                    if(response.code() == 401) {
                        liveData.postValue(Resource.error("Session Expired", null));
                    } else {
                        liveData.postValue(Resource.error("Error Checking Number: " + response.code(), null));
                    }
                }
            }

            @Override
            public void onFailure(Call<StoreExistResponse> call, Throwable t) {
                liveData.postValue(Resource.error("Network Error: " + t.getMessage(), null));
            }
        });
    }
}
