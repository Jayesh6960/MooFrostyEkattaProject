package com.example.moofrosty.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.BeatModel;
import com.example.moofrosty.data.model.CalendarDateModel;
import com.example.moofrosty.R;
import com.example.moofrosty.data.repository.MyBeatRepository;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DashboardMyBeatFragment extends Fragment {

    private MyBeatViewModel viewModel;
    private MyBeatStoreAdapter storeAdapter;

    // UI
    private TextView tvBeatDropdown, tvOrderValue, tvVisitedCount, tvOrderTakenCount, tvNoData;
    private TextInputEditText searchBar;
    private RecyclerView calendarRecycler, storeRecycler;
   // private ChipGroup filterChipGroup;
    private ImageView btnMap;
    private TabLayout tabLayoutFilter;
    private ProgressBar progressBar;
    private SessionManager sessionManager;


    public DashboardMyBeatFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_my_beat, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ✅ INIT VIEWMODEL (ONLY ONCE)
        // 2. Initialize Session Manager
        sessionManager = new SessionManager(requireContext());
        // 1. STANDARD VIEW MODEL INIT (No Factory needed because we use AndroidViewModel)
        viewModel = new ViewModelProvider(this).get(MyBeatViewModel.class);
        // Bind Views
        tvBeatDropdown = view.findViewById(R.id.tv_beat_dropdown);
        tvOrderValue = view.findViewById(R.id.tv_order_val);
        tvVisitedCount = view.findViewById(R.id.tv_visited_count);
        tvOrderTakenCount = view.findViewById(R.id.tv_order_taken_count);
        searchBar = view.findViewById(R.id.et_search);
        calendarRecycler = view.findViewById(R.id.calendar_recycler);
        storeRecycler = view.findViewById(R.id.store_recycler);
        tabLayoutFilter = view.findViewById(R.id.tab_layout_filter);
        btnMap = view.findViewById(R.id.btn_map_view);
        progressBar = view.findViewById(R.id.progress_bar);
        tvNoData = view.findViewById(R.id.tv_no_data);
        if (tvNoData == null) {
            // Handle case if you haven't added it to XML yet, prevents crash
            tvNoData = new TextView(requireContext());
        }


        // 3. Setup UI
        setupCalendar();
        setupTabs();

        storeRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        storeAdapter = new MyBeatStoreAdapter(requireContext(), new ArrayList<>());
        storeRecycler.setAdapter(storeAdapter);

        // 4. Observers
//        viewModel.getStoresResource().observe(getViewLifecycleOwner(), resource -> {
//            if (resource != null) {
//                switch (resource.status) {
//                    case LOADING:
//                        progressBar.setVisibility(View.VISIBLE);
//                        storeRecycler.setVisibility(View.GONE);
//                        tvNoData.setVisibility(View.GONE);
//                        break;
//                    case SUCCESS:
//                        progressBar.setVisibility(View.GONE);
//                        storeRecycler.setVisibility(View.VISIBLE);
//                        if (resource.data != null) {
//                            viewModel.setMasterStoreList(resource.data);
//                            tvNoData.setVisibility(View.GONE);
//                        }
//                        else {
//                            // Handle empty data
//                            storeAdapter.updateList(new ArrayList<>());
//                            tvNoData.setVisibility(View.VISIBLE);
//                        }
//                        break;
//                    case ERROR:
//                        progressBar.setVisibility(View.GONE);
//                        storeRecycler.setVisibility(View.GONE);
//                        tvNoData.setVisibility(View.VISIBLE);
//                        Toast.makeText(getContext(), resource.message, Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });
        viewModel.getStoresResource().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {

                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        storeRecycler.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.GONE);
                        break;

                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);

                        if (resource.data != null && !resource.data.isEmpty()) {

                            storeRecycler.setVisibility(View.VISIBLE);

                            // Save master list in ViewModel
                            viewModel.setMasterStoreList(resource.data);

                            // Update RecyclerView Adapter
                            storeAdapter.updateList(resource.data);

                            tvNoData.setVisibility(View.GONE);

                        } else {

                            // Handle empty data
                            storeRecycler.setVisibility(View.GONE);
                            storeAdapter.updateList(new ArrayList<>());
                            tvNoData.setVisibility(View.VISIBLE);
                        }
                        break;

                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        storeRecycler.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);

                        Toast.makeText(getContext(), resource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
//Store -->> Outlet
        viewModel.getFilteredStores().observe(getViewLifecycleOwner(), stores -> {
            storeAdapter.updateList(stores);
            searchBar.setHint(stores.size() + " Outlet(S)");
            if(stores.isEmpty()) tvNoData.setVisibility(View.VISIBLE);
            else tvNoData.setVisibility(View.GONE);
        });


//        viewModel.getBeats().observe(getViewLifecycleOwner(), beats -> {
//            StringBuilder sb = new StringBuilder("Beat Name : ");
//            int selectedCount = 0;
//            if (beats != null) {
//                for (BeatModel b : beats) {
//                    if (b.isSelected()) {
//                        if (selectedCount > 0) sb.append(", ");
//                        sb.append(b.getName());
//                        selectedCount++;
//                    }
//                }
//            }
//            if (selectedCount == 0) tvBeatDropdown.setText("Beat Name : None");
//            else tvBeatDropdown.setText(sb.toString());
//        });
//Ordered Values:Order values + Billed values
//        viewModel.getTotalOrderValue().observe(getViewLifecycleOwner(), val -> tvOrderValue.setText(String.valueOf(val.intValue())));
        viewModel.getTotalOrderValue().observe(getViewLifecycleOwner(), val -> {
            if (val != null) {
                // Formatting as an integer with the ₹ symbol for a clean UI
                tvOrderValue.setText("₹ " + val.intValue());
                Log.d("TotalOrderValue", "Total Order Value: ₹ " + val.intValue());
            }
        });
        // [HIGHLIGHT] Update the counts manually here based on the raw integers
        viewModel.getRepoTotalCount().observe(getViewLifecycleOwner(), total -> updateCountUI());
        viewModel.getRepoVisitedCount().observe(getViewLifecycleOwner(), visited -> updateCountUI());
        viewModel.getRepoOrderCount().observe(getViewLifecycleOwner(), ordered -> updateCountUI());

        // add this 02-03-2026  for mutlibeat
        viewModel.getBeatDropdownText().observe(getViewLifecycleOwner(), text -> {
            tvBeatDropdown.setText(text);
        });

        viewModel.getBeats().observe(getViewLifecycleOwner(), beats -> {
            viewModel.updateBeatDropdownText();
        });

        // [HIGHLIGHT] Setup Click Listener for Dropdown
        LinearLayout llBeatDropdown = view.findViewById(R.id.ll_beat_dropdown_container);
        if (llBeatDropdown != null) {
            llBeatDropdown.setOnClickListener(v -> {
                Log.d("DashboardFragment", "Dropdown Clicked!");
                showBeatSelectionDialog();
            });
        }

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.onSearchQueryChanged(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnMap.setOnClickListener(v -> {
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
        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("All"));
        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("Not Visited"));
        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("Visited"));
        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("Order Taken"));

        tabLayoutFilter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                storeAdapter.updateList(new ArrayList<>());
                viewModel.onTabFilterChanged(tab.getText().toString());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    // [HIGHLIGHT] Helper to format the strings
    private void updateCountUI() {
        int total = viewModel.getRepoTotalCount().getValue() != null ? viewModel.getRepoTotalCount().getValue() : 0;
        int visited = viewModel.getRepoVisitedCount().getValue() != null ? viewModel.getRepoVisitedCount().getValue() : 0;
        int ordered = viewModel.getRepoOrderCount().getValue() != null ? viewModel.getRepoOrderCount().getValue() : 0;

        tvVisitedCount.setText(visited + "/" + total);
        tvOrderTakenCount.setText(ordered + "/" + total);
    }

    private void setupCalendar() {
        List<CalendarDateModel> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -2);
        for(int i=0; i<7; i++) {
            String day = new SimpleDateFormat("EEE", Locale.getDefault()).format(cal.getTime());
            String date = new SimpleDateFormat("dd", Locale.getDefault()).format(cal.getTime());
            boolean isToday = i == 2;
            dates.add(new CalendarDateModel(day, date, isToday));
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        MyBeatCalendarAdapter adapter = new MyBeatCalendarAdapter(dates);
        calendarRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        calendarRecycler.setAdapter(adapter);
        calendarRecycler.scrollToPosition(1);
    }

    // [HIGHLIGHT] Dialog Logic
    private void showBeatSelectionDialog() {
        List<BeatModel> beats = viewModel.getBeats().getValue();

        // Let the user know if beats haven't loaded yet!
        if (beats == null || beats.isEmpty()) {
            Toast.makeText(requireContext(), "Fetching Beats... Please wait.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] names = new String[beats.size()];
        boolean[] checked = new boolean[beats.size()];

        for (int i = 0; i < beats.size(); i++) {
            BeatModel b = beats.get(i);
            names[i] = b.getName();
            checked[i] = b.isSelected();
        }

//        AlertDialog dialog = new AlertDialog.Builder(requireContext())
//                .setTitle("Select Beat(s)")
//                .setMultiChoiceItems(names, checked,
//                        (d, which, isChecked) -> viewModel.onBeatSelectionChanged(which, isChecked))
//                .setPositiveButton("OK", (d, which) -> {
//                    // Triggers API Call in ViewModel
//                    viewModel.confirmBeatSelection();
//                })
////                .setNegativeButton("Cancel", null)
//                .show();
//
//        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.Purple_Color));
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Beat(s)");
        builder.setMultiChoiceItems(names, checked,
                (d, which, isChecked) -> viewModel.onBeatSelectionChanged(which, isChecked));

        builder.setPositiveButton("OK", (d, which) -> {
            storeAdapter.updateList(new ArrayList<>());
            progressBar.setVisibility(View.VISIBLE);
            storeRecycler.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
            viewModel.confirmBeatSelection();
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.Purple_Color));
    }

//    private void showBeatSelectionDialog() {
//        List<BeatModel> beats = viewModel.getBeats().getValue();
//        if (beats == null) return;
//
//        String[] names = new String[beats.size()];
//        boolean[] checked = new boolean[beats.size()];
//
//        for (int i = 0; i < beats.size(); i++) {
//            BeatModel b = beats.get(i);
//            names[i] = b.getName() + " (" + b.getTotalStores() + " Stores)";
//            checked[i] = b.isSelected();
//        }
//
//        AlertDialog dialog = new AlertDialog.Builder(requireContext())
//                .setTitle("Select Beat")
//                .setMultiChoiceItems(names, checked,
//                        (d, which, isChecked) -> viewModel.onBeatSelectionChanged(which, isChecked))
//                .setPositiveButton("OK", null)
//                .show();
//
//        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                .setTextColor(ContextCompat.getColor(requireContext(), R.color.Purple_Color));
//    }



}

//        tvBeatDropdown = view.findViewById(R.id.tv_beat_dropdown);
//        tvOrderValue = view.findViewById(R.id.tv_order_val);
//        tvVisitedCount = view.findViewById(R.id.tv_visited_count);
//        tvOrderTakenCount = view.findViewById(R.id.tv_order_taken_count);
//        searchBar = view.findViewById(R.id.et_search);
//        calendarRecycler = view.findViewById(R.id.calendar_recycler);
//        storeRecycler = view.findViewById(R.id.store_recycler);
//     //   filterChipGroup = view.findViewById(R.id.filter_chip_group);
//        tabLayoutFilter = view.findViewById(R.id.tab_layout_filter);
//        btnMap = view.findViewById(R.id.btn_map_view);
//
//        // 2. Setup ViewModel
//        viewModel = new ViewModelProvider(this).get(MyBeatViewModel.class);
//
//        // 3. Setup Calendar (Horizontal)
//        setupCalendar();
//
//        setupTabs();
//
//        // 4. Setup Store List
//        storeRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
//        storeAdapter = new MyBeatStoreAdapter(requireContext(), new ArrayList<>());
//        storeRecycler.setAdapter(storeAdapter);
//
//        // 5. Observers
//        viewModel.getFilteredStores().observe(getViewLifecycleOwner(), stores -> {
//            storeAdapter.updateList(stores);
//            searchBar.setHint(stores.size() + " Store(s)");
//        });
//
//        viewModel.getTotalOrderValue().observe(getViewLifecycleOwner(), val -> tvOrderValue.setText(String.valueOf(val.intValue())));
//        viewModel.getVisitedCountText().observe(getViewLifecycleOwner(), txt -> tvVisitedCount.setText(txt));
//        viewModel.getOrderTakenCountText().observe(getViewLifecycleOwner(), txt -> tvOrderTakenCount.setText(txt));
//
//        viewModel.getBeats().observe(getViewLifecycleOwner(), beats -> {
//            // Update dropdown text based on selection
//            StringBuilder sb = new StringBuilder("Beat: ");
//            int selectedCount = 0;
//            for(BeatModel b : beats) {
//                if(b.isSelected()) {
//                    if(selectedCount > 0) sb.append(", ");
//                    sb.append(b.getName());
//                    selectedCount++;
//                }
//            }
//            if(selectedCount == beats.size()) tvBeatDropdown.setText("Beat: All Selected");
//            else if(selectedCount == 0) tvBeatDropdown.setText("Beat: None");
//            else tvBeatDropdown.setText(sb.toString());
//        });
//
//        // 6. Listeners
//
//        // Dropdown Click -> Show Multi-Select Dialog
//        tvBeatDropdown.setOnClickListener(v -> showBeatSelectionDialog());
//
//        // Chip Filters
////        filterChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
////            String filter = "All";
////            if(checkedId == R.id.chip_visited) filter = "Visited";
////            else if(checkedId == R.id.chip_not_visited) filter = "Not Visited";
////            else if(checkedId == R.id.chip_order) filter = "Order Taken";
////            viewModel.onTabFilterChanged(filter);
////        });
//
//        // Search Bar
//        searchBar.addTextChangedListener(new TextWatcher() {
//            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
//                viewModel.onSearchQueryChanged(s.toString());
//            }
//            @Override public void afterTextChanged(Editable s) {}
//        });
//
//        // Map Button
//        btnMap.setOnClickListener(v -> {
//            // Opens current location on map
//            Uri gmmIntentUri = Uri.parse("geo:0,0?q=my+location");
//            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//            mapIntent.setPackage("com.google.android.apps.maps");
//            if (mapIntent.resolveActivity(requireContext().getPackageManager()) != null) {
//                startActivity(mapIntent);
//            } else {
//                Toast.makeText(requireContext(), "Google Maps not found", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void setupTabs() {
//        // Add Tabs
//        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("All"));
//        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("Not Visited"));
//        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("Visited"));
//        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("Order Taken"));
//
////        for (int i = 0; i < tabLayoutFilter.getChildCount(); i++) {
////            View child = tabLayoutFilter.getChildAt(i);
////            if (child instanceof ViewGroup) {
////                ViewGroup slidingTabStrip = (ViewGroup) child;
////                for (int j = 0; j < slidingTabStrip.getChildCount(); j++) {
////                    View tabView = slidingTabStrip.getChildAt(j);
////                    if (tabView instanceof ViewGroup) {
////                        ViewGroup tabViewGroup = (ViewGroup) tabView;
////                        for (int k = 0; k < tabViewGroup.getChildCount(); k++) {
////                            View tabChild = tabViewGroup.getChildAt(k);
////                            if (tabChild instanceof TextView) {
////                                ((TextView) tabChild).setAllCaps(false);
////                            }
////                        }
////                    }
////                }
////            }
////        }
//
//        // Listener
//        tabLayoutFilter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                String filter = tab.getText().toString();
//                viewModel.onTabFilterChanged(filter);
//            }
//            @Override public void onTabUnselected(TabLayout.Tab tab) {}
//            @Override public void onTabReselected(TabLayout.Tab tab) {}
//        });
//    }
//
//    private void setupCalendar() {
//        // Generate last 2 days + today + next 4 days
//        List<CalendarDateModel> dates = new ArrayList<>();
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DAY_OF_YEAR, -2);
//
//        for(int i=0; i<7; i++) {
//            String day = new SimpleDateFormat("EEE", Locale.getDefault()).format(cal.getTime());
//            String date = new SimpleDateFormat("dd", Locale.getDefault()).format(cal.getTime());
//            // Check if it's today (simple logic for demo, assumes 3rd item is today based on loop)
//            boolean isToday = i == 2;
//            dates.add(new CalendarDateModel(day, date, isToday));
//            cal.add(Calendar.DAY_OF_YEAR, 1);
//        }
//
//        MyBeatCalendarAdapter adapter = new MyBeatCalendarAdapter(dates);
//        calendarRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
//        calendarRecycler.setAdapter(adapter);
//        calendarRecycler.scrollToPosition(1); // Center it a bit
//    }
//
//    private void showBeatSelectionDialog() {
//        List<BeatModel> beats = viewModel.getBeats().getValue();
//        if(beats == null) return;
//
//        String[] beatNames = new String[beats.size()];
//        boolean[] checkedItems = new boolean[beats.size()];
//
//        for(int i=0; i<beats.size(); i++) {
//            BeatModel b = beats.get(i);
//            beatNames[i] = b.getName() + " (" + b.getTotalStores() + " Stores)";
//            checkedItems[i] = b.isSelected();
//        }
//
//        AlertDialog dialog = new AlertDialog.Builder(requireContext())
//                .setTitle("Select Beat")
//                .setMultiChoiceItems(beatNames, checkedItems, (dialogInterface, which, isChecked) -> {
//                    viewModel.onBeatSelectionChanged(which, isChecked);
//                })
//                .setPositiveButton("OK", null)
//                .show();
//
//        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
//    }
//}