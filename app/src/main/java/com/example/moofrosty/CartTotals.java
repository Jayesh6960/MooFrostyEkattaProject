package com.example.moofrosty;

public class CartTotals {
    public final double totalPrice;
    public final double totalSavings;
    public final double totalMrp;
    public final int uniqueItemCount; // For toolbar badge
    public final int totalUnitCount;  // For cart page

    public CartTotals() {
        this(0, 0, 0, 0, 0);
    }

    public CartTotals(double price, double savings, double mrp, int uniqueCount, int unitCount) {
        this.totalPrice = price;
        this.totalSavings = savings;
        this.totalMrp = mrp;
        this.uniqueItemCount = uniqueCount;
        this.totalUnitCount = unitCount;
    }
}