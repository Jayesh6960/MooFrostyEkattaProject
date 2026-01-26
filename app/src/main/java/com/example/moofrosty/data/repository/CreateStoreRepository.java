    package com.example.moofrosty.data.repository;

    import android.util.Log;

    import androidx.lifecycle.MutableLiveData;

    import com.example.moofrosty.core.network.ApiClient;
    import com.example.moofrosty.core.network.ApiService;
    import com.example.moofrosty.core.network.Resource;
    import com.example.moofrosty.core.utils.NetworkUtil;
    import com.example.moofrosty.data.model.BeatResponse;
    import com.example.moofrosty.data.model.CreateStoreRequestModel;
    import com.example.moofrosty.data.model.GeneralResponse;
    import com.example.moofrosty.data.model.LocationResponse;
    import com.example.moofrosty.data.model.RssResponse;
    import com.example.moofrosty.data.model.SecondaryChannelResponse;

    import java.io.File;

    import okhttp3.MediaType;
    import okhttp3.MultipartBody;
    import okhttp3.RequestBody;
    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class CreateStoreRepository {
        private ApiService apiService;

        public CreateStoreRepository() {
            apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        }

        public <T> void fetchLocationData(Call<LocationResponse<T>> call, MutableLiveData<Resource<LocationResponse<T>>> liveData) {
            call.enqueue(new Callback<LocationResponse<T>>() {
                @Override public void onResponse(Call<LocationResponse<T>> call, Response<LocationResponse<T>> response) {
                    if(response.isSuccessful() && response.body() != null) liveData.postValue(Resource.success(response.body()));
                    else liveData.postValue(Resource.error("Failed to fetch data", null));
                }
                @Override public void onFailure(Call<LocationResponse<T>> call, Throwable t) {
                    liveData.postValue(Resource.error(t.getMessage(), null));
                }
            });
        }

        public void getCountries(String token, MutableLiveData<Resource<LocationResponse<LocationResponse.Country>>> liveData) {
            fetchLocationData(apiService.getCountries("Bearer " + token), liveData);
        }

        public void getStates(String token, int countryId, MutableLiveData<Resource<LocationResponse<LocationResponse.State>>> liveData) {
            fetchLocationData(apiService.getStates("Bearer " + token, countryId), liveData);
        }

        public void getDistricts(String token, int stateId, MutableLiveData<Resource<LocationResponse<LocationResponse.District>>> liveData) {
            fetchLocationData(apiService.getDistricts("Bearer " + token, stateId), liveData);
        }

        public void getCities(String token, int distId, MutableLiveData<Resource<LocationResponse<LocationResponse.City>>> liveData) {
            fetchLocationData(apiService.getCities("Bearer " + token, distId), liveData);
        }

    //    public void getBeats(String token, MutableLiveData<Resource<BeatResponse>> liveData) {
    //        apiService.getBeats("Bearer " + token).enqueue(new Callback<BeatResponse>() {
    //            @Override public void onResponse(Call<BeatResponse> call, Response<BeatResponse> response) {
    //                if(response.isSuccessful() && response.body() != null) liveData.postValue(Resource.success(response.body()));
    //                else liveData.postValue(Resource.error("Failed to load beats", null));
    //            }
    //            @Override public void onFailure(Call<BeatResponse> call, Throwable t) {
    //                liveData.postValue(Resource.error(t.getMessage(), null));
    //            }
    //        });
    //    }

        public void getBeats(String token,
                             MutableLiveData<Resource<BeatResponse>> liveData) {

            liveData.postValue(Resource.loading(null));

            apiService.getBeats("Bearer " + token)
                    .enqueue(new Callback<BeatResponse>() {

                        @Override
                        public void onResponse(Call<BeatResponse> call,
                                               Response<BeatResponse> response) {

                            if (response.isSuccessful()
                                    && response.body() != null
                                    && response.body().isStatus()) {

                                liveData.postValue(Resource.success(response.body()));
                            } else {
                                liveData.postValue(Resource.error("Failed to load beats", null));
                            }
                        }

                        @Override
                        public void onFailure(Call<BeatResponse> call, Throwable t) {
                            liveData.postValue(Resource.error(t.getMessage(), null));
                        }
                    });
        }

        // --- SUBMIT STORE ---
        public void submitStore(String token, CreateStoreRequestModel req, MutableLiveData<Resource<GeneralResponse>> liveData) {

            liveData.postValue(Resource.loading(null));

            // --- 1. LOG THE PAYLOAD ---
            Log.d("addstore", "---------- ADD STORE REQUEST START ----------");
            Log.d("addstore", "Owner Name: " + req.ownerFullName);
            Log.d("addstore", "Email: " + req.ownerEmail);
            Log.d("addstore", "Mobile: " + req.mobileNumber);
            Log.d("addstore", "Store Name: " + req.storeName);
            Log.d("addstore", "RS/SS ID: " + req.rsSsIdentifier);
            Log.d("addstore", "Sec Channel: " + req.secondaryChannel);
            Log.d("addstore", "Outlet Type: " + req.outletType);
            Log.d("addstore", "SS Name: " + req.ssName);
            Log.d("addstore", "Pincode: " + req.pincode);
            Log.d("addstore", "Address: " + req.address);
            Log.d("addstore", "Country ID: " + req.country);
            Log.d("addstore", "State ID: " + req.state);
            Log.d("addstore", "District ID: " + req.district);
            Log.d("addstore", "City ID: " + req.city);
            Log.d("addstore", "Beat ID: " + req.beatId);
            Log.d("addstore", "LatLong: " + req.latLong);
            Log.d("addstore", "Doc Type: " + req.documentType);
            Log.d("addstore", "Doc Num: " + req.documentNumber);
            Log.d("addstore", "File Doc: " + (req.uploadDocument != null ? req.uploadDocument.getName() : "NULL"));
            Log.d("addstore", "File Board: " + (req.uploadShopBoardImage != null ? req.uploadShopBoardImage.getName() : "NULL"));
            Log.d("addstore", "File Inside: " + (req.uploadShopInsideImage != null ? req.uploadShopInsideImage.getName() : "NULL"));
            Log.d("addstore", "---------------------------------------------");

            // Create Text Parts safely
            RequestBody rbOwner = createPart(req.ownerFullName);
            RequestBody rbEmail = createPart(req.ownerEmail);
            RequestBody rbMobile = createPart(req.mobileNumber);
            RequestBody rbStore = createPart(req.storeName);
            RequestBody rbRs = createPart(req.rsSsIdentifier);
            RequestBody rbType = createPart(req.outletType);
            RequestBody rbPin = createPart(req.pincode);
            RequestBody rbAddress = createPart(req.address);
            RequestBody rbCountry = createPart(req.country);
            RequestBody rbState = createPart(req.state);
            RequestBody rbDistrict = createPart(req.district);
            RequestBody rbCity = createPart(req.city);
            RequestBody rbBeat = createPart(req.beatId);
            RequestBody rbDocType = createPart(req.documentType);
            RequestBody rbDocNum = createPart(req.documentNumber);
            RequestBody rbSsName = createPart(req.ssName);
            RequestBody rbSecChannel = createPart(req.secondaryChannel);
            RequestBody rbLatLong = createPart(req.latLong);

            // Create File Parts
            MultipartBody.Part partDoc = prepareFilePart("uploadDocument", req.uploadDocument);
            MultipartBody.Part partBoard = prepareFilePart("UploadShopBoardImage", req.uploadShopBoardImage);
            MultipartBody.Part partInside = prepareFilePart("UploadShopInsideImage", req.uploadShopInsideImage);

//            apiService.addStore("Bearer " + token,
//                            rbOwner, rbEmail, rbMobile, rbStore, rbRs, rbSecChannel, rbType, rbSsName,
//                            rbPin, rbAddress,
//                            rbCountry, rbState, rbDistrict, rbCity,
//                            rbLatLong, rbDocType, rbDocNum,
//                            rbBeat,
//                            partDoc, partBoard, partInside)
//                    .enqueue(new Callback<GeneralResponse>() {
//                        @Override
//                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
//                            if(response.isSuccessful() && response.body() != null) {
//                                if("success".equalsIgnoreCase(response.body().getStatus()))
//                                    liveData.postValue(Resource.success(response.body()));
//                                else
//                                    liveData.postValue(Resource.error(response.body().getMessage(), null));
//                            } else {
//                                liveData.postValue(Resource.error("Error: " + response.code() + " " + response.message(), null));
//                            }
//                        }
//                        @Override public void onFailure(Call<GeneralResponse> call, Throwable t) {
//                            liveData.postValue(Resource.error("Network Error: " + t.getMessage(), null));
//                        }
//                    });

            apiService.addStore("Bearer " + token,
                            rbOwner, rbEmail, rbMobile, rbStore, rbRs, rbSecChannel, rbType, rbSsName,
                            rbPin, rbAddress,
                            rbCountry, rbState, rbDistrict, rbCity,
                            rbLatLong, rbDocType, rbDocNum,
                            rbBeat,
                            partDoc, partBoard, partInside)
                    .enqueue(new Callback<GeneralResponse>() {
                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                            // --- 2. LOG THE RESPONSE ---
                            Log.d("addstoreaftercall", "API Response Code: " + response.code());

                            if(response.isSuccessful() && response.body() != null) {
                                Log.d("addstoreaftercall", "API Success Status: " + response.body().getStatus());
                                Log.d("addstoreaftercall", "API Message: " + response.body().getMessage());

                                if ("success".equalsIgnoreCase(response.body().getStatus())) {

                                    liveData.postValue(Resource.success(response.body()));

                                } else {
                                    String cleanMsg =
                                            cleanErrorMessage(response.body().getMessage());
                                    liveData.postValue(Resource.error(cleanMsg, null));
                                }
                            } else {
                                // Log raw error body if possible
                                String errorBody = "";
                                try {
                                    if (response.errorBody() != null) errorBody = response.errorBody().string();
                                } catch (Exception e) { errorBody = "Unknown error"; }

                                Log.e("addstoreaftercall", "API Error Body: " + errorBody);
//                                liveData.postValue(Resource.error("Error: " + response.code() + " " + response.message(), null)  );
                                liveData.postValue(Resource.error("Something went wrong. Please try again.", null));
                            }
                        }
                        @Override public void onFailure(Call<GeneralResponse> call, Throwable t) {
                            // --- 3. LOG THE FAILURE ---
                            Log.d("addstoreaftercall", "API Failure: " + t.getMessage());
                            t.printStackTrace();
                            liveData.postValue(Resource.error("Network Error: " + t.getMessage(), null));
                        }
                    });
        }

        private String cleanErrorMessage(String message) {
            if (message == null) return "Something went wrong";
            if (message.contains(" (and")) {
                return message.substring(0, message.indexOf(" (and")).trim();
            }
            return message;
        }

        // Helper to prevent null pointer exceptions
        private RequestBody createPart(String value) {
            return RequestBody.create(MediaType.parse("text/plain"), value == null ? "" : value);
        }

        private MultipartBody.Part prepareFilePart(String partName, File file) {
            if (file == null) {
                return null;
            }
            // "image/*" matches most image uploads
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        }


        public void getRssIdentifiers(String token, MutableLiveData<Resource<RssResponse>> liveData) {
            liveData.postValue(Resource.loading(null));
            apiService.getRssIdentifiers("Bearer " + token).enqueue(new Callback<RssResponse>() {
                @Override
                public void onResponse(Call<RssResponse> call, Response<RssResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                        liveData.postValue(Resource.success(response.body()));
                    } else {
                        liveData.postValue(Resource.error("Failed to load RS/SS Identifiers", null));
                    }
                }
                @Override
                public void onFailure(Call<RssResponse> call, Throwable t) {
                    liveData.postValue(Resource.error(t.getMessage(), null));
                }
            });
        }

        // --- NEW: Fetch Secondary Channels ---
        public void getSecondaryChannels(String token, MutableLiveData<Resource<SecondaryChannelResponse>> liveData) {
            liveData.postValue(Resource.loading(null));
            apiService.getSecondaryChannels("Bearer " + token).enqueue(new Callback<SecondaryChannelResponse>() {
                @Override
                public void onResponse(Call<SecondaryChannelResponse> call, Response<SecondaryChannelResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                        liveData.postValue(Resource.success(response.body()));
                    } else {
                        liveData.postValue(Resource.error("Failed to load Secondary Channels", null));
                    }
                }
                @Override
                public void onFailure(Call<SecondaryChannelResponse> call, Throwable t) {
                    liveData.postValue(Resource.error(t.getMessage(), null));
                }
            });
        }
    }
