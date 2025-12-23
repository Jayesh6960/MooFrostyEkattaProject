package com.example.moofrosty.ui.store;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StorePagerAdapter extends FragmentStateAdapter{

    public StorePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new StoreProfileFragment();
        } else {
            return new OrderHistoryFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Profile, History
    }
}
