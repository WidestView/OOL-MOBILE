package com.example.ool_mobile.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.EmployeeRepository;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.ui.util.ViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    @NonNull
    private final EmployeeRepository employeeRepository;
    @NonNull
    private final PhotoshootApi photoshootApi;
    private MutableLiveData<String> dayOfWeek;
    private MutableLiveData<String> dayOfMonth;
    private MutableLiveData<String> employeeName;
    private MutableLiveData<List<Photoshoot>> pendingPhotoshoots;

    public HomeViewModel(
            @NonNull EmployeeRepository employeeRepository,
            @NonNull PhotoshootApi photoshootApi) {
        this.employeeRepository = employeeRepository;
        this.photoshootApi = photoshootApi;
    }

    @NonNull
    public static ViewModelProvider.Factory create(
            @NonNull EmployeeRepository repository,
            @NonNull PhotoshootApi photoshootApi
    ) {
        return ViewModelFactory.create(
                HomeViewModel.class, () -> new HomeViewModel(repository, photoshootApi)
        );
    }

    @NonNull
    public LiveData<String> getEmployeeName() {

        if (employeeName == null) {
            employeeName = new MutableLiveData<>();

            compositeDisposable.add(
                    employeeRepository.getCurrentEmployee()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(employee -> employeeName.setValue(employee.name()))
            );

        }

        return employeeName;
    }

    @NonNull
    public LiveData<String> getDayOfWeek() {

        if (dayOfWeek == null) {
            dayOfWeek = new MutableLiveData<>();

            SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

            String currentDay = weekFormat.format(new Date());

            String result = currentDay.substring(0, Math.min(3, currentDay.length()));

            dayOfWeek.setValue(result);
        }

        return dayOfWeek;
    }

    @NonNull
    public LiveData<String> getDayOfMonth() {

        if (dayOfMonth == null) {

            dayOfMonth = new MutableLiveData<>();

            SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());

            String day = dayFormat.format(new Date());

            String result = day.substring(0, Math.min(3, day.length()));

            dayOfMonth.setValue(result);
        }

        return dayOfMonth;
    }

    @NonNull
    public LiveData<List<Photoshoot>> getPendingPhotoshoots() {

        if (pendingPhotoshoots == null) {
            pendingPhotoshoots = new MutableLiveData<>();

            compositeDisposable.add(
                    photoshootApi.listFromCurrentEmployee()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::onPhotoshootsFetched)
            );
        }

        return pendingPhotoshoots;
    }

    private void onPhotoshootsFetched(List<Photoshoot> photoshoots) {

        List<Photoshoot> result = photoshoots.stream()
                .filter(photoshoot -> isToday(photoshoot.startTime()))
                .collect(Collectors.toList());

        this.pendingPhotoshoots.setValue(result);
    }

    private boolean isToday(Date date) {
        return isSameDay(date, new Date());
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        return calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR) &&
                calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
    }
}