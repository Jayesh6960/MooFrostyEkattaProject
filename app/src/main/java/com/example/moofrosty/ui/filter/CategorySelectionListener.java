package com.example.moofrosty.ui.filter;

import com.example.moofrosty.data.model.CategoryModel;

public interface CategorySelectionListener {
    void onCategorySelected(CategoryModel category);

    // Called when API loads list (Should set default ID, but NOT switch tab)
    void onDefaultCategoryLoaded(CategoryModel category);
}
