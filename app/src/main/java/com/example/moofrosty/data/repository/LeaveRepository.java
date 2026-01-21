package com.example.moofrosty.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.ApplyLeaveRequest;
import com.example.moofrosty.data.model.GeneralResponse;
import com.example.moofrosty.data.model.LeaveHistoryResponse;
import com.example.moofrosty.data.model.LeaveTypeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveRepository {

    private ApiService apiService;

    public LeaveRepository() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    // Apply Leave
    public void applyLeave(String rawToken, ApplyLeaveRequest request, MutableLiveData<Resource<GeneralResponse>> liveData) {
        liveData.postValue(Resource.loading(null));

        // Add "Bearer " prefix here so it's clean
        String authHeader = "Bearer " + rawToken;

        apiService.applyLeave(authHeader, request).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Check if the API status string is "success"
                    if ("success".equalsIgnoreCase(response.body().getStatus())) {
                        liveData.postValue(Resource.success(response.body()));
                    } else {
                        liveData.postValue(Resource.error(response.body().getMessage(), null));
                    }
                } else {
                    liveData.postValue(Resource.error("Error: " + response.code() + " " + response.message(), null));
                }
            }
            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                liveData.postValue(Resource.error("Network Error: " + t.getMessage(), null));
            }
        });
    }

    // Get History
    public void getLeaveHistory(String rawToken, MutableLiveData<Resource<LeaveHistoryResponse>> liveData) {
        liveData.postValue(Resource.loading(null));

        String authHeader = "Bearer " + rawToken;

        apiService.getLeaveHistory(authHeader).enqueue(new Callback<LeaveHistoryResponse>() {
            @Override
            public void onResponse(Call<LeaveHistoryResponse> call, Response<LeaveHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("success".equalsIgnoreCase(response.body().getStatus())) {
                        liveData.postValue(Resource.success(response.body()));
                    } else {
                        liveData.postValue(Resource.error(response.body().getMessage(), null));
                    }
                } else {
                    liveData.postValue(Resource.error("Error: " + response.code() + " " + response.message(), null));
                }
            }
            @Override
            public void onFailure(Call<LeaveHistoryResponse> call, Throwable t) {
                liveData.postValue(Resource.error("Network Error: " + t.getMessage(), null));
            }
        });
    }

    public void getLeaveTypes(String rawToken,
                              MutableLiveData<Resource<LeaveTypeResponse>> liveData) {

        liveData.postValue(Resource.loading(null));
        String authHeader = "Bearer " + rawToken;

        apiService.getLeaveTypes(authHeader).enqueue(new Callback<LeaveTypeResponse>() {
            @Override
            public void onResponse(Call<LeaveTypeResponse> call,
                                   Response<LeaveTypeResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if ("success".equalsIgnoreCase(response.body().getStatus())) {
                        liveData.postValue(Resource.success(response.body()));
                    } else {
                        liveData.postValue(Resource.error(response.body().getMessage(), null));
                    }
                } else {
                    liveData.postValue(Resource.error("Error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<LeaveTypeResponse> call, Throwable t) {
                liveData.postValue(Resource.error("Network Error: " + t.getMessage(), null));
            }
        });
    }
}
