package com.example.moofrosty.ui.store;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.CheckInRequest;
import com.example.moofrosty.data.model.Store;
import com.example.moofrosty.data.repository.StoreInOutRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StoreProfileViewModel extends ViewModel {

    private StoreInOutRepository repository;
    private MutableLiveData<Store> selectedStore = new MutableLiveData<>();

    // Use Resource for API Status (Loading/Success/Error)
    private MutableLiveData<Resource<String>> checkInStatus = new MutableLiveData<>();

    // Specific LiveData for the Geofence Popup
    private MutableLiveData<GeofenceData> geofenceAlert = new MutableLiveData<>();

    public StoreProfileViewModel() {
        repository = new StoreInOutRepository();
    }

    public void setStore(Store store) {
        selectedStore.setValue(store);
    }
    public LiveData<Store> getStore() { return selectedStore; }

    public LiveData<Resource<String>> getCheckInStatus() { return checkInStatus; }
    public LiveData<GeofenceData> getGeofenceAlert() { return geofenceAlert; }

    // --- MAIN LOGIC ---
    public void onEnterStoreClicked(Location currentLocation, boolean isNetworkAvailable, boolean isAttendanceMarked, String token) {
        Store store = selectedStore.getValue();
        if (store == null) return;

        // 1. Validation Logic
//        if (!isAttendanceMarked) {
//            checkInStatus.setValue(Resource.error("Please mark your attendance first.", null));
//            return;
//        }

        if (!isNetworkAvailable) {
            checkInStatus.setValue(Resource.error("No Internet Connection.", null));
            return;
        }

        if (currentLocation == null) {
            checkInStatus.setValue(Resource.error("Unable to fetch current location.", null));
            return;
        }

        // 2. Distance Calculation
        float[] results = new float[1];
        Location.distanceBetween(
                currentLocation.getLatitude(), currentLocation.getLongitude(),
                store.getLat(), store.getLng(),
                results
        );
        float distanceInMeters = results[0];

        // 3. Geofence Check (50 Meters)
        if (distanceInMeters >= 100) {
            // Send Data to View to show Popup
            GeofenceData data = new GeofenceData(distanceInMeters, currentLocation.getLatitude(), currentLocation.getLongitude());
            geofenceAlert.setValue(data);
        } else {
            // 4. API Call
            performCheckInApi(store.getShopId(), token);
        }
    }

    private void performCheckInApi(int shopId, String token) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        CheckInRequest request = new CheckInRequest(shopId, date, time, "in");

        repository.enterStore(token, request, checkInStatus);
    }

    // Helper class for the Popup
    public static class GeofenceData {
        public float distance;
        public double currentLat;
        public double currentLng;
        public GeofenceData(float d, double lat, double lng) { this.distance = d; this.currentLat = lat; this.currentLng = lng; }
    }

 //   this tis for fragment data set

    // Variables to track expansion state
    private MutableLiveData<Boolean> isClassificationExpanded = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isBusinessDetailsExpanded = new MutableLiveData<>(false);

//    public void setStore(Store store) {
//        selectedStore.setValue(store);
//    }
//
//    public LiveData<Store> getStore() {
//        return selectedStore;
//    }

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
