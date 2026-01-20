package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductResponse {

    @SerializedName("status")
    public boolean status;
    @SerializedName("data")
    public List<ProductApiModel> data;
}
