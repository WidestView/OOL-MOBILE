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
import java.util.function.Consumer;

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

        assertThat(
                validation.validate(input).blockingGet()
        ).isNotNull();


        ensureNoError();
    }

    @Test
    public void reportsMissingName() {
        ensureError(Event.MissingName, input -> input.getName().set(null));
    }

    @Test
    public void reportsMissingBirthDate() {
        ensureError(Event.MissingBirthDate, input -> input.getBirthDate().set(null));
    }

    @Test
    public void reportsMissingPhone() {
        ensureError(Event.MissingPhone, input -> input.getPhone().set(null));
    }

    @Test
    public void reportsMissingEmail() {
        ensureError(Event.MissingEmail, input -> input.getEmail().set(null));
    }

    @Test
    public void reportsMissingPassword() {
        ensureError(Event.MissingPassword, input -> input.getPassword().set(null));
    }

    @Test
    public void reportsMissingPasswordConfirmation() {
        ensureError(Event.MissingPasswordConfirmation, input -> input.getPasswordConfirmation().set(null));
    }

    @Test
    public void reportsMissingAccessLevel() {
        ensureError(Event.MissingAccessLevel, input -> input.getAccessLevelSelection().set(-1));
    }

    @Test
    public void reportsMissingGender() {
        ensureError(Event.MissingGender, input -> input.getGender().set(null));
    }

    @Test
    public void reportsMissingOccupation() {
        ensureError(Event.MissingOccupation, input -> input.getOccupationSelection().set(-1));
    }

    @Test
    public void reportsPasswordsDoNotMatch() {
        ensureError(Event.PasswordsDoNotMatch, input -> {
            input.getPasswordConfirmation().set(input.getPassword().get() + "butDifferent");
        });
    }

    @Test
    public void reportsMissingRg() {
        ensureError(Event.MissingRg, input -> input.getRg().set(null));
    }


    private void ensureError(Event error, Consumer<EmployeeInput> consumer) {

        EmployeeInput input = createValidEmployeeInput();

        consumer.accept(input);

        assertThat(
                validation.validate(input).blockingGet()
        ).isNull();

        eventSubject.onComplete();

        assertThat(
                events.toList().blockingGet()
        ).contains(error);
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
