package com.example.moofrosty.ui.ATMSummary;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.Resource;

import java.util.List;

public class AtmViewModel extends ViewModel {

    private AtmRepository repository;

    public AtmViewModel() {
        repository = new AtmRepository();
    }

    public LiveData<Resource<List<StoreModel>>> getStoreList() {
        return repository.getStoreData();
    }
}