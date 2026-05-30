package com.example.moofrosty.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtil {
    public static boolean isNetworkAvailable(Context mContext) {
        boolean isResult = Boolean.FALSE;

        final ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connManager != null) {
            final NetworkInfo netInfo = connManager.getActiveNetworkInfo();

            if (netInfo != null) {
                // Your preferred logic: Check if connected OR if it's a different valid network type
                if ((netInfo.isConnected() && netInfo.isAvailable()) ||
                        (netInfo.getType() != ConnectivityManager.TYPE_WIFI && netInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                    isResult = true;
                    Log.d("isNetworkAvailable", "isNetworkAvailable: ");
                }
            }
        }
        return isResult;
    }
}