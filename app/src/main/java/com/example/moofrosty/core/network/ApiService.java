package com.example.moofrosty.core.network;

import com.example.moofrosty.data.model.ApplyLeaveRequest;
import com.example.moofrosty.data.model.GeneralResponse;
import com.example.moofrosty.data.model.LeaveHistoryResponse;
import com.example.moofrosty.data.model.LeaveResponse;
import com.example.moofrosty.data.model.LoginRequest;
import com.example.moofrosty.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/admin/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    // --- NEW: Apply Leave API ---
    @POST("api/admin/user-leaves")
    Call<GeneralResponse> applyLeave(
            @Header("Authorization") String token,
            @Body ApplyLeaveRequest request
    );

    // --- NEW: Get Leave History API ---
    @GET("api/admin/get-user-leaves")
    Call<LeaveHistoryResponse> getLeaveHistory(
            @Header("Authorization") String token
    );

    @GET("api/admin/get-user-leaves")
    Call<LeaveResponse> getUserLeaves(@Header("Authorization") String token);





}
