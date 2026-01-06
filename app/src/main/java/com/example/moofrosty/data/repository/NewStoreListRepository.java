package com.example.moofrosty.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.StoreCreationModel;
import com.example.moofrosty.data.model.StoreExistResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewStoreListRepository {

    private ApiService apiService;

    public NewStoreListRepository() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void fetchStoresByDate(String date, MutableLiveData<List<StoreCreationModel>> liveData) {
        List<StoreCreationModel> list = new ArrayList<>();

        // Mock Data
        list.add(new StoreCreationModel("1", "Sai Dairy", "Ramesh Patil", "Pending", date));
        list.add(new StoreCreationModel("2", "Krishna Sweets", "Suresh Kumar", "Approved", date));

        liveData.setValue(list);
    }
}
