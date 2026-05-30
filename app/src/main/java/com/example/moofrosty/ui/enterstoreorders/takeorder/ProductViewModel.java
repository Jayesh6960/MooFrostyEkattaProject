package com.example.moofrosty.ui.enterstoreorders.takeorder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.ProductResponse;
import com.example.moofrosty.data.repository.TakeOrderRepository;

public class ProductViewModel extends AndroidViewModel {

    private TakeOrderRepository repository;
    private MutableLiveData<Resource<ProductResponse>> products = new MutableLiveData<>();

    public ProductViewModel(@NonNull Application application) {
        super(application);
        repository = new TakeOrderRepository(application);
    }

    public LiveData<Resource<ProductResponse>> getProducts() {
        return products;
    }

    public void loadProducts(String token) {
        repository.getProducts(token, products);
    }


//    private TakeOrderRepository repository;
//    private MutableLiveData<ProductResponse> products;
//    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
//    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
//
//    public ProductViewModel(@NonNull Application application) {
//        super(application);
//        // Pass application context to repo
//        repository = new TakeOrderRepository(application);
//    }
//
//    public LiveData<ProductResponse> getProducts() {
//        if (products == null) {
//            products = new MutableLiveData<>();
//        }
//        return products;
//    }
//
//    public void loadProducts(String token) {
//        isLoading.setValue(true);
//        repository.getProducts(token).observeForever(response -> {
//            isLoading.setValue(false);
//            if (response != null && response.data != null) {
//                products.setValue(response);
//            } else {
//                errorMessage.setValue("Failed to load products or Session Expired");
//            }
//        });
//    }
//
//    public LiveData<Boolean> getIsLoading() { return isLoading; }
//    public LiveData<String> getErrorMessage() { return errorMessage; }

}
//package com.example.moofrosty.ui.enterstoreorders.takeorder;
//
//import android.app.Application;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import com.example.moofrosty.core.network.Resource;
//import com.example.moofrosty.data.model.ProductResponse;
//import com.example.moofrosty.data.repository.TakeOrderRepository;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ProductViewModel extends AndroidViewModel {
//
//    private TakeOrderRepository repository;
//
//    // =========================
//    // API PRODUCTS
//    // =========================
//    private MutableLiveData<Resource<ProductResponse>> products = new MutableLiveData<>();
//
//    // =========================
//    // USER QUANTITY STORAGE (MVVM STATE)
//    // productId -> quantity
//    // =========================
//    private final MutableLiveData<Map<String, Integer>> quantityMap =
//            new MutableLiveData<>(new HashMap<>());
//
//    public ProductViewModel(@NonNull Application application) {
//        super(application);
//        repository = new TakeOrderRepository(application);
//    }
//
//    // =========================
//    // API SECTION (UNCHANGED)
//    // =========================
//    public LiveData<Resource<ProductResponse>> getProducts() {
//        return products;
//    }
//
//    public void loadProducts(String token) {
//        repository.getProducts(token, products);
//    }
//
//    // =========================
//    // QUANTITY LIVE DATA
//    // =========================
//    public LiveData<Map<String, Integer>> getQuantityMap() {
//        return quantityMap;
//    }
//
//    // =========================
//    // SET MANUAL USER VALUE
//    // =========================
//    public void setQuantity(String productId, int value) {
//
//        Map<String, Integer> map = quantityMap.getValue();
//        if (map == null) map = new HashMap<>();
//
//        if (value < 1) value = 1;
//
//        map.put(productId, value);
//
//        quantityMap.setValue(map);
//    }
//
//    // =========================
//    // INCREMENT (+)
//    // =========================
//    public void increment(String productId) {
//
//        Map<String, Integer> map = quantityMap.getValue();
//        if (map == null) map = new HashMap<>();
//
//        int current = map.containsKey(productId) ? map.get(productId) : 1;
//
//        map.put(productId, current + 1);
//
//        quantityMap.setValue(map);
//    }
//
//    // =========================
//    // DECREMENT (-)
//    // =========================
//    public void decrement(String productId) {
//
//        Map<String, Integer> map = quantityMap.getValue();
//        if (map == null) map = new HashMap<>();
//
//        int current = map.containsKey(productId) ? map.get(productId) : 1;
//
//        if (current > 1) {
//            map.put(productId, current - 1);
//        }
//
//        quantityMap.setValue(map);
//    }
//}
