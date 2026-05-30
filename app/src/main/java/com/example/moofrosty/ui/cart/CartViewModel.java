package com.example.moofrosty.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.CartItem;
import com.example.moofrosty.data.model.CartTotals;
import com.example.moofrosty.data.model.OrderHistoryResponse;
import com.example.moofrosty.data.model.Product;
import com.example.moofrosty.data.repository.CartRepository;

public class CartViewModel extends ViewModel {

    private final CartRepository cartRepository = CartRepository.getInstance();

    // Checkout Result
    //can able to mute the data
    private final MutableLiveData<Resource<String>> checkoutResult =
            new MutableLiveData<>();
    private final MutableLiveData<Integer> caseQuantity = new MutableLiveData<>(0);

    public LiveData<Integer> getCaseQuantity() {
        return caseQuantity;
    }

    public void setCaseQuantity(int value) {
        if (value < 0) value = 0;
        caseQuantity.setValue(value);
    }

    public void incrementCase() {
        Integer current = caseQuantity.getValue();
        if (current == null) current = 0;
        caseQuantity.setValue(current + 1);
    }

    public void decrementCase() {
        Integer current = caseQuantity.getValue();
        if (current == null) current = 0;
        if (current > 0) {
            caseQuantity.setValue(current - 1);
        }
    }

    // Order History Result
    private final MutableLiveData<Resource<OrderHistoryResponse>> orderHistory =
            new MutableLiveData<>();

    // Session Data
    private String token = "";
    private int userId = 0;
    private int shopId = 0;

    // =========================
    // SESSION DATA
    // =========================

    public void setSessionData(String token, int userId, int shopId) {
        this.token = token;
        this.userId = userId;
        this.shopId = shopId;
    }

    // =========================
    // LIVE DATA GETTERS
    // =========================

    public LiveData<Resource<String>> getCheckoutResult() {
        return checkoutResult;
    }

    public LiveData<Resource<OrderHistoryResponse>> getOrderHistory() {
        return orderHistory;
    }
//can't able ot mute the data
    public LiveData<CartTotals> getCartTotals() {
        return cartRepository.getCartTotals();
    }

    public LiveData<java.util.Map<String, CartItem>> getCartMap() {
        return cartRepository.getCartMap();
    }

    // =========================
    // CHECKOUT
    // =========================

    public void checkout() {

        if (token == null || token.isEmpty()) {
            return;
        }

        cartRepository.checkout(
                token,
                userId,
                shopId,
                checkoutResult
        );
    }

    // =========================
    // CART OPERATIONS
    // =========================

    public void addToCart(Product product) {
        cartRepository.addToCart(product);
    }

    public void incrementUnit(Product product) {
        cartRepository.incrementUnit(product);
    }

    public void decrementUnit(Product product) {
        cartRepository.decrementUnit(product);
    }

    public void incrementCase(Product product) {
        cartRepository.incrementCase(product);
    }

    public void decrementCase(Product product) {
        cartRepository.decrementCase(product);
    }

    public void removeFromCart(Product product) {
        cartRepository.removeFromCart(product);
    }

    // =========================
    // MANUAL CASE QUANTITY UPDATE
    // =========================

    public void updateCaseQuantity(Product product, int quantity) {

        if (product == null) {
            return;
        }

        // Prevent negative values
        if (quantity < 0) {
            quantity = 0;
        }

        cartRepository.updateCaseQuantity(product, quantity);
    }


    // =========================
    // ORDER HISTORY
    // =========================

    public void fetchOrders() {

        if (token == null || token.isEmpty()) {
            return;
        }

        if (shopId == 0) {
            return;
        }

        cartRepository.getOrderHistory(
                token,
                shopId,
                orderHistory
        );
    }

    // =========================
    // CLEAR CART
    // =========================

    public void clearCart() {
        cartRepository.clearCart();
    }

    public void setUnitQuantityDirect(Product product, int qty) {
        cartRepository.setUnitQuantityDirect(product, qty);
    }
    public void setCaseQuantityDirect(Product product, int qty) {
        cartRepository.setCaseQuantityDirect(product, qty);
    }

}