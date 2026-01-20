package com.example.moofrosty.data.model;

public class BeatModel {

    private String id;
    private String name;
    private int totalStores;
    private boolean isSelected;

    public BeatModel(String id, String name, int totalStores, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.totalStores = totalStores;
        this.isSelected = isSelected;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getTotalStores() { return totalStores; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }

    // Added Setter as requested
    public void setTotalStores(int totalStores) { this.totalStores = totalStores; }
}
