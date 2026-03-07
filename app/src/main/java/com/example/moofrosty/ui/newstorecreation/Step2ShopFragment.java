package com.example.moofrosty.ui.newstorecreation;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.BeatResponse;
import com.example.moofrosty.data.model.LocationResponse;
import com.example.moofrosty.data.model.RssResponse;
import com.example.moofrosty.data.model.SecondaryChannelResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Step2ShopFragment extends Fragment {

    private CreateStoreViewModel viewModel;
    private SessionManager sessionManager;

    private AutoCompleteTextView spCountry, spState, spDist, spCity, spBeat, spRsId, spSecondaryChannel, spOutletType;
    private TextInputEditText etShopName, etPin, etAddressline1,etAddressline2,etAddressline3;
    private TextInputLayout tilShopName, tilCountry, tilState, tilDistrict, tilCity, tilBeat, tilRsId, tilSecondaryChannel, tilOutletType, tilAddress1,tilAddress2,tilAddress3,tilPin;
    private View loadingOverlay;
    public Step2ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step2_shop, container, false);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CreateStoreViewModel.class);
        sessionManager = new SessionManager(requireContext());
        String token = sessionManager.getToken();
        loadingOverlay = view.findViewById(R.id.loading_overlay);
        // --- INITIALIZE VIEWS ---
        spCountry = view.findViewById(R.id.sp_country);
        spState = view.findViewById(R.id.sp_state);
        spDist = view.findViewById(R.id.sp_district);
        spCity = view.findViewById(R.id.sp_city);
        spBeat = view.findViewById(R.id.sp_beat);
        spRsId = view.findViewById(R.id.sp_rs_id);
        spSecondaryChannel = view.findViewById(R.id.sp_secondary_channel);
        spOutletType = view.findViewById(R.id.sp_outlet_type);

        etShopName = view.findViewById(R.id.et_shop_name);
        spOutletType = view.findViewById(R.id.sp_outlet_type);
        etPin = view.findViewById(R.id.et_pin);
        etAddressline1 = view.findViewById(R.id.et_address1);
        etAddressline2=view.findViewById(R.id.et_address2);
        etAddressline3=view.findViewById(R.id.et_address3);



        tilShopName = view.findViewById(R.id.til_shop_name);
        tilPin = view.findViewById(R.id.til_pin);
        tilCountry = view.findViewById(R.id.til_country);
        tilState = view.findViewById(R.id.til_state);
        tilDistrict = view.findViewById(R.id.til_district);
        tilCity = view.findViewById(R.id.til_city);
        tilBeat = view.findViewById(R.id.til_beat);
        tilRsId = view.findViewById(R.id.til_rs_id);
        tilSecondaryChannel = view.findViewById(R.id.til_secondary_channel);
        tilOutletType = view.findViewById(R.id.til_outlet_type);
        tilAddress1 = view.findViewById(R.id.til_address1);
        tilAddress2 = view.findViewById(R.id.til_address2);
        tilAddress3=view.findViewById(R.id.til_address3);

        MaterialButton btnNext = view.findViewById(R.id.btn_next);

        String[] outletTypes = {"OM", "CM"};
        ArrayAdapter<String> outletAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, outletTypes);
        spOutletType.setAdapter(outletAdapter);

//        spOutletType.setText(outletTypes[0], false); // Defaults to COC
//        viewModel.outletType = outletTypes[0]; // Ensure ViewModel has default value

        spOutletType.setOnItemClickListener((parent, v, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            viewModel.outletType = selected;
        });

        // -------- INITIAL API CALLS (NETWORK CHECK) --------
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            viewModel.fetchCountries(token);
            viewModel.fetchBeats(token);
            viewModel.fetchRss(token);
            viewModel.fetchSecondaryChannels(token);
        } else {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        viewModel.countries.observe(getViewLifecycleOwner(), res -> {
            if (res.status == Resource.Status.LOADING) {
                loadingOverlay.setVisibility(View.VISIBLE); // Show Loader
            } else {
                loadingOverlay.setVisibility(View.GONE); // Hide Loader
                if (res.status == Resource.Status.SUCCESS && res.data != null) {
                    ArrayAdapter<LocationResponse.Country> adapter =
                            new ArrayAdapter<>(requireContext(),
                                    android.R.layout.simple_dropdown_item_1line,
                                    res.data.getData());
                    spCountry.setAdapter(adapter);

                    if (viewModel.selectedCountryId == null || viewModel.selectedCountryId.isEmpty()) {
                        for (LocationResponse.Country country : res.data.getData()) {
                            if (country.getName().equalsIgnoreCase("India")) {
                                // 1. Set the text in the UI (false = don't popup the list)
                                spCountry.setText(country.getName(), false);

                                // 2. Save to ViewModel
                                viewModel.selectedCountryId = String.valueOf(country.getId());
                                viewModel.selectedCountryName = country.getName();

                                // 3. Automatically fetch states for India
                                if (NetworkUtil.isNetworkAvailable(requireContext())) {
                                    viewModel.fetchStates(sessionManager.getToken(), country.getId());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        });

        spCountry.setOnItemClickListener((parent, v, position, id) -> {
            LocationResponse.Country country =
                    (LocationResponse.Country) parent.getItemAtPosition(position);

            viewModel.selectedCountryId = String.valueOf(country.getId());
            viewModel.selectedCountryName = country.getName(); // Save Name!

            // CLEAR DEPENDENTS
            viewModel.selectedStateId = "";
            viewModel.selectedDistId = "";
            viewModel.selectedCityId = "";

            spState.setText("");
            spDist.setText("");
            spCity.setText("");

            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                viewModel.fetchStates(token, country.getId());
            }
        });

        // -------- STATE --------
        viewModel.states.observe(getViewLifecycleOwner(), res -> {
            if (res.status == Resource.Status.LOADING) {
                loadingOverlay.setVisibility(View.VISIBLE); // Show Loader
            } else {
                loadingOverlay.setVisibility(View.GONE); // Hide Loader
                if (res.status == Resource.Status.SUCCESS && res.data != null) {
                    spState.setAdapter(new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            res.data.getData()));

                    if (viewModel.selectedStateId == null || viewModel.selectedStateId.isEmpty()) {
                        for (LocationResponse.State state : res.data.getData()) {
                            if (state.getName().equalsIgnoreCase("Maharashtra")) {
                                // 1. Set the text in the UI
                                spState.setText(state.getName(), false);

                                // 2. Save to ViewModel
                                viewModel.selectedStateId = String.valueOf(state.getId());
                                viewModel.selectedStateName = state.getName();

                                // 3. Automatically fetch districts for Maharashtra
                                if (NetworkUtil.isNetworkAvailable(requireContext())) {
                                    viewModel.fetchDistricts(sessionManager.getToken(), state.getId());
                                }
                                break;
                            }
                        }
                    }
                } else if (res.status == Resource.Status.ERROR) {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        spState.setOnItemClickListener((parent, v, position, id) -> {
            LocationResponse.State state =
                    (LocationResponse.State) parent.getItemAtPosition(position);

            viewModel.selectedStateId = String.valueOf(state.getId());
            viewModel.selectedStateName = state.getName();

            viewModel.selectedDistId = "";
            viewModel.selectedCityId = "";

            spDist.setText("");
            spCity.setText("");

            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                viewModel.fetchDistricts(token, state.getId());
            }
        });

        // -------- DISTRICT --------
        viewModel.districts.observe(getViewLifecycleOwner(), res -> {
            if (res.status == Resource.Status.LOADING) {
                loadingOverlay.setVisibility(View.VISIBLE); // Show Loader
            } else {
                loadingOverlay.setVisibility(View.GONE); // Hide Loader
                if (res.status == Resource.Status.SUCCESS && res.data != null) {
                    spDist.setAdapter(new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            res.data.getData()));
                } else if (res.status == Resource.Status.ERROR) {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        spDist.setOnItemClickListener((parent, v, position, id) -> {
            LocationResponse.District dist =
                    (LocationResponse.District) parent.getItemAtPosition(position);

            viewModel.selectedDistId = String.valueOf(dist.getId());
            viewModel.selectedDistName = dist.getName();

            viewModel.selectedCityId = "";
            spCity.setText("");

            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                viewModel.fetchCities(token, dist.getId());
            }
        });

        // -------- CITY --------
        viewModel.cities.observe(getViewLifecycleOwner(), res -> {
            if (res.status == Resource.Status.SUCCESS && res.data != null) {
                spCity.setAdapter(new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        res.data.getData()));
            }
        });

        spCity.setOnItemClickListener((parent, v, position, id) -> {
            LocationResponse.City city =
                    (LocationResponse.City) parent.getItemAtPosition(position);

            viewModel.selectedCityId = String.valueOf(city.getId());
            viewModel.selectedCityName = city.getName();
        });

        viewModel.rssList.observe(getViewLifecycleOwner(), res -> {
            if (res.status == Resource.Status.SUCCESS && res.data != null) {
                if(res.data.getData() != null) {
                    ArrayAdapter<RssResponse.RssData> adapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_dropdown_item_1line, res.data.getData());
                    spRsId.setAdapter(adapter);
                }
            }
        });

        spRsId.setOnItemClickListener((parent, v, position, id) -> {
            RssResponse.RssData selectedItem = (RssResponse.RssData) parent.getItemAtPosition(position);
            viewModel.rsId = String.valueOf(selectedItem.getId());
            viewModel.selectedrsId = selectedItem.getTitle();
        });

        viewModel.secondaryChannelList.observe(getViewLifecycleOwner(), res -> {
            if (res.status == Resource.Status.SUCCESS && res.data != null) {
                if(res.data.getData() != null) {
                    ArrayAdapter<SecondaryChannelResponse.ChannelData> adapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_dropdown_item_1line, res.data.getData());
                    spSecondaryChannel.setAdapter(adapter);
                }
            }
        });

        spSecondaryChannel.setOnItemClickListener((parent, v, position, id) -> {
            SecondaryChannelResponse.ChannelData selectedItem = (SecondaryChannelResponse.ChannelData) parent.getItemAtPosition(position);
            // Save ID to ViewModel
            viewModel.secondaryChannel = String.valueOf(selectedItem.getId());
            viewModel.selectedSecondaryChannelName = selectedItem.getTitle();
        });


        // -------- BEAT OBSERVER --------
        viewModel.beats.observe(getViewLifecycleOwner(), res -> {

            if (res.status == Resource.Status.LOADING) {
                // optional loader
                return;
            }

            if (res.status == Resource.Status.SUCCESS && res.data != null) {

                if (res.data.getBeatData() == null || res.data.getBeatData().isEmpty()) {
                    Toast.makeText(requireContext(),
                            "No beats available",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayAdapter<BeatResponse.BeatData> adapter =
                        new ArrayAdapter<>(requireContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                res.data.getBeatData());

                spBeat.setAdapter(adapter);
            }

            if (res.status == Resource.Status.ERROR) {
                Toast.makeText(requireContext(),
                        res.message,
                        Toast.LENGTH_SHORT).show();
            }
        });

// -------- SELECT BEAT --------
        spBeat.setOnItemClickListener((parent, View, position, id) -> {
            BeatResponse.BeatData beat =
                    (BeatResponse.BeatData) parent.getItemAtPosition(position);
            viewModel.selectedBeatId = String.valueOf(beat.getId());
            viewModel.selectedBeatName = beat.getFrom()+"-"+beat.getTo();
        });

        // --- 🔥 FIX: RESTORE DATA ---
        if (viewModel.storeName != null) etShopName.setText(viewModel.storeName);
        if (viewModel.pinCode != null) etPin.setText(viewModel.pinCode);
//        if (viewModel.address != null) etAddress.setText(viewModel.address);
                if (viewModel.address != null) {

            String[] address = viewModel.address.split(",");

            if (address.length > 0) etAddressline1.setText(address[0].trim());
            if (address.length > 1) etAddressline2.setText(address[1].trim());
            if (address.length > 2) etAddressline3.setText(address[2].trim());
        }

        // Restore Dropdowns (We set the text, but false hides the filter list)
        if (viewModel.outletType != null) spOutletType.setText(viewModel.outletType, false);
        if (viewModel.rsId != null) spRsId.setText(viewModel.selectedrsId, false);
        if (viewModel.secondaryChannel != null) spSecondaryChannel.setText(viewModel.selectedSecondaryChannelName, false);
        if (viewModel.selectedBeatId != null) spBeat.setText(viewModel.selectedBeatName, false);
        if (viewModel.selectedCountryId != null) spCountry.setText(viewModel.selectedCountryName, false);
        if (viewModel.selectedStateId != null) spState.setText(viewModel.selectedStateName, false);
        if (viewModel.selectedDistId != null) spDist.setText(viewModel.selectedDistName, false);
        if (viewModel.selectedCityId != null) spCity.setText(viewModel.selectedCityName, false);


        // -------- NEXT BUTTON --------
        btnNext.setOnClickListener(v -> {
            boolean isValid = true;

            // Clear previous errors
            tilShopName.setError(null);
            tilCountry.setError(null);
            tilState.setError(null);
            tilDistrict.setError(null);
            tilCity.setError(null);
            tilBeat.setError(null);
            tilRsId.setError(null);
            tilSecondaryChannel.setError(null);
            tilOutletType.setError(null);
//            tilAddress.setError(null);
            tilAddress1.setError(null);

            // Individual validations
            if (etShopName.getText().toString().trim().isEmpty()) {
                tilShopName.setError("Shop name is required");
                isValid = false;
            }
            if (etPin.getText().toString().trim().isEmpty()) {
                tilPin.setError("Pin code is required");
                isValid = false;
            }
            if (viewModel.selectedCountryId == null || viewModel.selectedCountryId.isEmpty()) {
                tilCountry.setError("Please select country");
                isValid = false;
            }
            if (viewModel.selectedStateId == null || viewModel.selectedStateId.isEmpty()) {
                tilState.setError("Please select state");
                isValid = false;
            }
            if (viewModel.selectedDistId == null || viewModel.selectedDistId.isEmpty()) {
                tilDistrict.setError("Please select district");
                isValid = false;
            }
            if (viewModel.selectedCityId == null || viewModel.selectedCityId.isEmpty()) {
                tilCity.setError("Please select city");
                isValid = false;
            }
            if (viewModel.selectedBeatId == null || viewModel.selectedBeatId.isEmpty()) {
                tilBeat.setError("Please select beat");
                isValid = false;
            }
            if (spRsId.getText().toString().trim().isEmpty()) {
                tilRsId.setError("Please select RS/SS identifier");
                isValid = false;
            }
            if (spSecondaryChannel.getText().toString().trim().isEmpty()) {
                tilSecondaryChannel.setError("Please select Secondary channel ");
                isValid = false;
            }
            if (spOutletType.getText().toString().trim().isEmpty()) {
                tilOutletType.setError("Please select Outlet type ");
                isValid = false;
            }
//            if (etAddress.getText().toString().trim().isEmpty()) {
//                tilAddress.setError("Please select Address ");
//                isValid = false;
//            }

            if (etAddressline1.getText().toString().trim().isEmpty()) {
                tilAddress1.setError("Address Line 1 required");
                isValid = false;
            }

            if (etAddressline2.getText().toString().trim().isEmpty()) {
                tilAddress2.setError("Address Line 2 required");
                isValid = false;
            }
            String address1 = etAddressline1.getText().toString().trim();
            String address2 = etAddressline2.getText().toString().trim();
            String address3 = etAddressline3.getText().toString().trim();

            String fullAddress = address1;

            if (!address2.isEmpty())
                fullAddress += "#" + address2;

            if (!address3.isEmpty())
                fullAddress += "#" + address3;

            viewModel.address = fullAddress;
            Log.d("STORE_CREATIONjjkj", "Address Line 1 : " + address1);
//            Log.d("STORE_CREATION", "Address Line 2 : " + address2);
            Log.d("FullAddress", "Address Line 3 : " + fullAddress);




            // Stop if invalid
            if (!isValid) return;

            // Backend assignment (unchanged)
            viewModel.storeName = etShopName.getText().toString();
            //           viewModel.rsId = etRsId.getText().toString();
//            viewModel.outletType = etType.getText().toString();
            viewModel.pinCode = etPin.getText().toString();
//            viewModel.address = etAddress.getText().toString();


//            // ✅ SAME PATTERN
//            viewModel.secondaryChannel =
//                    etsecondchannel.getText().toString().trim();
//
//            viewModel.ssName =
//                    etssname.getText().toString().trim();

            ((CreateStoreWizardActivity) requireActivity()).nextStep();
            String logDetails =
                    "STORE_CREATION_DETAILS\n" +
                            "----------------------------\n" +
                            "Shop Name: " + etShopName.getText().toString().trim() + "\n" +
                            "Pin Code: " + etPin.getText().toString().trim() + "\n" +
                            "Country ID: " + viewModel.selectedCountryId + "\n" +
                            "State ID: " + viewModel.selectedStateId + "\n" +
                            "District ID: " + viewModel.selectedDistId + "\n" +
                            "City ID: " + viewModel.selectedCityId + "\n" +
                            "Beat ID: " + viewModel.selectedBeatId + "\n" +
                            "RS/SS ID: " + spRsId.getText().toString().trim() + "\n" +
                            "Secondary Channel: " + spSecondaryChannel.getText().toString().trim() + "\n" +
                            "Outlet Type: " + spOutletType.getText().toString().trim() + "\n" +
                            "Address Line 1: " + address1 + "\n" +
                            "Address Line 2: " + address2 + "\n" +
                            "Address Line 3: " + address3 + "\n" +
                            "Full Address: " + fullAddress + "\n";

            Log.d("STORE_CREATION_Details", logDetails);
        });

    }
}
//package com.example.moofrosty.ui.newstorecreation;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Toast;
//
//import com.example.moofrosty.R;
//import com.example.moofrosty.core.network.Resource;
//import com.example.moofrosty.core.utils.NetworkUtil;
//import com.example.moofrosty.data.local.SessionManager;
//import com.example.moofrosty.data.model.BeatResponse;
//import com.example.moofrosty.data.model.LocationResponse;
//import com.example.moofrosty.data.model.RssResponse;
//import com.example.moofrosty.data.model.SecondaryChannelResponse;
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.textfield.TextInputLayout;
//
//public class Step2ShopFragment extends Fragment {
//
//    private CreateStoreViewModel viewModel;
//    private SessionManager sessionManager;
//
//    private AutoCompleteTextView spCountry, spState, spDist, spCity,
//            spBeat, spRsId, spSecondaryChannel, spOutletType;
//
//    private TextInputEditText etShopName, etPin;
//    private TextInputEditText etAddress1, etAddress2, etAddress3;
//
//    private TextInputLayout tilShopName, tilCountry, tilState, tilDistrict, tilCity,
//            tilBeat, tilRsId, tilSecondaryChannel, tilOutletType, tilPin;
//
//    private TextInputLayout tilAddress1, tilAddress2, tilAddress3;
//
//    public Step2ShopFragment(){}
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        return inflater.inflate(R.layout.fragment_step2_shop, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//
//        super.onViewCreated(view, savedInstanceState);
//
//        viewModel = new ViewModelProvider(requireActivity()).get(CreateStoreViewModel.class);
//        sessionManager = new SessionManager(requireContext());
//
//        String token = sessionManager.getToken();
//
//        // ---------------- VIEWS ----------------
//
//        spCountry = view.findViewById(R.id.sp_country);
//        spState = view.findViewById(R.id.sp_state);
//        spDist = view.findViewById(R.id.sp_district);
//        spCity = view.findViewById(R.id.sp_city);
//        spBeat = view.findViewById(R.id.sp_beat);
//        spRsId = view.findViewById(R.id.sp_rs_id);
//        spSecondaryChannel = view.findViewById(R.id.sp_secondary_channel);
//        spOutletType = view.findViewById(R.id.sp_outlet_type);
//
//        etShopName = view.findViewById(R.id.et_shop_name);
//        etPin = view.findViewById(R.id.et_pin);
//
//        etAddress1 = view.findViewById(R.id.et_address1);
//        etAddress2 = view.findViewById(R.id.et_address2);
//        etAddress3 = view.findViewById(R.id.et_address3);
//
//        tilShopName = view.findViewById(R.id.til_shop_name);
//        tilCountry = view.findViewById(R.id.til_country);
//        tilState = view.findViewById(R.id.til_state);
//        tilDistrict = view.findViewById(R.id.til_district);
//        tilCity = view.findViewById(R.id.til_city);
//        tilBeat = view.findViewById(R.id.til_beat);
//        tilRsId = view.findViewById(R.id.til_rs_id);
//        tilSecondaryChannel = view.findViewById(R.id.til_secondary_channel);
//        tilOutletType = view.findViewById(R.id.til_outlet_type);
//        tilPin = view.findViewById(R.id.til_pin);
//
//        tilAddress1 = view.findViewById(R.id.til_address1);
//        tilAddress2 = view.findViewById(R.id.til_address2);
//        tilAddress3 = view.findViewById(R.id.til_address3);
//
//        MaterialButton btnNext = view.findViewById(R.id.btn_next);
//
//        // ---------------- OUTLET TYPE ----------------
//
//        String[] outletTypes = {"COC", "ROC"};
//
//        ArrayAdapter<String> outletAdapter =
//                new ArrayAdapter<>(requireContext(),
//                        android.R.layout.simple_dropdown_item_1line,
//                        outletTypes);
//
//        spOutletType.setAdapter(outletAdapter);
//        viewModel.rssList.observe(getViewLifecycleOwner(), res -> {
//            if (res.status == Resource.Status.SUCCESS && res.data != null) {
//                if(res.data.getData() != null) {
//                    ArrayAdapter<RssResponse.RssData> adapter = new ArrayAdapter<>(requireContext(),
//                            android.R.layout.simple_dropdown_item_1line, res.data.getData());
//                    spRsId.setAdapter(adapter);
//                }
//            }
//        });
//        spRsId.setOnItemClickListener((parent, v, position, id) -> {
//            RssResponse.RssData selectedItem = (RssResponse.RssData) parent.getItemAtPosition(position);
//            viewModel.rsId = String.valueOf(selectedItem.getId());
//            viewModel.selectedrsId = selectedItem.getTitle();
//        });
//                viewModel.secondaryChannelList.observe(getViewLifecycleOwner(), res -> {
//            if (res.status == Resource.Status.SUCCESS && res.data != null) {
//                if(res.data.getData() != null) {
//                    ArrayAdapter<SecondaryChannelResponse.ChannelData> adapter = new ArrayAdapter<>(requireContext(),
//                            android.R.layout.simple_dropdown_item_1line, res.data.getData());
//                    spSecondaryChannel.setAdapter(adapter);
//                }
//            }
//        });
//
//        spSecondaryChannel.setOnItemClickListener((parent, v, position, id) -> {
//            SecondaryChannelResponse.ChannelData selectedItem = (SecondaryChannelResponse.ChannelData) parent.getItemAtPosition(position);
//            // Save ID to ViewModel
//            viewModel.secondaryChannel = String.valueOf(selectedItem.getId());
//            viewModel.selectedSecondaryChannelName = selectedItem.getTitle();
//        });
//
//        spOutletType.setOnItemClickListener((parent, v, position, id) -> {
//
//            viewModel.outletType =
//                    (String) parent.getItemAtPosition(position);
//        });
//
//        // ---------------- API CALLS ----------------
//
//        if (NetworkUtil.isNetworkAvailable(requireContext())) {
//
//            viewModel.fetchCountries(token);
//            viewModel.fetchBeats(token);
//            viewModel.fetchRss(token);
//            viewModel.fetchSecondaryChannels(token);
//
//        } else {
//
//            Toast.makeText(requireContext(),
//                    "No Internet Connection",
//                    Toast.LENGTH_SHORT).show();
//        }
//
//        // ---------------- COUNTRY ----------------
//
//        viewModel.countries.observe(getViewLifecycleOwner(), res -> {
//
//            if (res.status == Resource.Status.SUCCESS && res.data != null) {
//
//                ArrayAdapter<LocationResponse.Country> adapter =
//                        new ArrayAdapter<>(requireContext(),
//                                android.R.layout.simple_dropdown_item_1line,
//                                res.data.getData());
//
//                spCountry.setAdapter(adapter);
//            }
//        });
//
//        spCountry.setOnItemClickListener((parent, v, position, id) -> {
//
//            LocationResponse.Country country =
//                    (LocationResponse.Country) parent.getItemAtPosition(position);
//
//            viewModel.selectedCountryId = String.valueOf(country.getId());
//            viewModel.selectedCountryName = country.getName();
//
//            if (NetworkUtil.isNetworkAvailable(requireContext()))
//                viewModel.fetchStates(token, country.getId());
//        });
//
//        // ---------------- STATE ----------------
//
//        viewModel.states.observe(getViewLifecycleOwner(), res -> {
//
//            if (res.status == Resource.Status.SUCCESS && res.data != null) {
//
//                spState.setAdapter(new ArrayAdapter<>(requireContext(),
//                        android.R.layout.simple_dropdown_item_1line,
//                        res.data.getData()));
//            }
//        });
//
//        spState.setOnItemClickListener((parent, v, position, id) -> {
//
//            LocationResponse.State state =
//                    (LocationResponse.State) parent.getItemAtPosition(position);
//
//            viewModel.selectedStateId = String.valueOf(state.getId());
//            viewModel.selectedStateName = state.getName();
//
//            viewModel.fetchDistricts(token, state.getId());
//        });
//
//        // ---------------- DISTRICT ----------------
//
//        viewModel.districts.observe(getViewLifecycleOwner(), res -> {
//
//            if (res.status == Resource.Status.SUCCESS && res.data != null) {
//
//                spDist.setAdapter(new ArrayAdapter<>(requireContext(),
//                        android.R.layout.simple_dropdown_item_1line,
//                        res.data.getData()));
//            }
//        });
//
//        spDist.setOnItemClickListener((parent, v, position, id) -> {
//
//            LocationResponse.District dist =
//                    (LocationResponse.District) parent.getItemAtPosition(position);
//
//            viewModel.selectedDistId = String.valueOf(dist.getId());
//            viewModel.selectedDistName = dist.getName();
//
//            viewModel.fetchCities(token, dist.getId());
//        });
//
//        // ---------------- CITY ----------------
//
//        viewModel.cities.observe(getViewLifecycleOwner(), res -> {
//
//            if (res.status == Resource.Status.SUCCESS && res.data != null) {
//
//                spCity.setAdapter(new ArrayAdapter<>(requireContext(),
//                        android.R.layout.simple_dropdown_item_1line,
//                        res.data.getData()));
//            }
//        });
//
//        spCity.setOnItemClickListener((parent, v, position, id) -> {
//
//            LocationResponse.City city =
//                    (LocationResponse.City) parent.getItemAtPosition(position);
//
//            viewModel.selectedCityId = String.valueOf(city.getId());
//            viewModel.selectedCityName = city.getName();
//        });
//
//        // ---------------- BEAT ----------------
//
//        viewModel.beats.observe(getViewLifecycleOwner(), res -> {
//
//            if (res.status == Resource.Status.SUCCESS && res.data != null) {
//
//                ArrayAdapter<BeatResponse.BeatData> adapter =
//                        new ArrayAdapter<>(requireContext(),
//                                android.R.layout.simple_dropdown_item_1line,
//                                res.data.getBeatData());
//
//                spBeat.setAdapter(adapter);
//            }
//        });
//
//        spBeat.setOnItemClickListener((parent, v, position, id) -> {
//
//            BeatResponse.BeatData beat =
//                    (BeatResponse.BeatData) parent.getItemAtPosition(position);
//
//            viewModel.selectedBeatId = String.valueOf(beat.getId());
//            viewModel.selectedBeatName = beat.getFrom() + "-" + beat.getTo();
//        });
//
//        // ---------------- RESTORE DATA ----------------
//
//        if (viewModel.storeName != null)
//            etShopName.setText(viewModel.storeName);
//
//        if (viewModel.pinCode != null)
//            etPin.setText(viewModel.pinCode);
//
//        if (viewModel.address != null) {
//
//            String[] address = viewModel.address.split(",");
//
//            if (address.length > 0) etAddress1.setText(address[0].trim());
//            if (address.length > 1) etAddress2.setText(address[1].trim());
//            if (address.length > 2) etAddress3.setText(address[2].trim());
//        }
//
//        // ---------------- NEXT BUTTON ----------------
//
//        btnNext.setOnClickListener(v -> {
//
//            boolean isValid = true;
//
//            tilShopName.setError(null);
//            tilPin.setError(null);
//            tilAddress1.setError(null);
//
//            if (etShopName.getText().toString().trim().isEmpty()) {
//
//                tilShopName.setError("Shop name required");
//                isValid = false;
//            }
//
//            if (etPin.getText().toString().trim().isEmpty()) {
//
//                tilPin.setError("Pin required");
//                isValid = false;
//            }
//
//            if (etAddress1.getText().toString().trim().isEmpty()) {
//
//                tilAddress1.setError("Address Line 1 required");
//                isValid = false;
//            }
//
//            if (!isValid)
//                return;
//
//            viewModel.storeName = etShopName.getText().toString();
//            viewModel.pinCode = etPin.getText().toString();
//
//            String address1 = etAddress1.getText().toString().trim();
//            String address2 = etAddress2.getText().toString().trim();
//            String address3 = etAddress3.getText().toString().trim();
//
//            String fullAddress = address1;
//
//            if (!address2.isEmpty())
//                fullAddress += ", " + address2;
//
//            if (!address3.isEmpty())
//                fullAddress += ", " + address3;
//
//            viewModel.address = fullAddress;
//
//            // -------- LOGGING ALL DATA --------
//
//            Log.d("STORE_CREATION", "------ STORE DETAILS ------");
//            Log.d("STORE_CREATION", "Shop Name : " + viewModel.storeName);
//            Log.d("STORE_CREATION", "Pin Code : " + viewModel.pinCode);
//
//            Log.d("STORE_CREATION", "Address Line 1 : " + address1);
//            Log.d("STORE_CREATION", "Address Line 2 : " + address2);
//            Log.d("STORE_CREATION", "Address Line 3 : " + address3);
//
//            Log.d("STORE_CREATION", "Full Address : " + viewModel.address);
//
//            Log.d("STORE_CREATION", "Country Id : " + viewModel.selectedCountryId);
//            Log.d("STORE_CREATION", "State Id : " + viewModel.selectedStateId);
//            Log.d("STORE_CREATION", "District Id : " + viewModel.selectedDistId);
//            Log.d("STORE_CREATION", "City Id : " + viewModel.selectedCityId);
//
//            Log.d("STORE_CREATION", "Beat Id : " + viewModel.selectedBeatId);
//            Log.d("STORE_CREATION", "RS Id : " + viewModel.selectedrsId);
//            Log.d("STORE_CREATION", "Secondary Channel Id : " + viewModel.selectedSecondaryChannelName);
//
//            Log.d("STORE_CREATION", "Outlet Type : " + viewModel.outletType);
//
//            Log.d("STORE_CREATION", "---------------------------");
//
//            ((CreateStoreWizardActivity) requireActivity()).nextStep();
//        });
//    }
//}



