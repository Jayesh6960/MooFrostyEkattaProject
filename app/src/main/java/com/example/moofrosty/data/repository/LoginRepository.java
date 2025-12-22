package com.example.moofrosty.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.LoginRequest;
import com.example.moofrosty.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private ApiService apiService;

    public LoginRepository() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void loginUser(String email, String password, MutableLiveData<Resource<LoginResponse>> liveData) {
        liveData.postValue(Resource.loading(null));

        LoginRequest request = new LoginRequest(email, password);
        Log.d("API_DEBUG", "Sending Payload: Email=" + email + ", Pass=" + password);

        apiService.loginUser(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("API_DEBUG", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse resp = response.body();
                    Log.d("API_DEBUG", "Response Status: " + resp.isStatus());
                    Log.d("API_DEBUG", "Response Message: " + resp.getMessage());
                    // Direct access (No .getData())
                    Log.d("API_DEBUG", "Token Found: " + resp.getToken());

                    if (resp.isStatus()) {
                        liveData.postValue(Resource.success(resp));
                    } else {
                        liveData.postValue(Resource.error(resp.getMessage(), null));
                    }
                } else {
                    Log.e("API_DEBUG", "Response Unsuccessful or Body is Null");
                    liveData.postValue(Resource.error("Error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("API_DEBUG", "Network Failure: " + t.getMessage());
                liveData.postValue(Resource.error("Network Failure: " + t.getMessage(), null));
            }
        });
    }

//    public void loginUser(String email, String password, MutableLiveData<Resource<LoginResponse>> liveData) {
//        // 1. Emit Loading State
//        liveData.postValue(Resource.loading(null));
//
//        LoginRequest request = new LoginRequest(email, password);
//
//        // 2. Make API Call
//        apiService.loginUser(request).enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    // Check internal API status (e.g. status: true/false)
//                    // If your API returns status=false even on HTTP 200, handle it here
//                    // Assuming response.body().isStatus() exists from your model
//                    if (response.body().isStatus()) {
//                        liveData.postValue(Resource.success(response.body()));
//                    } else {
//                        liveData.postValue(Resource.error(response.body().getMessage(), null));
//                    }
//                } else {
//                    liveData.postValue(Resource.error("Error: " + response.code() + " " + response.message(), null));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                liveData.postValue(Resource.error("Network Failure: " + t.getMessage(), null));
//            }
//        });
//    }
}
