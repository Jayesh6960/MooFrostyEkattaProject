package com.example.moofrosty.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.example.moofrosty.data.model.UserDetailResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
//Session manager is for the Global
public class SessionManager {

    private static final String PREF_NAME = "MoofrostySession";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_DETAILS = "user_details";
    private static final String KEY_USER_JSON = "user_json";

    // NEW keys (Beat)
    private static final String KEY_BEAT_ID = "beat_id";
    private static final String KEY_BEAT_NAME = "beat_name";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private Gson gson;

    // 🔹 NEW KEY (ONLY ADDITION FOR LOCATION FLOW)
    private static final String KEY_LOCATION_READY = "location_ready";

//    public SessionManager(Context context) {
//        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        editor = pref.edit();
//    }

    public SessionManager(Context context) {
        this.context = context;
        this.pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = pref.edit();
        this.gson = new Gson();
    }

    public void saveLoginSession(String token, String fullJsonString) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER_JSON, fullJsonString);
        editor.apply();
    }
    public String readUserDetails() {
        return pref.getString(KEY_USER_JSON, "{}");
    }

    public String getToken() {
        return pref.getString(KEY_TOKEN, "");
    }

    public boolean isLoggedIn() {
        return !getToken().isEmpty();
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    // ---------------- BEAT (NEW + REQUIRED) ----------------

    public String getUserFullName() {
        String json = pref.getString(KEY_USER_JSON, "");
        String fullName = "Welcome User"; // Default fallback

        if (json.isEmpty() || json.equals("{}")) return fullName;

        try {
            JSONObject root = new JSONObject(json);
            JSONObject userObj = null;

            if (root.has("user") && !root.isNull("user")) {
                userObj = root.getJSONObject("user");
            } else if (root.has("firstName")) {
                userObj = root;
            }

            if (userObj != null) {
                String first = userObj.optString("firstName", "");
                String middle = userObj.optString("middleName", "");
                String last = userObj.optString("lastName", "");

                if (!middle.isEmpty()) {
                    fullName = first + " " + middle + " " + last;
                } else {
                    fullName = first + " " + last;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fullName;
    }

    public String getUserMobile() {
        String json = pref.getString(KEY_USER_JSON, "");
        if (json.isEmpty() || json.equals("{}")) return "";

        try {
            JSONObject root = new JSONObject(json);
            JSONObject userObj = null;

            if (root.has("user") && !root.isNull("user")) {
                userObj = root.getJSONObject("user");
            } else if (root.has("firstName")) {
                userObj = root;
            }

            if (userObj != null) {
                return userObj.optString("mobileNumber", "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 🔹 REAL SYSTEM CHECK (UNCHANGED – PERFECT)
    public boolean isLocationAndPermissionsEnabled() {

        boolean fineLocation =
                ContextCompat.checkSelfPermission(context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        boolean backgroundLocationGranted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            backgroundLocationGranted =
                    ContextCompat.checkSelfPermission(context,
                            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            == PackageManager.PERMISSION_GRANTED;
        }

        boolean cameraGranted =
                ContextCompat.checkSelfPermission(context,
                        android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED;

        boolean contactsGranted =
                ContextCompat.checkSelfPermission(context,
                        android.Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED;

        boolean allPermissionsGranted =
                fineLocation && backgroundLocationGranted && cameraGranted && contactsGranted;

        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean gpsEnabled = false;
        if (locationManager != null) {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        return allPermissionsGranted && gpsEnabled;
    }

    public int getBeatId() {
        return pref.getInt(KEY_BEAT_ID, 0);
    }

    public String getBeatName() {
        return pref.getString(KEY_BEAT_NAME, "");
    }

    // 🔹 ONLY UPDATED PART (REQUIRED)
    public void setLocationAndPermissionsEnabled(boolean enabled) {
        // This does NOT mean real permission state
        // Only remembers that user completed permission + GPS flow once
        editor.putBoolean(KEY_LOCATION_READY, enabled);
        editor.apply();
    }

    public boolean wasLocationFlowCompleted() {
        return pref.getBoolean(KEY_LOCATION_READY, false);
    }
}
