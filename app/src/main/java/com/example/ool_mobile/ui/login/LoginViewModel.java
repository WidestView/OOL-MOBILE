package com.example.ool_mobile.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.service.EmployeeRepository;
import com.example.ool_mobile.ui.util.ViewModelFactory;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class LoginViewModel extends ViewModel {

    private final PublishSubject<Boolean> events = PublishSubject.create();

    private final EmployeeRepository repository;

    public LoginViewModel(@NonNull EmployeeRepository repository) {
        this.repository = repository;
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull EmployeeRepository repository) {
        return ViewModelFactory.create(
                LoginViewModel.class,
                () -> new LoginViewModel(repository)
        );
    }

    @NonNull
    public Observable<Boolean> getEvents() {
        return events;
    }

    public void login(@NonNull String username, @NonNull String password) {

        Single<Boolean> result = repository.login(username, password);

        result.subscribe(new DisposableSingleObserver<Boolean>() {
            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Boolean success) {
                events.onNext(success);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable ex) {
                events.onError(ex);
            }
        });
    }

}
