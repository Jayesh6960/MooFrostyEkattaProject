package com.example.moofrosty;

import static android.app.PendingIntent.getActivity;

import androidx.fragment.app.Fragment;

import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.ui.splash.BaseActivity;

public class BaseFragment extends Fragment {

    protected BaseActivity getBaseActivity() {
        if (getActivity() instanceof BaseActivity) {
            return (BaseActivity) getActivity();
        } else {
            throw new RuntimeException("Activity must extend BaseActivity");
        }
    }

    protected SessionManager getSession() {
        return getBaseActivity().sessionManager;
    }

    protected void logoutUser() {
        getBaseActivity().logoutUser();
    }
}