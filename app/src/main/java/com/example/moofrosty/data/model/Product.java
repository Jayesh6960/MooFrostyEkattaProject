package com.example.moofrosty.data.model;

public class Product {

    public  String id;
    public  double mrpPrice;
    public  double ratePrice;
    public int stock;
    public int caseSize = 1; // Default case size 1, can be overwritten

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

    String category;
    String brand;

    // --- ADDED THIS FIELD ---
    public String productType;
    public String imageUrl;

    // Updated Constructor
    public Product(String id, String name, String mrp, String rate, String margin, String stockString, String capacity, String vmq,
                   String l3mrr, String mtd, int imageResId, String category, String brand, String productType,String imageUrl) {

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
        this.imageResId = imageResId;
        this.category = category;
        this.brand = brand;

        // --- Initialize New Field ---
        this.productType = productType;
        this.imageUrl = imageUrl;

        this.mrpPrice = parsePrice(mrp);
        this.ratePrice = parsePrice(rate);

        try {
            this.stock = Integer.parseInt(stockString);
        } catch (NumberFormatException e) {
            this.stock = 0;
        }
    }

//    // Constructor Overload (If you use it without productType somewhere, defaults to "unit")
//    public Product(String id, String name, String mrp, String rate, String margin, String stockString, String capacity, String vmq,
//                   String l3mrr, String mtd, int imageResId, String category, String brand) {
//        this(id, name, mrp, rate, margin, stockString, capacity, vmq, l3mrr, mtd, imageResId, category, brand, "unit");
//    }

    private double parsePrice(String price) {
        try {
            if (price == null) return 0.0;
            return Double.parseDouble(price.replaceAll("[^\\d.]", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }

    public double getSavings() {
        return mrpPrice - ratePrice;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getMrp() { return mrp; }
    public String getRate() { return rate; }
    public String getMargin() { return margin; }
    public String getStock() { return stockString; }
    public int getStockInt() { return stock; }
    public String getCapacity() { return capacity; }

    public int getImageResId() { return imageResId; }
    public String getCategory() { return category; }
    public String getBrand() { return brand; }
    public String getImageUrl() { return imageUrl; }

    // Getter for type (optional since field is public, but good practice)
    public String getProductType() { return productType; }
}