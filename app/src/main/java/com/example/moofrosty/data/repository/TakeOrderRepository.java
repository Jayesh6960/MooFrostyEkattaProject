package com.example.moofrosty.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.model.CategoryResponse;
import com.example.moofrosty.data.model.ProductResponse;
import com.example.moofrosty.data.model.SubCategoryResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeOrderRepository {

    private static final String TAG = "TakeOrderRepo";
    private ApiService apiService;
    private Context context;

    public TakeOrderRepository(Context context) {
        this.context = context;
        this.apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    private String formatToken(String token) {
        if (token != null && !token.startsWith("Bearer ")) {
            return "Bearer " + token;
        }
        return token;
    }

    // --- 1. Get Products ---
    public void getProducts(String rawToken, MutableLiveData<Resource<ProductResponse>> liveData) {
        liveData.setValue(Resource.loading(null));

        if (!NetworkUtil.isNetworkAvailable(context)) {
            liveData.setValue(Resource.error("No Internet Connection", null));
            return;
        }

        String token = formatToken(rawToken);
        apiService.getProducts(token).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Error: " + response.code(), null));
                }
            }
            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
            }
        });
    }

    // --- 2. Get Categories ---
    public void getCategories(String rawToken, MutableLiveData<Resource<CategoryResponse>> liveData) {
        liveData.setValue(Resource.loading(null));

        if (!NetworkUtil.isNetworkAvailable(context)) {
            liveData.setValue(Resource.error("No Internet Connection", null));
            return;
        }

        String token = formatToken(rawToken);
        apiService.getCategories(token).enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Check status int from JSON (200 = success)
                    if(response.body().status) {
                        liveData.setValue(Resource.success(response.body()));
                    } else {
                        liveData.setValue(Resource.error("API Status Failed", null));
                    }
                } else {
                    liveData.setValue(Resource.error("Error: " + response.code(), null));
                }
            }
            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
            }
        });
    }

    // --- 3. Get Sub-Categories ---
    public void getSubCategories(String rawToken, int catId, MutableLiveData<Resource<SubCategoryResponse>> liveData) {
        liveData.setValue(Resource.loading(null));

        if (!NetworkUtil.isNetworkAvailable(context)) {
            liveData.setValue(Resource.error("No Internet Connection", null));
            return;
        }

        String token = formatToken(rawToken);
        apiService.getSubCategories(token, catId).enqueue(new Callback<SubCategoryResponse>() {
            @Override
            public void onResponse(Call<SubCategoryResponse> call, Response<SubCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if(response.body().status) {
                        liveData.setValue(Resource.success(response.body()));
                    } else {
                        liveData.setValue(Resource.error("API Status Failed", null));
                    }
                } else {
                    liveData.setValue(Resource.error("Error: " + response.code(), null));
                }
            }
            @Override
            public void onFailure(Call<SubCategoryResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
            }
        });
    }
}