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


public class Step2ShopFragment extends Fragment {

    private CreateStoreViewModel viewModel;
    private SessionManager sessionManager;
    private AutoCompleteTextView spCountry, spState, spDist, spCity, spBeat,spRsId, spSecondaryChannel,spOutletType;
    private TextInputEditText etShopName, etRsId, etType, etPin, etAddress,etssname,etsecondchannel;

    public Step2ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step2_shop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CreateStoreViewModel.class);
        sessionManager = new SessionManager(requireContext());
        String token = sessionManager.getToken();

        spCountry = view.findViewById(R.id.sp_country);
        spState = view.findViewById(R.id.sp_state);
        spDist = view.findViewById(R.id.sp_district);
        spCity = view.findViewById(R.id.sp_city);
        spBeat = view.findViewById(R.id.sp_beat);
        etShopName = view.findViewById(R.id.et_shop_name);
        spOutletType = view.findViewById(R.id.sp_outlet_type);
        etPin = view.findViewById(R.id.et_pin);
        etAddress = view.findViewById(R.id.et_address);
        etssname = view.findViewById(R.id.et_ss_name);
        spRsId = view.findViewById(R.id.sp_rs_id);
        spSecondaryChannel = view.findViewById(R.id.sp_secondary_channel);
        MaterialButton btnNext = view.findViewById(R.id.btn_next);

        String[] outletTypes = {"COC", "ROC"};
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
            if (res.status == Resource.Status.SUCCESS && res.data != null) {
                ArrayAdapter<LocationResponse.Country> adapter =
                        new ArrayAdapter<>(requireContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                res.data.getData());

                spCountry.setAdapter(adapter);
            }
        });

        spCountry.setOnItemClickListener((parent, v, position, id) -> {
            LocationResponse.Country country =
                    (LocationResponse.Country) parent.getItemAtPosition(position);

            viewModel.selectedCountryId = String.valueOf(country.getId());

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
            if (res.status == Resource.Status.SUCCESS && res.data != null) {
                spState.setAdapter(new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        res.data.getData()));
            }
        });

        spState.setOnItemClickListener((parent, v, position, id) -> {
            LocationResponse.State state =
                    (LocationResponse.State) parent.getItemAtPosition(position);

            viewModel.selectedStateId = String.valueOf(state.getId());

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
            if (res.status == Resource.Status.SUCCESS && res.data != null) {
                spDist.setAdapter(new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        res.data.getData()));
            }
        });

        spDist.setOnItemClickListener((parent, v, position, id) -> {
            LocationResponse.District dist =
                    (LocationResponse.District) parent.getItemAtPosition(position);

            viewModel.selectedDistId = String.valueOf(dist.getId());

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
            viewModel.rsId = String.valueOf(selectedItem.getTitle());
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
            viewModel.secondaryChannel = String.valueOf(selectedItem.getTitle());
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
        });

        // -------- NEXT BUTTON --------
        btnNext.setOnClickListener(v -> {

            if (etShopName.getText().toString().trim().isEmpty()
                    || viewModel.selectedCountryId.isEmpty()
                    || viewModel.selectedStateId.isEmpty()
                    || viewModel.selectedDistId.isEmpty()
                    || viewModel.selectedCityId.isEmpty()
                    || viewModel.selectedBeatId.isEmpty()) {

                Toast.makeText(requireContext(),
                        "Please fill all required fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.storeName = etShopName.getText().toString();
 //           viewModel.rsId = etRsId.getText().toString();
//            viewModel.outletType = etType.getText().toString();
            viewModel.pinCode = etPin.getText().toString();
            viewModel.address = etAddress.getText().toString();

//            // ✅ SAME PATTERN
//            viewModel.secondaryChannel =
//                    etsecondchannel.getText().toString().trim();
//
//            viewModel.ssName =
//                    etssname.getText().toString().trim();

            ((CreateStoreWizardActivity) requireActivity()).nextStep();
        });
    }
}