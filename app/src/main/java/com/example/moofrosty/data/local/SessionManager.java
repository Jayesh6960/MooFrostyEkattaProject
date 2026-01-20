package com.example.moofrosty.data.local;

import android.content.Context;
import android.content.SharedPreferences;

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

    // NEW keys (Beat)
    private static final String KEY_BEAT_ID = "beat_id";
    private static final String KEY_BEAT_NAME = "beat_name";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private Gson gson;

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

    public int getBeatId() {
        return pref.getInt(KEY_BEAT_ID, 0);
    }

    public String getBeatName() {
        return pref.getString(KEY_BEAT_NAME, "");
    }
}
