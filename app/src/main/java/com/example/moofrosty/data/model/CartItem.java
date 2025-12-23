package com.example.moofrosty.data.model;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    // --- Logic for Case/Unit ---
    public int getCaseQuantity() {
        return quantity / product.caseSize;
    }

    public int getUnitQuantity() {
        return quantity % product.caseSize;
    }

    // --- Logic for Totals ---
    public double getTotalPrice() {
        return product.ratePrice * quantity;
    }

    public double getTotalSavings() {
        return product.getSavings() * quantity;
    }

    public double getTotalMrp() {
        return product.mrpPrice * quantity;
    }

    // --- Methods to modify quantity ---
    public void incrementUnit() {
        if (quantity < product.getStockInt()) {
            quantity++;
        }
    }

    public void decrementUnit() {
        if (quantity > 0) {
            quantity--;
        }
    }

    public void incrementCase() {
        int newQuantity = quantity + product.caseSize;
        if (newQuantity <= product.getStockInt()) {
            quantity = newQuantity;
        } else {
            // Can't add a full case, add max possible units
            quantity = product.getStockInt();
        }
    }

    public void decrementCase() {
        int newQuantity = quantity - product.caseSize;
        if (newQuantity < 0) {
            quantity = 0;
        } else {
            quantity = newQuantity;
        }
    }
}
