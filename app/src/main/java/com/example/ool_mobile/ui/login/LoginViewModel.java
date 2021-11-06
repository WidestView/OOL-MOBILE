package com.example.ool_mobile.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.service.EmployeeRepository;
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.view_model.ViewModelFactory;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class LoginViewModel extends SubscriptionViewModel {

    public interface Event {

        void accept(@NonNull Visitor visitor);

        Event StartContent = Visitor::visitStartContent;
        Event ReportFailedLogin = Visitor::visitReportFailedLogin;
        Event ReportApiUnavailable = Visitor::visitReportApiUnavailable;

        interface Visitor {

            void visitStartContent();

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

    public void login() {

        String username = this.input.getEmail();
        String password = this.input.getPassword();

        repository.login(username, password)
                .to(disposedWhenCleared())
                .subscribe(success -> {
                    if (success) {
                        events.onNext(Event.StartContent);
                    } else {
                        events.onNext(Event.ReportFailedLogin);
                    }
                }, error -> {
                    error.printStackTrace();
                    events.onNext(Event.ReportApiUnavailable);
                });
    }
}
