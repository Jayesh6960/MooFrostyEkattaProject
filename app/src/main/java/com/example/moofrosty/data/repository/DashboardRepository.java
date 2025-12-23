package com.example.moofrosty.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.data.model.DashboardItem;

import java.util.ArrayList;
import java.util.List;

public class DashboardRepository {

    public void fetchMocData(String mocName, MutableLiveData<List<DashboardItem>> dataList, MutableLiveData<String> totalIncentives) {

        List<DashboardItem> items = new ArrayList<>();

        // Safety check to prevent NullPointer
        if (mocName == null) mocName = "MOC 12";

        if (mocName.contains("MOC 12")) {
            // DECEMBER DATA
            totalIncentives.setValue("120 / 1720");
            items.add(new DashboardItem("MGP(Sales)", 33, "132817/400000", 400000, 132817));
            items.add(new DashboardItem("GPS BP (Days)", 3, "1 / 30", 30, 1));
            items.add(new DashboardItem("ECO (Outlet)", 21, "32 / 156", 156, 32));
            items.add(new DashboardItem("ENVISION LINES", 1, "1 / 86", 86, 1));
            items.add(new DashboardItem("COC ECO", 9, "17 / 181", 181, 17));
            items.add(new DashboardItem("COC Scanned", 34, "62 / 181", 181, 62));

        } else if (mocName.contains("MOC 11")) {
            // NOVEMBER DATA
            totalIncentives.setValue("850 / 1720");
            items.add(new DashboardItem("MGP(Sales)", 60, "240000/400000", 400000, 240000));
            items.add(new DashboardItem("GPS BP (Days)", 50, "15 / 30", 30, 15));
            items.add(new DashboardItem("ECO (Outlet)", 80, "125 / 156", 156, 125));
            items.add(new DashboardItem("ENVISION LINES", 40, "34 / 86", 86, 34));
            items.add(new DashboardItem("COC ECO", 50, "90 / 181", 181, 90));
            items.add(new DashboardItem("COC Scanned", 90, "162 / 181", 181, 162));

        } else {
            // OCTOBER DATA
            totalIncentives.setValue("1500 / 1720");
            items.add(new DashboardItem("MGP(Sales)", 100, "400000/400000", 400000, 400000));
            items.add(new DashboardItem("GPS BP (Days)", 100, "30 / 30", 30, 30));
            items.add(new DashboardItem("ECO (Outlet)", 100, "156 / 156", 156, 156));
            items.add(new DashboardItem("ENVISION LINES", 90, "77 / 86", 86, 77));
            items.add(new DashboardItem("COC ECO", 95, "171 / 181", 181, 171));
            items.add(new DashboardItem("COC Scanned", 88, "159 / 181", 181, 159));
        }

        // Updates the LiveData which triggers the UI update
        dataList.setValue(items);
    }
}

