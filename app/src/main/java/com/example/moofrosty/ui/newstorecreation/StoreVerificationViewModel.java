package com.example.moofrosty.ui.newstorecreation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.Resource;
import androidx.lifecycle.ViewModel;
import com.example.moofrosty.data.model.StoreExistResponse;
import com.example.moofrosty.data.repository.StoreVerificationRepository;

public class StoreVerificationViewModel extends ViewModel{

    private StoreVerificationRepository repository;
    private MutableLiveData<Resource<StoreExistResponse>> checkResult = new MutableLiveData<>();

    public StoreVerificationViewModel() {
        repository = new StoreVerificationRepository();
    }

    public LiveData<Resource<StoreExistResponse>> getCheckResult() { return checkResult; }

    public void verifyNumber(String token, String mobile) {
        repository.checkStoreExistence(token, mobile, checkResult);
    }
}
