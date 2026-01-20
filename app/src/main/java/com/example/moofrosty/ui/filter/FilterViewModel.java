package com.example.moofrosty.ui.filter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FilterViewModel extends ViewModel {
    public final MutableLiveData<String> selectedCategory = new MutableLiveData<>("All");

    // Stores the Sub-Category (e.g., "Cones") - NEW FIELD
    public final MutableLiveData<String> selectedSubCategory = new MutableLiveData<>("All");

    public final MutableLiveData<String> selectedBrand = new MutableLiveData<>("All");

    public void setCategory(String category) {
        selectedCategory.setValue(category);
        // Important: Reset sub-category when the main category changes to avoid invalid combinations
        selectedSubCategory.setValue("All");
    }

    public void setSubCategory(String subCategory) {
        selectedSubCategory.setValue(subCategory);
    }

    public void setBrand(String brand) {
        selectedBrand.setValue(brand);
    }
}
