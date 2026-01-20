package com.example.moofrosty.data.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.model.UserDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailRepository {

    private ApiService apiService;
    private Context context;

    public UserDetailRepository(Context context) {
        this.context = context;
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    private String formatToken(String token) {
        return token.startsWith("Bearer ") ? token : "Bearer " + token;
    }

    public void getUserDetails(String token,
                               MutableLiveData<Resource<UserDetailResponse>> liveData) {

        liveData.setValue(Resource.loading(null));

        if (!NetworkUtil.isNetworkAvailable(context)) {
            liveData.setValue(Resource.error("No Internet Connection", null));
            return;
        }

        apiService.getUserDetail(formatToken(token))
                .enqueue(new Callback<UserDetailResponse>() {
                    @Override
                    public void onResponse(Call<UserDetailResponse> call,
                                           Response<UserDetailResponse> response) {
                        if (response.isSuccessful() && response.body() != null &&
                                response.body().isSuccess()) {
                            liveData.setValue(Resource.success(response.body()));
                        } else {
                            liveData.setValue(Resource.error("Failed to load user details", null));
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                        liveData.setValue(Resource.error(t.getMessage(), null));
                    }
                });
    }
}
