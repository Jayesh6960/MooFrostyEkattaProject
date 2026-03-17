package com.example.moofrosty.ui.attendance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.AttendanceStatusResponse;
import com.example.moofrosty.data.repository.AttendanceRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceViewModel extends ViewModel {


    private AttendanceRepository repository;
    private String userToken = "";

    // LiveData for Menu
    private MutableLiveData<List<AttendanceMenuModel>> menuItems = new MutableLiveData<>();

    // LiveData for Status API Response
    private MutableLiveData<Resource<AttendanceStatusResponse>> statusResponse = new MutableLiveData<>();

    // LiveData for Punch API Result
    private MutableLiveData<Resource<String>> punchResult = new MutableLiveData<>();

    public AttendanceViewModel() {
        repository = new AttendanceRepository();
        loadMenuItems();
    }

    public void setToken(String token) {
        this.userToken = token;
    }

    // --- Menu Logic ---
    private void loadMenuItems() {
        repository.fetchMenuItems(menuItems);
    }
    public LiveData<List<AttendanceMenuModel>> getMenuItems() { return menuItems; }

    // --- Status Logic ---
    public void checkTodayStatus() {
        if(!userToken.isEmpty()) repository.checkTodayStatus(userToken, statusResponse);
    }
    public LiveData<Resource<AttendanceStatusResponse>> getStatusResponse() { return statusResponse; }

    // --- Punch Logic ---
    public void performPunch(String location, String coordinates) {
        if(userToken.isEmpty()) return;

        // Get Mobile Date & Time
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        repository.markAttendance(userToken, currentDate, currentTime, location, coordinates, punchResult);
    }
    public LiveData<Resource<String>> getPunchResult() { return punchResult; }

    // --- SIMPLIFIED Logic to calculate Button State ---
    // Returns an Integer State:
    // 0: Normal Punch In
    // 1: Punch Out (Allowed immediately)
    // 2: Completed (Done for day)
    // 4: Sunday (Disabled)
    // 5: Holiday/Leave (Disabled)
    public int calculatePunchState(AttendanceStatusResponse data) {

        // 1. Check Sunday
//        Calendar calendar = Calendar.getInstance();
//        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
//            return 4; // Sunday
//        }

        // 2. Check Leave/Holiday
        // isAbleToPunch false means user is on leave or restricted
        if (data.isHoliday || !data.isAbleToPunch) {
            return 5; // Holiday/Leave
        }

        // 3. Check Basic Punch Flow
        if (data.isPresent) {
            // User has ALREADY Punched In
            if (data.isPresentOut) {
                // User has ALSO Punched Out
                return 2; // Done (Attendance Marked)
            } else {
                // Punched In, but NOT Punched Out yet
                // No 1-hour wait check here anymore.
                return 1; // Ready to Punch Out
            }
        } else {
            // User has NOT Punched In yet
            return 0; // Ready to Punch In
        }
    }
}

//    private AttendanceRepository repository;
//    private String userToken = "";
//
//    // LiveData for Menu
//    private MutableLiveData<List<AttendanceMenuModel>> menuItems = new MutableLiveData<>();
//
//    // LiveData for Status API Response
//    private MutableLiveData<Resource<AttendanceStatusResponse>> statusResponse = new MutableLiveData<>();
//
//    // LiveData for Punch API Result
//    private MutableLiveData<Resource<String>> punchResult = new MutableLiveData<>();
//
//    public AttendanceViewModel() {
//        repository = new AttendanceRepository();
//        loadMenuItems();
//    }
//
//    public void setToken(String token) {
//        this.userToken = token;
//    }
//
//    // --- Menu Logic ---
//    private void loadMenuItems() {
//        repository.fetchMenuItems(menuItems);
//    }
//    public LiveData<List<AttendanceMenuModel>> getMenuItems() { return menuItems; }
//
//    // --- Status Logic ---
//    public void checkTodayStatus() {
//        if(!userToken.isEmpty()) repository.checkTodayStatus(userToken, statusResponse);
//    }
//    public LiveData<Resource<AttendanceStatusResponse>> getStatusResponse() { return statusResponse; }
//
//    // --- Punch Logic ---
//    public void performPunch(String location, String coordinates) {
//        if(userToken.isEmpty()) return;
//
//        // Get Mobile Date & Time as per your requirement
//        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//
//        repository.markAttendance(userToken, currentDate, currentTime, location, coordinates, punchResult);
//    }
//    public LiveData<Resource<String>> getPunchResult() { return punchResult; }
//
//    // --- HELPER: Logic to calculate Button State ---
//    // Returns an Integer State:
//    // 0: Normal Punch In
//    // 1: Punch Out (Allowed)
//    // 2: Completed (Done for day)
//    // 3: Wait 1 Hour (Disabled)
//    // 4: Sunday (Disabled)
//    // 5: Holiday/Leave (Disabled)
//    // 6: Not Between Time (Disabled)
//    public int calculatePunchState(AttendanceStatusResponse data) {
//        // 1. Check Sunday
//        Calendar calendar = Calendar.getInstance();
//        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
//            return 4; // Sunday
//        }
//
//        // 2. Check Time Range (isBetweenTime)
//        if (!data.isBetweenTime) {
//            return 6; // Not in time
//        }
//
//        // 3. Check Leave/Holiday
//        if (data.isHoliday || !data.isAbleToPunch) {
//            return 5; // Holiday/Leave
//        }
//
//        // 4. Check Punch Flow
//        if (data.isPresent) {
//            if (data.isPresentOut) {
//                return 2; // Done (In + Out)
//            } else {
//                // Punched In, Checking 1 Hour Rule
//                if (hasOneHourPassed(data.intime)) {
//                    return 1; // Ready to Punch Out
//                } else {
//                    return 3; // Wait 1 Hour
//                }
//            }
//        } else {
//            return 0; // Ready to Punch In
//        }
//    }
//
//    private boolean hasOneHourPassed(String inTimeStr) {
//        if (inTimeStr == null || inTimeStr.isEmpty()) return true; // Fallback
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//            Date inTime = sdf.parse(inTimeStr);
//            Date nowTime = sdf.parse(sdf.format(new Date())); // Current Time
//
//            long diff = nowTime.getTime() - inTime.getTime();
//            long oneHourMillis = 60 * 60 * 1000;
//
//            return diff >= oneHourMillis;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return true; // If parse fails, allow punch to be safe
//        }
//    }
//}

//here one hr and all add that faklse code above

//    private AttendanceRepository repository;
//
//    // 0: Ready to In, 1: Ready to Out, 2: Done, 3: Disabled/Holiday
//    private MutableLiveData<Resource<Integer>> punchState = new MutableLiveData<>();
//    private MutableLiveData<Resource<String>> punchResult = new MutableLiveData<>();
//
//    private MutableLiveData<List<AttendanceMenuModel>> menuItems = new MutableLiveData<>();
//
//    private String userToken = "";
//
//    public AttendanceViewModel() {
//        repository = new AttendanceRepository();
//        loadMenuItems();
//    }
//
//    public void setToken(String token) {
//        this.userToken = token;
//    }
//    // --- MENU ACTIONS ---
//    private void loadMenuItems() {
//        repository.fetchMenuItems(menuItems);
//    }
//
//    public LiveData<List<AttendanceMenuModel>> getMenuItems() {
//        return menuItems;
//    }
//
//    // --- Getters for Observers ---
//    public LiveData<Resource<Integer>> getPunchState() { return punchState; }
//    public LiveData<Resource<String>> getPunchResult() { return punchResult; }
//
//    // --- Actions ---
//
//    public void checkTodayStatus() {
//        if(userToken.isEmpty()) return;
//        repository.checkTodayStatus(userToken, punchState);
//    }
//
//    public void performPunch(String location, String coordinates) {
//        if(userToken.isEmpty()) return;
//
//        // Generate current Date and Time
//        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
//
//        repository.markAttendance(userToken, currentDate, currentTime, location, coordinates, punchResult);
//    }
//
//    // Helper to refresh state after successful punch
//    public void refreshStateAfterSuccess(boolean isPunchIn) {
//        // If we just Punched In, next state is 1 (Punch Out).
//        // If we Punched Out, next state is 2 (Done).
//        if(isPunchIn) {
//            punchState.setValue(Resource.success(1));
//        } else {
//            punchState.setValue(Resource.success(2));
//        }
//    }
//}



// code no api static

//    private AttendanceRepository repository;
//    private MutableLiveData<List<AttendanceMenuModel>> menuItems = new MutableLiveData<>();
//
//    private MutableLiveData<Integer> punchState = new MutableLiveData<>(0);
//
//
//    public AttendanceViewModel() {
//        repository = new AttendanceRepository();
//        loadMenuItems();
//    }
//
//    private void loadMenuItems() {
//        repository.fetchMenuItems(menuItems);
//    }
//
//    public LiveData<List<AttendanceMenuModel>> getMenuItems() {
//        return menuItems;
//    }
//
//    // Get current state to update UI
//    public LiveData<Integer> getPunchState() {
//        return punchState;
//    }
//
//    // Handle Button Click
//    public void performPunch() {
//        Integer currentState = punchState.getValue();
//        if (currentState != null) {
//            if (currentState == 0) {
//                // Was "Punch In", now switch to "Punch Out" state
//                punchState.setValue(1);
//            } else if (currentState == 1) {
//                // Was "Punch Out", now switch to "Done" state
//                punchState.setValue(2);
//            }
//        }
//    }
//}