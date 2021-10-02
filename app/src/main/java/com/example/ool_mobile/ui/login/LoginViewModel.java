package com.example.ool_mobile.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.service.EmployeeRepository;
import com.example.ool_mobile.ui.util.ViewModelFactory;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class LoginViewModel extends ViewModel {

    public static final int START_CONTENT_WITHOUT_ANIMATION = 1;
    public static final int START_CONTENT_WITH_ANIMATION = 2;
    public static final int REPORT_FAILED_LOGIN = 3;
    public static final int REPORT_API_UNAVAILABLE = 4;

    private final PublishSubject<Integer> events = PublishSubject.create();

    private final EmployeeRepository repository;

    private final CompositeDisposable subscriptions = new CompositeDisposable();

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
    public Observable<Integer> getEvents() {
        return events;
    }

    public void checkAlreadyLogged() {

        Disposable subscription = repository.getCurrentEmployee()
                .map(employee -> true)
                .switchIfEmpty(Single.just(false))
                .subscribe(isLogged -> {
                    if (isLogged) {
                        events.onNext(START_CONTENT_WITHOUT_ANIMATION);
                    }
                }, error -> {
                    events.onNext(REPORT_API_UNAVAILABLE);

                    error.printStackTrace();
                });

        subscriptions.add(subscription);
    }

    public void login(@NonNull String username, @NonNull String password) {

        Single<Boolean> result = repository.login(username, password);

        subscriptions.add(result.subscribe(success -> {
            if (success) {
                events.onNext(START_CONTENT_WITH_ANIMATION);
            } else {
                events.onNext(REPORT_FAILED_LOGIN);
            }
        }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        subscriptions.clear();
    }
}
