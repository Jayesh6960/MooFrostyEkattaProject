package com.example.moofrosty.ui.newstorecreation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.StoreCreationModel;
import com.example.moofrosty.data.model.StoreExistResponse;
import com.example.moofrosty.data.model.StoreListResponse;
import com.example.moofrosty.data.repository.NewStoreListRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewStoreListViewModel extends ViewModel {

    private NewStoreListRepository repository;
    private MutableLiveData<Resource<List<StoreListResponse.StoreModel>>> storeList = new MutableLiveData<>();
    private MutableLiveData<String> selectedDate = new MutableLiveData<>();
    private Calendar currentCalendar = Calendar.getInstance();
    private String token = "";

    public NewStoreListViewModel() {
        repository = new NewStoreListRepository();
        // Set Today's Date by default
  //      updateDate(currentCalendar.getTime());
    }

    public void setToken(String token) {
        this.token = token;
        // Fetch for today immediately when token is available
        updateDate(currentCalendar.getTime());
    }

  //  public LiveData<List<StoreCreationModel>> getStoreList() {
//        return storeList;
//    }
    public LiveData<Resource<List<StoreListResponse.StoreModel>>> getStoreList() {
        return storeList;
    }

    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }

    public void setDate(int year, int month, int day) {
        currentCalendar.set(year, month, day);
        updateDate(currentCalendar.getTime());
    }
    private void updateDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String formattedDate = sdf.format(date);
        selectedDate.setValue(formattedDate);

        if (!token.isEmpty()) {
            repository.fetchStoresByDate(token, formattedDate, storeList);
        }
    }
//    private void updateDate(Date date) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//        String formattedDate = sdf.format(date);
//        selectedDate.setValue(formattedDate);
//
//        // Fetch data for this date
//        repository.fetchStoresByDate(formattedDate, storeList);
//    }
}
