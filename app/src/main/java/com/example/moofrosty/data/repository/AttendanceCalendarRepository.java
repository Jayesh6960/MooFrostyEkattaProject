package com.example.moofrosty.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.LeaveResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceCalendarRepository {
    private ApiService apiService;

    public AttendanceCalendarRepository() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void getLeaves(String token, MutableLiveData<Resource<LeaveResponse>> liveData) {
        liveData.postValue(Resource.loading(null));

        apiService.getUserLeaves("Bearer " + token).enqueue(new Callback<LeaveResponse>() {
            @Override
            public void onResponse(Call<LeaveResponse> call, Response<LeaveResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("success".equalsIgnoreCase(response.body().getStatus())) {
                        liveData.postValue(Resource.success(response.body()));
                    } else {
                        liveData.postValue(Resource.error(response.body().getMessage(), null));
                    }
                } else {
                    if (response.code() == 401) {
                        liveData.postValue(Resource.error("Session Expired", null));
                    } else {
                        liveData.postValue(Resource.error("Error: " + response.code(), null));
                    }
                }
            }
            @Override
            public void onFailure(Call<LeaveResponse> call, Throwable t) {
                liveData.postValue(Resource.error("Network Error: " + t.getMessage(), null));
            }
        });
    }
}
