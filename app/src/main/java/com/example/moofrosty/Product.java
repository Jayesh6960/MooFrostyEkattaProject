package com.example.moofrosty;

public class Product {

    private static int idCounter = 0;
    public final String id;
    public final double mrpPrice;
    public final double ratePrice;
    public int stock;
    public final int caseSize = 6; // Fixed case size as requested

    String name;
    String mrp;
    String rate;
    String margin;
    String stockString;
    String capacity;
    String vmq;
    String l3mrr;
    String mtd;
    int imageResId;

    // --- ADDED THESE ---
    String category;
    String brand;

    public Product(String id, String name, String mrp, String rate, String margin, String stockString, String capacity, String vmq,
                   String l3mrr, String mtd, int imageResId,String category, String brand) { // <-- ADDED int imageResId
     //   this.id = idCounter++;
        this.id = id;
        this.name = name;
        this.mrp = mrp;
        this.rate = rate;
        this.margin = margin;
        this.stockString = stockString;
        this.capacity = capacity;
        this.vmq = vmq;
        this.l3mrr = l3mrr;
        this.mtd = mtd;
        this.imageResId = imageResId; // <-- ADDED THIS
        this.category = category; // <-- ADDED
        this.brand = brand;       // <-- ADDED

        this.mrpPrice = parsePrice(mrp);
        this.ratePrice = parsePrice(rate);
        //this.stock = Integer.parseInt(stockString);
        try {
            this.stock = Integer.parseInt(stockString);
        } catch (NumberFormatException e) {
            this.stock = 0; // Default to 0 if stock is "0" or invalid
        }
    }

    private double parsePrice(String price) {
        try {
            return Double.parseDouble(price.replaceAll("[^\\d.]", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }

    public double getSavings() {
        return mrpPrice - ratePrice;
    }



    // --- ADD GETTERS ---
    public String getId() { return id; }
    public String getCategory() { return category; }
    public String getBrand() { return brand; }
    // Add getters for all fields
    public String getName() { return name; }
    public String getMrp() { return mrp; }
    public String getRate() { return rate; }
    public String getMargin() { return margin; }
    public String getStock() { return stockString; }
    public int getStockInt() { return stock; } // Use clean int
    public String getCapacity() { return capacity; }
    public String getVmq() { return "VMQ: " + vmq; }
    public String getL3mrr() { return "L3MRR: " + l3mrr; }
    public String getMtd() { return "MTD: " + mtd; }
    public int getImageResId() { return imageResId; } // <-- ADDED THIS
}