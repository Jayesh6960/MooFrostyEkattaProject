package com.example.moofrosty.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.data.model.BeatModel;
import com.example.moofrosty.data.model.Store;
import com.example.moofrosty.data.repository.MyBeatRepository;

import java.util.ArrayList;
import java.util.List;

public class MyBeatViewModel extends ViewModel {

    private MyBeatRepository repository;

    // Master Lists (Raw Data)
    private MutableLiveData<List<BeatModel>> allBeats = new MutableLiveData<>();
    private MutableLiveData<List<Store>> allStores = new MutableLiveData<>();

    // Filtered Output (UI Observes This)
    private MutableLiveData<List<Store>> filteredStores = new MutableLiveData<>();

    // Summary Data
    private MutableLiveData<Double> totalOrderValue = new MutableLiveData<>(0.0);
    private MutableLiveData<String> visitedCountText = new MutableLiveData<>("0/0");
    private MutableLiveData<String> orderTakenCountText = new MutableLiveData<>("0/0");

    // Current Filter States
    private String currentTabFilter = "All"; // "All", "Not Visited", "Order Taken"
    private String currentSearchQuery = "";

    public MyBeatViewModel() {
        repository = new MyBeatRepository();
        loadData();
    }

    private void loadData() {
        repository.fetchInitialData(allBeats, allStores);
        applyFilters(); // Initial calculation
    }

    public LiveData<List<Store>> getFilteredStores() { return filteredStores; }
    public LiveData<List<BeatModel>> getBeats() { return allBeats; }

    // Summary LiveData
    public LiveData<Double> getTotalOrderValue() { return totalOrderValue; }
    public LiveData<String> getVisitedCountText() { return visitedCountText; }
    public LiveData<String> getOrderTakenCountText() { return orderTakenCountText; }

    // User Actions
    public void onBeatSelectionChanged(int position, boolean isSelected) {
        List<BeatModel> beats = allBeats.getValue();
        if (beats != null) {
            beats.get(position).setSelected(isSelected);
            allBeats.setValue(beats); // Update UI
            applyFilters(); // Re-calculate list
        }
    }

    public void onTabFilterChanged(String tab) {
        this.currentTabFilter = tab;
        applyFilters();
    }

    public void onSearchQueryChanged(String query) {
        this.currentSearchQuery = query.toLowerCase();
        applyFilters();
    }

    // CORE LOGIC: Filtering
    private void applyFilters() {
        List<Store> masterList = allStores.getValue();
        List<BeatModel> beats = allBeats.getValue();
        if (masterList == null || beats == null) return;

        List<Store> result = new ArrayList<>();
        List<String> selectedBeatIds = new ArrayList<>();

        // 1. Get Selected Beat IDs
        for (BeatModel b : beats) {
            if (b.isSelected()) selectedBeatIds.add(b.getId());
        }

        double totalVal = 0;
        int visitedCount = 0;
        int orderCount = 0;
        int totalDisplayed = 0; // Total available in selected beats (denominator)

        // 2. Filter Logic
        for (Store store : masterList) {
            // Check Beat Selection
            if (selectedBeatIds.contains(store.getBeatId())) {
                totalDisplayed++; // It belongs to a selected beat

                // Summary Calculation (happens before tab filtering usually, or after depending on req.
                // Requirement implies Summary is based on "Selected Beat", not "Selected Tab".
                // Usually summary is for the whole beat. Let's calculate for selected beats.)
                if (store.isVisited()) visitedCount++;
                if (store.isOrderTaken()) {
                    orderCount++;
                    totalVal += store.getOrderValue();
                }

                // 3. Check Tab Filter
                boolean matchesTab = true;
                if (currentTabFilter.equals("Not Visited") && store.isVisited()) matchesTab = false;
                if (currentTabFilter.equals("Visited") && !store.isVisited()) matchesTab = false;
                if (currentTabFilter.equals("Order Taken") && !store.isOrderTaken()) matchesTab = false;

                // 4. Check Search
                boolean matchesSearch = store.getName().toLowerCase().contains(currentSearchQuery);

                if (matchesTab && matchesSearch) {
                    result.add(store);
                }
            }
        }

        // Update LiveData
        filteredStores.setValue(result);
        totalOrderValue.setValue(totalVal);
        visitedCountText.setValue(visitedCount + "/" + totalDisplayed);
        orderTakenCountText.setValue(orderCount + "/" + totalDisplayed);
    }
}
