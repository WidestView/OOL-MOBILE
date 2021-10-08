package com.example.ool_mobile.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.service.EmployeeRepository;
import com.example.ool_mobile.ui.util.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.ViewModelFactory;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class LoginViewModel extends SubscriptionViewModel {

    public interface Event {

        void accept(@NonNull Visitor visitor);

        Event StartContentWithoutAnimation = Visitor::visitStartContentWithoutAnimation;
        Event StartContentWithAnimation = Visitor::visitStartContentWithAnimation;
        Event ReportFailedLogin = Visitor::visitReportFailedLogin;
        Event ReportApiUnavailable = Visitor::visitReportApiUnavailable;

        interface Visitor {
            void visitStartContentWithoutAnimation();

            void visitStartContentWithAnimation();

            void visitReportFailedLogin();

            void visitReportApiUnavailable();
        }
    }

    private final PublishSubject<Event> events = PublishSubject.create();

    private final LoginInput input = new LoginInput();

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
    public Observable<Event> getEvents() {
        return events;
    }

    @NonNull
    public LoginInput getInput() {
        return input;
    }

    public void checkAlreadyLogged() {

        Disposable subscription = repository.getCurrentEmployee()
                .map(employee -> true)
                .switchIfEmpty(Single.just(false))
                .subscribe(isLogged -> {
                    if (isLogged) {
                        events.onNext(Event.StartContentWithoutAnimation);
                    }
                }, error -> {
                    events.onNext(Event.ReportApiUnavailable);

                    error.printStackTrace();
                });

        subscriptions.add(subscription);
    }

    public void login() {

        String username = this.input.getEmail();
        String password = this.input.getPassword();

        Single<Boolean> result = repository.login(username, password);

        subscriptions.add(result.subscribe(success -> {
            if (success) {
                events.onNext(Event.StartContentWithAnimation);
            } else {
                events.onNext(Event.ReportFailedLogin);
            }
        }));
    }
}
