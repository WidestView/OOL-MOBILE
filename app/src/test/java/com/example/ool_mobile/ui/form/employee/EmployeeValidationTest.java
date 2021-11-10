package com.example.ool_mobile.ui.form.employee;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.ool_mobile.TrampolineSchedulersRule;
import com.example.ool_mobile.model.Employee;
import com.example.ool_mobile.model.ImmutableAccessLevel;
import com.example.ool_mobile.model.ImmutableEmployee;
import com.example.ool_mobile.model.ImmutableOccupation;
import com.example.ool_mobile.service.api.setup.json.EmployeeToJson;
import com.example.ool_mobile.ui.form.employee.EmployeeViewModel.Event;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

import static com.google.common.truth.Truth.assertThat;

public class EmployeeValidationTest {

    @Rule
    public TrampolineSchedulersRule schedulersRule = new TrampolineSchedulersRule();

    @Rule
    public InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule();

    private EmployeeValidation validation;

    private Subject<Event> eventSubject;
    private Observable<Event> events;

    @Before
    public void before() {

        eventSubject = PublishSubject.create();

        events = eventSubject.cache();

        // start caching
        events.subscribe();

        validation = new EmployeeValidation(eventSubject);
    }

    @Test
    public void validatesValidInput() {

        EmployeeInput input = createValidEmployeeInput();

        EmployeeToJson employee = validation.validate(input).blockingGet();

        eventSubject.onComplete();

        ensureNoError();

        assertThat(employee).isNotNull();
    }

    @Test
    public void allowsEmptySocialName() {

        EmployeeInput input = createValidEmployeeInput();

        input.getSocialName().set(null);

        validation.validate(input).blockingSubscribe();

        ensureNoError();
    }

    @Test
    public void reportsMissingName() {

        EmployeeInput input = createValidEmployeeInput();

        input.getName().set(null);

        validation.validate(input).blockingSubscribe();

        eventSubject.onComplete();

        assertThat(
                events.toList().blockingGet()
        ).containsExactly(Event.MissingName);
    }


    public void ensureNoError() {

        // we complete the event subject so we
        // don't wait for completion (forever).
        eventSubject.onComplete();

        assertThat(events.toList().blockingGet()).isEmpty();
    }


    private EmployeeInput createValidEmployeeInput() {

        Employee employee = ImmutableEmployee
                .builder()
                .cpf("11111111111")
                .name("bob")
                .socialName("boop")
                .occupationId(1)
                .birthDate(new Date())
                .phone("11940028922")
                .occupation(null)
                .email("beep@beep.com")
                .accessLevel(1)
                .gender("male")
                .rg("1111111111111")
                .build();


        EmployeeInput.Lists lists = new EmployeeInput.Lists(
                Collections.singletonList(
                        ImmutableAccessLevel
                                .builder()
                                .id(1)
                                .name("Some Access Level")
                                .build()
                ),
                Collections.singletonList(
                        ImmutableOccupation
                                .builder()
                                .id(1)
                                .name("Some name")
                                .description("Some description")
                                .build()
                )
        );

        EmployeeInput employeeInput = new EmployeeInput(employee, lists);
        employeeInput.getPassword().set("beepboop");
        employeeInput.getPasswordConfirmation().set("beepboop");

        return employeeInput;
    }
}
