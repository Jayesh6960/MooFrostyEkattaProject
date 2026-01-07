package com.example.moofrosty.core.network;

import com.example.moofrosty.data.model.ApplyLeaveRequest;
import com.example.moofrosty.data.model.BeatResponse;
import com.example.moofrosty.data.model.GeneralResponse;
import com.example.moofrosty.data.model.LeaveHistoryResponse;
import com.example.moofrosty.data.model.LeaveResponse;
import com.example.moofrosty.data.model.LocationResponse;
import com.example.moofrosty.data.model.LoginRequest;
import com.example.moofrosty.data.model.LoginResponse;
import com.example.moofrosty.data.model.RssResponse;
import com.example.moofrosty.data.model.SecondaryChannelResponse;
import com.example.moofrosty.data.model.StoreExistRequest;
import com.example.moofrosty.data.model.StoreExistResponse;
import com.example.moofrosty.data.model.StoreListResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @POST("api/admin/store-exist")
    Call<StoreExistResponse> checkStoreExist(
            @Header("Authorization") String token,
            @Body StoreExistRequest request
    );

    @GET("api/admin/countries")
    Call<LocationResponse<LocationResponse.Country>> getCountries(@Header("Authorization") String token);

    @GET("api/admin/states/{countryId}")
    Call<LocationResponse<LocationResponse.State>> getStates(@Header("Authorization") String token, @Path("countryId") int countryId);

    @GET("api/admin/districts/{stateId}")
    Call<LocationResponse<LocationResponse.District>> getDistricts(@Header("Authorization") String token, @Path("stateId") int stateId);

    @GET("api/admin/cities/{districtId}")
    Call<LocationResponse<LocationResponse.City>> getCities(@Header("Authorization") String token, @Path("districtId") int districtId);

    @GET("api/admin/get-beat")
    Call<BeatResponse> getBeats(@Header("Authorization") String token);

    @Multipart
    @POST("api/admin/add-store")
    Call<GeneralResponse> addStore(
            @Header("Authorization") String token,
            @Part("ownerfullName") RequestBody ownerFullName,
            @Part("owneremailId") RequestBody ownerEmail,
            @Part("mobileNumber") RequestBody mobileNumber,

            @Part("storeName") RequestBody storeName,
            @Part("RS_SSIdentifier") RequestBody rsSsIdentifier,
            @Part("secondaryChannel") RequestBody secondaryChannel,
            @Part("outletType") RequestBody outletType,
            @Part("ssName") RequestBody ssName,

            @Part("pincode") RequestBody pincode,
            @Part("address") RequestBody address,

            @Part("country") RequestBody country,
            @Part("state") RequestBody state,
            @Part("district") RequestBody district,
            @Part("city") RequestBody city,

            @Part("lat_long") RequestBody latLong,
            @Part("documentType") RequestBody documentType,
            @Part("documentNumber") RequestBody documentNumber,

            @Part("beatId") RequestBody beatId,

            @Part MultipartBody.Part uploadDocument,
            @Part MultipartBody.Part UploadShopBoardImage,
            @Part MultipartBody.Part UploadShopInsideImage
    );

    // --- NEW ENDPOINTS ---
    @GET("api/admin/rss-identifier")
    Call<RssResponse> getRssIdentifiers(@Header("Authorization") String token);

    @GET("api/admin/secondary-channel")
    Call<SecondaryChannelResponse> getSecondaryChannels(@Header("Authorization") String token);

    @GET("api/admin/get-store-list")
    Call<StoreListResponse> getStoreList(
            @Header("Authorization") String token,
            @Query("date") String date
    );

}
