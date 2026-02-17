package com.example.moofrosty.ui.attendance.leave;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.ApplyLeaveRequest;
import com.example.moofrosty.data.model.GeneralResponse;
import com.example.moofrosty.data.model.LeaveHistoryResponse;
import com.example.moofrosty.data.model.LeaveTypeResponse;
import com.example.moofrosty.data.repository.LeaveRepository;

public class LeaveViewModel  extends ViewModel {

    private LeaveRepository repository;
    private MutableLiveData<Resource<GeneralResponse>> applyLeaveResult = new MutableLiveData<>();
    private MutableLiveData<Resource<LeaveHistoryResponse>> historyResult = new MutableLiveData<>();
    private MutableLiveData<Resource<LeaveTypeResponse>> leaveTypesResult = new MutableLiveData<>();
    public LeaveViewModel() {
        repository = new LeaveRepository();
    }
    public LiveData<Resource<GeneralResponse>> getApplyLeaveResult() {
        return applyLeaveResult;
    }
    public LiveData<Resource<LeaveHistoryResponse>> getHistoryResult() {
        return historyResult;
    }

    public void applyLeave(String token, String type, String start, String end, String reason) {
        ApplyLeaveRequest request = new ApplyLeaveRequest(type, start, end, reason);
        repository.applyLeave(token, request, applyLeaveResult);
    }

    public void fetchHistory(String token) {
        repository.getLeaveHistory(token, historyResult);
    }


    public LiveData<Resource<LeaveTypeResponse>> getLeaveTypesResult() {
        return leaveTypesResult;
    }
    public void fetchLeaveTypes(String token) {
        repository.getLeaveTypes(token, leaveTypesResult);
    }

}
