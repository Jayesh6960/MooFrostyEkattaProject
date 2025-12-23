package com.example.moofrosty.ui.dashboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moofrosty.data.model.BeatModel;
import com.example.moofrosty.data.model.CalendarDateModel;
import com.example.moofrosty.R;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DashboardMyBeatFragment extends Fragment {

    private MyBeatViewModel viewModel;
    private MyBeatStoreAdapter storeAdapter;

    // UI
    private TextView tvBeatDropdown, tvOrderValue, tvVisitedCount, tvOrderTakenCount;
    private EditText searchBar;
    private RecyclerView calendarRecycler, storeRecycler;
   // private ChipGroup filterChipGroup;
    private ImageView btnMap;
    private TabLayout tabLayoutFilter;



    public DashboardMyBeatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_my_beat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvBeatDropdown = view.findViewById(R.id.tv_beat_dropdown);
        tvOrderValue = view.findViewById(R.id.tv_order_val);
        tvVisitedCount = view.findViewById(R.id.tv_visited_count);
        tvOrderTakenCount = view.findViewById(R.id.tv_order_taken_count);
        searchBar = view.findViewById(R.id.et_search);
        calendarRecycler = view.findViewById(R.id.calendar_recycler);
        storeRecycler = view.findViewById(R.id.store_recycler);
     //   filterChipGroup = view.findViewById(R.id.filter_chip_group);
        tabLayoutFilter = view.findViewById(R.id.tab_layout_filter);
        btnMap = view.findViewById(R.id.btn_map_view);

        // 2. Setup ViewModel
        viewModel = new ViewModelProvider(this).get(MyBeatViewModel.class);

        // 3. Setup Calendar (Horizontal)
        setupCalendar();

        setupTabs();

        // 4. Setup Store List
        storeRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        storeAdapter = new MyBeatStoreAdapter(requireContext(), new ArrayList<>());
        storeRecycler.setAdapter(storeAdapter);

        // 5. Observers
        viewModel.getFilteredStores().observe(getViewLifecycleOwner(), stores -> {
            storeAdapter.updateList(stores);
            searchBar.setHint(stores.size() + " Store(s)");
        });

        viewModel.getTotalOrderValue().observe(getViewLifecycleOwner(), val -> tvOrderValue.setText(String.valueOf(val.intValue())));
        viewModel.getVisitedCountText().observe(getViewLifecycleOwner(), txt -> tvVisitedCount.setText(txt));
        viewModel.getOrderTakenCountText().observe(getViewLifecycleOwner(), txt -> tvOrderTakenCount.setText(txt));

        viewModel.getBeats().observe(getViewLifecycleOwner(), beats -> {
            // Update dropdown text based on selection
            StringBuilder sb = new StringBuilder("Beat: ");
            int selectedCount = 0;
            for(BeatModel b : beats) {
                if(b.isSelected()) {
                    if(selectedCount > 0) sb.append(", ");
                    sb.append(b.getName());
                    selectedCount++;
                }
            }
            if(selectedCount == beats.size()) tvBeatDropdown.setText("Beat: All Selected");
            else if(selectedCount == 0) tvBeatDropdown.setText("Beat: None");
            else tvBeatDropdown.setText(sb.toString());
        });

        // 6. Listeners

        // Dropdown Click -> Show Multi-Select Dialog
        tvBeatDropdown.setOnClickListener(v -> showBeatSelectionDialog());

        // Chip Filters
//        filterChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            String filter = "All";
//            if(checkedId == R.id.chip_visited) filter = "Visited";
//            else if(checkedId == R.id.chip_not_visited) filter = "Not Visited";
//            else if(checkedId == R.id.chip_order) filter = "Order Taken";
//            viewModel.onTabFilterChanged(filter);
//        });

        // Search Bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.onSearchQueryChanged(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Map Button
        btnMap.setOnClickListener(v -> {
            // Opens current location on map
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=my+location");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(requireContext(), "Google Maps not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupTabs() {
        // Add Tabs
        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("All"));
        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("Not Visited"));
        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("Visited"));
        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("Order Taken"));

//        for (int i = 0; i < tabLayoutFilter.getChildCount(); i++) {
//            View child = tabLayoutFilter.getChildAt(i);
//            if (child instanceof ViewGroup) {
//                ViewGroup slidingTabStrip = (ViewGroup) child;
//                for (int j = 0; j < slidingTabStrip.getChildCount(); j++) {
//                    View tabView = slidingTabStrip.getChildAt(j);
//                    if (tabView instanceof ViewGroup) {
//                        ViewGroup tabViewGroup = (ViewGroup) tabView;
//                        for (int k = 0; k < tabViewGroup.getChildCount(); k++) {
//                            View tabChild = tabViewGroup.getChildAt(k);
//                            if (tabChild instanceof TextView) {
//                                ((TextView) tabChild).setAllCaps(false);
//                            }
//                        }
//                    }
//                }
//            }
//        }

        // Listener
        tabLayoutFilter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String filter = tab.getText().toString();
                viewModel.onTabFilterChanged(filter);
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupCalendar() {
        // Generate last 2 days + today + next 4 days
        List<CalendarDateModel> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -2);

        for(int i=0; i<7; i++) {
            String day = new SimpleDateFormat("EEE", Locale.getDefault()).format(cal.getTime());
            String date = new SimpleDateFormat("dd", Locale.getDefault()).format(cal.getTime());
            // Check if it's today (simple logic for demo, assumes 3rd item is today based on loop)
            boolean isToday = i == 2;
            dates.add(new CalendarDateModel(day, date, isToday));
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        MyBeatCalendarAdapter adapter = new MyBeatCalendarAdapter(dates);
        calendarRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        calendarRecycler.setAdapter(adapter);
        calendarRecycler.scrollToPosition(1); // Center it a bit
    }

    private void showBeatSelectionDialog() {
        List<BeatModel> beats = viewModel.getBeats().getValue();
        if(beats == null) return;

        String[] beatNames = new String[beats.size()];
        boolean[] checkedItems = new boolean[beats.size()];

        for(int i=0; i<beats.size(); i++) {
            BeatModel b = beats.get(i);
            beatNames[i] = b.getName() + " (" + b.getTotalStores() + " Stores)";
            checkedItems[i] = b.isSelected();
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Select Beat")
                .setMultiChoiceItems(beatNames, checkedItems, (dialogInterface, which, isChecked) -> {
                    viewModel.onBeatSelectionChanged(which, isChecked);
                })
                .setPositiveButton("OK", null)
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
    }
}