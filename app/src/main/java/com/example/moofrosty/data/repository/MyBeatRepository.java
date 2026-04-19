package com.example.moofrosty.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.BeatModel;
import com.example.moofrosty.data.model.Store;
import com.example.moofrosty.data.model.StoreListResponse;
import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.data.model.StoreListResponses;
import com.example.moofrosty.data.model.StoreListWrapperResponse;
import com.example.moofrosty.data.model.UserDetailResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBeatRepository {

    private static final String TAG = "MyBeatRepo";

    private final ApiService apiService;
    private final SessionManager sessionManager;
    private final Context context;

    public MyBeatRepository(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        this.sessionManager = new SessionManager(this.context);
    }


    // --- 1. INITIAL FETCH (Gets User Beats, then triggers the first store fetch) ---
//    public void fetchInitialDashboardData(MutableLiveData<Resource<List<Store>>> storesLiveData,
//                                          MutableLiveData<List<BeatModel>> beatsLiveData,
//                                          MutableLiveData<Integer> totalCountData,
//                                          MutableLiveData<Double> totalOrderValue,
//                                          MutableLiveData<Integer> visitedCount,
//                                          MutableLiveData<Integer> orderCount) {
//
//        storesLiveData.setValue(Resource.loading(null));
//
//        if (!NetworkUtil.isNetworkAvailable(context)) {
//            storesLiveData.setValue(Resource.error("No Internet Connection", null));
//            return;
//        }
//
//        String token = "Bearer " + sessionManager.getToken();
//        Log.d(TAG, "API REQUEST: getUserDetail");
//
//     }
    public void fetchInitialDashboardData(MutableLiveData<Resource<List<Store>>> storesLiveData,
                                          MutableLiveData<List<BeatModel>> beatsLiveData,
                                          MutableLiveData<Integer> totalCountData,
                                          MutableLiveData<Double> totalOrderValue,
                                          MutableLiveData<Integer> visitedCount,
                                          MutableLiveData<Integer> orderCount) {

        storesLiveData.setValue(Resource.loading(null));

        if (!NetworkUtil.isNetworkAvailable(context)) {
            storesLiveData.setValue(Resource.error("No Internet Connection", null));
            return;
        }

        String token = "Bearer " + sessionManager.getToken();

        Log.d(TAG, "API REQUEST: getUserDetail");

        apiService.getUserDetail(token).enqueue(new Callback<UserDetailResponse>() {

            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {

                Log.d(TAG, "API RESPONSE: getUserDetail | Code: " + response.code());

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {

                    UserDetailResponse.Data data = response.body().getData();

                    if (data != null && data.getBeats() != null && !data.getBeats().isEmpty()) {

                        List<UserDetailResponse.Data.Beat> beats = data.getBeats();

                        List<BeatModel> beatModels = new ArrayList<>();
                        StringBuilder beatIdsBuilder = new StringBuilder();

                        for (int i = 0; i < beats.size(); i++) {

                            UserDetailResponse.Data.Beat b = beats.get(i);

                            BeatModel beatModel = new BeatModel(
                                    String.valueOf(b.getBeatId()),
                                    b.getFullBeatName(),
                                    0,
                                    true
                            );

                            beatModels.add(beatModel);

                            beatIdsBuilder.append(b.getBeatId());

                            if (i < beats.size() - 1) {
                                beatIdsBuilder.append(",");
                            }
                        }

                        String beatIds = beatIdsBuilder.toString();

                        beatsLiveData.postValue(beatModels);

                        Log.d(TAG, "Beat IDs fetched: " + beatIds);

                        // Now fetch stores for ALL tab
                        fetchStoresForTab(
                                token,
                                beatIds,
                                "All",
                                storesLiveData,
                                totalCountData,
                                totalOrderValue
                        );

                        // Fetch global counts
                        fetchGlobalCounts(token, beatIds, visitedCount, orderCount);

                    } else {

                        storesLiveData.setValue(Resource.error("No beats assigned to user", null));
                    }

                } else {

                    storesLiveData.setValue(Resource.error("Failed to fetch user details", null));
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {

                Log.e(TAG, "API FAILURE: getUserDetail | Error: " + t.getMessage());

                storesLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
    }

    // --- 2. DYNAMIC ROUTER FOR TABS ---
    public void fetchStoresForTab(String token, String beatIds, String filterType,
                                  MutableLiveData<Resource<List<Store>>> storesLiveData,
                                  MutableLiveData<Integer> totalCountData,
                                  MutableLiveData<Double> totalOrderValue) {

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (filterType.equals("All")) {
            fetchAllStores(token, beatIds, date, storesLiveData, totalCountData, totalOrderValue);
        } else if (filterType.equals("Visited")) {
            fetchVisitedStores(token, beatIds, date, storesLiveData);
        } else {
            fetchDirectFilteredStores(token, beatIds, date, filterType, storesLiveData);
        }
    }

    // --- A. FETCH ALL (bit_wise_all) ---
    private void fetchAllStores(String token, String beatIds, String date,
                                MutableLiveData<Resource<List<Store>>> storesLiveData,
                                MutableLiveData<Integer> totalCountData,
                                MutableLiveData<Double> totalOrderValue) {

        Log.d("getuserdetails", "API REQUEST: getStoreList (ALL) | BeatIds: " + beatIds + " | Date: " + date);

        apiService.getStoreList(token, beatIds, date, "bit_wise_all").enqueue(new Callback<StoreListResponses>() {
            @Override
            public void onResponse(Call<StoreListResponses> call, Response<StoreListResponses> response) {
                Log.d(TAG, "API RESPONSE: getStoreList (ALL) | Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isStatus()) {
                        if (totalCountData != null) totalCountData.postValue(response.body().getCount());
                        if (totalOrderValue != null) totalOrderValue.postValue(response.body().getTotalBill());

                        List<Store> list = response.body().getStoreList();
                        if (list == null) list = new ArrayList<>();

                        Log.d("ShopDetails", "API SUCCESS: Fetched " + list + " stores.");
                        storesLiveData.setValue(Resource.success(list));
                    } else {
                        storesLiveData.setValue(Resource.error("No stores found", null));
                    }
                } else {
                    storesLiveData.setValue(Resource.error("API Error: " + response.code(), null));
                }
            }
            @Override
            public void onFailure(Call<StoreListResponses> call, Throwable t) {
                Log.e(TAG, "API FAILURE: getStoreList (ALL) | Error: " + t.getMessage());
                storesLiveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
            }
        });
    }

    // --- B. FETCH VISITED ---
    private void fetchVisitedStores(String token, String beatIds, String date, MutableLiveData<Resource<List<Store>>> storesLiveData) {
        Log.d(TAG, "API REQUEST: getStoreListVisited | BeatIds: " + beatIds + " | Date: " + date);

//        apiService.getStoreListVisited(token, beatIds, date, "visited_shop").enqueue(new Callback<StoreListWrapperResponse>() {
//            @Override
//            public void onResponse(Call<StoreListWrapperResponse> call, Response<StoreListWrapperResponse> response) {
//                Log.d(TAG, "API RESPONSE: getStoreListVisited | Code: " + response.code());
//
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Store> extractedList = new ArrayList<>();
//                    if (response.body().getWrappers() != null) {
//                        for (StoreListWrapperResponse.StoreWrapper w : response.body().getWrappers()) {
//                            if (w.getStore() != null) {
//                                Store s = w.getStore();
//                                s.setVisited(true);
//                                extractedList.add(s);
//                            }
//                        }
//                    }
//                    Log.d(TAG, "API SUCCESS: Fetched " + extractedList.size() + " VISITED stores.");
//                    storesLiveData.setValue(Resource.success(extractedList));
//                } else {
//                    storesLiveData.setValue(Resource.error("Server Error", null));
//                }
//            }
//            @Override
//            public void onFailure(Call<StoreListWrapperResponse> call, Throwable t) {
//                Log.e(TAG, "API FAILURE: getStoreListVisited | Error: " + t.getMessage());
//                storesLiveData.setValue(Resource.error(t.getMessage(), null));
//            }
//        });
//        apiService.getStoreListVisited(token, beatIds, date, "visited_shop")
//                .enqueue(new Callback<StoreListWrapperResponse>() {
//
//                    @Override
//                    public void onResponse(Call<StoreListWrapperResponse> call,
//                                           Response<StoreListWrapperResponse> response) {
//
//                        Log.d(TAG, "API RESPONSE: getStoreListVisited | Code: " + response.code());
//
//                        if (response.isSuccessful() && response.body() != null) {
//
//                            List<Store> extractedList = new ArrayList<>();
//
//                            if (response.body().getWrappers() != null) {
//
//                                for (StoreListWrapperResponse.StoreWrapper w : response.body().getWrappers()) {
//
//                                    if (w.getStore() != null) {
//
//                                        Store s = w.getStore();
//
//                                        // Read status from API
//                                        s.setVisited(s.getIsVisitedApi() == 1);
//                                        s.setOrderTaken(s.getIsOrderTakenApi() == 1);
//
//                                        extractedList.add(s);
//                                    }
//                                }
//                            }
//
//                            Log.d(TAG, "API SUCCESS: Fetched " + extractedList.size() + " VISITED stores.");
//
//                            storesLiveData.setValue(Resource.success(extractedList));
//
//                        } else {
//                            storesLiveData.setValue(Resource.error("Server Error", null));
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<StoreListWrapperResponse> call, Throwable t) {
//
//                        Log.e(TAG, "API FAILURE: getStoreListVisited | Error: " + t.getMessage());
//                        storesLiveData.setValue(Resource.error(t.getMessage(), null));
//                    }
//                });
//        apiService.getStoreListVisited(token, beatIds, date, "visited_shop")
//                .enqueue(new Callback<StoreListWrapperResponse>() {
//
//                    @Override
//                    public void onResponse(Call<StoreListWrapperResponse> call,
//                                           Response<StoreListWrapperResponse> response) {
//
//                        Log.d("API Code Response", "API RESPONSE: getStoreListVisited | Code: " + response.code());
//
//                        if (response.isSuccessful() && response.body() != null) {
//
//                            List<Store> extractedList = new ArrayList<>();
//
//                            if (response.body().getWrappers() != null) {
//
//                                for (StoreListWrapperResponse.StoreWrapper w : response.body().getWrappers()) {
//
//                                    if (w.getStore() != null) {
//
//                                        Store s = w.getStore();
//
//                                        // Convert API int values to boolean
//                                        s.setVisited(s.getIsVisitedApi() == 1);
//                                        s.setOrderTaken(s.getIsOrderTakenApi() == 1);
//                                        Log.d(TAG, "STORE DATA:");
//                                        Log.d(TAG, "Store ID: " + s.getShopId());
//                                        Log.d(TAG, "Store Name: " + s.getStoreName());
//                                        Log.d(TAG, "Visited: " + s.isVisited());
//                                        Log.d(TAG, "Order Taken: " + s.isOrderTaken());
//
//                                        extractedList.add(s);
//                                        Log.d("Extactlist", "data"+extractedList);
//
//                                    }
//                                }
//                            }
//
//                            Log.d("API list", "API SUCCESS: Fetched " + extractedList + " VISITED stores.");
//
//                            storesLiveData.setValue(Resource.success(extractedList));
//
//                        } else {
//                            storesLiveData.setValue(Resource.error("Server Error", null));
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<StoreListWrapperResponse> call, Throwable t) {
//
//                        Log.e(TAG, "API FAILURE: getStoreListVisited | Error: " + t.getMessage());
//                        storesLiveData.setValue(Resource.error(t.getMessage(), null));
//                    }
//                });
//        apiService.getStoreListVisited(token, beatIds, date, "visited_shop")
//                .enqueue(new Callback<StoreListWrapperResponse>() {
//
//                    @Override
//                    public void onResponse(Call<StoreListWrapperResponse> call,
//                                           Response<StoreListWrapperResponse> response) {
//
//                        Log.d("API_RESPONSE", "Code: " + response.code());
//
//                        if (response.isSuccessful() && response.body() != null) {
//
//                            List<Store> extractedList = new ArrayList<>();
//
//                            if (response.body().getStores() != null) {
//
//                                for (Store s : response.body().getStores()) {
//
//                                    if (s == null) continue;
//
//                                    s.setVisited(s.getIsVisitedApi() == 1);
//                                    s.setOrderTaken(s.getIsOrderTakenApi() == 1);
//
//                                    Log.d("STORE_DATA", "Store ID: " + s.getShopId());
//                                    Log.d("STORE_DATA", "Store Name: " + s.getStoreName());
//                                    Log.d("STORE_DATA", "Visited: " + s.isVisited());
//                                    Log.d("", "Order Taken: " + s.isOrderTaken());
//
//                                    extractedList.add(s);
//                                }
//                            }
//
//                            Log.d("API_LIST_SIZE", "Fetched Stores: " + extractedList.size());
//
//                            storesLiveData.setValue(Resource.success(extractedList));
//
//                        } else {
//                            storesLiveData.setValue(Resource.error("Server Error", null));
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<StoreListWrapperResponse> call, Throwable t) {
//
//                        Log.e("API_FAILURE", "Error: " + t.getMessage());
//                        storesLiveData.setValue(Resource.error(t.getMessage(), null));
//                    }
//                });

//        apiService.getStoreListVisited(token, beatIds, date, "visited_shop")
//                .enqueue(new Callback<StoreListWrapperResponse>() {
//
//                    @Override
//                    public void onResponse(Call<StoreListWrapperResponse> call,
//                                           Response<StoreListWrapperResponse> response) {
//
//                        Log.d("API_RESPONSE", "Code: " + response.code());
//
//                        if (response.isSuccessful() && response.body() != null) {
//
//                            List<Store> extractedList = new ArrayList<>();
//
//                            if (response.body().getStores() != null) {
//
//                                for (Store s : response.body().getStores()) {
//
//                                    if (s == null) continue;
//
//                                    s.setVisited(s.getIsVisitedApi() == 1);
//                                    s.setOrderTaken(s.getIsOrderTakenApi() == 1);
//
//                                    Log.d("STORE_DATA", "Store ID: " + s.getShopId());
//                                    Log.d("STORE_DATA", "Store Name: " + s.getStoreName());
//                                    Log.d("STORE_DATA", "Visited: " + s.isVisited());
//                                    Log.d("STORE_DATA", "Order Taken: " + s.isOrderTaken());
//
//                                    extractedList.add(s);
//                                }
//                            }
//
//                            Log.d("API_LIST_SIZE", "Fetched Stores: " + extractedList.size());
//                            Log.d("API_COUNT", "Count from API: " + response.body().getCount());
//
//                            storesLiveData.setValue(Resource.success(extractedList));
//
//                        } else {
//                            storesLiveData.setValue(Resource.error("Server Error", null));
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<StoreListWrapperResponse> call, Throwable t) {
//
//                        Log.e("API_FAILURE", "Error: " + t.getMessage());
//                        storesLiveData.setValue(Resource.error(t.getMessage(), null));
//                    }
//                });
        apiService.getStoreListVisited(token, beatIds, date, "visited_shop")
                .enqueue(new Callback<StoreListWrapperResponse>() {

                    @Override
                    public void onResponse(Call<StoreListWrapperResponse> call,
                                           Response<StoreListWrapperResponse> response) {

                        Log.d("API_RESPONSE", "Code: " + response.code());

                        if (response.isSuccessful() && response.body() != null) {

                            List<Store> extractedList = new ArrayList<>();

                            if (response.body().getStores() != null) {

                                for (Store s : response.body().getStores()) {

                                    if (s == null) continue;

                                    // convert API values to boolean
                                    boolean visited = s.getIsVisitedApi() == 1;
                                    boolean orderTaken = s.getIsOrderTakenApi() == 0;

                                    s.setVisited(visited);
//                                    s.setOrderTaken(orderTaken);

                                    Log.d("STORE_DATA", "Store ID: " + s.getShopId());
                                    Log.d("STORE_DATA", "Store Name: " + s.getStoreName());
                                    Log.d("STORE_DATA", "Visited: " + visited);
//                                    Log.d("STORE_DATA", "Order Taken: " + orderTaken);

                                    // ✅ Keep only if both are true
                                    if (visited  && orderTaken) { 
                                        extractedList.add(s);
                                    }
                                }
                            }

                            Log.d("API_LIST_SIZE", "Filtered Stores: " + extractedList.size());

                            storesLiveData.setValue(Resource.success(extractedList));

                        } else {
                            storesLiveData.setValue(Resource.error("Server Error", null));
                        }
                    }

                    @Override
                    public void onFailure(Call<StoreListWrapperResponse> call, Throwable t) {

                        Log.e("API_FAILURE", "Error: " + t.getMessage());
                        storesLiveData.setValue(Resource.error(t.getMessage(), null));
                    }
                });
    }

    // --- C. FETCH NOT VISITED OR ORDER TAKEN ---
    private void fetchDirectFilteredStores(String token, String beatIds, String date, String filterType, MutableLiveData<Resource<List<Store>>> storesLiveData) {
        Call<StoreListResponses> call;

        Log.d(TAG, "API REQUEST: getStoreList (" + filterType + ") | BeatIds: " + beatIds + " | Date: " + date);

        if (filterType.equals("Not Visited")) {
            call = apiService.getStoreListNotVisited(token, beatIds, date, "not_visited_shop");
        } else {
            call = apiService.getStoreListByOrder(token, beatIds, date, "order_taken_shop");
        }

        call.enqueue(new Callback<StoreListResponses>() {
            @Override
            public void onResponse(Call<StoreListResponses> call, Response<StoreListResponses> response) {
                Log.d(TAG, "API RESPONSE: getStoreList (" + filterType + ") | Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    List<Store> list = response.body().getStoreList();
                    if (list == null) list = new ArrayList<>();

                    // Set local UI flags based on the tab chosen
                    for(Store s : list) {
                        if(filterType.equals("Order Taken")) {
                            s.setOrderTaken(true);
                            s.setVisited(true);
                        } else if (filterType.equals("Not Visited")) {
                            s.setVisited(false);
                            s.setOrderTaken(false);
                        }
                    }
                    Log.d(TAG, "API SUCCESS: Fetched " + list.size()+ " " + filterType + " stores.");
                    storesLiveData.setValue(Resource.success(list));
                } else {
                    storesLiveData.setValue(Resource.error("Server Error", null));
                }
            }
            @Override
            public void onFailure(Call<StoreListResponses> call, Throwable t) {
                Log.e(TAG, "API FAILURE: getStoreList (" + filterType + ") | Error: " + t.getMessage());
                storesLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
    }

    // --- 3. FETCH GLOBAL COUNTS ---
    public void fetchGlobalCounts(String token, String beatIds, MutableLiveData<Integer> visitedCount, MutableLiveData<Integer> orderCount) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Log.d(TAG, "API REQUEST: fetchGlobalCounts | BeatIds: " + beatIds + " | Date: " + date);

        apiService.getStoreListVisited(token, beatIds, date, "visited_shop").enqueue(new Callback<StoreListWrapperResponse>() {
            @Override
            public void onResponse(Call<StoreListWrapperResponse> call, Response<StoreListWrapperResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "GLOBAL COUNT (Visited): " + response.body().getCount());
                    visitedCount.postValue(response.body().getCount());
                }
            }
            @Override public void onFailure(Call<StoreListWrapperResponse> call, Throwable t) {}
        });

        apiService.getStoreListByOrder(token, beatIds, date, "order_taken_shop").enqueue(new Callback<StoreListResponses>() {
            @Override
            public void onResponse(Call<StoreListResponses> call, Response<StoreListResponses> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "GLOBAL COUNT (Order): " + response.body().getCount());
                    orderCount.postValue(response.body().getCount());
                }
            }
            @Override public void onFailure(Call<StoreListResponses> call, Throwable t) {}
        });
    }

//    public void fetchDashboardData(MutableLiveData<Resource<List<Store>>> storesLiveData,
//                                   MutableLiveData<List<BeatModel>> beatsLiveData, MutableLiveData<Integer> totalCountData,MutableLiveData<Double> totalOrderValue) {
//
//        storesLiveData.setValue(Resource.loading(null));
//
//        if (!NetworkUtil.isNetworkAvailable(context)) {
//            storesLiveData.setValue(Resource.error("No Internet Connection", null));
//            return;
//        }
//
//        String token = "Bearer " + sessionManager.getToken();
//
//        // STEP 1: Call User Detail API first to get correct Beat ID
//        apiService.getUserDetail(token).enqueue(new Callback<UserDetailResponse>() {
//            @Override
//            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
//                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
//
//                    UserDetailResponse.Data data = response.body().getData();
//
//                    if (data != null && data.getBeat() != null) {
//                        int beatId = data.getBeat().getBeatId();
//                        String beatName = data.getBeat().getFullBeatName();
//
//                        Log.d(TAG, "UserDetail API Success. BeatID: " + beatId + ", Name: " + beatName);
//
//                        // Create Beat Model for UI (Count will be 0 initially)
//                        List<BeatModel> beatList = new ArrayList<>();
//                        BeatModel myBeat = new BeatModel(String.valueOf(beatId), beatName, 0, true);
//                        beatList.add(myBeat);
//                        beatsLiveData.postValue(beatList);
//
//                        // STEP 2: Now call Store List API with this ID
//                        fetchStores(token, beatId, myBeat, beatList, beatsLiveData, storesLiveData,totalCountData,totalOrderValue);
//
//                    } else {
//                        storesLiveData.setValue(Resource.error("User has no assigned Beat", null));
//                    }
//                } else {
//                    storesLiveData.setValue(Resource.error("Failed to fetch User Details: " + response.code(), null));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
//                storesLiveData.setValue(Resource.error("Network Error (User API): " + t.getMessage(), null));
//            }
//        });
//    }
//
//    private void fetchStores(String token, int beatId, BeatModel myBeat, List<BeatModel> beatList,
//                             MutableLiveData<List<BeatModel>> beatsLiveData,
//                             MutableLiveData<Resource<List<Store>>> storesLiveData,MutableLiveData<Integer> totalCountData,MutableLiveData<Double> totalOrderValue) {
//
//        apiService.getStoreList(token, beatId,"bit_wise_all").enqueue(new Callback<StoreListResponses>() {
//            @Override
//            public void onResponse(Call<StoreListResponses> call, Response<StoreListResponses> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    if (response.body().isStatus()) {
//                        // Update UI: Beat Count
//                        int count = response.body().getCount();
//                        myBeat.setTotalStores(count);
//                        beatsLiveData.postValue(beatList); // Update dropdown text
//                        totalCountData.postValue(count);
//
//                        // [HIGHLIGHT] Update UI: Order Value (Total Bill)
//                        if (totalOrderValue != null) {
//                            totalOrderValue.postValue(response.body().getTotalBill());
//                        }
//                        // Update UI: Store List
//                        List<Store> list = response.body().getStoreList();
//                        if (list == null) list = new ArrayList<>();
//                        storesLiveData.setValue(Resource.success(list));
//                    } else {
//                        storesLiveData.setValue(Resource.error("No stores found in this beat", null));
//                    }
//                } else {
//                    storesLiveData.setValue(Resource.error("Store API Error: " + response.code(), null));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<StoreListResponses> call, Throwable t) {
//                storesLiveData.setValue(Resource.error("Network Error (Store API): " + t.getMessage(), null));
//            }
//        });
//    }
//
//    // --- 2. FILTERED FETCH ROUTER ---
//    public void fetchFilteredStores(String filterType, MutableLiveData<Resource<List<Store>>> storesLiveData) {
//        storesLiveData.setValue(Resource.loading(null));
//
//        if (!NetworkUtil.isNetworkAvailable(context)) {
//            storesLiveData.setValue(Resource.error("No Internet Connection", null));
//            return;
//        }
//
//        String token = "Bearer " + sessionManager.getToken();
//        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//
//        if (filterType.equals("Visited")) {
//            // SPECIAL CASE: Nested Wrapper
//            fetchVisitedStores(token, date, storesLiveData);
//        } else {
//            // DIRECT LIST: Not Visited OR Order Taken
//            fetchDirectFilteredStores(token, date, filterType, storesLiveData);
//        }
//    }
//
//    // --- [NEW SECTION]: GLOBAL COUNTS FETCH ---
//    // This fetches the specific counts for the top cards without affecting the list
//    public void fetchGlobalCounts(MutableLiveData<Integer> visitedCount, MutableLiveData<Integer> orderCount) {
//        if (!NetworkUtil.isNetworkAvailable(context)) return;
//
//        String token = "Bearer " + sessionManager.getToken();
//        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//
//        // A. Get Visited Count
//        apiService.getStoreListVisited(token, date, "visited_shop").enqueue(new Callback<StoreListWrapperResponse>() {
//            @Override
//            public void onResponse(Call<StoreListWrapperResponse> call, Response<StoreListWrapperResponse> response) {
//                if(response.isSuccessful() && response.body() != null) {
//                    visitedCount.postValue(response.body().getCount());
//                }
//            }
//            @Override
//            public void onFailure(Call<StoreListWrapperResponse> call, Throwable t) {}
//        });
//
//        // B. Get Order Count
//        apiService.getStoreListByOrder(token, date, "order_taken_shop").enqueue(new Callback<StoreListResponses>() {
//            @Override
//            public void onResponse(Call<StoreListResponses> call, Response<StoreListResponses> response) {
//                if(response.isSuccessful() && response.body() != null) {
//                    orderCount.postValue(response.body().getCount());
//                }
//            }
//            @Override
//            public void onFailure(Call<StoreListResponses> call, Throwable t) {}
//        });
//    }
//
//    // A. For "Visited" (Nested Structure)
//    private void fetchVisitedStores(String token, String date, MutableLiveData<Resource<List<Store>>> storesLiveData) {
//        apiService.getStoreListVisited(token, date, "visited_shop").enqueue(new Callback<StoreListWrapperResponse>() {
//            @Override
//            public void onResponse(Call<StoreListWrapperResponse> call, Response<StoreListWrapperResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Store> extractedList = new ArrayList<>();
//                    if (response.body().getWrappers() != null) {
//                        for (StoreListWrapperResponse.StoreWrapper w : response.body().getWrappers()) {
//                            if (w.getStore() != null) {
//                                Store s = w.getStore();
//                                s.setVisited(true); // Manually Flag for Icon
//                                extractedList.add(s);
//                            }
//                        }
//                    }
//                    storesLiveData.setValue(Resource.success(extractedList));
//                } else {
//                    storesLiveData.setValue(Resource.error("Server Error: " + response.code(), null));
//                }
//            }
//            @Override
//            public void onFailure(Call<StoreListWrapperResponse> call, Throwable t) {
//                storesLiveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
//            }
//        });
//    }
//
//    // B. For "Not Visited" & "Order Taken" (Direct Structure)
//    private void fetchDirectFilteredStores(String token, String date, String filterType, MutableLiveData<Resource<List<Store>>> storesLiveData) {
//        Call<StoreListResponses> call;
//
//        if (filterType.equals("Not Visited")) {
//            call = apiService.getStoreListNotVisited(token, date, "not_visited_shop");
//        } else {
//            // Order Taken
//            call = apiService.getStoreListByOrder(token, date, "order_taken_shop");
//        }
//
//        call.enqueue(new Callback<StoreListResponses>() {
//            @Override
//            public void onResponse(Call<StoreListResponses> call, Response<StoreListResponses> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Store> list = response.body().getStoreList();
//                    if (list == null) list = new ArrayList<>();
//
//                    // Manually set OrderTaken flag for Order tab so Icon shows
//                    if(filterType.equals("Order Taken")) {
//                        for(Store s : list) s.setOrderTaken(true);
//                    }
//
//                    storesLiveData.setValue(Resource.success(list));
//                } else {
//                    storesLiveData.setValue(Resource.error("Server Error: " + response.code(), null));
//                }
//            }
//            @Override
//            public void onFailure(Call<StoreListResponses> call, Throwable t) {
//                storesLiveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
//            }
//        });
//    }
}




























//   look above code


//private void fetchStores(String token, int beatId, BeatModel myBeat, List<BeatModel> beatList,
//                         MutableLiveData<List<BeatModel>> beatsLiveData,
//                         MutableLiveData<Resource<List<Store>>> storesLiveData,MutableLiveData<Integer> totalCountData) {
//
//    apiService.getStoreList(token, beatId,true).enqueue(new Callback<StoreListResponses>() {
//        @Override
//        public void onResponse(Call<StoreListResponses> call, Response<StoreListResponses> response) {
//            if (response.isSuccessful() && response.body() != null) {
//                if (response.body().isStatus()) {
//                    // Update UI: Beat Count
//                    int count = response.body().getCount();
//                    myBeat.setTotalStores(count);
//                    beatsLiveData.postValue(beatList); // Update dropdown text
//                    totalCountData.postValue(count);
//                    // Update UI: Store List
//                    List<Store> list = response.body().getStoreList();
//                    if (list == null) list = new ArrayList<>();
//                    storesLiveData.setValue(Resource.success(list));
//                } else {
//                    storesLiveData.setValue(Resource.error("No stores found in this beat", null));
//                }
//            } else {
//                storesLiveData.setValue(Resource.error("Store API Error: " + response.code(), null));
//            }
//        }
//
//        @Override
//        public void onFailure(Call<StoreListResponses> call, Throwable t) {
//            storesLiveData.setValue(Resource.error("Network Error (Store API): " + t.getMessage(), null));
//        }
//    });
//}
//
//// --- 2. FILTERED FETCH ROUTER ---
//public void fetchFilteredStores(String filterType, MutableLiveData<Resource<List<Store>>> storesLiveData) {
//    storesLiveData.setValue(Resource.loading(null));
//
//    if (!NetworkUtil.isNetworkAvailable(context)) {
//        storesLiveData.setValue(Resource.error("No Internet Connection", null));
//        return;
//    }
//
//    String token = "Bearer " + sessionManager.getToken();
//    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//
//    if (filterType.equals("Visited")) {
//        // SPECIAL CASE: Nested Wrapper
//        fetchVisitedStores(token, date, storesLiveData);
//    } else {
//        // DIRECT LIST: Not Visited OR Order Taken
//        fetchDirectFilteredStores(token, date, filterType, storesLiveData);
//    }
//}
//
//// --- [NEW SECTION]: GLOBAL COUNTS FETCH ---
//// This fetches the specific counts for the top cards without affecting the list
//public void fetchGlobalCounts(MutableLiveData<Integer> visitedCount, MutableLiveData<Integer> orderCount) {
//    if (!NetworkUtil.isNetworkAvailable(context)) return;
//
//    String token = "Bearer " + sessionManager.getToken();
//    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//
//    // A. Get Visited Count
//    apiService.getStoreListVisited(token, date, "yes", true).enqueue(new Callback<StoreListWrapperResponse>() {
//        @Override
//        public void onResponse(Call<StoreListWrapperResponse> call, Response<StoreListWrapperResponse> response) {
//            if(response.isSuccessful() && response.body() != null) {
//                visitedCount.postValue(response.body().getCount());
//            }
//        }
//        @Override
//        public void onFailure(Call<StoreListWrapperResponse> call, Throwable t) {}
//    });
//
//    // B. Get Order Count
//    apiService.getStoreListByOrder(token, date, "yes", true).enqueue(new Callback<StoreListResponses>() {
//        @Override
//        public void onResponse(Call<StoreListResponses> call, Response<StoreListResponses> response) {
//            if(response.isSuccessful() && response.body() != null) {
//                orderCount.postValue(response.body().getCount());
//            }
//        }
//        @Override
//        public void onFailure(Call<StoreListResponses> call, Throwable t) {}
//    });
//}
//
//// A. For "Visited" (Nested Structure)
//private void fetchVisitedStores(String token, String date, MutableLiveData<Resource<List<Store>>> storesLiveData) {
//    apiService.getStoreListVisited(token, date, "yes", true).enqueue(new Callback<StoreListWrapperResponse>() {
//        @Override
//        public void onResponse(Call<StoreListWrapperResponse> call, Response<StoreListWrapperResponse> response) {
//            if (response.isSuccessful() && response.body() != null) {
//                List<Store> extractedList = new ArrayList<>();
//                if (response.body().getWrappers() != null) {
//                    for (StoreListWrapperResponse.StoreWrapper w : response.body().getWrappers()) {
//                        if (w.getStore() != null) {
//                            Store s = w.getStore();
//                            s.setVisited(true); // Manually Flag for Icon
//                            extractedList.add(s);
//                        }
//                    }
//                }
//                storesLiveData.setValue(Resource.success(extractedList));
//            } else {
//                storesLiveData.setValue(Resource.error("Server Error: " + response.code(), null));
//            }
//        }
//        @Override
//        public void onFailure(Call<StoreListWrapperResponse> call, Throwable t) {
//            storesLiveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
//        }
//    });
//}
//
//// B. For "Not Visited" & "Order Taken" (Direct Structure)
//private void fetchDirectFilteredStores(String token, String date, String filterType, MutableLiveData<Resource<List<Store>>> storesLiveData) {
//    Call<StoreListResponses> call;
//
//    if (filterType.equals("Not Visited")) {
//        call = apiService.getStoreListNotVisited(token, date, "no", true);
//    } else {
//        // Order Taken
//        call = apiService.getStoreListByOrder(token, date, "yes", true);
//    }
//
//    call.enqueue(new Callback<StoreListResponses>() {
//        @Override
//        public void onResponse(Call<StoreListResponses> call, Response<StoreListResponses> response) {
//            if (response.isSuccessful() && response.body() != null) {
//                List<Store> list = response.body().getStoreList();
//                if (list == null) list = new ArrayList<>();
//
//                // Manually set OrderTaken flag for Order tab so Icon shows
//                if(filterType.equals("Order Taken")) {
//                    for(Store s : list) s.setOrderTaken(true);
//                }
//
//                storesLiveData.setValue(Resource.success(list));
//            } else {
//                storesLiveData.setValue(Resource.error("Server Error: " + response.code(), null));
//            }
//        }
//        @Override
//        public void onFailure(Call<StoreListResponses> call, Throwable t) {
//            storesLiveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
//        }
//    });
//}













//    private static final String TAG = "MyBeatRepo";
//    private ApiService apiService;
//    private SessionManager sessionManager;
//    private Context context;
//
//    public MyBeatRepository(Context context) {
//        this.context = context;
//        this.apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
//        this.sessionManager = new SessionManager(context);
//    }
//
//    public void fetchDashboardData(MutableLiveData<Resource<List<Store>>> storesLiveData,
//                                   MutableLiveData<List<BeatModel>> beatsLiveData) {
//
//        // 1. Loading State
//        storesLiveData.setValue(Resource.loading(null));
//        Log.d(TAG, "fetchDashboardData: Started loading...");
//
//        // 2. Network Check
//        if (!NetworkUtil.isNetworkAvailable(context)) {
//            Log.e(TAG, "fetchDashboardData: No Internet Connection");
//            storesLiveData.setValue(Resource.error("No Internet Connection", null));
//            return;
//        }
//
//        String token = "Bearer " + sessionManager.getToken();
//        Log.d(TAG, "fetchDashboardData: Token retrieved (Masked): " + (token.length() > 20 ? token.substring(0, 20) + "..." : token));
//
//        // 3. GET DYNAMIC BEAT INFO FROM SESSION
//        Log.d(TAG, "fetchDashboardData: Attempting to retrieve Beat info from SessionManager...");
//
//        int beatId = sessionManager.getBeatId();
//        String beatName = sessionManager.getBeatName();
//
//        // LOGGING THE RETRIEVED VALUES
//        Log.d(TAG, "fetchDashboardData: [SESSION DATA] Beat ID = " + beatId);
//        Log.d(TAG, "fetchDashboardData: [SESSION DATA] Beat Name = " + beatName);
//
//        // Strict Check: If Beat ID is 0, it means no beat assigned or parsing failed.
//        if (beatId == 0) {
//            Log.e(TAG, "fetchDashboardData: ERROR - Beat ID is 0. Aborting API call.");
//            storesLiveData.setValue(Resource.error("No Assigned Beat Found for User", null));
//            return;
//        }
//
//        // Handle empty name case
//        if (beatName == null || beatName.isEmpty() || beatName.equals("My Beat")) {
//            beatName = "Beat " + beatId;
//            Log.w(TAG, "fetchDashboardData: Beat Name was empty/default, changed to: " + beatName);
//        }
//
//        // 4. Setup Beat Dropdown
//        List<BeatModel> beatList = new ArrayList<>();
//        BeatModel myBeat = new BeatModel(String.valueOf(beatId), beatName, 0, true);
//        beatList.add(myBeat);
//        beatsLiveData.setValue(beatList);
//        Log.d(TAG, "fetchDashboardData: Beat Dropdown List set with: " + beatName);
//
//        // 5. API Call using StoreListResponses
//        Log.d(TAG, "fetchDashboardData: Calling API getStoreList with Beat ID: " + beatId);
//
//        apiService.getStoreList(token, beatId).enqueue(new Callback<StoreListResponses>() {
//            @Override
//            public void onResponse(Call<StoreListResponses> call, Response<StoreListResponses> response) {
//                Log.d(TAG, "API Response Code: " + response.code());
//
//                if (response.isSuccessful() && response.body() != null) {
//                    Log.d(TAG, "API Status: " + response.body().isStatus());
//                    Log.d(TAG, "API Count: " + response.body().getCount());
//
//                    if (response.body().isStatus()) {
//
//                        // Update Beat Count
//                        myBeat.setTotalStores(response.body().getCount());
//                        beatsLiveData.setValue(beatList);
//                        Log.d(TAG, "Beat Count Updated to: " + response.body().getCount());
//
//                        // DIRECT PASS
//                        List<Store> fetchedStores = response.body().getStoreList();
//                        if (fetchedStores != null) {
//                            Log.d(TAG, "API Success: Fetched " + fetchedStores.size() + " stores.");
//                            storesLiveData.setValue(Resource.success(fetchedStores));
//                        } else {
//                            Log.w(TAG, "API Success but Store List is NULL");
//                            storesLiveData.setValue(Resource.success(new ArrayList<>())); // Return empty list instead of null
//                        }
//
//                    } else {
//                        Log.e(TAG, "API Logical Error: Status is false");
//                        storesLiveData.setValue(Resource.error("No stores found", null));
//                    }
//                } else {
//                    Log.e(TAG, "API Server Error: " + response.message());
//                    storesLiveData.setValue(Resource.error("Server Error: " + response.code(), null));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<StoreListResponses> call, Throwable t) {
//                Log.e(TAG, "API Network Failure: " + t.getMessage());
//                storesLiveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
//            }
//        });
//    }
//}




//code 1st no api
    // Mock Data Generation
//    public void fetchInitialData(MutableLiveData<List<BeatModel>> beats, MutableLiveData<List<Store>> stores) {
//
//        // 1. Create Beats
//        List<BeatModel> beatList = new ArrayList<>();
//        beatList.add(new BeatModel("B1", "Waluj Pandharpur (HULI)", 16, true));
//        beatList.add(new BeatModel("B2", "PAITHAN (HULI)", 24, true)); // 16+24 = 40 total
//        beats.setValue(beatList);
//
//        // 2. Create Stores
//        List<Store> storeList = new ArrayList<>();
//
//        // Stores for Beat 1 (Waluj)
//        for (int i = 1; i <= 16; i++) {
//            boolean visited = i % 3 == 0;
//            boolean orderTaken = i % 6 == 0;
//            double value = orderTaken ? (i * 100) : 0;
//
//            // Proper Address Logic for Waluj
//            String address = "Plot No " + (10 + i) + ", Bajaj Nagar, Waluj, Aurangabad";
//            String owner = "Owner Name " + i;
//            String hulCode = "HUL-W" + (1000 + i);
//
//            storeList.add(new Store("S1_" + i, "Shrikrishna Dairy " + i, "B1", visited, orderTaken, value,
//                    "1234567890", 19.8, 75.3, address, owner, hulCode));
//        }
//
//        // Beat 2: Paithan Stores
//        for (int i = 1; i <= 24; i++) {
//            boolean visited = i % 2 == 0;
//            boolean orderTaken = i % 4 == 0;
//            double value = orderTaken ? (i * 150) : 0;
//
//            // Proper Address Logic for Paithan
//            String address = "Shop No " + i + ", Main Market Road, Paithan Gate, Aurangabad";
//            String owner = "Owner Name " + (16 + i);
//            String hulCode = "HUL-P" + (2000 + i);
//
//            storeList.add(new Store("S2_" + i, "Kailash Restaurant " + i, "B2", visited, orderTaken, value,
//                    "0987654321", 19.5, 75.4, address, owner, hulCode));
//        }
//
//        stores.setValue(storeList);
//    }
//}
