package com.example.moofrosty.ui.ATMSummary;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AtmRepository {

    private ApiService apiService;

    public AtmRepository() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }


        private MutableLiveData<Resource<List<StoreModel>>> liveData = new MutableLiveData<>();

        public LiveData<Resource<List<StoreModel>>> getStoreData() {

            // 🔄 Loading state
            liveData.setValue(new Resource<>(Resource.Status.LOADING, null, null));

            apiService.getStoreList().enqueue(new Callback<List<StoreModel>>() {
                @Override
                public void onResponse(Call<List<StoreModel>> call, Response<List<StoreModel>> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        // ✅ SUCCESS
                        liveData.setValue(new Resource<>(Resource.Status.SUCCESS, response.body(), null));

                    } else {

                        // 🔥 USE DUMMY DATA IF API EMPTY
                        liveData.setValue(new Resource<>(Resource.Status.SUCCESS,getDummyData(), null));
                    }
                }

                @Override
                public void onFailure(Call<List<StoreModel>> call, Throwable t) {

                    // 🔥 FALLBACK TO DUMMY DATA
                    liveData.setValue(new Resource<>(Resource.Status.SUCCESS,getDummyData(),"Using dummy data"));
                }
            });

            return liveData;
        }
        private List<StoreModel> getDummyData() {

            List<StoreModel> list = new ArrayList<>();
            return list;
        }
}
//    public LiveData<List<StoreModel>> getStoreData() {
//
//        MutableLiveData<List<StoreModel>> data = new MutableLiveData<>();
//
//        apiService.getStoreList().enqueue(new Callback<List<StoreModel>>() {
//            @Override
//            public void onResponse(Call<List<StoreModel>> call, Response<List<StoreModel>> response) {
//                if (response.isSuccessful()) {
//                    data.setValue(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<StoreModel>> call, Throwable t) {
//                data.setValue(null);
//            }
//        });
//
//        return data;
//    }


