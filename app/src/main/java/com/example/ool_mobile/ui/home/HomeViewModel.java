package com.example.ool_mobile.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    @NonNull
    public String getDayOfWeek() {

        SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        String result = weekFormat.format(new Date());

        return result.substring(0, Math.min(3, result.length()));
    }

    @NonNull
    public String getDayOfMonth() {

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());

        String result =  dayFormat.format(new Date());

        return result.substring(0, Math.min(3, result.length()));
    }
}