package com.example.moofrosty.ui.newstorecreation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
    private TextInputEditText etShopName, etPin, etAddress;
    private TextInputLayout tilShopName, tilCountry, tilState, tilDistrict, tilCity, tilBeat, tilRsId, tilSecondaryChannel, tilOutletType, tilAddress,tilPin;

    public Step2ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step2_shop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CreateStoreViewModel.class);
        sessionManager = new SessionManager(requireContext());
        String token = sessionManager.getToken();

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
        etPin = view.findViewById(R.id.et_pin);
        etAddress = view.findViewById(R.id.et_address);

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
        tilAddress = view.findViewById(R.id.til_address);

        MaterialButton btnNext = view.findViewById(R.id.btn_next);

        // --- OUTLET TYPE SETUP ---
        String[] outletTypes = {"COC", "ROC"};
        ArrayAdapter<String> outletAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, outletTypes);
        spOutletType.setAdapter(outletAdapter);
        spOutletType.setOnItemClickListener((parent, v, position, id1) -> {
            viewModel.outletType = (String) parent.getItemAtPosition(position);
        });

        // --- FETCH DATA ---
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            viewModel.fetchCountries(token);
            viewModel.fetchBeats(token);
            viewModel.fetchRss(token);
            viewModel.fetchSecondaryChannels(token);
        } else {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        // --- COUNTRY OBSERVER ---
        viewModel.countries.observe(getViewLifecycleOwner(), res -> {
            if (res.status == Resource.Status.SUCCESS && res.data != null) {
                spCountry.setAdapter(new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        res.data.getData()));
            }
        });

        spCountry.setOnItemClickListener((parent, v, position, id1) -> {
            LocationResponse.Country country = (LocationResponse.Country) parent.getItemAtPosition(position);
            viewModel.selectedCountryId = String.valueOf(country.getId());
            viewModel.selectedCountryName = country.getName();

            viewModel.selectedStateId = "";
            viewModel.selectedDistId = "";
            viewModel.selectedCityId = "";
            spState.setText("");
            spDist.setText("");
            spCity.setText("");

            if (NetworkUtil.isNetworkAvailable(requireContext())) viewModel.fetchStates(token, country.getId());
        });

        // --- STATE OBSERVER ---
        viewModel.states.observe(getViewLifecycleOwner(), res -> {
            if (res.status == Resource.Status.SUCCESS && res.data != null) {
                spState.setAdapter(new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        res.data.getData()));
            }
        });

        spState.setOnItemClickListener((parent, v, position, id1) -> {
            LocationResponse.State state = (LocationResponse.State) parent.getItemAtPosition(position);
            viewModel.selectedStateId = String.valueOf(state.getId());
            viewModel.selectedStateName = state.getName();

            viewModel.selectedDistId = "";
            viewModel.selectedCityId = "";
            spDist.setText("");
            spCity.setText("");

            if (NetworkUtil.isNetworkAvailable(requireContext())) viewModel.fetchDistricts(token, state.getId());
        });

        // --- DISTRICT OBSERVER ---
        viewModel.districts.observe(getViewLifecycleOwner(), res -> {
            if (res.status == Resource.Status.SUCCESS && res.data != null) {
                spDist.setAdapter(new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        res.data.getData()));
            }
        });

        spDist.setOnItemClickListener((parent, v, position, id1) -> {
            LocationResponse.District dist = (LocationResponse.District) parent.getItemAtPosition(position);
            viewModel.selectedDistId = String.valueOf(dist.getId());
            viewModel.selectedDistName = dist.getName();

            viewModel.selectedCityId = "";
            spCity.setText("");

            if (NetworkUtil.isNetworkAvailable(requireContext())) viewModel.fetchCities(token, dist.getId());
        });

        // --- CITY OBSERVER ---
        viewModel.cities.observe(getViewLifecycleOwner(), res -> {
            if (res.status == Resource.Status.SUCCESS && res.data != null) {
                spCity.setAdapter(new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        res.data.getData()));
            }
        });

        spCity.setOnItemClickListener((parent, v, position, id1) -> {
            LocationResponse.City city = (LocationResponse.City) parent.getItemAtPosition(position);
            viewModel.selectedCityId = String.valueOf(city.getId());
            viewModel.selectedCityName = city.getName();
        });

        // --- RS/SS OBSERVER ---
        viewModel.rssList.observe(getViewLifecycleOwner(), res -> {
            if (res.status == Resource.Status.SUCCESS && res.data != null && res.data.getData() != null) {
                spRsId.setAdapter(new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        res.data.getData()));
            }
        });

        spRsId.setOnItemClickListener((parent, v, position, id1) -> {
            RssResponse.RssData selected = (RssResponse.RssData) parent.getItemAtPosition(position);
            viewModel.rsId = selected.getTitle();
        });

        // --- SECONDARY CHANNEL OBSERVER ---
        viewModel.secondaryChannelList.observe(getViewLifecycleOwner(), res -> {
            if (res.status == Resource.Status.SUCCESS && res.data != null && res.data.getData() != null) {
                spSecondaryChannel.setAdapter(new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        res.data.getData()));
            }
        });

        spSecondaryChannel.setOnItemClickListener((parent, v, position, id1) -> {
            SecondaryChannelResponse.ChannelData selected = (SecondaryChannelResponse.ChannelData) parent.getItemAtPosition(position);
            viewModel.secondaryChannel = selected.getTitle();
        });

        // --- BEAT OBSERVER ---
        viewModel.beats.observe(getViewLifecycleOwner(), res -> {
            if (res.status == Resource.Status.SUCCESS && res.data != null && res.data.getBeatData() != null) {
                spBeat.setAdapter(new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        res.data.getBeatData()));
            }
        });

        spBeat.setOnItemClickListener((parent, v, position, id1) -> {
            BeatResponse.BeatData beat = (BeatResponse.BeatData) parent.getItemAtPosition(position);
            viewModel.selectedBeatId = String.valueOf(beat.getId());
            viewModel.selectedBeatName = beat.getFrom() + "-" + beat.getTo();
        });

        // --- RESTORE DATA ---
        if (viewModel.storeName != null) etShopName.setText(viewModel.storeName);
        if (viewModel.pinCode != null) etPin.setText(viewModel.pinCode);
        if (viewModel.address != null) etAddress.setText(viewModel.address);
        if (viewModel.outletType != null) spOutletType.setText(viewModel.outletType, false);
        if (viewModel.rsId != null) spRsId.setText(viewModel.rsId, false);
        if (viewModel.secondaryChannel != null) spSecondaryChannel.setText(viewModel.secondaryChannel, false);
        if (viewModel.selectedBeatId != null) spBeat.setText(viewModel.selectedBeatName, false);
        if (viewModel.selectedCountryId != null) spCountry.setText(viewModel.selectedCountryName, false);
        if (viewModel.selectedStateId != null) spState.setText(viewModel.selectedStateName, false);
        if (viewModel.selectedDistId != null) spDist.setText(viewModel.selectedDistName, false);
        if (viewModel.selectedCityId != null) spCity.setText(viewModel.selectedCityName, false);

        // --- NEXT BUTTON VALIDATION ---
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
            tilAddress.setError(null);

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
            if (etAddress.getText().toString().trim().isEmpty()) {
                tilAddress.setError("Please select Address ");
                isValid = false;
            }

            // Stop if invalid
            if (!isValid) return;

            // Backend assignment (unchanged)
            viewModel.storeName = etShopName.getText().toString();
            viewModel.pinCode = etPin.getText().toString();
            viewModel.address = etAddress.getText().toString();

            ((CreateStoreWizardActivity) requireActivity()).nextStep();
        });
    }
}
