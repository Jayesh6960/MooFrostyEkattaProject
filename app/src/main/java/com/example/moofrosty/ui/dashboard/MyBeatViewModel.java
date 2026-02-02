package com.example.moofrosty.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.BeatModel;
import com.example.moofrosty.data.model.Store;
import com.example.moofrosty.data.repository.MyBeatRepository;

import java.util.ArrayList;
import java.util.List;

public class MyBeatViewModel extends AndroidViewModel {

    private final MyBeatRepository repository;
    private boolean Storelist = true;

    // API Data
    private final MutableLiveData<Resource<List<Store>>> storesResource = new MutableLiveData<>();
    private final MutableLiveData<List<BeatModel>> beats = new MutableLiveData<>();

    // UI Data
    private final MutableLiveData<List<Store>> filteredStores = new MutableLiveData<>();
//    private List<Store> masterStoreList = new ArrayList<>();

    // Master list holds the data from the CURRENT API Call
    private List<Store> currentApiData = new ArrayList<>();

    // Summary
    private final MutableLiveData<Double> totalOrderValue = new MutableLiveData<>(0.0);
    private final MutableLiveData<String> visitedCountText = new MutableLiveData<>("0/0");
    private final MutableLiveData<String> orderTakenCountText = new MutableLiveData<>("0/0");

    private final MutableLiveData<Integer> repoTotalCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> repoVisitedCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> repoOrderCount = new MutableLiveData<>(0);
    // Filters
//    private String currentTab = "All";
//    private String searchQuery = "";
    private String currentSearchQuery = "";
    private String currentTabFilter = "All";

    private int globalTotalCount = 0;
    private int globalVisitedCount = 0;
    private int globalOrderCount = 0;
    private double globalOrderValue = 0.0;

    public MyBeatViewModel(@NonNull Application application) {
        super(application);
        this.repository = new MyBeatRepository(application);
        loadDataForTab("All");
        fetchStatsFromApi();
    }

    public void onTabFilterChanged(String tabName) {
        this.currentTabFilter = tabName;
        loadDataForTab(tabName);
    }

    private void loadDataForTab(String tab) {
        if (tab.equals("All")) {
            repository.fetchDashboardData(storesResource, beats,repoTotalCount);
        } else {
            repository.fetchFilteredStores(tab, storesResource);
        }
    }

    // [NEW]: Helper to fetch stats
    private void fetchStatsFromApi() {
        repository.fetchGlobalCounts(repoVisitedCount, repoOrderCount);
    }

    public void setMasterStoreList(List<Store> stores) {
        this.currentApiData = stores;
        applyLocalSearch();
//        updateSummaryText(); // [NEW]: Update cards text
    }

    // [NEW]: Logic to create "X/Y" strings
    private void updateSummaryText() {
        int total = repoTotalCount.getValue() != null ? repoTotalCount.getValue() : 0;
        int visited = repoVisitedCount.getValue() != null ? repoVisitedCount.getValue() : 0;
        int ordered = repoOrderCount.getValue() != null ? repoOrderCount.getValue() : 0;

        visitedCountText.setValue(visited + "/" + total);
        orderTakenCountText.setValue(ordered + "/" + total);
    }

    public void onSearchQueryChanged(String query) {
        this.currentSearchQuery = query;
        applyLocalSearch();
    }

    // Only filters the LIST, does NOT touch the Summary Cards anymore
    private void applyLocalSearch() {
        List<Store> temp = new ArrayList<>();

        if (currentApiData == null) currentApiData = new ArrayList<>();

        for (Store s : currentApiData) {
            String name = s.getStoreName();
            if (name != null && name.toLowerCase().contains(currentSearchQuery.toLowerCase())) {
                temp.add(s);
            } else if (name == null && currentSearchQuery.isEmpty()) {
                temp.add(s);
            }
        }

        filteredStores.setValue(temp);

        // Note: We deliberately removed the code that updates visitedCountText here.
        // The cards now remain static based on the "All" tab data.
    }

    // Getters
    public LiveData<Resource<List<Store>>> getStoresResource() { return storesResource; }
    public LiveData<List<BeatModel>> getBeats() { return beats; }
    public LiveData<List<Store>> getFilteredStores() { return filteredStores; }
    public LiveData<Double> getTotalOrderValue() { return totalOrderValue; }
    public LiveData<String> getVisitedCountText() { return visitedCountText; }
    public LiveData<String> getOrderTakenCountText() { return orderTakenCountText; }

    // [HIGHLIGHT] Expose raw counts so Fragment can observe and format
    public LiveData<Integer> getRepoTotalCount() { return repoTotalCount; }
    public LiveData<Integer> getRepoVisitedCount() { return repoVisitedCount; }
    public LiveData<Integer> getRepoOrderCount() { return repoOrderCount; }

    public void onBeatSelectionChanged(int position, boolean isSelected) {
        List<BeatModel> beatList = beats.getValue();
        if (beatList != null && position < beatList.size()) {
            beatList.get(position).setSelected(isSelected);
            beats.setValue(beatList);
        }
    }

//    public void setMasterStoreList(List<Store> stores) {
//        this.currentApiData = stores;
//        applyLocalSearch();
//    }
//
//    public void onSearchQueryChanged(String query) {
//        this.currentSearchQuery = query;
//        applyLocalSearch();
//    }
//
//    private void applyLocalSearch() {
//        List<Store> temp = new ArrayList<>();
//        int visited = 0;
//        int orderTaken = 0;
//        double value = 0.0;
//
//        if (currentApiData == null) currentApiData = new ArrayList<>();
//
//        for (Store s : currentApiData) {
//            // Stats (Calculated based on flags set in Repo)
//            if (s.isVisited()) visited++;
//            if (s.isOrderTaken()) {
//                orderTaken++;
//                value += s.getOrderValue();
//            }
//
//            // Name Filter (Safely handled)
//            String name = s.getStoreName();
//            if (name != null && name.toLowerCase().contains(currentSearchQuery.toLowerCase())) {
//                temp.add(s);
//            } else if (name == null && currentSearchQuery.isEmpty()) {
//                temp.add(s); // Include nameless stores if not searching
//            }
//        }
//
//        filteredStores.setValue(temp);
//
//        int total = currentApiData.size();
//        totalOrderValue.setValue(value);
//        visitedCountText.setValue(visited + "/" + total);
//        orderTakenCountText.setValue(orderTaken + "/" + total);
//    }
//
//    public LiveData<Resource<List<Store>>> getStoresResource() { return storesResource; }
//    public LiveData<List<BeatModel>> getBeats() { return beats; }
//    public LiveData<List<Store>> getFilteredStores() { return filteredStores; }
//    public LiveData<Double> getTotalOrderValue() { return totalOrderValue; }
//    public LiveData<String> getVisitedCountText() { return visitedCountText; }
//    public LiveData<String> getOrderTakenCountText() { return orderTakenCountText; }
//
//    public void onBeatSelectionChanged(int position, boolean isSelected) {
//        List<BeatModel> beatList = beats.getValue();
//        if (beatList != null && position < beatList.size()) {
//            beatList.get(position).setSelected(isSelected);
//            beats.setValue(beatList);
//        }
//    }
}



     ///   visited and order taken add
//    public MyBeatViewModel(MyBeatRepository repository) {
//        this.repository = repository;
//        loadData();
//    }

//    // 2. Constructor takes Application (No Factory needed now)
//    public MyBeatViewModel(@NonNull Application application) {
//        super(application);
//        // Initialize Repository here with Application Context
//        this.repository = new MyBeatRepository(application);
//        loadData();
//    }
//
//    private void loadData() {
//        repository.fetchDashboardData(storesResource, beats);
//    }
//
//    // ---------------- GETTERS ----------------
//
//    public LiveData<Resource<List<Store>>> getStoresResource() {
//        return storesResource;
//    }
//
//    public LiveData<List<BeatModel>> getBeats() {
//        return beats;
//    }
//
//    public LiveData<List<Store>> getFilteredStores() {
//        return filteredStores;
//    }
//
//    public LiveData<Double> getTotalOrderValue() {
//        return totalOrderValue;
//    }
//
//    public LiveData<String> getVisitedCountText() {
//        return visitedCountText;
//    }
//
//    public LiveData<String> getOrderTakenCountText() {
//        return orderTakenCountText;
//    }
//
//    // ---------------- USER ACTIONS ----------------
//
//    public void setMasterStoreList(List<Store> stores) {
//        masterStoreList = stores;
//        applyFilters();
//    }
//
//    public void onSearchQueryChanged(String query) {
//        this.currentSearchQuery = query;
//        applyFilters();
//    }
//
//    public void onTabFilterChanged(String filter) {
//        this.currentTabFilter = filter;
//        applyFilters();
//    }
//
//    // 🔥 THIS METHOD WAS MISSING / BROKEN
//    public void onBeatSelectionChanged(int position, boolean isSelected) {
//        List<BeatModel> beatList = beats.getValue();
//        if (beatList == null || position >= beatList.size()) return;
//
//        beatList.get(position).setSelected(isSelected);
//        beats.setValue(beatList);
//
//        applyFilters();
//    }
//
//    // ---------------- FILTER LOGIC ----------------
//// Trung the
//    private void applyFilters() {
//
//        List<Store> temp = new ArrayList<>();
//
//        int total = masterStoreList.size();
//        int visited = 0;
//        int orderTaken = 0;
//        double value = 0.0;
//
//        // First: apply filtering
//        for (Store s : masterStoreList) {
//
//            boolean matchesSearch = s.getStoreName()
//                    .toLowerCase()
//                    .contains(currentSearchQuery.toLowerCase());
//
//            boolean matchesTab = true;
//
//            switch (currentTabFilter) {
//                case "Not Visited":
//                    matchesTab = !s.isVisited();
//                    break;
//
//                case "Visited":
//                    matchesTab = s.isVisited();
//                    break;
//
//                case "Order Taken":
//                    matchesTab = s.isOrderTaken();
//                    break;
//
//                default:
//                    matchesTab = true;
//                    break;
//            }
//
//            if (matchesSearch && matchesTab) {
//                temp.add(s);
//            }
//        }
//
//        // Second: calculate stats ONLY from filtered list
//        for (Store s : temp) {
//            if (s.isVisited()) visited++;
//
//            if (s.isOrderTaken()) {
//                orderTaken++;
//                value += s.getOrderValue();
//            }
//        }
//
//        // Update LiveData (UI updates automatically)
//        filteredStores.setValue(temp);
//        totalOrderValue.setValue(value);
//
//        visitedCountText.setValue(visited + "/" + temp.size());
//        orderTakenCountText.setValue(orderTaken + "/" + temp.size());
//    }
//
//}





///     below code statuc above code running beofe visited and order taken add
//    private MyBeatRepository repository;
//
//    // Data Holders
//    private MutableLiveData<Resource<List<Store>>> storesResource = new MutableLiveData<>();
//    private MutableLiveData<List<BeatModel>> allBeats = new MutableLiveData<>();
//
//    // UI Holders
//    private MutableLiveData<List<Store>> filteredStores = new MutableLiveData<>();
//    private List<Store> masterStoreList = new ArrayList<>();
//
//    // Summary Holders
//    private MutableLiveData<Double> totalOrderValue = new MutableLiveData<>(0.0);
//    private MutableLiveData<String> visitedCountText = new MutableLiveData<>("0/0");
//    private MutableLiveData<String> orderTakenCountText = new MutableLiveData<>("0/0");
//
//    private String currentTabFilter = "All";
//    private String currentSearchQuery = "";
//
//    // Constructor accepts Repository
//    public MyBeatViewModel() {
//        repository = new MyBeatRepository();
//        loadData();
//    }
//
//
//    public void loadData() {
//        repository.fetchDashboardData(storesResource, allBeats);
//    }
//
//    public LiveData<Resource<List<Store>>> getStoresResource() { return storesResource; }
//    public LiveData<List<BeatModel>> getBeats() { return allBeats; }
//    public LiveData<List<Store>> getFilteredStores() { return filteredStores; }
//
//    public LiveData<Double> getTotalOrderValue() { return totalOrderValue; }
//    public LiveData<String> getVisitedCountText() { return visitedCountText; }
//    public LiveData<String> getOrderTakenCountText() { return orderTakenCountText; }
//
//    public void setMasterStoreList(List<Store> stores) {
//        this.masterStoreList = stores;
//        applyFilters();
//    }
//
//    public void onBeatSelectionChanged(int position, boolean isSelected) {
//        List<BeatModel> beats = allBeats.getValue();
//        if (beats != null && position < beats.size()) {
//            beats.get(position).setSelected(isSelected);
//            allBeats.setValue(beats);
//            applyFilters();
//        }
//    }
//
//    public void onTabFilterChanged(String tab) {
//        this.currentTabFilter = tab;
//        applyFilters();
//    }
//
//    public void onSearchQueryChanged(String query) {
//        this.currentSearchQuery = query.toLowerCase();
//        applyFilters();
//    }
//
////    private void applyFilters() {
////        if (masterStoreList == null) return;
////        List<Store> result = new ArrayList<>();
////        double totalVal = 0;
////        int visitedCount = 0;
////        int orderCount = 0;
////
////        for (Store store : masterStoreList) {
////            if (store.isVisited()) visitedCount++;
////            if (store.isOrderTaken()) {
////                orderCount++;
////                totalVal += store.getOrderValue();
////            }
////
////            boolean matchesTab = true;
////            if (currentTabFilter.equals("Not Visited") && store.isVisited()) matchesTab = false;
////            if (currentTabFilter.equals("Visited") && !store.isVisited()) matchesTab = false;
////            if (currentTabFilter.equals("Order Taken") && !store.isOrderTaken()) matchesTab = false;
////
////            boolean matchesSearch = (store.getName() != null) &&
////                    store.getName().toLowerCase().contains(currentSearchQuery);
////
////            if (matchesTab && matchesSearch) {
////                result.add(store);
////            }
////        }
//
////
//        private void applyFilters() {
//            if (masterStoreList == null) return;
//
//            List<Store> result = new ArrayList<>();
//            double totalVal = 0;
//            int visited = 0;
//            int ordered = 0;
//
//            for (Store store : masterStoreList) {
//
//                if (store.isVisited()) visited++;
//                if (store.isOrderTaken()) {
//                    ordered++;
//                    totalVal += store.getOrderValue();
//                }
//
//                boolean matchesTab = true;
//                if ("Visited".equals(currentTabFilter) && !store.isVisited()) matchesTab = false;
//                if ("Not Visited".equals(currentTabFilter) && store.isVisited()) matchesTab = false;
//                if ("Order Taken".equals(currentTabFilter) && !store.isOrderTaken()) matchesTab = false;
//
//                boolean matchesSearch =
//                        store.getName() != null &&
//                                store.getName().toLowerCase().contains(currentSearchQuery);
//
//                if (matchesTab && matchesSearch) {
//                    result.add(store);
//                }
//            }
//
//        filteredStores.setValue(result);
//        totalOrderValue.setValue(totalVal);
////        visitedCountText.setValue(visitedCount + "/" + masterStoreList.size());
////        orderTakenCountText.setValue(orderCount + "/" + masterStoreList.size());
//            visitedCountText.setValue(visited + "/" + masterStoreList.size());
//            orderTakenCountText.setValue(ordered + "/" + masterStoreList.size());
//    }
//}




//code 1st

//    private MyBeatRepository repository;
//
//    // Master Lists (Raw Data)
//    private MutableLiveData<List<BeatModel>> allBeats = new MutableLiveData<>();
//    private MutableLiveData<List<Store>> allStores = new MutableLiveData<>();
//
//    // Filtered Output (UI Observes This)
//    private MutableLiveData<List<Store>> filteredStores = new MutableLiveData<>();
//
//    // Summary Data
//    private MutableLiveData<Double> totalOrderValue = new MutableLiveData<>(0.0);
//    private MutableLiveData<String> visitedCountText = new MutableLiveData<>("0/0");
//    private MutableLiveData<String> orderTakenCountText = new MutableLiveData<>("0/0");
//
//    // Current Filter States
//    private String currentTabFilter = "All"; // "All", "Not Visited", "Order Taken"
//    private String currentSearchQuery = "";
//
//    public MyBeatViewModel() {
//        repository = new MyBeatRepository();
//        loadData();
//    }
//
//    private void loadData() {
//        repository.fetchInitialData(allBeats, allStores);
//        applyFilters(); // Initial calculation
//    }
//
//    public LiveData<List<Store>> getFilteredStores() { return filteredStores; }
//    public LiveData<List<BeatModel>> getBeats() { return allBeats; }
//
//    // Summary LiveData
//    public LiveData<Double> getTotalOrderValue() { return totalOrderValue; }
//    public LiveData<String> getVisitedCountText() { return visitedCountText; }
//    public LiveData<String> getOrderTakenCountText() { return orderTakenCountText; }
//
//    // User Actions
//    public void onBeatSelectionChanged(int position, boolean isSelected) {
//        List<BeatModel> beats = allBeats.getValue();
//        if (beats != null) {
//            beats.get(position).setSelected(isSelected);
//            allBeats.setValue(beats); // Update UI
//            applyFilters(); // Re-calculate list
//        }
//    }
//
//    public void onTabFilterChanged(String tab) {
//        this.currentTabFilter = tab;
//        applyFilters();
//    }
//
//    public void onSearchQueryChanged(String query) {
//        this.currentSearchQuery = query.toLowerCase();
//        applyFilters();
//    }
//
//    // CORE LOGIC: Filtering
//    private void applyFilters() {
//        List<Store> masterList = allStores.getValue();
//        List<BeatModel> beats = allBeats.getValue();
//        if (masterList == null || beats == null) return;
//
//        List<Store> result = new ArrayList<>();
//        List<String> selectedBeatIds = new ArrayList<>();
//
//        // 1. Get Selected Beat IDs
//        for (BeatModel b : beats) {
//            if (b.isSelected()) selectedBeatIds.add(b.getId());
//        }
//
//        double totalVal = 0;
//        int visitedCount = 0;
//        int orderCount = 0;
//        int totalDisplayed = 0; // Total available in selected beats (denominator)
//
//        // 2. Filter Logic
//        for (Store store : masterList) {
//            // Check Beat Selection
//            if (selectedBeatIds.contains(store.getBeatId())) {
//                totalDisplayed++; // It belongs to a selected beat
//
//                // Summary Calculation (happens before tab filtering usually, or after depending on req.
//                // Requirement implies Summary is based on "Selected Beat", not "Selected Tab".
//                // Usually summary is for the whole beat. Let's calculate for selected beats.)
//                if (store.isVisited()) visitedCount++;
//                if (store.isOrderTaken()) {
//                    orderCount++;
//                    totalVal += store.getOrderValue();
//                }
//
//                // 3. Check Tab Filter
//                boolean matchesTab = true;
//                if (currentTabFilter.equals("Not Visited") && store.isVisited()) matchesTab = false;
//                if (currentTabFilter.equals("Visited") && !store.isVisited()) matchesTab = false;
//                if (currentTabFilter.equals("Order Taken") && !store.isOrderTaken()) matchesTab = false;
//
//                // 4. Check Search
//                boolean matchesSearch = store.getName().toLowerCase().contains(currentSearchQuery);
//
//                if (matchesTab && matchesSearch) {
//                    result.add(store);
//                }
//            }
//        }
//
//        // Update LiveData
//        filteredStores.setValue(result);
//        totalOrderValue.setValue(totalVal);
//        visitedCountText.setValue(visitedCount + "/" + totalDisplayed);
//        orderTakenCountText.setValue(orderCount + "/" + totalDisplayed);
//    }
//}
