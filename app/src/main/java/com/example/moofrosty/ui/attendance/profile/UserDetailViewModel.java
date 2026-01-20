package com.example.moofrosty.ui.attendance.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.UserDetailResponse;
import com.example.moofrosty.data.repository.UserDetailRepository;

public class UserDetailViewModel extends AndroidViewModel {

    private UserDetailRepository repository;
    private MutableLiveData<Resource<UserDetailResponse>> userDetailLiveData = new MutableLiveData<>();

    public UserDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new UserDetailRepository(application);
    }

    public MutableLiveData<Resource<UserDetailResponse>> getUserDetail() {
        return userDetailLiveData;
    }

    public void loadUserDetail(String token) {
        repository.getUserDetails(token, userDetailLiveData);
    }
}
