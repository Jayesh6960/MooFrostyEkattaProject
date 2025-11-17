package com.example.moofrosty;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

public class CartRepository {
    private static CartRepository instance;

    private final MutableLiveData<Map<String, CartItem>> _cartMap = new MutableLiveData<>(new HashMap<>());
    public LiveData<Map<String, CartItem>> getCartMap() {
        return _cartMap;
    }

    // --- FIX: Use the new independent CartTotals class ---
    private final MutableLiveData<CartTotals> _cartTotals = new MutableLiveData<>(new CartTotals());
    public LiveData<CartTotals> getCartTotals() {
        return _cartTotals;
    }

    public static synchronized CartRepository getInstance() {
        if (instance == null) {
            instance = new CartRepository();
        }
        return instance;
    }

    public void addToCart(Product product) {
        Map<String, CartItem> currentCart = _cartMap.getValue();
        if (currentCart == null) currentCart = new HashMap<>();

        CartItem cartItem = currentCart.get(product.getId());
        if (cartItem == null) {
            cartItem = new CartItem(product, 1);
        } else {
            cartItem.incrementUnit();
        }
        currentCart.put(product.getId(), cartItem);
        updateLiveData(currentCart);
    }

    // --- FIX: Use the new independent CartAction enum ---
    public void incrementUnit(Product product) {
        modifyQuantity(product, CartAction.INC_UNIT);
    }
    public void decrementUnit(Product product) {
        modifyQuantity(product, CartAction.DEC_UNIT);
    }
    public void incrementCase(Product product) {
        modifyQuantity(product, CartAction.INC_CASE);
    }
    public void decrementCase(Product product) {
        modifyQuantity(product, CartAction.DEC_CASE);
    }
    public void removeFromCart(Product product) {
        modifyQuantity(product, CartAction.REMOVE);
    }

    // --- FIX: Use the new independent CartAction enum ---
    private void modifyQuantity(Product product, CartAction action) {
        Map<String, CartItem> currentCart = _cartMap.getValue();
        if (currentCart == null) currentCart = new HashMap<>();

        CartItem cartItem = currentCart.get(product.getId());
        if (cartItem == null) {
            if (action == CartAction.INC_UNIT || action == CartAction.INC_CASE) {
                cartItem = new CartItem(product, 0);
                currentCart.put(product.getId(), cartItem);
            } else {
                return;
            }
        }

        switch (action) {
            case INC_UNIT: cartItem.incrementUnit(); break;
            case DEC_UNIT: cartItem.decrementUnit(); break;
            case INC_CASE: cartItem.incrementCase(); break;
            case DEC_CASE: cartItem.decrementCase(); break;
            case REMOVE: cartItem = new CartItem(product, 0); break;
        }

        if (cartItem.getQuantity() == 0) {
            currentCart.remove(product.getId());
        } else {
            currentCart.put(product.getId(), cartItem);
        }

        updateLiveData(currentCart);
    }

    private void updateLiveData(Map<String, CartItem> newCartMap) {
        _cartMap.setValue(newCartMap);
        recalculateTotals(newCartMap);
    }

    // --- FIX: Use the new independent CartTotals class ---
    private void recalculateTotals(Map<String, CartItem> cartMap) {
        double totalMrp = 0, totalRate = 0;
        int uniqueItemCount = 0, totalUnitCount = 0;

        for (CartItem item : cartMap.values()) {
            if (item.getQuantity() > 0) {
                totalMrp += item.getTotalMrp();
                totalRate += item.getTotalPrice();
                totalUnitCount += item.getQuantity();
                uniqueItemCount++;
            }
        }

        double totalSavings = totalMrp - totalRate;
        _cartTotals.setValue(new CartTotals(totalRate, totalSavings, totalMrp, uniqueItemCount, totalUnitCount));
    }
}
