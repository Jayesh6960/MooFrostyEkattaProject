package com.example.moofrosty.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.R;
import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.AttendanceStatusResponse;
import com.example.moofrosty.data.model.PunchResponse;
import com.example.moofrosty.ui.attendance.AttendanceMenuModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceRepository {

    private static final String TAG = "AttendanceRepository";
    private ApiService apiService;

    public AttendanceRepository() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Log.d(TAG, "Repository Initialized");
    }

    // --------------------------------------------------
    // 1. Fetch Menu Items
    // --------------------------------------------------
    public void fetchMenuItems(MutableLiveData<List<AttendanceMenuModel>> data) {
        Log.d(TAG, "fetchMenuItems() called");

        List<AttendanceMenuModel> items = new ArrayList<>();
        items.add(new AttendanceMenuModel("Attendance", R.drawable.attendanceiconattendance));
        items.add(new AttendanceMenuModel("Leave", R.drawable.calendericonattendance));
        items.add(new AttendanceMenuModel("Support", R.drawable.supporticonattendance));
        items.add(new AttendanceMenuModel("Profile", R.drawable.profileiconattendance));

        Log.d(TAG, "Menu items size: " + items.size());
        data.setValue(items);
    }

    // --------------------------------------------------
    // 2. Check Today's Attendance Status
    // --------------------------------------------------
    public void checkTodayStatus(String token,
                                 MutableLiveData<Resource<AttendanceStatusResponse>> liveData) {

        Log.d(TAG, "checkTodayStatus() called");
        Log.d(TAG, "Token: " + token);

        liveData.setValue(Resource.loading(null));
        Log.d(TAG, "Status API -> LOADING");

        apiService.checkAttendanceStatus("Bearer " + token)
                .enqueue(new Callback<AttendanceStatusResponse>() {

                    @Override
                    public void onResponse(Call<AttendanceStatusResponse> call,
                                           Response<AttendanceStatusResponse> response) {

                        Log.d(TAG, "checkTodayStatus() response received");
                        Log.d(TAG, "HTTP Code: " + response.code());

                        if (response.isSuccessful() && response.body() != null) {

                            AttendanceStatusResponse body = response.body();
                            Log.d(TAG, "Response SUCCESS");
                            Log.d(TAG, "Holiday: " + body.isHoliday());
                            Log.d(TAG, "Present: " + body.isPresent());
                            Log.d(TAG, "PresentOut: " + body.isPresentOut());
                            Log.d(TAG, "AbleToPunch: " + body.isAbleToPunch());

                            liveData.setValue(Resource.success(body));

                        } else {
                            Log.e(TAG, "Response FAILED or Body NULL");
                            liveData.setValue(Resource.error(
                                    "Failed to get attendance status", null));
                        }
                    }

                    @Override
                    public void onFailure(Call<AttendanceStatusResponse> call, Throwable t) {
                        Log.e(TAG, "checkTodayStatus() API FAILURE", t);
                        liveData.setValue(Resource.error(
                                "Network Error: " + t.getMessage(), null));
                    }
                });
    }

    // --------------------------------------------------
    // 3. Mark Attendance (Punch In / Punch Out)
    // --------------------------------------------------
    public void markAttendance(String token,
                               String date,
                               String time,
                               String loc,
                               String coords,
                               MutableLiveData<Resource<String>> punchResult) {

        Log.d(TAG, "markAttendance() called");
        Log.d(TAG, "Date: " + date);
        Log.d(TAG, "Time: " + time);
        Log.d(TAG, "Location: " + loc);
        Log.d(TAG, "Coords: " + coords);

        punchResult.setValue(Resource.loading(null));
        Log.d(TAG, "Punch API -> LOADING");

        apiService.markAttendance("Bearer " + token, date, time, loc, coords)
                .enqueue(new Callback<PunchResponse>() {

                    @Override
                    public void onResponse(Call<PunchResponse> call,
                                           Response<PunchResponse> response) {

                        Log.d(TAG, "markAttendance() response received");
                        Log.d(TAG, "HTTP Code: " + response.code());

                        if (response.isSuccessful() && response.body() != null) {

                            PunchResponse data = response.body();
                            Log.d(TAG, "Status: " + data.getStatus());
                            Log.d(TAG, "Message: " + data.getMessage());
                            Log.d(TAG, "Is Punch In: " + data.isIn());
                            Log.d(TAG, "Is Punch Out: " + data.isOut());

                            if ("success".equalsIgnoreCase(data.getStatus())) {
                                String msg;
//                                        ? "Punch In Successful"
//                                        : "Punch Out Successful";
                                if (data.isOut()) {
                                    msg = "Punch Out Successful";
                                } else {
                                    // If isOut is false, but success, it must be Punch In
                                    msg = "Punch In Successful";
                                }

                                Log.d(TAG, msg);
                                punchResult.setValue(Resource.success(msg));

                            } else {
                                Log.e(TAG, "Punch Failed: " + data.getMessage());
                                punchResult.setValue(Resource.error(
                                        data.getMessage(), null));
                            }

                        } else {
                            Log.e(TAG, "Server Error: " + response.code());
                            punchResult.setValue(Resource.error(
                                    "Server Error: " + response.code(), null));
                        }
                    }

                    @Override
                    public void onFailure(Call<PunchResponse> call, Throwable t) {
                        Log.e(TAG, "markAttendance() API FAILURE", t);
                        Log.d(TAG, "markAttendance() API FAILURE"+ t.getMessage());
                        punchResult.setValue(Resource.error(
                                "Network Error: " + t.getMessage(), null));
                    }
                });
    }
}

//    private ApiService apiService;
//
//    public AttendanceRepository() {
//        // Assuming ApiClient is your Retrofit Singleton
//        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
//    }
//
//        public void fetchMenuItems(MutableLiveData<List<AttendanceMenuModel>> data) {
//            List<AttendanceMenuModel> items = new ArrayList<>();
//
//            // Add your 6 items here. Replace 0 with R.drawable.your_icon
//            // Since I don't have your specific drawables, I'm using standard ones as placeholders.
//            // PLEASE REPLACE WITH YOUR ACTUAL DRAWABLE IDs
//            items.add(new AttendanceMenuModel("Attendance", R.drawable.attendanceiconattendance));
//            items.add(new AttendanceMenuModel("Leave", R.drawable.calendericonattendance));
//            items.add(new AttendanceMenuModel("Support", R.drawable.supporticonattendance));
//            items.add(new AttendanceMenuModel("Profile", R.drawable.profileiconattendance));
//
//            data.setValue(items);
//        }
//
//    // --- 1. Check Current Status ---
//    public void checkTodayStatus(String token, MutableLiveData<Resource<Integer>> punchStateLive) {
//        punchStateLive.setValue(Resource.loading(null));
//
//        apiService.checkAttendanceStatus("Bearer " + token).enqueue(new Callback<AttendanceStatusResponse>() {
//            @Override
//            public void onResponse(Call<AttendanceStatusResponse> call, Response<AttendanceStatusResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    AttendanceStatusResponse data = response.body();
//
//                    // LOGIC: Determine State based on API flags
//                    int state = 0; // Default: Ready to Punch In
//
//                    if (data.isHoliday()) {
//                        state = 3; // Holiday / Disabled
//                    } else if (data.isPresent() && data.isPresentOut()) {
//                        state = 2; // Attendance Completed (In + Out done)
//                    } else if (data.isPresent() && !data.isPresentOut()) {
//                        state = 1; // Punched In, Ready to Punch Out
//                    } else if (!data.isAbleToPunch()) {
//                        state = 3; // Disabled (e.g., late or not allowed)
//                    } else {
//                        state = 0; // Not Punched Yet (Ready to In)
//                    }
//
//                    punchStateLive.setValue(Resource.success(state));
//                } else {
//                    punchStateLive.setValue(Resource.error("Failed to check status", null));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AttendanceStatusResponse> call, Throwable t) {
//                punchStateLive.setValue(Resource.error("Network Error: " + t.getMessage(), null));
//            }
//        });
//    }
//
//    // --- 2. Perform Punch ---
//    public void markAttendance(String token, String date, String time, String loc, String coords,
//                               MutableLiveData<Resource<String>> punchResult) {
//
//        punchResult.setValue(Resource.loading(null));
//
//        apiService.markAttendance("Bearer " + token, date, time, loc, coords).enqueue(new Callback<PunchResponse>() {
//            @Override
//            public void onResponse(Call<PunchResponse> call, Response<PunchResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    PunchResponse data = response.body();
//                    if (data.getStatus() != null && data.getStatus().equals("success")) {
//                        // Success
//                        String msg = data.isIn() ? "Punch In Successful" : "Punch Out Successful";
//                        punchResult.setValue(Resource.success(msg));
//                    } else {
//                        punchResult.setValue(Resource.error("Punch Failed", null));
//                    }
//                } else {
//                    punchResult.setValue(Resource.error("Server Error: " + response.code(), null));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PunchResponse> call, Throwable t) {
//                punchResult.setValue(Resource.error("Network Error: " + t.getMessage(), null));
//            }
//        });
//    }
//}
