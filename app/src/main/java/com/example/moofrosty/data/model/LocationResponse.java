package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LocationResponse<T> {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private List<T> data;

    public boolean isStatus() { return status; }
    public List<T> getData() { return data; }

    public static class Country {
        @SerializedName("countriesId") private int id;
        @SerializedName("countryName") private String name;
        public int getId() { return id; }
        public String getName() { return name; }
        @Override public String toString() { return name; }
    }

    public static class State {
        @SerializedName("stateId") private int id;
        @SerializedName("stateName") private String name;
        public int getId() { return id; }
        public String getName() { return name; }
        @Override public String toString() { return name; }
    }

    public static class District {
        @SerializedName("districtId") private int id;
        @SerializedName("districtName") private String name;
        public int getId() { return id; }
        public String getName() { return name; }
        @Override public String toString() { return name; }
    }

    public static class City {
        @SerializedName("cityId") private int id;
        @SerializedName("cityName") private String name;
        public int getId() { return id; }
        public String getName() { return name; }
        @Override public String toString() { return name; }
    }
}
