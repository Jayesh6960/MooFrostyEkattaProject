package com.example.moofrosty.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.data.model.BeatModel;
import com.example.moofrosty.data.model.Store;

import java.util.ArrayList;
import java.util.List;

public class MyBeatRepository {
    // Mock Data Generation
    public void fetchInitialData(MutableLiveData<List<BeatModel>> beats, MutableLiveData<List<Store>> stores) {

        // 1. Create Beats
        List<BeatModel> beatList = new ArrayList<>();
        beatList.add(new BeatModel("B1", "Waluj Pandharpur (HULI)", 16, true));
        beatList.add(new BeatModel("B2", "PAITHAN (HULI)", 24, true)); // 16+24 = 40 total
        beats.setValue(beatList);

        // 2. Create Stores
        List<Store> storeList = new ArrayList<>();

        // Stores for Beat 1 (Waluj)
        for (int i = 1; i <= 16; i++) {
            boolean visited = i % 3 == 0;
            boolean orderTaken = i % 6 == 0;
            double value = orderTaken ? (i * 100) : 0;

            // Proper Address Logic for Waluj
            String address = "Plot No " + (10 + i) + ", Bajaj Nagar, Waluj, Aurangabad";
            String owner = "Owner Name " + i;
            String hulCode = "HUL-W" + (1000 + i);

            storeList.add(new Store("S1_" + i, "Shrikrishna Dairy " + i, "B1", visited, orderTaken, value,
                    "1234567890", 19.8, 75.3, address, owner, hulCode));
        }

        // Beat 2: Paithan Stores
        for (int i = 1; i <= 24; i++) {
            boolean visited = i % 2 == 0;
            boolean orderTaken = i % 4 == 0;
            double value = orderTaken ? (i * 150) : 0;

            // Proper Address Logic for Paithan
            String address = "Shop No " + i + ", Main Market Road, Paithan Gate, Aurangabad";
            String owner = "Owner Name " + (16 + i);
            String hulCode = "HUL-P" + (2000 + i);

            storeList.add(new Store("S2_" + i, "Kailash Restaurant " + i, "B2", visited, orderTaken, value,
                    "0987654321", 19.5, 75.4, address, owner, hulCode));
        }

        stores.setValue(storeList);
    }
}
