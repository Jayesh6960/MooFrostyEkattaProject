package com.example.moofrosty.data.repository;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.DashboardItem;
import com.example.moofrosty.data.model.UpdateResponse;
import com.example.moofrosty.data.model.UserDetailResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {

    public void fetchMocData(String mocName, MutableLiveData<List<DashboardItem>> dataList, MutableLiveData<String> totalIncentives) {

        List<DashboardItem> items = new ArrayList<>();


        // Safety check to prevent NullPointer
        if (mocName == null) mocName = "MOC 02";
//MGP(Slaes)- Monthly Sales
//        EFOS -->> ATM (Actual time in Market)
        if (mocName.contains("MOC 02")) {
            // DECEMBER DATA
            totalIncentives.setValue("0 / 0");
            items.add(new DashboardItem("Monthly Sales", 0, "0/0", 0, 0));
            items.add(new DashboardItem("ATM", 0, "0/0", 0, 0));
        } else if (mocName.contains("MOC 1")) {
            // NOVEMBER DATA
            //Changes Before  Changes After MGP Sales--Monthly Sales
            //Changes Before Change  Before Change After change ATM  Actul Time in Market
            totalIncentives.setValue("0 / 0");
            items.add(new DashboardItem("Monthly Sales ", 0, "0/0", 0, 0));
            items.add(new DashboardItem("ATM", 0, "0/0", 0, 0));
        }

//        if (mocName.contains("MOC 01")) {
//            // DECEMBER DATA
//            totalIncentives.setValue("120 / 1720");
//            items.add(new DashboardItem("MGP(Sales)", 33, "132817/400000", 400000, 132817));
//            items.add(new DashboardItem("EFOS", 65, "11/17", 17, 11));
//        } else if (mocName.contains("MOC 12")) {
//            // NOVEMBER DATA
//            totalIncentives.setValue("850 / 1720");
//            items.add(new DashboardItem("MGP(Sales)", 60, "240000/400000", 400000, 240000));
//            items.add(new DashboardItem("EFOS", 65, "13/17", 17, 11));
//        } else {
//            // OCTOBER DATA
//            totalIncentives.setValue("1500 / 1720");
//            items.add(new DashboardItem("MGP(Sales)", 100, "400000/400000", 400000, 400000));
//            items.add(new DashboardItem("EFOS", 65, "12/17", 17, 11));
//        }

        // Updates the LiveData which triggers the UI update
        dataList.setValue(items);
    }
//    Latest Updated 24-03-2026
//    Changes in  the Active and thye Deactive status
    public void checkUserStatus(String token, MutableLiveData<Resource<UserDetailResponse>> liveData) {
        ApiService apiService = ApiClient.getApi();
        apiService.getUserDetail(token).enqueue(new Callback<UserDetailResponse>() {

            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {

                Log.d("StatusCheck", "Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {

                    UserDetailResponse resp = response.body();
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(resp);

                    Log.d("FULL_JSON_RESP0ONSE_UserDetails", jsonResponse);

                    if (resp.getData() != null) {

                        String status = resp.getData().getstatus();
                        Log.d("StatusCheck", "Server status: " + status);

                        liveData.postValue(Resource.success(resp));

                    } else {
                        liveData.postValue(Resource.error("Data NULL", null));
                    }

                } else {
                    liveData.postValue(Resource.error("Error Code: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                Log.d("StatusCheck", "Error: " + t.getMessage());
                liveData.postValue(Resource.error(t.getMessage(), null));
            }
        });
    }
//    public void checkAppUpdate() {
//
//        ApiService apiService = ApiClient.getApi();
//
//        Call<UpdateResponse> call = apiService.checkAppUpdate();
//
//        call.enqueue(new retrofit2.Callback<UpdateResponse>() {
//            @Override
//            public void onResponse(Call<UpdateResponse> call, retrofit2.Response<UpdateResponse> response) {
//
//                if (response.isSuccessful() && response.body() != null) {
//
//                    UpdateResponse data = response.body();
//
//                    int latestVersionCode = data.getLatestVersionCode();
//                    String apkUrl = data.getApkUrl();
//                    boolean forceUpdate = data.isForceUpdate();
//                    String message = data.getUpdateMessage();
//
//                    try {
//                        int currentVersionCode = getPackageManager()
//                                .getPackageInfo(getPackageName(), 0).versionCode;
//
//                        if (latestVersionCode > currentVersionCode) {
//
//                            if (forceUpdate) {
//                                showForceUpdateDialog(apkUrl, message);
//                            } else {
//                                showOptionalUpdateDialog(apkUrl, message);
//                            }
//
//                        } else {
//                            Toast.makeText(getApplicationContext(),
//                                    "You are using the latest version",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//
////            private String getPackageName() {
////                return null;
////            }
////
////            private void showOptionalUpdateDialog(String apkUrl, String message) {
////                Toast.makeText(getApplicationContext(),
////                        "Optional Update",
////                        Toast.LENGTH_SHORT).show();
////            }
////
////            private void showForceUpdateDialog(String apkUrl, String message) {
////                Toast.makeText(getApplicationContext(),
////                        "Force Update",
////                        Toast.LENGTH_SHORT).show();
////            }
//
//            private PackageManager getPackageManager() {
//                return null;
//            }
//
//            private Context getApplicationContext() {
//                return null;
//            }
//
//            @Override
//            public void onFailure(Call<UpdateResponse> call, Throwable t) {
//                t.printStackTrace();
//                Toast.makeText(getApplicationContext(),
//                        "Update check failed",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}

