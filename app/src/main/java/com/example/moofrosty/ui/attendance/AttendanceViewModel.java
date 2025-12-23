package com.example.moofrosty.ui.attendance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.data.repository.AttendanceRepository;

import java.util.List;

public class AttendanceViewModel extends ViewModel {

    private AttendanceRepository repository;
    private MutableLiveData<List<AttendanceMenuModel>> menuItems = new MutableLiveData<>();

    private MutableLiveData<Integer> punchState = new MutableLiveData<>(0);


    public AttendanceViewModel() {
        repository = new AttendanceRepository();
        loadMenuItems();
    }

    private void loadMenuItems() {
        repository.fetchMenuItems(menuItems);
    }

    public LiveData<List<AttendanceMenuModel>> getMenuItems() {
        return menuItems;
    }

    // Get current state to update UI
    public LiveData<Integer> getPunchState() {
        return punchState;
    }

    // Handle Button Click
    public void performPunch() {
        Integer currentState = punchState.getValue();
        if (currentState != null) {
            if (currentState == 0) {
                // Was "Punch In", now switch to "Punch Out" state
                punchState.setValue(1);
            } else if (currentState == 1) {
                // Was "Punch Out", now switch to "Done" state
                punchState.setValue(2);
            }
        }
    }
}