    package com.example.moofrosty.core.network;
    import androidx.lifecycle.LiveData;
    import com.example.moofrosty.data.model.ApplyLeaveRequest;
    import com.example.moofrosty.data.model.AttendanceStatusResponse;
    import com.example.moofrosty.data.model.BeatResponse;
    import com.example.moofrosty.data.model.CategoryResponse;
    import com.example.moofrosty.data.model.CheckInRequest;
    import com.example.moofrosty.data.model.CheckoutRequest;
    import com.example.moofrosty.data.model.GeneralResponse;
    import com.example.moofrosty.data.model.LeaveHistoryResponse;
    import com.example.moofrosty.data.model.LeaveResponse;
    import com.example.moofrosty.data.model.LeaveTypeResponse;
    import com.example.moofrosty.data.model.LocationResponse;
    import com.example.moofrosty.data.model.LoginRequest;
    import com.example.moofrosty.data.model.LoginResponse;
    import com.example.moofrosty.data.model.OrderHistoryResponse;
    import com.example.moofrosty.data.model.ProductResponse;
    import com.example.moofrosty.data.model.PunchResponse;
    import com.example.moofrosty.data.model.RssResponse;
    import com.example.moofrosty.data.model.SecondaryChannelResponse;
    import com.example.moofrosty.data.model.StoreExistRequest;
    import com.example.moofrosty.data.model.StoreExistResponse;
    import com.example.moofrosty.data.model.StoreListResponse;
    import com.example.moofrosty.data.model.StoreListResponses;
    import com.example.moofrosty.data.model.StoreListWrapperResponse;
    import com.example.moofrosty.data.model.SubCategoryResponse;
    import com.example.moofrosty.data.model.UserDetailResponse;

    import okhttp3.MultipartBody;
    import okhttp3.RequestBody;
    import okhttp3.ResponseBody;
    import retrofit2.Call;
    import retrofit2.http.Body;
    import retrofit2.http.Field;
    import retrofit2.http.FormUrlEncoded;
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
                @Part("gstinNumber") RequestBody gstnNumber,

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
                @Query("date") String date,
//                @Query("Storelist") boolean Storelist
                @Query("page") String page
        );
        @GET("api/admin/get-store-list")
        Call<StoreListResponses> getStoreList(
                @Header("Authorization") String token,
                @Query("beatId") int beatId,
                @Query("page") String page // Pass "bit_wise_all" here
        );

        @GET("api/admin/get-store-list")
        Call<StoreListWrapperResponse> getStoreListVisited(
                @Header("Authorization") String token,
                @Query("date") String date, // Changed from logDate to date
                @Query("page") String page  // Pass "visited_shop" here
        );

        // 3. NOT VISITED STORES
        @GET("api/admin/get-store-list")
        Call<StoreListResponses> getStoreListNotVisited(
                @Header("Authorization") String token,
                @Query("date") String date, // Changed from logDate to date
                @Query("page") String page  // Pass "not_visited_shop" here
        );

        // 4. ORDER TAKEN STORES
        @GET("api/admin/get-store-list")
        Call<StoreListResponses> getStoreListByOrder(
                @Header("Authorization") String token,
                @Query("date") String date, // Changed from logDate to date
                @Query("page") String page  // Pass "order_taken_shop" here
        );

//        @GET("api/admin/get-store-list")
//        Call<StoreListResponses> getStoreList(
//                @Header("Authorization") String token,
//                @Query("beatId") int beatId,
//                @Query("Storelist") boolean storeList
//        );
//
//        @GET("api/admin/get-store-list")
//        Call<StoreListWrapperResponse> getStoreListVisited(
//                @Header("Authorization") String token,
//                @Query("logDate") String logDate,
//                @Query("visited") String visitedStatus, // "yes"
//                @Query("Storevisited") boolean storeVisited
//        );
//
//        // 3. NOT VISITED STORES (Direct List - confirmed via your JSON)
//        @GET("api/admin/get-store-list")
//        Call<StoreListResponses> getStoreListNotVisited(
//                @Header("Authorization") String token,
//                @Query("logDate") String logDate,
//                @Query("visited") String visitedStatus, // "no"
//                @Query("Storevisited") boolean storeVisited
//        );
//
//        // 4. ORDER TAKEN STORES (Direct List - confirmed via your JSON)
//        @GET("api/admin/get-store-list")
//        Call<StoreListResponses> getStoreListByOrder(
//                @Header("Authorization") String token,
//                @Query("logDate") String logDate,
//                @Query("order_taken") String orderTaken,
//                @Query("Storeorder") boolean storeOrder
//        );

        @GET("api/admin/user-detail")
        Call<UserDetailResponse> getUserDetail(@Header("Authorization") String token);

        @GET("api/admin/get-user-attendance")
        Call<AttendanceStatusResponse> checkAttendanceStatus(@Header("Authorization") String token);

        // Mark Attendance API (Punch In/Out)
        @FormUrlEncoded
        @POST("api/admin/user-attendance")
        Call<PunchResponse> markAttendance(
                @Header("Authorization") String token,
                @Field("attendanceDate") String date,
                @Field("attendanceTime") String time,
                @Field("location") String location,
                @Field("coordinates") String coordinates
        );

        @POST("api/admin/add-checkincheckout")
        Call<GeneralResponse> checkInStore(
                @Header("Authorization") String token,
                @Body CheckInRequest request
        );
        @POST("order/missed")
        Call<GeneralResponse> markOrderMissed(
                @Query("order_id") String orderId,
                @Query("reason") String reason
        );

        @GET("api/admin/get-products")
        Call<ProductResponse> getProducts(
                @Header("Authorization") String token
        );

        @GET("api/admin/get-category")
        Call<CategoryResponse> getCategories(
                @Header("Authorization") String token
        );

        @GET("api/admin/get-subcategory/{catId}")
        Call<SubCategoryResponse> getSubCategories(
                @Header("Authorization") String token,
                @Path("catId") int catId
        );

        @GET("api/admin/leaves-type")
        Call<LeaveTypeResponse> getLeaveTypes(
                @Header("Authorization") String token
        );

        @POST("api/admin/product-checkout")
        Call<GeneralResponse> checkoutCart(
                @Header("Authorization") String token,
                @Body CheckoutRequest request
        );

//        @GET("api/admin/list-product-checkout")
//        Call<OrderHistoryResponse> getOrderHistory(
//                @Header("Authorization") String token
//        );

        @GET("api/admin/list-product-checkout")
        Call<OrderHistoryResponse> getOrderHistory(
                @Header("Authorization") String token,
                @Query("shop_id") int shopId // [HIGHLIGHT] Added shop_id query parameter
        );
    }
