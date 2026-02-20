package com.example.moofrosty.ui.menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moofrosty.ui.enterstoreorders.damageexpiry.DamageExpiryActivity;
import com.example.moofrosty.R;
import com.example.moofrosty.data.model.MenuOption;
import com.example.moofrosty.ui.offers.Offers;

import java.util.ArrayList;
import java.util.List;


public class MenuFragment extends Fragment {
//public class MenuFragment extends Fragment implements MenuAdapters.OnItemClickListener {

    private RecyclerView recyclerView;
    private MenuAdapters adapter;
    private List<MenuOption> menuOptionList;


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
//        recyclerView = view.findViewById(R.id.menu_recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        loadMenuOptions();
//
//      //  adapter = new MenuAdapters(menuOptionList);
//        adapter = new MenuAdapters(menuOptionList, this);
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void loadMenuOptions() {
//        // You'll need to add these icons to your res/drawable folder
//        // (Right-click > New > Vector Asset)
//        menuOptionList = new ArrayList<>();
//        menuOptionList.add(new MenuOption(R.drawable.offer, "Offers"));
////        menuOptionList.add(new MenuOption(R.drawable.ushop, "Ushop"));
////        menuOptionList.add(new MenuOption(R.drawable.merchanising, "Merchandising"));
////        menuOptionList.add(new MenuOption(R.drawable.claims, "Claims"));
//        menuOptionList.add(new MenuOption(R.drawable.damage, "Damage/Shortage/Expiry"));
////        menuOptionList.add(new MenuOption(R.drawable.shopkatta, "Shop Khata"));
//        menuOptionList.add(new MenuOption(R.drawable.referandearn, "Refer & Earn"));
////        menuOptionList.add(new MenuOption(R.drawable.mecorner, "ME Corner"));
////        menuOptionList.add(new MenuOption(R.drawable.changelang, "Change Language"));
////        menuOptionList.add(new MenuOption(R.drawable.customersupport, "Customer Service"));
////        menuOptionList.add(new MenuOption(R.drawable.list, "Smart List"));
//    }
//
//    @Override
//    public void onItemClick(int position) {
//        // Get the title of the clicked item
//        String selectedTitle = menuOptionList.get(position).getTitle();
//
//        // Use a switch statement to decide which screen to open
//        switch (selectedTitle) {
//            case "Offers":
//                // Example: Open an "OffersFragment"
//                //code updated  in the above activity
//                Intent intent1 = new Intent(getActivity(), Offers.class);
//                intent1.putExtra("ACTIVITY_TITLE", selectedTitle);
//                startActivity(intent1);
//                break;
//
//            case "Ushop":
//           //     loadFragment(new UshopFragment());
//                break;
//
//            case "Damage/Shortage/Expiry":
//                // Example: Open your "DamageExpiryActivity"
//                Intent intent = new Intent(getActivity(), DamageExpiryActivity.class);
//                intent.putExtra("ACTIVITY_TITLE", selectedTitle);
//                startActivity(intent);
//                break;
//
//            case "Claims":
//            //    loadFragment(new ClaimsFragment());
//                break;
//
//            case "Shop Khata":
//            //    loadFragment(new ShopKhataFragment());
//                break;
//
//            // ... Add cases for all your other options ...
//
//            case "Change Language":
//           //     loadFragment(new ChangeLanguageFragment());
//                break;
//
//            case "Smart List":
//            //    loadFragment(new SmartListFragment());
//                break;
//        }
//    }
//
//    // --- 4. Helper method to load a new fragment ---
//    private void loadFragment(Fragment fragment) {
//        // R.id.fragment_container is the ID of the FrameLayout in your *Activity*
//        // You may need to change R.id.fragment_container
//        getParentFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, fragment)
//                .addToBackStack(null) // This lets the user press 'back'
//                .commit();
//    }
}