package com.example.ool_mobile.ui.login;

import com.example.ool_mobile.TrampolineSchedulersRule;
import com.example.ool_mobile.service.EmployeeRepository;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class LoginViewModelTest {

    @ClassRule
    public static TrampolineSchedulersRule schedulersRule = new TrampolineSchedulersRule();

    @Mock
    public EmployeeRepository employeeRepository;

    private LoginViewModel viewModel;

    private Observable<LoginViewModel.Event> events;

    @Before
    public void setup() {
        viewModel = new LoginViewModel(employeeRepository);

        events = viewModel.getEvents().cache();

        events.subscribe();
    }

    @Test
    public void initializesInput() {

        assertThat(viewModel.getInput())
                .isNotNull();

    }

    @Test
    public void reportsSuccessLogin() {

        Mockito.when(
                employeeRepository.login(any(), any())
        ).thenReturn(Single.just(true));

        viewModel.login();

        LoginViewModel.Event event = events.firstElement().blockingGet();

        assertThat(event).isSameInstanceAs(LoginViewModel.Event.StartContent);
    }

    @Test
    public void reportsFailedLogin() {

        Mockito.when(
                employeeRepository.login(any(), any())
        ).thenReturn(Single.just(false));

        viewModel.login();

        LoginViewModel.Event event = events.firstElement().blockingGet();

        assertThat(event).isSameInstanceAs(LoginViewModel.Event.ReportFailedLogin);
    }

    @Test
    public void usesLoginInput() {

        Mockito.when(
                employeeRepository.login(anyString(), anyString())
        ).thenReturn(Single.just(false));

        Mockito.when(
                employeeRepository.login("beep", "boop")
        ).thenReturn(Single.just(true));

        viewModel.getInput().setEmail("beep");
        viewModel.getInput().setPassword("boop");

        viewModel.login();

        LoginViewModel.Event event = events.firstElement().blockingGet();

        assertThat(event).isSameInstanceAs(LoginViewModel.Event.StartContent);
    }

    @Test
    public void reportsError() {

        Mockito.when(
                employeeRepository.login(anyString(), anyString())
        ).thenReturn(Single.error(new UnsupportedOperationException()));

        viewModel.login();

        LoginViewModel.Event event = events.firstElement().blockingGet();

        assertThat(event).isSameInstanceAs(LoginViewModel.Event.ReportApiUnavailable);
    }
}