package com.example.moofrosty;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FilterViewModel extends ViewModel {
    public final MutableLiveData<String> selectedCategory = new MutableLiveData<>("All");
    public final MutableLiveData<String> selectedBrand = new MutableLiveData<>("All");

    public void setCategory(String category) {
        selectedCategory.setValue(category);
    }

    public void setBrand(String brand) {
        selectedBrand.setValue(brand);
    }
}
