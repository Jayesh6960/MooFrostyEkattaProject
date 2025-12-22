package com.example.moofrosty.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.LoginResponse;
import com.example.moofrosty.data.repository.LoginRepository;

public class LoginViewModel extends ViewModel {

    private LoginRepository repository;
    private MutableLiveData<Resource<LoginResponse>> loginResult = new MutableLiveData<>();

    public LoginViewModel() {
        repository = new LoginRepository();
    }

    public LiveData<Resource<LoginResponse>> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        repository.loginUser(email, password, loginResult);
    }

}
