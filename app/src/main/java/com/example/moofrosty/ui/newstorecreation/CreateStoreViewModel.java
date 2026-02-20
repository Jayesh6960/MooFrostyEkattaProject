package com.example.moofrosty.ui.newstorecreation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.BeatResponse;
import com.example.moofrosty.data.model.CreateStoreRequestModel;
import com.example.moofrosty.data.model.GeneralResponse;
import com.example.moofrosty.data.model.LocationResponse;
import com.example.moofrosty.data.model.RssResponse;
import com.example.moofrosty.data.model.SecondaryChannelResponse;
import com.example.moofrosty.data.repository.CreateStoreRepository;

import java.io.File;

public class CreateStoreViewModel extends ViewModel {

    private CreateStoreRepository repository;
    public int currentStep = 1;

    // Holds all form data
    public CreateStoreRequestModel formData = new CreateStoreRequestModel();

    public String mobileNumber = "";
    public String ownerName = "";
    public String email = "";

    public String storeName = "";
    public String rsId = "";
    public String outletType = "";
    public String pinCode = "";
    public String address = "";

    public String selectedrsId = "";
    public String selectedSecondaryChannelName = "";

    public String selectedCountryId  = "";
    public String selectedStateId   = "";
    public String selectedDistId    = "";
    public String selectedCityId    = "";
    public String selectedBeatId = "";

    // --- NAMES (For UI - Add these!) ---

    public String selectedoutletType;
    public String selectedCountryName;
    public String selectedStateName;
    public String selectedDistName;
    public String selectedCityName;
    public String selectedBeatName;

    public String selectedDocType = "";
    public String gstnnumber = "";
    public String docNumber = "";
    public File docImage, boardImage, insideImage;
    public String latLong = "";

    public String secondaryChannel = "";
    public String ssName = "";

    public MutableLiveData<Resource<LocationResponse<LocationResponse.Country>>> countries = new MutableLiveData<>();
    public MutableLiveData<Resource<LocationResponse<LocationResponse.State>>> states = new MutableLiveData<>();
    public MutableLiveData<Resource<LocationResponse<LocationResponse.District>>> districts = new MutableLiveData<>();
    public MutableLiveData<Resource<LocationResponse<LocationResponse.City>>> cities = new MutableLiveData<>();
    public MutableLiveData<Resource<BeatResponse>> beats = new MutableLiveData<>();
    public MutableLiveData<Resource<GeneralResponse>> submitResult = new MutableLiveData<>();

    public MutableLiveData<Resource<RssResponse>> rssList = new MutableLiveData<>();
    public MutableLiveData<Resource<SecondaryChannelResponse>> secondaryChannelList = new MutableLiveData<>();

    // ... existing constructor ...


    public CreateStoreViewModel() {
        repository = new CreateStoreRepository();
    }

    public void fetchCountries(String token) {
        repository.getCountries(token, countries);
    }

    public void fetchStates(String token, int cid) {
        repository.getStates(token, cid, states);
    }

    public void fetchDistricts(String token, int sid) {
        repository.getDistricts(token, sid, districts);
    }

    public void fetchCities(String token, int did) {
        repository.getCities(token, did, cities);
    }

    public void fetchBeats(String token) {
        repository.getBeats(token, beats);
    }

//    public void submitStore(String token) {
//        repository.submitStore(token, formData, submitResult);
//    }

    public void submitStore(String token) {
        // Populate the request model locally to pass to repo
        CreateStoreRequestModel req = new CreateStoreRequestModel();
        req.ownerFullName = ownerName;
        req.ownerEmail = email;
        req.mobileNumber = mobileNumber;
        req.storeName = storeName;
        req.rsSsIdentifier = rsId;
        req.outletType = outletType;
        req.secondaryChannel = secondaryChannel;
        req.ssName = ssName;
        req.pincode = pinCode;
        req.address = address;
        req.country = selectedCountryId;
        req.state = selectedStateId;
        req.district = selectedDistId;
        req.city = selectedCityId;
        req.beatId = selectedBeatId;
        req.documentType = selectedDocType;
        req.documentNumber = docNumber;
        req.gstinNumber = gstnnumber;
        req.latLong = latLong;
        req.uploadDocument = docImage;
        req.uploadShopBoardImage = boardImage;
        req.uploadShopInsideImage = insideImage;

        repository.submitStore(token, req, submitResult);

    }
    public void fetchRss(String token) {
        repository.getRssIdentifiers(token, rssList);
    }

    public void fetchSecondaryChannels(String token) {
        repository.getSecondaryChannels(token, secondaryChannelList);
    }


}
