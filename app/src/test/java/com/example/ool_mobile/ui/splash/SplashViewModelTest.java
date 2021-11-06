package com.example.ool_mobile.ui.splash;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.ool_mobile.model.Employee;
import com.example.ool_mobile.service.EmployeeRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SplashViewModelTest {

    @Mock
    public EmployeeRepository employeeRepository;

    @Rule
    public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();

    private SplashViewModel viewModel;

    private Observable<SplashViewModel.Event> events;

    @Before
    public void setup() {

        RxAndroidPlugins.setInitMainThreadSchedulerHandler(a -> Schedulers.trampoline());
        RxAndroidPlugins.setMainThreadSchedulerHandler(a -> Schedulers.trampoline());

        viewModel = new SplashViewModel(employeeRepository);

        events = viewModel.getEvents().cache();

        // start caching events
        events.subscribe();
    }

    @Test
    public void emitsAlreadyLoggedEvent() {

        Employee sampleEmployee = Mockito.mock(Employee.class);

        Mockito.when(employeeRepository.getCurrentEmployee())
                .thenReturn(Maybe.just(sampleEmployee));

        viewModel.checkAlreadyLogged();

        SplashViewModel.Event result = events.firstElement()
                .blockingGet();

        assertThat(result)
                .isSameInstanceAs(SplashViewModel.Event.AlreadyLogged);
    }

    @Test
    public void emitsNotLoggedEvent() {

        Mockito.when(employeeRepository.getCurrentEmployee())
                .thenReturn(Maybe.empty());

        viewModel.checkAlreadyLogged();

        SplashViewModel.Event result = events.firstElement()
                .blockingGet();

        assertThat(result)
                .isSameInstanceAs(SplashViewModel.Event.NotLogged);
    }

    @Test
    public void emitsErrorEvent() {

        Mockito.when(employeeRepository.getCurrentEmployee())
                .thenReturn(Maybe.error(new UnsupportedOperationException()));

        viewModel.checkAlreadyLogged();


        SplashViewModel.Event result = events.firstElement()
                .blockingGet();

        assertThat(result)
                .isSameInstanceAs(SplashViewModel.Event.Error);
    }
}
