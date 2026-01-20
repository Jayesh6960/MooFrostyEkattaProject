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
