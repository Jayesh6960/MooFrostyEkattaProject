package com.example.moofrosty.ui.ATMSummary;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StoreModel {

    @SerializedName("Salesperson_name")
    private String salespersonname;

    @SerializedName("current_date")
    private String currentdate;

    @SerializedName("total_time")
    private String totaltime;

    @SerializedName("outlets")
    private String outlets;

    @SerializedName("average_time")
    private String averagetime;

    @SerializedName("store_name")
    private String storeName;

    @SerializedName("moc_sales")
    private int mocSales;

    @SerializedName("day_sales")
    private int daySales;

    @SerializedName("number_of_lines")
    private int numberOfLines;

    @SerializedName("in_time")
    private String inTime;

    @SerializedName("out_time")
    private String outTime;

    // 🔹 Empty constructor (Required for Retrofit)
    public StoreModel() {}
//Ready for the Live Api implementation
    public StoreModel(String salespersonname, String currentdate, String totaltime, String outlets, String averagetime,String storeName, int mocSales, int daySales, int numberOfLines, String inTime, String outTime) {
        this.storeName = storeName;
        this.salespersonname=salespersonname;
        this.mocSales = mocSales;
        this.daySales = daySales;
        this.numberOfLines = numberOfLines;
        this.inTime=inTime;
        this.outTime=outTime;
    }
    public void StoreModelStore(String currentdate, String totaltime, String outlets, String averagetime, String storeName, int mocSales, int daySales, int numberOfLines, String inTime, String outTime) {
        this.storeName = storeName;
        this.salespersonname=salespersonname;
        this.mocSales = mocSales;
        this.daySales = daySales;
        this.numberOfLines = numberOfLines;
        this.inTime=inTime;
        this.outTime=outTime;
    }
    String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(new Date());

    // 🔹 Getters
    public String getSalesperson() {
        return salespersonname != null ? salespersonname :salespersonname  ;//Static data Updated in the code
    }

    public String getCurrentdate() {
        return currentdate != null ? currentdate : today;
    }

    public String getTotaltime() {
        return totaltime != null ? totaltime : "--";
    }

    public int getActualOutlets() {
        return 0;
    }

    public int getTotalOutlets() {
        return 0;
    }


    public String getOutlets(int actualOutlets, int totalOutlets) {

        if (totalOutlets == 0) {
            return "--";
        }

        return actualOutlets + "/" + totalOutlets;
    }

    public String getAveragetime() {
        return averagetime != null ? averagetime : "--";
    }


    public String getStoreName() {
        return storeName != null ? storeName : "--";
    }

    public int getMocSales() {
        return mocSales;
//        return 15;
    }

    public int getDaySales() {
        return daySales;

    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public String getInTime() {
        return inTime != null ? inTime : "--";
    }

    public String getOutTime() {
        return outTime != null ? outTime : "--";
    }
}