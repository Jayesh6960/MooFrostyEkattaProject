package com.example.moofrosty.data.model;

public class CartItem {

    private Product product;
    private int unitQuantity;
    private int caseQuantity;

    public CartItem(Product product) {
        this.product = product;
        this.unitQuantity = 0;
        this.caseQuantity = 0;
    }

    public Product getProduct() { return product; }

    public int getUnitQuantity() { return unitQuantity; }
    public void setUnitQuantity(int unitQuantity) { this.unitQuantity = unitQuantity; }

    public int getCaseQuantity() { return caseQuantity; }
    public void setCaseQuantity(int caseQuantity) { this.caseQuantity = caseQuantity; }

    public void incrementUnit() { this.unitQuantity++; }
    public void decrementUnit() { if (this.unitQuantity > 0) this.unitQuantity--; }

    public void incrementCase() { this.caseQuantity++; }
    public void decrementCase() { if (this.caseQuantity > 0) this.caseQuantity--; }

    // --- CALCULATION LOGIC (3 Decimals) ---

    // Total Units actually being bought (Case Size * Cases + Units)
    public int getTotalUnits() {
        int caseSize = product.caseSize > 0 ? product.caseSize : 1;
        return (caseQuantity * caseSize) + unitQuantity;
    }

    // Rate * Total Units
    public double getTotalPrice() {
        double price = 0.0;
        try { price = Double.parseDouble(product.getRate()); } catch (Exception e) {}
        return price * getTotalUnits();
    }

    // MRP * Total Units
    public double getTotalMrp() {
        double mrp = 0.0;
        try { mrp = Double.parseDouble(product.getMrp()); } catch (Exception e) {}
        return mrp * getTotalUnits();
    }

    public double getTotalSavings() {
        return getTotalMrp() - getTotalPrice();
    }
//    private Product product;
//    private int quantity;
//
//    public CartItem(Product product, int quantity) {
//        this.product = product;
//        this.quantity = quantity;
//    }
//
//    public Product getProduct() {
//        return product;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    // --- Logic for Case/Unit ---
//    public int getCaseQuantity() {
//        return quantity / product.caseSize;
//    }
//
//    public int getUnitQuantity() {
//        return quantity % product.caseSize;
//    }
//
//    // --- Logic for Totals ---
//    public double getTotalPrice() {
//        return product.ratePrice * quantity;
//    }
//
//    public double getTotalSavings() {
//        return product.getSavings() * quantity;
//    }
//
//    public double getTotalMrp() {
//        return product.mrpPrice * quantity;
//    }
//
//    // --- Methods to modify quantity ---
//    public void incrementUnit() {
//        if (quantity < product.getStockInt()) {
//            quantity++;
//        }
//    }
//
//    public void decrementUnit() {
//        if (quantity > 0) {
//            quantity--;
//        }
//    }
//
//    public void incrementCase() {
//        int newQuantity = quantity + product.caseSize;
//        if (newQuantity <= product.getStockInt()) {
//            quantity = newQuantity;
//        } else {
//            // Can't add a full case, add max possible units
//            quantity = product.getStockInt();
//        }
//    }
//
//    public void decrementCase() {
//        int newQuantity = quantity - product.caseSize;
//        if (newQuantity < 0) {
//            quantity = 0;
//        } else {
//            quantity = newQuantity;
//        }
//    }
}
