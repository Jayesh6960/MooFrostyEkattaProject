package com.example.moofrosty.ui.enterstoreorders.takeorder;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.CheckInRequest;
import com.example.moofrosty.data.model.GeneralResponse;
import com.example.moofrosty.data.repository.TakeOrderActivityRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TakeOrderActivityViewModel extends ViewModel {

    private final TakeOrderActivityRepository repository;
    private final MutableLiveData<Resource<GeneralResponse>> checkoutStatus = new MutableLiveData<>();

    public TakeOrderActivityViewModel() {
        repository = new TakeOrderActivityRepository();
    }

    public LiveData<Resource<GeneralResponse>> getCheckoutStatus() {
        return checkoutStatus;
    }

    public void performCheckOut(String token, int shopId, String reason) {
        // 1. Get Current Date and Time
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        // 2. Create Request (storein = "out")
        CheckInRequest request = new CheckInRequest(shopId, date, time, "out", reason);
        Log.d("VM_REASON", "Received reason: " + reason);

        // 3. Call Repo
        repository.checkOutStore(token, request, checkoutStatus);
    }


}
