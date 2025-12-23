package com.example.moofrosty.ui.attendance.attendancecalender;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.LeaveResponse;
import com.example.moofrosty.data.repository.AttendanceCalendarRepository;

public class AttendanceCalendarViewModel extends ViewModel {

    private AttendanceCalendarRepository repository;
    private MutableLiveData<Resource<LeaveResponse>> leaveData = new MutableLiveData<>();

    public AttendanceCalendarViewModel() {
        repository = new AttendanceCalendarRepository();
    }

    public LiveData<Resource<LeaveResponse>> getLeaveData() {
        return leaveData;
    }

    public void fetchLeaves(String token) {
        repository.getLeaves(token, leaveData);
    }
}
