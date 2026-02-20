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

public class SessionManager {

    private static final String PREF_NAME = "MoofrostySession";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_DETAILS = "user_details";
    private static final String KEY_USER_JSON = "user_json";

    private static final String KEY_USER_ID = "user_id";

    // NEW keys (Beat)
    private static final String KEY_BEAT_ID = "beat_id";
    private static final String KEY_BEAT_NAME = "beat_name";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private Gson gson;

    // 🔹 NEW KEY (ONLY ADDITION FOR LOCATION FLOW)
    private static final String KEY_LOCATION_READY = "location_ready";
    private static final String KEY_IS_PRESENT = "is_present";

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

    public void saveUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, 0); // 0 = not logged in / invalid
    }

    public void saveShopId(int shopId) {
        editor.putInt("shop_id", shopId);
        editor.apply();
    }

    // GET
    public int getShopId() {
        return pref.getInt("shop_id", 0);
    }

    // OPTIONAL (on checkout complete)
    public void clearShopId() {
        editor.remove("shop_id").apply();
    }

//    public UserDetail getUserDetail() {
//        String json = pref.getString(KEY_USER_DETAILS, "");
//        if (json.isEmpty()) return null;
//        return gson.fromJson(json, UserDetail.class);
//    }


    // ---------------- BEAT (NEW + REQUIRED) ----------------
//    public void saveBeat(UserDetailResponse.Data data) {
//        if (data != null && data.getBeat() != null) {
//            editor.putInt(KEY_BEAT_ID, data.getBeat().beatId);
//            editor.putString(
//                    KEY_BEAT_NAME,
//                    data.getBeat().beatNameFrom + " - " + data.getBeat().beatNameTo
//            );
//            editor.apply();
//        }
//    }

    public String getUserFullName() {
        String json = pref.getString(KEY_USER_JSON, "");
        String fullName = "Welcome User"; // Default fallback

        if (json.isEmpty() || json.equals("{}")) return fullName;

        try {
            JSONObject root = new JSONObject(json);
            JSONObject userObj = null;

            // In your LoginResponse JSON, 'user' is inside the root object
            if (root.has("user") && !root.isNull("user")) {
                userObj = root.getJSONObject("user");
            }
            // Fallback: If you saved just the user object directly
            else if (root.has("firstName")) {
                userObj = root;
            }

            if (userObj != null) {
                String first = userObj.optString("firstName", "");
                String middle = userObj.optString("middleName", "");
                String last = userObj.optString("lastName", "");

                // Logic to handle empty middle name smoothly
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

    // --- Get Attendance Status ---
    //Need  to change in  Will have to change the boolean  Status
    public boolean isAttendanceMarked() {
        return pref.getBoolean(KEY_IS_PRESENT, false); // Default to false if not set
    }

    // --- Save Attendance Status ---
    public void saveIsPresent(boolean isPresent) {
        editor.putBoolean(KEY_IS_PRESENT, isPresent);
        editor.apply();
    }

    public boolean wasLocationFlowCompleted() {
        return pref.getBoolean(KEY_LOCATION_READY, false);
    }
}
