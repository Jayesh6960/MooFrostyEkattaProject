package com.example.moofrosty.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.data.model.DashboardItem;
import com.example.moofrosty.data.repository.DashboardRepository;

import java.util.List;

public class DashboardViewModel extends ViewModel{

//    private MutableLiveData<List<DashboardItem>> dashboardItems;
//
//    public LiveData<List<DashboardItem>> getDashboardItems() {
//        if (dashboardItems == null) {
//            dashboardItems = new MutableLiveData<>();
//            loadItems();
//        }
//        return dashboardItems;
//    }
//
//    private void loadItems() {
//        // Simulating data loading based on screenshots
//        List<DashboardItem> items = new ArrayList<>();
//        items.add(new DashboardItem("MGP(Sales) ?", 0, "35185 / 0"));
//        items.add(new DashboardItem("GPS BP (Days)", 0, "0 / 30"));
//        items.add(new DashboardItem("ECO (Outlet Count)", 8, "13 / 158"));
//        items.add(new DashboardItem("ENVISION LINES", 3, "2 / 77"));
//        items.add(new DashboardItem("COC ECO", 2, "4 / 182"));
//        items.add(new DashboardItem("COC Scanned", 22, "40 / 180"));
//        items.add(new DashboardItem("COC ECO", 2, "4 / 182"));
//        items.add(new DashboardItem("COC Scanned", 100, "180 / 180"));
//
//        dashboardItems.setValue(items);
//    }

    private DashboardRepository repository;
    private MutableLiveData<List<DashboardItem>> dashboardItems = new MutableLiveData<>();
    private MutableLiveData<String> totalIncentives = new MutableLiveData<>();

    public DashboardViewModel() {
        repository = new DashboardRepository();
        // IMPORTANT: Load initial data immediately so the list isn't empty!
        loadData("MOC 12");
    }

    public LiveData<List<DashboardItem>> getDashboardItems() {
        return dashboardItems;
    }

    public LiveData<String> getTotalIncentives() {
        return totalIncentives;
    }

    public void loadData(String mocName) {
        // This fetches data from Repository and updates dashboardItems
        repository.fetchMocData(mocName, dashboardItems, totalIncentives);
    }
}
