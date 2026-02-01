package com.example.moofrosty.data.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.core.network.ApiClient;
import com.google.android.gms.common.api.internal.zabe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderMissedViewModel extends ViewModel {

    private final ApiService apiService;
    private final MutableLiveData<Resource<GeneralResponse>> orderMissedResult =
            new MutableLiveData<>();

    public OrderMissedViewModel() {

        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public LiveData<Resource<GeneralResponse>> getOrderMissedResult() {
        return orderMissedResult;
    }

    public void markOrderMissed(String orderId, String reason) {

        orderMissedResult.setValue(Resource.loading(null));

        apiService.markOrderMissed(orderId, reason)
                .enqueue(new Callback<GeneralResponse>() {

                    @Override
                    public void onResponse(
                            Call<GeneralResponse> call,
                            Response<GeneralResponse> response
                    ) {

                        if (response.isSuccessful() && response.body() != null) {
                            orderMissedResult.setValue(
                                    Resource.success(response.body())
                            );
                        } else {
                            orderMissedResult.setValue(
                                    Resource.error("Failed to mark order missed", null)
                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        orderMissedResult.setValue(
                                Resource.error(t.getMessage(), null)
                        );
                    }
                });
    }
}

