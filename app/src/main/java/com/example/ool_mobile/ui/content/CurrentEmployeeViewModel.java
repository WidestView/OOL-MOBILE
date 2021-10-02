package com.example.ool_mobile.ui.content;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.Employee;
import com.example.ool_mobile.service.EmployeeRepository;
import com.example.ool_mobile.ui.util.ViewModelFactory;

import java.net.URL;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class CurrentEmployeeViewModel extends ViewModel {

    @NonNull
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    @NonNull
    private final EmployeeRepository employeeRepository;
    private MutableLiveData<Employee> currentEmployee;

    private CurrentEmployeeViewModel(@NonNull EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull EmployeeRepository employeeRepository) {
        return ViewModelFactory.create(
                CurrentEmployeeViewModel.class,
                () -> new CurrentEmployeeViewModel(employeeRepository)
        );
    }

    @NonNull
    public LiveData<Employee> getCurrentEmployee() {

        if (currentEmployee == null) {
            currentEmployee = new MutableLiveData<>();

            compositeDisposable.add(
                    employeeRepository
                            .getCurrentEmployee()
                            .observeOn(AndroidSchedulers.mainThread())
                            .toSingle()
                            .subscribe(employee ->
                                    currentEmployee.setValue(employee)
                            )
            );
        }

        return currentEmployee;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    @NonNull
    public URL getEmployeeImageURL() {
        return employeeRepository.getCurrentEmployeePictureURL();
    }
}
