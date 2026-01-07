package com.example.moofrosty.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.data.model.DashboardItem;

import java.util.ArrayList;
import java.util.List;

public class DashboardRepository {

    public void fetchMocData(String mocName, MutableLiveData<List<DashboardItem>> dataList, MutableLiveData<String> totalIncentives) {

        List<DashboardItem> items = new ArrayList<>();

        // Safety check to prevent NullPointer
        if (mocName == null) mocName = "MOC 01";

        if (mocName.contains("MOC 01")) {
            // DECEMBER DATA
            totalIncentives.setValue("120 / 1720");
            items.add(new DashboardItem("MGP(Sales)", 33, "132817/400000", 400000, 132817));
            items.add(new DashboardItem("EFOS", 65, "11/17", 17, 11));
        } else if (mocName.contains("MOC 12")) {
            // NOVEMBER DATA
            totalIncentives.setValue("850 / 1720");
            items.add(new DashboardItem("MGP(Sales)", 60, "240000/400000", 400000, 240000));
            items.add(new DashboardItem("EFOS", 65, "13/17", 17, 11));
        } else {
            // OCTOBER DATA
            totalIncentives.setValue("1500 / 1720");
            items.add(new DashboardItem("MGP(Sales)", 100, "400000/400000", 400000, 400000));
            items.add(new DashboardItem("EFOS", 65, "12/17", 17, 11));


        }

        // Updates the LiveData which triggers the UI update
        dataList.setValue(items);
    }
}

