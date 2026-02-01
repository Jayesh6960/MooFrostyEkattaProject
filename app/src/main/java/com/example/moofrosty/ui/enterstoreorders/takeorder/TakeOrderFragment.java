package com.example.moofrosty.ui.enterstoreorders.takeorder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.moofrosty.R;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.Product;
import com.example.moofrosty.data.model.ProductApiModel;
import com.example.moofrosty.ui.cart.CartViewModel;
import com.example.moofrosty.ui.enterstoreorders.categories.BottomSheetSubCategeries;
import com.example.moofrosty.ui.filter.FilterSelectionListener;
import com.example.moofrosty.ui.filter.FilterViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TakeOrderFragment extends Fragment implements FilterSelectionListener, ProductAdapter.CartInteractionListener {

//    private RecyclerView recyclerView;
//    private ProductAdapter adapter;
//    private List<Product> productList;
//    private AutoCompleteTextView filterTextView;
//    private String selectedCategory = "All"; // Current category filter
//    private String selectedBrand = "All";    // Current brand filter
//    private BottomSheetSubCategeries bottomSheet;
//
//    private FilterViewModel filterViewModel;
//    private ImageButton refreshButton;
//
//    // --- NEW: Add CartViewModel ---
//    private CartViewModel cartViewModel;

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<ProductApiModel> fullProductList = new ArrayList<>();
    private AutoCompleteTextView filterTextView;
    private ProgressBar progressBar;

    private ProductViewModel productViewModel;
    private FilterViewModel filterViewModel;
    private CartViewModel cartViewModel;
    private SessionManager sessionManager;
    private String selectedCategory = "All";
    private String selectedSubCategory = "All";


    public TakeOrderFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_take_order, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        String token = sessionManager.getToken();

        recyclerView = view.findViewById(R.id.product_recycler_view);
        filterTextView = view.findViewById(R.id.allcateoriesautocomplete);
        progressBar = view.findViewById(R.id.progressBar);
        TextInputLayout inputLayoutFilter = view.findViewById(R.id.allcateories);
        ImageButton refreshButton = view.findViewById(R.id.refreshbtn);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);

        adapter = new ProductAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Load Data
        if (NetworkUtil.isNetworkAvailable(getContext())) {
            productViewModel.loadProducts(token);
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }

        // --- Observer using Resource<T> ---
        productViewModel.getProducts().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        if (resource.data != null && resource.data.data != null) {
                            fullProductList.clear();
                            fullProductList.addAll(resource.data.data);
                            filterProductList(); // Apply current filters
                        }
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), resource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // Filter Logic
        filterViewModel.selectedCategory.observe(getViewLifecycleOwner(), category -> {
            selectedCategory = category;
            filterProductList();
        });

        // Observer 2: Sub-Category Changed (NEW)
        filterViewModel.selectedSubCategory.observe(getViewLifecycleOwner(), subCategory -> {
            selectedSubCategory = subCategory;
            filterProductList();
        });

        cartViewModel.getCartMap().observe(getViewLifecycleOwner(), cartMap -> {
            adapter.setCartMap(cartMap);
        });

        // Open BottomSheet
        inputLayoutFilter.setEndIconOnClickListener(v -> {
            BottomSheetSubCategeries bottomSheet = new BottomSheetSubCategeries();
            bottomSheet.show(getChildFragmentManager(), "BottomSheetSubCategeries");
        });

        refreshButton.setOnClickListener(v -> {
            filterViewModel.setCategory("All");
            if (NetworkUtil.isNetworkAvailable(getContext())) {
                productViewModel.loadProducts(token);
            }
        });
    }

    // --- MAIN FILTERING LOGIC ---
    private void filterProductList() {
        List<ProductApiModel> filteredList = new ArrayList<>(fullProductList);

        // 1. Filter by Main Category
        if (!"All".equals(selectedCategory)) {
            filteredList = filteredList.stream()
                    .filter(p -> p.category != null && p.category.categoryTitle.equalsIgnoreCase(selectedCategory))
                    .collect(Collectors.toList());
        }

        // 2. Filter by Sub-Category (AND logic)
        if (!"All".equals(selectedSubCategory)) {
            filteredList = filteredList.stream()
                    .filter(p -> p.subcategory != null && p.subcategory.subcategoryTitle.equalsIgnoreCase(selectedSubCategory))
                    .collect(Collectors.toList());
        }

        adapter.updateList(filteredList);

        // Update the text view to show breadcrumb-style info
        if ("All".equals(selectedCategory)) {
            filterTextView.setText("All Categories");
        } else if ("All".equals(selectedSubCategory)) {
            filterTextView.setText(selectedCategory);
        } else {
            filterTextView.setText(selectedCategory + " , " + selectedSubCategory);
        }
    }

//    private void filterProductList() {
//        List<ProductApiModel> filteredList = new ArrayList<>(fullProductList);
//        if (selectedCategory != null && !selectedCategory.equals("All")) {
//            filteredList = filteredList.stream()
//                    .filter(p -> (p.category != null && p.category.categoryTitle.equalsIgnoreCase(selectedCategory)) ||
//                            (p.subcategory != null && p.subcategory.subcategoryTitle.equalsIgnoreCase(selectedCategory)))
//                    .collect(Collectors.toList());
//        }
//        adapter.updateList(filteredList);
//        filterTextView.setText(selectedCategory.equals("All") ? "All Categories" : selectedCategory);
//    }

    // --- Implementation of FilterSelectionListener ---
    @Override
    public void onFilterSelected(String filterType, String value) {
        if ("category".equals(filterType)) {
            filterViewModel.setCategory(value);
        } else if ("subcategory".equals(filterType)) {
            // FIXED: Set the sub-category specifically
            filterViewModel.setSubCategory(value);
        }
    }

    public void onSearchQuery(String query) {
        if (adapter != null) adapter.filterList(fullProductList, query);
    }

    // --- Converter: API Model -> Cart Model ---
    private Product mapToCartProduct(ProductApiModel apiModel) {
        String productType = "";

        if (apiModel.batches != null && !apiModel.batches.isEmpty()) {
            productType = apiModel.batches.get(0).productType;
        }
        Product product = new Product(
                String.valueOf(apiModel.productId),
                apiModel.productName,
                apiModel.getMrp(),
                apiModel.getSellingPrice(),
                apiModel.getMargin(),
                apiModel.getStock(),
                apiModel.getCapacity(),
                "0", "0", "0", 0,
                apiModel.category != null ? apiModel.category.categoryTitle : "",
                "",
//                apiModel.productType,
                productType,
                apiModel.productImage
        );
        try {
            product.caseSize = Integer.parseInt(apiModel.getUnit());
        } catch (Exception e) { product.caseSize = 1; }
        return product;
    }

    @Override
    public void onAddToCartClick(ProductApiModel apiModel) {
        Product product = mapToCartProduct(apiModel);

        // Logic Change: If type is "case", add 1 CASE. Else add 1 UNIT.
        String productType = null;
        if (apiModel.batches != null && !apiModel.batches.isEmpty()) {
            productType = apiModel.batches.get(0).productType;
        }
        if ("case".equalsIgnoreCase(productType)) {
            cartViewModel.incrementCase(product);
        } else {
            cartViewModel.addToCart(product); // This adds 1 unit (default)
        }
    }

//    @Override public void onAddToCartClick(ProductApiModel p) { cartViewModel.addToCart(mapToCartProduct(p)); }
    @Override public void onIncrementUnit(ProductApiModel p) { cartViewModel.incrementUnit(mapToCartProduct(p)); }
    @Override public void onDecrementUnit(ProductApiModel p) { cartViewModel.decrementUnit(mapToCartProduct(p)); }
    @Override public void onIncrementCase(ProductApiModel p) { cartViewModel.incrementCase(mapToCartProduct(p)); }
    @Override public void onDecrementCase(ProductApiModel p) { cartViewModel.decrementCase(mapToCartProduct(p)); }

//        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
//
//        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class); // ADD THIS
//
//        recyclerView = view.findViewById(R.id.product_recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        TextInputLayout inputLayoutFilter = view.findViewById(R.id.allcateories);
//        filterTextView = view.findViewById(R.id.allcateoriesautocomplete);
//        refreshButton = view.findViewById(R.id.refreshbtn);
//
//        loadProducts();
//
//        adapter = new ProductAdapter(productList,this);
//        recyclerView.setAdapter(adapter);
//
//        // Disable scrolling for RecyclerView since it's inside a NestedScrollView
//        recyclerView.setNestedScrollingEnabled(false);
//
//        inputLayoutFilter.setEndIconOnClickListener(v -> {
//            // Open your bottom sheet when the arrow is clicked
//            bottomSheet = new BottomSheetSubCategeries();
//            bottomSheet.show(getChildFragmentManager(), "BottomSheetSubCategeries");
//        });
//
//        setupRefreshListener();
//
//        filterViewModel.selectedCategory.observe(getViewLifecycleOwner(), category -> {
//            selectedCategory = category;
//            filterProductList(); // Re-run filter
//        });
//        filterViewModel.selectedBrand.observe(getViewLifecycleOwner(), brand -> {
//            selectedBrand = brand;
//            filterProductList(); // Re-run filter
//        });
//
//        cartViewModel.getCartMap().observe(getViewLifecycleOwner(), cartMap -> {
//            if (adapter != null) {
//                adapter.setCartMap(cartMap);
//            }
//        });
//
//        // Initial filter
//        filterProductList();
//    }
//
//    private void setupRefreshListener() {
//        refreshButton.setOnClickListener(v -> {
//            // 1. Reset the ViewModel filters
//            filterViewModel.setCategory("All");
//            filterViewModel.setBrand("All");
//
//            if (getActivity() != null) {
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container, new TakeOrderFragment());
//                fragmentTransaction.commit();
//            }
//        });
//    }
//
//    private void loadProducts() {
//
//        productList = new ArrayList<>();
//        productList.clear();
////        productList.add(new Product("Kw Bp Kulfi 4l", "₹1,000", "₹500", "100.0%", "0", "4L", "1", "0", "0", R.drawable.icecategori));
////        productList.add(new Product("Bis American Dry Nuts Bulk 4000ml", "₹870", "₹725", "20.0%", "0", "4000ml", "1", "0", "0", R.drawable.icecategori2)); // <-- YOUR IMAGE
////        productList.add(new Product("Kw Bp - American Nuts 4l", "₹800", "₹400", "100.0%", "17", "4L", "1", "0", "0", R.drawable.icecategori3));
//// --- Category: "Big Cones" ---
//        productList.add(new Product("P101","Amul Big Cone", "₹50", "₹40", "25.0%", "50", "150ml", "1", "10", "20", R.drawable.conecategory, "Big Cones", "Amul"));
//        productList.add(new Product("P102","Cornetto Choco Cone", "₹40", "₹35", "14.0%", "100", "120ml", "1", "20", "50", R.drawable.conecategory, "Big Cones", "Cornetto"));
//        productList.add(new Product("P103","Kwalitywall Choco Cone", "₹45", "₹40", "12.5%", "80", "130ml", "1", "15", "30", R.drawable.conecategory, "Big Cones", "kwalitywall"));
//        productList.add(new Product("P104","Havmore Cone", "₹55", "₹50", "10.0%", "60", "140ml", "1", "5", "15", R.drawable.conecategory, "Big Cones", "havemore"));
//
//        productList.add(new Product("P1001","Amul Ice cream Vanilla", "₹200", "₹180", "25.0%", "50", "1000ml", "1", "10", "20", R.drawable.amulfruitandnut, "Ice Cream", "Amul"));
//        productList.add(new Product("P1002","Amul Ice cream Fruit and Nut", "₹250", "₹240", "14.0%", "100", "1000ml", "1", "20", "50", R.drawable.amulvanella, "Ice Cream", "Amul"));
//        productList.add(new Product("P1003","Kwalitywall ice cream Mango", "₹300", "₹380", "12.5%", "80", "500ml", "1", "15", "30", R.drawable.kwmango, "Ice Cream", "kwalitywall"));
//        productList.add(new Product("P1004","Kwalitywall ice cream Gulab Jam", "₹400", "₹360", "10.0%", "60", "500ml", "1", "5", "15", R.drawable.kwgulab, "Ice Cream", "kwalitywall"));
//        productList.add(new Product("P1005","Kwalitywall ice cream Strawberry", "₹350", "₹320", "12.5%", "80", "500ml", "1", "15", "30", R.drawable.kwstrawberry, "Ice Cream", "kwalitywall"));
//        productList.add(new Product("P1006","Havmore ice cream Strawberry", "₹300", "₹280", "10.0%", "60", "1000ml", "1", "5", "15", R.drawable.havemorestrawberry, "Ice Cream", "havemore"));
//        productList.add(new Product("P1007","Havmore ice cream Chocolate", "₹350", "₹320", "10.0%", "60", "1000ml", "1", "5", "15", R.drawable.havemorechocolate, "Ice Cream", "havemore"));
//
//        // --- Category: "Kulfi" ---
//        productList.add(new Product("P201","Kw Bp Kulfi 4l", "₹1,000", "₹500", "100.0%", "0", "4L", "1", "0", "0", R.drawable.kulficategory, "Kulfi", "kwalitywall"));
//        productList.add(new Product("P202","Amul Kesar Kulfi", "₹30", "₹25", "20.0%", "100", "100ml", "1", "30", "60", R.drawable.kulficategory, "Kulfi", "Amul"));
//        productList.add(new Product("P203","Vadinal Mawa Kulfi", "₹35", "₹30", "16.7%", "70", "110ml", "1", "10", "25", R.drawable.kulficategory, "Kulfi", "Vadinal"));
//        productList.add(new Product("P204","Motherdairy Pista Kulfi", "₹30", "₹28", "7.1%", "90", "100ml", "1", "20", "40", R.drawable.kulficategory, "Kulfi", "motherdairy"));
//
//        // --- Category: "Cups" ---
//        productList.add(new Product("P301","Amul Vanilla Cup", "₹20", "₹18", "11.1%", "200", "100ml", "1", "50", "100", R.drawable.cupcategory, "Cups", "Amul"));
//        productList.add(new Product("P302","Motherdairy Choco Cup", "₹25", "₹22", "13.6%", "150", "100ml", "1", "40", "80", R.drawable.cupcategory, "Cups", "motherdairy"));
//        productList.add(new Product("P303","Arun Butterscotch Cup", "₹20", "₹18", "11.1%", "180", "100ml", "1", "30", "70", R.drawable.cupcategory, "Cups", "arun"));
//        productList.add(new Product("P304","Havmore Strawberry Cup", "₹25", "₹22", "13.6%", "130", "100ml", "1", "25", "50", R.drawable.cupcategory, "Cups", "havemore"));
//
//        // --- Category: "Family Pack" ---
//        productList.add(new Product("P401","Kw Bp - American Nuts 4l", "₹800", "₹400", "100.0%", "17", "4L", "1", "0", "0", R.drawable.familypackone, "Family Pack", "kwalitywall"));
//        productList.add(new Product("P402","Amul Tricone Family Pack", "₹250", "₹220", "13.6%", "30", "1L", "1", "5", "10", R.drawable.familypackone, "Family Pack", "Amul"));
//        productList.add(new Product("P403","Havmore Kesar Pista FP", "₹280", "₹250", "12.0%", "25", "1L", "1", "3", "8", R.drawable.familypackone, "Family Pack", "havemore"));
//
//        // --- Category: "Magnum kulfi" ---
//        productList.add(new Product("P501","Magnum Almond Brownie", "₹90", "₹85", "5.9%", "50", "100ml", "1", "10", "20", R.drawable.magnunkulficategory, "Magnum kulfi", "magnum"));
//        productList.add(new Product("P502","Magnum Classic", "₹85", "₹80", "6.25%", "60", "100ml", "1", "12", "25", R.drawable.magnunkulficategory, "Magnum kulfi", "magnum"));
//
//        // --- Category: "Family Pack Big" ---
//        productList.add(new Product("P601","Bis American Dry Nuts Bulk 4000ml", "₹870", "₹725", "20.0%", "0", "4000ml", "1", "0", "0", R.drawable.familypackcategory, "Family Pack Big", "Amul"));
//        productList.add(new Product("P602","Vadinal Party Pack 2L", "₹500", "₹450", "11.1%", "10", "2L", "1", "1", "5", R.drawable.familypackcategory, "Family Pack Big", "Vadinal"));
//        productList.add(new Product("P603","Kwalitywall Party Pack 2L", "₹520", "₹470", "10.6%", "12", "2L", "1", "2", "6", R.drawable.familypackcategory, "Family Pack Big", "kwalitywall"));
//
//        // --- Category: "Family Pack Small" ---
//        productList.add(new Product("P701","Motherdairy Vanilla 750ml", "₹180", "₹160", "12.5%", "20", "750ml", "1", "4", "9", R.drawable.familypackone, "Family Pack Small", "motherdairy"));
//        productList.add(new Product("P702","Arun Kaju Kismis 500ml", "₹150", "₹140", "7.1%", "30", "500ml", "1", "6", "12", R.drawable.familypackone, "Family Pack Small", "arun"));
//        productList.add(new Product("P703","Havmore Choco Chips 500ml", "₹160", "₹150", "6.7%", "35", "500ml", "1", "8", "15", R.drawable.familypackone, "Family Pack Small", "havemore"));
//
//        // --- Category: "Mini Cone" ---
//        productList.add(new Product("P801","Cornetto Mini", "₹120", "₹100", "20.0%", "40", "6 pack", "1", "10", "30", R.drawable.miniconecategory, "Mini Cone", "Cornetto"));
//        productList.add(new Product("P802","Amul Mini Cone Pack", "₹100", "₹90", "11.1%", "45", "5 pack", "1", "11", "33", R.drawable.miniconecategory, "Mini Cone", "Amul"));
//
//        // --- Category: "Party Pack" ---
//        productList.add(new Product("P901","Vadinal Party Pack 2L", "₹500", "₹450", "11.1%", "10", "2L", "1", "1", "5", R.drawable.familypacktwo, "Party Pack", "Vadinal"));
//        productList.add(new Product("P902","Kwalitywall Party Pack 2L", "₹520", "₹470", "10.6%", "12", "2L", "1", "2", "6", R.drawable.familypacktwo, "Party Pack", "kwalitywall"));
//        productList.add(new Product("P903","Amul Party Pack 2L", "₹490", "₹440", "11.4%", "15", "2L", "1", "3", "7", R.drawable.familypacktwo, "Party Pack", "Amul"));    }
//
//    private void filterProductList() {
//        // Start with the full list
//        List<Product> filteredList = new ArrayList<>(productList);
//
//        // --- Filter by Category ---
//        if (selectedCategory != null && !selectedCategory.equals("All")) {
//            // Use Java 8 stream to filter
//            filteredList = filteredList.stream()
//                    .filter(product -> product.getCategory().equals(selectedCategory))
//                    .collect(Collectors.toList());
//        }
//
//        // --- Filter by Brand ---
//        if (selectedBrand != null && !selectedBrand.equals("All")) {
//            // Filter the *already filtered* list
//            filteredList = filteredList.stream()
//                    .filter(product -> product.getBrand().equals(selectedBrand))
//                    .collect(Collectors.toList());
//        }
//
//        // Update the adapter with the new filtered list
//        if (adapter != null) {
//            adapter.updateList(filteredList);
//        }
//        // Update the text view
//        if ((selectedCategory == null || selectedCategory.equals("All")) && (selectedBrand == null || selectedBrand.equals("All"))) {
//            filterTextView.setText("All Categories");
//        } else if (selectedCategory != null && !selectedCategory.equals("All") && (selectedBrand == null || selectedBrand.equals("All"))) {
//            filterTextView.setText(selectedCategory);
//        } else if ((selectedCategory == null || selectedCategory.equals("All")) && selectedBrand != null && !selectedBrand.equals("All")) {
//            filterTextView.setText(selectedBrand);
//        } else {
//            // Both are selected
//            filterTextView.setText(selectedCategory + ", " + selectedBrand);
//        }
//    }
//
//
//    @Override
//    public void onFilterSelected(String filterType, String value) {
//        // This is called from the BottomSheet, so we update the ViewModel
//        if (filterType.equals("category")) {
//            filterViewModel.setCategory(value);
//        } else if (filterType.equals("brand")) {
//            filterViewModel.setBrand(value);
//        }
//
//        if (bottomSheet != null) {
//            bottomSheet.dismiss();
//        }
//    }
//
//    public void onSearchQuery(String query) {
//        if (adapter != null && productList != null) {
//            // We filter from 'allProducts' which is your master list
//            adapter.filterList(productList, query);
//        }
//    }
//
//    @Override
//    public void onAddToCartClick(Product product) {
//        cartViewModel.addToCart(product);
//    }
//    @Override
//    public void onIncrementUnit(Product product) {
//        cartViewModel.incrementUnit(product);
//    }
//    @Override
//    public void onDecrementUnit(Product product) {
//        cartViewModel.decrementUnit(product);
//    }
//    @Override
//    public void onIncrementCase(Product product) {
//        cartViewModel.incrementCase(product);
//    }
//    @Override
//    public void onDecrementCase(Product product) {
//        cartViewModel.decrementCase(product);
//    }
//    }
}