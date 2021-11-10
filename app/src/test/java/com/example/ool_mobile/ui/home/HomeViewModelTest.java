package com.example.ool_mobile.ui.home;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.ool_mobile.TrampolineSchedulersRule;
import com.example.ool_mobile.model.Employee;
import com.example.ool_mobile.model.ImmutableEmployee;
import com.example.ool_mobile.model.ImmutablePhotoshoot;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.EmployeeRepository;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.ui.home.HomeViewModel.Event;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.example.ool_mobile.LiveDataTestUtil.observeWithTimeout;
import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class HomeViewModelTest {

    @Rule
    public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();

    @ClassRule
    public static TrampolineSchedulersRule trampolineRule = new TrampolineSchedulersRule();

    @Mock
    public EmployeeRepository employeeRepository;

    @Mock
    public PhotoshootApi photoshootApi;

    private HomeViewModel viewModel;

    @Before
    public void setup() {


        viewModel = new HomeViewModel(employeeRepository, photoshootApi);
    }

    @Test
    public void employeeNameSendsErrorEvent() {

        Observable<Event> events = viewModel.getEvents().cache();
        events.subscribe();

        Mockito.when(employeeRepository.getCurrentEmployee())
                .thenReturn(Maybe.error(new Throwable()));

        String name = viewModel.getEmployeeName().getValue();

        assertThat(name).isNull();

        Event event = events.firstElement().blockingGet();

        assertThat(event).isNotNull();
    }

    @Test
    public void photoshootListSendsErrorEvent() {

        Observable<Event> events = viewModel.getEvents().cache();
        events.subscribe();

        Mockito.when(photoshootApi.listFromCurrentEmployee())
                .thenReturn(Single.error(new Throwable()));

        assertThat(
                viewModel.getPendingPhotoshoots().getValue()
        ).isNull();

        assertThat(
                events.firstElement().blockingGet()
        ).isNotNull();
    }


    @Test
    public void getsEmployeeName() throws Exception {

        Employee sampleEmployee = ImmutableEmployee.builder()
                .cpf("11111111111")
                .rg("1234567")
                .gender("male")
                .occupationId(1)
                .accessLevel(10)
                .email("bob@bob.com")
                .phone("11980214989")
                .birthDate(new Date())
                .name("bob")
                .occupation(null)
                .socialName(null)
                .build();

        Mockito.when(employeeRepository.getCurrentEmployee())
                .thenReturn(Maybe.just(sampleEmployee).subscribeOn(Schedulers.io()));

        String name = observeWithTimeout(viewModel.getEmployeeName());

        assertThat(name).isEqualTo(sampleEmployee.name());
    }

    @Test
    public void getDayOfWeek() throws Exception {

        String day = observeWithTimeout(viewModel.getDayOfWeek());

        assertThat(day).isNotEmpty();

        assertThat(day.length()).isAtMost(3);
    }

    @Test
    public void getDayOfMonth() throws Exception {

        String month = observeWithTimeout(viewModel.getDayOfMonth());

        assertThat(month.length()).isAtMost(2);
    }

    @Test
    public void getsPendingPhotoshoots() throws Exception {

        Photoshoot photoshoot = ImmutablePhotoshoot
                .builder()
                .durationMinutes(10)
                .startTime(new Date())
                .orderId(1)
                .address("Some place")
                .resourceId(UUID.randomUUID())
                .build();

        Mockito.when(photoshootApi.listFromCurrentEmployee())
                .thenReturn(Single.just(Collections.singletonList(photoshoot)).subscribeOn(Schedulers.io()));

        assertThat(
                observeWithTimeout(viewModel.getPendingPhotoshoots())
        ).contains(photoshoot);
    }
}