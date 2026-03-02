package com.example.moofrosty.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.CartAction;
import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.CartItem;
import com.example.moofrosty.data.model.CartTotals;
import com.example.moofrosty.data.model.CheckoutRequest;
import com.example.moofrosty.data.model.GeneralResponse;
import com.example.moofrosty.data.model.Order;
import com.example.moofrosty.data.model.OrderHistoryResponse;
import com.example.moofrosty.data.model.Product;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {

    private static final String TAG = "CartRepository";
    private static CartRepository instance;
    private ApiService apiService;

    // Data Holders
    private final MutableLiveData<Map<String, CartItem>> _cartMap = new MutableLiveData<>(new HashMap<>());
    private final MutableLiveData<CartTotals> _cartTotals = new MutableLiveData<>(new CartTotals());

    private CartRepository() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public static synchronized CartRepository getInstance() {
        if (instance == null) instance = new CartRepository();
        return instance;
    }

    public LiveData<Map<String, CartItem>> getCartMap() {
        return _cartMap;
    }

    public LiveData<CartTotals> getCartTotals() {
        return _cartTotals;
    }

    // --- CHECKOUT API ---
    public void checkout(String token, int userId, int shopId, MutableLiveData<Resource<String>> result) {
        result.setValue(Resource.loading(null));

        Map<String, CartItem> currentCart = _cartMap.getValue();
        if (currentCart == null || currentCart.isEmpty()) {
            result.setValue(Resource.error("Cart is empty", null));
            return;
        }

        List<CheckoutRequest.CheckoutItem> apiItems = new ArrayList<>();

        for (CartItem item : currentCart.values()) {
            try {
                int pId = Integer.parseInt(item.getProduct().getId());
                int qty;
                // LOGIC: If case product, send number of cases? Or total units?
                // Usually API expects Total Units if type isn't specified,
                // but if your API expects specific 'case_qty', we send getCaseQuantity().
                // Assuming based on previous code you want TOTAL UNITS in the order:
            //    int qty = item.getTotalUnits();
                if ("case".equalsIgnoreCase(item.getProduct().productType)) {
                    // If Product Type is "case", send the number of CASES
//                    qty = item.getCaseQuantity();
                    qty = item.getTotalUnits();
                } else {
                    // If Product Type is "unit" (or others), send the TOTAL STOCK UNITS
                    // (This covers unitQuantity + any converted cases if applicable)
                    qty = item.getTotalUnits();
                }

                apiItems.add(new CheckoutRequest.CheckoutItem(pId, qty));
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing Product ID: " + item.getProduct().getId());
            }
        }

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        CheckoutRequest request = new CheckoutRequest(userId, shopId, date, apiItems);

        Log.d(TAG, "----- CHECKOUT REQUEST START -----");
        Log.d(TAG, "Token: Bearer " + token);
        Log.d(TAG, "UserId: " + userId);
        Log.d(TAG, "ShopId: " + shopId);
        Log.d(TAG, "Order Date: " + date);

// Log each item
//        for (CheckoutRequest.CheckoutItem item : apiItems) {
//            Log.d(TAG, "Item -> product_id: " + item.getProductId()
//                    + ", quantity: " + item.getQuantity());
//        }

// Log full JSON (🔥 very useful)
        Log.d(TAG, "CheckoutRequest JSON: " + new Gson().toJson(request));

        apiService.checkoutCart("Bearer " + token, request).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body() != null) {
                    Log.d(TAG, "Checkout API Response Body: " + new Gson().toJson(response.body()));
                } else {
                    Log.e(TAG, "Checkout API Response Body is NULL");
                }
                if (response.isSuccessful() && response.body() != null) {
                    if ("success".equalsIgnoreCase(response.body().getStatus())) {
                        clearCart();
                        result.setValue(Resource.success(response.body().getMessage()));
                    } else {
                        result.setValue(Resource.error(response.body().getMessage(), null));
                    }
                } else {
                    String errorMsg = "Server Error: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            String errorStr = response.errorBody().string();
                            Log.e(TAG, "Error Body: " + errorStr);

                            // Try to parse the JSON error body to get the real message
                            GeneralResponse errorResponse = new Gson().fromJson(errorStr, GeneralResponse.class);
                            if (errorResponse != null && errorResponse.getMessage() != null) {
                                errorMsg = errorResponse.getMessage();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    result.setValue(Resource.error(errorMsg, null));
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Log.e(TAG, "Checkout API FAILED", t);
                result.setValue(Resource.error("Network Error: " + t.getMessage(), null));
            }
        });
    }

    public void clearCart() {
        _cartMap.setValue(new HashMap<>());
        _cartTotals.setValue(new CartTotals());
    }

    // --- MODIFICATION LOGIC WITH STOCK CHECK ---

    public void addToCart(Product product) {
        // Default behavior: if type is case, add 1 case. If unit, add 1 unit.
        // But this is usually handled by the button click listener calling increment.
        // Here we just initialize.
        if ("case".equalsIgnoreCase(product.productType)) {
            incrementCase(product);
        } else {
            incrementUnit(product);
        }
    }

    public void incrementUnit(Product p) {
        modify(p, CartAction.INC_UNIT);
    }

    public void decrementUnit(Product p) {
        modify(p, CartAction.DEC_UNIT);
    }

    public void incrementCase(Product p) {
        modify(p, CartAction.INC_CASE);
    }

    public void decrementCase(Product p) {
        modify(p, CartAction.DEC_CASE);
    }

    public void removeFromCart(Product p) {
        modify(p, CartAction.REMOVE);
    }

    private void modify(Product product, CartAction action) {
//        Map<String, CartItem> map = getSafeMap();
        Map<String, CartItem> map = new HashMap<>(getSafeMap());       // this means give extra hasmap for list handle upside is single
        CartItem item = map.get(product.getId());

        if (item == null) {
            if (action == CartAction.INC_UNIT || action == CartAction.INC_CASE) {
                item = new CartItem(product);
                map.put(product.getId(), item);
            } else {
                return; // Can't decrease something that doesn't exist
            }
        }

        // Get Stock Limits   for stock limit need then this need
//        int stock = product.getStockInt();
//        int currentTotal = item.getTotalUnits();

        int caseSize = product.caseSize > 0 ? product.caseSize : 1;

        switch (action) {
            case INC_UNIT:
                // Only allow if we have enough stock for 1 more unit
//                if (currentTotal + 1 <= stock) {
//                    item.incrementUnit();
//                }
                item.incrementUnit();
                break;

            case DEC_UNIT:
                item.decrementUnit();
                break;

            case INC_CASE:
                // Only allow if we have enough stock for 1 more case
//                if (currentTotal + caseSize <= stock) {
//                    item.incrementCase();
//                }
                item.incrementCase();
                break;

            case DEC_CASE:
                item.decrementCase();
                break;

            case REMOVE:
                map.remove(product.getId());
                break;
        }

        // Clean up if empty (unless we just created it and failed to increment due to stock)
        if (map.containsKey(product.getId()) && item.getTotalUnits() == 0) {
            map.remove(product.getId());
        }

        updateLiveData(map);
    }

    private Map<String, CartItem> getSafeMap() {
        Map<String, CartItem> map = _cartMap.getValue();
        return map == null ? new HashMap<>() : map;
    }

    private void updateLiveData(Map<String, CartItem> newCartMap) {
        _cartMap.setValue(newCartMap);
        recalculateTotals(newCartMap);
    }

    private void recalculateTotals(Map<String, CartItem> cartMap) {
        double totalMrp = 0.0;
        double totalRate = 0.0;
        int uniqueItemCount = 0;
        int totalUnitCount = 0;

        for (CartItem item : cartMap.values()) {
            int units = item.getTotalUnits();

            if (units > 0) {
                // Get precise totals from item
                totalMrp += item.getTotalMrp();
                totalRate += item.getTotalPrice();
                totalUnitCount += units;
                uniqueItemCount++;
            }
        }

        double totalSavings = totalMrp - totalRate;

        // Round to 3 decimals to avoid floating point errors like 10.33300000001
        totalRate = Math.round(totalRate * 1000.0) / 1000.0;
        totalSavings = Math.round(totalSavings * 1000.0) / 1000.0;
        totalMrp = Math.round(totalMrp * 1000.0) / 1000.0;

        _cartTotals.setValue(
                new CartTotals(
                        totalRate,
                        totalSavings,
                        totalMrp,
                        uniqueItemCount,
                        totalUnitCount
                )
        );
    }

    // order list in order frag that code here

    // --- FETCH ORDER HISTORY ---
//
//    public void getOrderHistory(String token, int shopId, MutableLiveData<Resource<OrderHistoryResponse>> liveData) {
//        liveData.setValue(Resource.loading(null));
//
//        String authToken = token.startsWith("Bearer ") ? token : "Bearer " + token;
//
//        Log.d(TAG, "Fetching Orders with Token: " + authToken + " and ShopId: " + shopId);
//
//        // [HIGHLIGHT] Pass shopId to apiService
//        apiService.getOrderHistory(authToken, shopId).enqueue(new Callback<OrderHistoryResponse>() {
//            @Override
//            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    OrderHistoryResponse resp = response.body();
//                    liveData.setValue(Resource.success(resp));
//                } else {
//                    liveData.setValue(Resource.error("Error: " + response.code(), null));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
//                liveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
//            }
//        });
//    }

    public void getOrderHistory(String token, int shopId, MutableLiveData<Resource<OrderHistoryResponse>> liveData) {
        liveData.setValue(Resource.loading(null));
        String authToken = token.startsWith("Bearer ") ? token : "Bearer " + token;
        Log.d("orderdetails", "Fetching Orders with Token: " + authToken + " and ShopId: " + shopId);
        apiService.getOrderHistory(authToken, shopId).enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                // [HIGHLIGHT] 1. Log the EXACT URL that Retrofit generated and called
                Log.d("orderdetails", "API URL Called: " + call.request().url().toString());
                // [HIGHL"orderdetails"G, "API Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    OrderHistoryResponse resp = response.body();
                    // [HIGHLIGHT] 3. Log success details
                    Log.d("orderdetails", "API Success! Status: " + resp.status);
                    if (resp.data != null) {
                        Log.d("orderdetails", "Order List Size: " + resp.data.size());
                    } else {
                        Log.e("orderdetails", "WARNING: API returned success, but 'data' array is NULL!");
                    }
                    liveData.setValue(Resource.success(resp));
                } else {
                    // [HIGHLIGHT] 4. Log the EXACT ERROR message returned by the server
                    String errorMsg = "Error: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            String rawErrorBody = response.errorBody().string();
                            Log.e("orderdetails", "Server Error Body: " + rawErrorBody);
                            errorMsg += " -> " + rawErrorBody;
                        } else {
                            Log.e("orderdetails", "Server Error Body is NULL");
                        }
                    } catch (Exception e) {
                        Log.e("orderdetails", "Failed to parse error body", e);
                    }

                    liveData.setValue(Resource.error(errorMsg, null));
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                // [HIGHLIGHT] 5. Log the EXACT reason it failed (e.g., Timeout, No Internet, JSON Parsing Crash)
                Log.e("orderdetails", "API Call FAILED completely! URL: " + call.request().url().toString(), t);
                Log.e("orderdetails", "Failure Message: " + t.getMessage());

                liveData.setValue(Resource.error("Network/Parsing Error: " + t.getMessage(), null));
            }
        });
    }
//    public void getOrderHistory(String token, MutableLiveData<Resource<OrderHistoryResponse>> liveData) {
//        liveData.setValue(Resource.loading(null));
//
//        String authToken = token.startsWith("Bearer ") ? token : "Bearer " + token;
//
//        Log.d(TAG, "Fetching Orders with Token: " + authToken); // LOG 1
//
//        apiService.getOrderHistory(authToken).enqueue(new Callback<OrderHistoryResponse>() {
//            @Override
//            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    OrderHistoryResponse resp = response.body();
//
//                    // LOG 2: Check data size
//                    if(resp.data != null) {
//                        Log.d(TAG, "Order List Size: " + resp.data.size());
//                    } else {
//                        Log.e(TAG, "Order List is NULL");
//                    }
//                    liveData.setValue(Resource.success(resp));
//                } else {
//                    Log.e(TAG, "API Error Code: " + response.code());
//                    liveData.setValue(Resource.error("Error: " + response.code(), null));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
//                Log.e(TAG, "Network Failure: " + t.getMessage());
//                liveData.setValue(Resource.error("Network Error: " + t.getMessage(), null));
//            }
//        });
//    }


}
  //// woking code
////     private void recalculateTotals(Map<String, CartItem> cartMap) {
////        double totalMrp = 0, totalRate = 0;
////        int uniqueItemCount = 0, totalUnitCount = 0;
////
////        for (CartItem item : cartMap.values()) {
////            if (item.getQuantity() > 0) {
////                totalMrp += item.getTotalMrp();
////                totalRate += item.getTotalPrice();
////                totalUnitCount += item.getQuantity();
////                uniqueItemCount++;
////            }
////        }
////        double totalSavings = totalMrp - totalRate;
////        _cartTotals.setValue(new CartTotals(totalRate, totalSavings, totalMrp, uniqueItemCount, totalUnitCount));
////    }
////}    //// woking code

//    private static CartRepository instance;
//    private ApiService apiService;
//
//    private final MutableLiveData<Map<String, CartItem>> _cartMap = new MutableLiveData<>(new HashMap<>());
//        // api
//
//
//    public LiveData<Map<String, CartItem>> getCartMap() {
//        return _cartMap;
//    }
//
//    private CartRepository() {
//        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
//    }
//
//    public static synchronized CartRepository getInstance() {
//        if (instance == null) {
//            instance = new CartRepository();
//        }
//        return instance;
//    }
//    // --- FIX: Use the new independent CartTotals class ---
//    private final MutableLiveData<CartTotals> _cartTotals = new MutableLiveData<>(new CartTotals());
//
//    public LiveData<CartTotals> getCartTotals() {
//        return _cartTotals;
//    }
//
//    private final MutableLiveData<List<Order>> _ordersList = new MutableLiveData<>(new ArrayList<>());
//
//    public LiveData<List<Order>> getOrders() {
//        return _ordersList;
//    }
//
////    public void checkout() {
////        Map<String, CartItem> currentCart = _cartMap.getValue();
////        CartTotals currentTotals = _cartTotals.getValue();
////
////        if (currentCart == null || currentCart.isEmpty() || currentTotals == null) {
////            return;
////        }
////
////        List<CartItem> items = new ArrayList<>(currentCart.values());
////        String newOrderId = "20SMN00002P3387" + System.currentTimeMillis();
////        Order newOrder = new Order(newOrderId, currentTotals.totalPrice, currentTotals.totalUnitCount, "Billed", items);
////
////        List<Order> currentOrders = _ordersList.getValue();
////        if (currentOrders == null) {
////            currentOrders = new ArrayList<>();
////        }
////        currentOrders.add(0, newOrder);
////        _ordersList.setValue(currentOrders);
////
////        // --- RESET THE CART ---
////        _cartMap.setValue(new HashMap<>());
////        _cartTotals.setValue(new CartTotals());
////    }
//
//    // --- API: Checkout ---
//    public void checkout(String token, int userId, int shopId, MutableLiveData<Resource<String>> result) {
//        result.setValue(Resource.loading(null));
//
//        Map<String, CartItem> currentCart = _cartMap.getValue();
//        if (currentCart == null || currentCart.isEmpty()) {
//            result.setValue(Resource.error("Cart is empty", null));
//            return;
//        }
//
//        // Prepare Request
//        List<CheckoutRequest.CheckoutItem> apiItems = new ArrayList<>();
//        for (CartItem item : currentCart.values()) {
//            // Assuming quantity represents total units, you might need conversion if API expects strict cases
//            apiItems.add(new CheckoutRequest.CheckoutItem(
//                    Integer.parseInt(item.getProduct().getId()), // Ensure ID is int parsable
//                    item.getQuantity() // or item.getCaseQuantity() depending on business logic
//            ));
//        }
//
//        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//        CheckoutRequest request = new CheckoutRequest(userId, shopId, date, apiItems);
//
//        apiService.checkoutCart("Bearer " + token, request).enqueue(new Callback<GeneralResponse>() {
//            @Override
//            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    if ("success".equalsIgnoreCase(response.body().getStatus())) {
//                        clearCart();
//                        result.setValue(Resource.success(response.body().getMessage()));
//                    } else {
//                        result.setValue(Resource.error(response.body().getMessage(), null));
//                    }
//                } else {
//                    result.setValue(Resource.error("Server Error: " + response.code(), null));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GeneralResponse> call, Throwable t) {
//                result.setValue(Resource.error("Network Error: " + t.getMessage(), null));
//            }
//        });
//    }
//
//    public void clearCart() {
//        _cartMap.setValue(new HashMap<>());
//        _cartTotals.setValue(new CartTotals());
//    }
//
//    public Order getOrderById(String orderId) {
//        List<Order> currentOrders = _ordersList.getValue();
//        if (currentOrders == null || orderId == null) {
//            return null;
//        }
//        for (Order order : currentOrders) {
//            if (orderId.equals(order.getId())) {
//                return order;
//            }
//        }
//        return null;
//    }
//
//
//
//    public void addToCart(Product product) {
//        Map<String, CartItem> currentCart = _cartMap.getValue();
//        if (currentCart == null) currentCart = new HashMap<>();
//
//        CartItem cartItem = currentCart.get(product.getId());
//        if (cartItem == null) {
//            cartItem = new CartItem(product, 1);
//        } else {
//            cartItem.incrementUnit();
//        }
//        currentCart.put(product.getId(), cartItem);
//        updateLiveData(currentCart);
//    }
//
//    // --- FIX: Use the new independent CartAction enum ---
//    public void incrementUnit(Product product) {
//        modifyQuantity(product, CartAction.INC_UNIT);
//    }
//    public void decrementUnit(Product product) {
//        modifyQuantity(product, CartAction.DEC_UNIT);
//    }
//    public void incrementCase(Product product) {
//        modifyQuantity(product, CartAction.INC_CASE);
//    }
//    public void decrementCase(Product product) {
//        modifyQuantity(product, CartAction.DEC_CASE);
//    }
//    public void removeFromCart(Product product) {
//        modifyQuantity(product, CartAction.REMOVE);
//    }
//
//    // --- FIX: Use the new independent CartAction enum ---
//    private void modifyQuantity(Product product, CartAction action) {
//        Map<String, CartItem> currentCart = _cartMap.getValue();
//        if (currentCart == null) currentCart = new HashMap<>();
//
//        CartItem cartItem = currentCart.get(product.getId());
//        if (cartItem == null) {
//            if (action == CartAction.INC_UNIT || action == CartAction.INC_CASE) {
//                cartItem = new CartItem(product, 0);
//                currentCart.put(product.getId(), cartItem);
//            } else {
//                return;
//            }
//        }
//
//        switch (action) {
//            case INC_UNIT: cartItem.incrementUnit(); break;
//            case DEC_UNIT: cartItem.decrementUnit(); break;
//            case INC_CASE: cartItem.incrementCase(); break;
//            case DEC_CASE: cartItem.decrementCase(); break;
//            case REMOVE: cartItem = new CartItem(product, 0); break;
//        }
//
//        if (cartItem.getQuantity() == 0) {
//            currentCart.remove(product.getId());
//        } else {
//            currentCart.put(product.getId(), cartItem);
//        }
//
//        updateLiveData(currentCart);
//    }
//
//    private Map<String, CartItem> getSafeMap() {
//        Map<String, CartItem> map = _cartMap.getValue();
//        return map == null ? new HashMap<>() : map;
//    }
//
//    private void updateLiveData(Map<String, CartItem> newCartMap) {
//        _cartMap.setValue(newCartMap);
//        recalculateTotals(newCartMap);
//    }
//
//    // --- FIX: Use the new independent CartTotals class ---
//    private void recalculateTotals(Map<String, CartItem> cartMap) {
//        double totalMrp = 0, totalRate = 0;
//        int uniqueItemCount = 0, totalUnitCount = 0;
//
//        for (CartItem item : cartMap.values()) {
//            if (item.getQuantity() > 0) {
//                totalMrp += item.getTotalMrp();
//                totalRate += item.getTotalPrice();
//                totalUnitCount += item.getQuantity();
//                uniqueItemCount++;
//            }
//        }
//
//        double totalSavings = totalMrp - totalRate;
//        _cartTotals.setValue(new CartTotals(totalRate, totalSavings, totalMrp, uniqueItemCount, totalUnitCount));
//    }
//}
