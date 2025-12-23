package com.example.moofrosty.ui.store;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.data.model.Store;

public class StoreProfileViewModel extends ViewModel {

    private MutableLiveData<Store> selectedStore = new MutableLiveData<>();

    // Variables to track expansion state
    private MutableLiveData<Boolean> isClassificationExpanded = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isBusinessDetailsExpanded = new MutableLiveData<>(false);

    public void setStore(Store store) {
        selectedStore.setValue(store);
    }

    public LiveData<Store> getStore() {
        return selectedStore;
    }

    // Toggle Logic for Expandable Views
    public void toggleClassification() {
        Boolean current = isClassificationExpanded.getValue();
        isClassificationExpanded.setValue(current == null ? true : !current);
    }

    public void toggleBusinessDetails() {
        Boolean current = isBusinessDetailsExpanded.getValue();
        isBusinessDetailsExpanded.setValue(current == null ? true : !current);
    }

    public LiveData<Boolean> getIsClassificationExpanded() { return isClassificationExpanded; }
    public LiveData<Boolean> getIsBusinessDetailsExpanded() { return isBusinessDetailsExpanded; }
}
