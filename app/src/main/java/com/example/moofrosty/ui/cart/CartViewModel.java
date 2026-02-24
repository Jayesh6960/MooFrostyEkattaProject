package com.example.moofrosty.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.CartItem;
import com.example.moofrosty.data.model.CartTotals;
import com.example.moofrosty.data.model.Order;
import com.example.moofrosty.data.model.OrderHistoryResponse;
import com.example.moofrosty.data.model.Product;
import com.example.moofrosty.data.repository.CartRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartViewModel extends ViewModel {

//    private final CartRepository cartRepository = CartRepository.getInstance();
//    private final MutableLiveData<Resource<String>> checkoutResult = new MutableLiveData<>();
//
//    // Session Data
//    private String token = "";
//    private int userId = 0;
//    private int shopId = 0;
//
//    public void setSessionData(String token, int userId, int shopId) {
//        this.token = token;
//        this.userId = userId;
//        this.shopId = shopId;
//    }
//
//    // Getters for UI
//    public LiveData<Map<String, CartItem>> getCartMap() {
//        return cartRepository.getCartMap();
//    }
//
//    public LiveData<CartTotals> getCartTotals() {
//        return cartRepository.getCartTotals();
//    }
//
//    public LiveData<Resource<String>> getCheckoutResult() {
//        return checkoutResult;
//    }
//
//    // Checkout Action
//    public void checkout() {
//        if(token.isEmpty()) return;
//        cartRepository.checkout(token, userId, shopId, checkoutResult);
//    }

    private final CartRepository cartRepository = CartRepository.getInstance();
    private final MutableLiveData<Resource<String>> checkoutResult = new MutableLiveData<>();

    // Session Data needed for API
    private String token = "";
    private int userId = 0;
    private int shopId = 0;

    // Called from Activity/Fragment to pass data
    public void setSessionData(String token, int userId, int shopId) {
        this.token = token;
        this.userId = userId;
        this.shopId = shopId;
    }

    public LiveData<Map<String, CartItem>> getCartMap() { return cartRepository.getCartMap(); }
    public LiveData<CartTotals> getCartTotals() { return cartRepository.getCartTotals(); }
    public LiveData<Resource<String>> getCheckoutResult() { return checkoutResult; }

    public void checkout() {
        if(token.isEmpty()) return;
        cartRepository.checkout(token, userId, shopId, checkoutResult);
    }

    // Cart Modifications
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



    // order history code

    private final MutableLiveData<Resource<OrderHistoryResponse>> orderHistory = new MutableLiveData<>();

    public LiveData<Resource<OrderHistoryResponse>> getOrderHistory() {
        return orderHistory;
    }

    // --- API Call Trigger ---
//    public void fetchOrders() {
//        if(token.isEmpty()) return;
//        cartRepository.getOrderHistory(token, orderHistory);
//    }

    public void fetchOrders() {
        if(token.isEmpty() || shopId == 0) return; // [HIGHLIGHT] Added shopId check

        // [HIGHLIGHT] Passing shopId to repository
        cartRepository.getOrderHistory(token, shopId, orderHistory);
    }

    public void clearCart() {
        cartRepository.clearCart();
    }





    // below code firt working

//    private final CartRepository cartRepository = CartRepository.getInstance();
//
//    private final MutableLiveData<Map<String, CartItem>> _cartMap = new MutableLiveData<>(new HashMap<>());    //  interger to string
//
//    public LiveData<Map<String, CartItem>> getCartMap() {
//        return cartRepository.getCartMap();
//    }
//
//    public LiveData<List<Order>> getOrders() {
//        return cartRepository.getOrders();
//    }
//
////    public void checkout() {
////        cartRepository.checkout();
////    }
//
//    private final MutableLiveData<Resource<String>> checkoutResult = new MutableLiveData<>();
//    private String token = "";
//    private int userId = 0; // Set these from SessionManager in Activity
//    private int shopId = 0;
//
//    public void setSessionData(String token, int userId, int shopId) {
//        this.token = token;
//        this.userId = userId;
//        this.shopId = shopId;
//    }
//
//
//
//    public LiveData<Resource<String>> getCheckoutResult() { return checkoutResult; }
//
//    public void checkout() {
//        if(token.isEmpty()) return;
//        cartRepository.checkout(token, userId, shopId, checkoutResult);
//    }
//
//    // LiveData for totals, calculated from the cartMap
//    private final MutableLiveData<CartTotals> _cartTotals = new MutableLiveData<>(new CartTotals());
//
//    public LiveData<CartTotals> getCartTotals() {
//        return cartRepository.getCartTotals();
//    }
//
//    public void addToCart(Product product) {
//        cartRepository.addToCart(product); // Just pass the call
//    }
//
//    public void incrementUnit(Product product) {
//        cartRepository.incrementUnit(product); // Just pass the call
//    }
//
//    public void decrementUnit(Product product) {
//        cartRepository.decrementUnit(product); // Just pass the call
//    }
//
//    public void incrementCase(Product product) {
//        cartRepository.incrementCase(product); // Just pass the call
//    }
//
//    public void decrementCase(Product product) {
//        cartRepository.decrementCase(product); // Just pass the call
//    }
//
//    public void removeFromCart(Product product) {
//        cartRepository.removeFromCart(product); // Just pass the call
//    }



    //   above code

    // --- Public Methods to Modify Cart ---

//    public void addToCart(Product product) {
//        Map<String, CartItem> currentCart = _cartMap.getValue();
//        if (currentCart == null) currentCart = new HashMap<>();
//
//        CartItem cartItem = currentCart.get(product.getId());
//        if (cartItem == null) {
//            cartItem = new CartItem(product, 1);
//        } else {
//            cartItem.incrementUnit();
//        }
//        currentCart.put(product.getId(), cartItem);
//        updateLiveData(currentCart);
//    }

//    public void incrementUnit(Product product) {
//        modifyQuantity(product, CartAction.INC_UNIT);
//    }
//    public void decrementUnit(Product product) {
//        modifyQuantity(product, CartAction.DEC_UNIT);
//    }
//    public void incrementCase(Product product) {
//        modifyQuantity(product, CartAction.INC_CASE);
//    }
//    public void decrementCase(Product product) {
//        modifyQuantity(product, CartAction.DEC_CASE);
//    }
//    public void removeFromCart(Product product) {
//        modifyQuantity(product, CartAction.REMOVE);
//    }
//
//    // --- Private Logic ---
//
//    private enum CartAction { INC_UNIT, DEC_UNIT, INC_CASE, DEC_CASE, REMOVE }
//
//    private void modifyQuantity(Product product, CartAction action) {
//        Map<Integer, CartItem> currentCart = _cartMap.getValue();
//        if (currentCart == null) currentCart = new HashMap<>();
//
//        CartItem cartItem = currentCart.get(product.getId());
//        if (cartItem == null) {
//            // Should not happen if using +/- buttons, but good to check
//            if (action == CartAction.INC_UNIT || action == CartAction.INC_CASE) {
//                cartItem = new CartItem(product, 0);
//                currentCart.put(product.getId(), cartItem);
//            } else {
//                return; // Can't decrement non-existent item
//            }
//        }
//
//        // Apply action
//        switch (action) {
//            case INC_UNIT: cartItem.incrementUnit(); break;
//            case DEC_UNIT: cartItem.decrementUnit(); break;
//            case INC_CASE: cartItem.incrementCase(); break;
//            case DEC_CASE: cartItem.decrementCase(); break;
//            case REMOVE: cartItem = new CartItem(product, 0); break; // Or remove from map
//        }
//
//        // If quantity is 0, remove it from the map
//        if (cartItem.getQuantity() == 0) {
//            currentCart.remove(product.getId());
//        } else {
//            currentCart.put(product.getId(), cartItem);
//        }
//
//        updateLiveData(currentCart);
//    }
//
//    private void updateLiveData(Map<Integer, CartItem> newCartMap) {
//        _cartMap.setValue(newCartMap);
//        recalculateTotals(newCartMap);
//    }
//
//    private void recalculateTotals(Map<Integer, CartItem> cartMap) {
//        double totalMrp = 0;
//        double totalRate = 0;
//        int uniqueItemCount = 0; // Number of unique products
//        int totalUnitCount = 0;  // Total number of all units
//
//        for (CartItem item : cartMap.values()) {
//            if (item.getQuantity() > 0) {
//                totalMrp += item.getTotalMrp();
//                totalRate += item.getTotalPrice();
//                totalUnitCount += item.getQuantity();
//                uniqueItemCount++;
//            }
//        }
//
//        double totalSavings = totalMrp - totalRate;
//        _cartTotals.setValue(new CartTotals(totalRate, totalSavings, totalMrp, uniqueItemCount, totalUnitCount));
//    }
//
//    // Helper class for totals
//    public static class CartTotals {
//        public final double totalPrice;
//        public final double totalSavings;
//        public final double totalMrp;
//        public final int uniqueItemCount; // For toolbar badge
//        public final int totalUnitCount;  // For cart page
//
//        public CartTotals() {
//            this(0, 0, 0, 0, 0);
//        }
//        public CartTotals(double price, double savings, double mrp, int uniqueCount, int unitCount) {
//            this.totalPrice = price;
//            this.totalSavings = savings;
//            this.totalMrp = mrp;
//            this.uniqueItemCount = uniqueCount;
//            this.totalUnitCount = unitCount;
//        }
//    }


    ///     this commet for activity
//    public void incrementUnit(Product product) {
//        modifyQuantity(product, CartAction.INC_UNIT);
//    }
//
//    public void decrementUnit(Product product) {
//        modifyQuantity(product, CartAction.DEC_UNIT);
//    }
//
//    public void incrementCase(Product product) {
//        modifyQuantity(product, CartAction.INC_CASE);
//    }
//
//    public void decrementCase(Product product) {
//        modifyQuantity(product, CartAction.DEC_CASE);
//    }
//
//    public void removeFromCart(Product product) {
//        modifyQuantity(product, CartAction.REMOVE);
//    }
//
//    private enum CartAction {INC_UNIT, DEC_UNIT, INC_CASE, DEC_CASE, REMOVE}
//
//    private void modifyQuantity(Product product, CartAction action) {
//        Map<String, CartItem> currentCart = _cartMap.getValue();
//        if (currentCart == null) currentCart = new HashMap<>();
//
//        CartItem cartItem = currentCart.get(product.getId());
//        if (cartItem == null) {
//            if (action == CartAction.INC_UNIT || action == CartAction.INC_CASE) {
//                cartItem = new CartItem(product, 0);
//                currentCart.put(product.getId(), cartItem);
//            } else {
//                return;
//            }
//        }
//
//        switch (action) {
//            case INC_UNIT:
//                cartItem.incrementUnit();
//                break;
//            case DEC_UNIT:
//                cartItem.decrementUnit();
//                break;
//            case INC_CASE:
//                cartItem.incrementCase();
//                break;
//            case DEC_CASE:
//                cartItem.decrementCase();
//                break;
//            case REMOVE:
//                cartItem = new CartItem(product, 0);
//                break;
//        }
//
//        if (cartItem.getQuantity() == 0) {
//            currentCart.remove(product.getId());
//        } else {
//            currentCart.put(product.getId(), cartItem);
//        }
//
//        updateLiveData(currentCart);
//    }
//
//    private void updateLiveData(Map<String, CartItem> newCartMap) {
//        _cartMap.setValue(newCartMap);
//        recalculateTotals(newCartMap);
//    }
//
//    private void recalculateTotals(Map<String, CartItem> cartMap) {
//        double totalMrp = 0, totalRate = 0;
//        int uniqueItemCount = 0, totalUnitCount = 0;
//
//        for (CartItem item : cartMap.values()) {
//            if (item.getQuantity() > 0) {
//                totalMrp += item.getTotalMrp();
//                totalRate += item.getTotalPrice();
//                totalUnitCount += item.getQuantity();
//                uniqueItemCount++;
//            }
//        }
//
//        double totalSavings = totalMrp - totalRate;
//        _cartTotals.setValue(new CartTotals(totalRate, totalSavings, totalMrp, uniqueItemCount, totalUnitCount));
//    }
//
//    // (CartTotals helper class remains the same)
//    public static class CartTotals {
//        public final double totalPrice;
//        public final double totalSavings;
//        public final double totalMrp;
//        public final int uniqueItemCount; // For toolbar badge
//        public final int totalUnitCount;  // For cart page
//
//        public CartTotals() {
//            this(0, 0, 0, 0, 0);
//        }
//
//        public CartTotals(double price, double savings, double mrp, int uniqueCount, int unitCount) {
//            this.totalPrice = price;
//            this.totalSavings = savings;
//            this.totalMrp = mrp;
//            this.uniqueItemCount = uniqueCount;
//            this.totalUnitCount = unitCount;
//        }
//    }

    // 6. Delete all private helper methods
    //    (DELETED: private enum CartAction ...)
    //    (DELETED: private void modifyQuantity(...) ...)
    //    (DELETED: private void updateLiveData(...) ...)
    //    (DELETED: private void recalculateTotals(...) ...)

    // 7. Keep the public static helper class (this is just a data structure)
//    public static class CartTotals {
//        public final double totalPrice;
//        public final double totalSavings;
//        public final double totalMrp;
//        public final int uniqueItemCount; // For toolbar badge
//        public final int totalUnitCount;  // For cart page
//
//        public CartTotals() {
//            this(0, 0, 0, 0, 0);
//        }
//
//        public CartTotals(double price, double savings, double mrp, int uniqueCount, int unitCount) {
//            this.totalPrice = price;
//            this.totalSavings = savings;
//            this.totalMrp = mrp;
//            this.uniqueItemCount = uniqueCount;
//            this.totalUnitCount = unitCount;
//        }
//    }
}
