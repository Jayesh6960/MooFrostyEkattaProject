package com.example.moofrosty.ui.enterstoreorders.takeorder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.CategoryResponse;
import com.example.moofrosty.data.model.SubCategoryResponse;
import com.example.moofrosty.data.repository.TakeOrderRepository;

public class CategoryViewModel extends AndroidViewModel {

    private TakeOrderRepository repository;
    private MutableLiveData<Resource<CategoryResponse>> categories = new MutableLiveData<>();
    private MutableLiveData<Resource<SubCategoryResponse>> subCategories = new MutableLiveData<>();

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository = new TakeOrderRepository(application);
    }

    public LiveData<Resource<CategoryResponse>> getCategories() { return categories; }
    public LiveData<Resource<SubCategoryResponse>> getSubCategories() { return subCategories; }

    public void fetchCategories(String token) {
        repository.getCategories(token, categories);
    }

    public void fetchSubCategories(String token, int catId) {
        repository.getSubCategories(token, catId, subCategories);
    }
}
