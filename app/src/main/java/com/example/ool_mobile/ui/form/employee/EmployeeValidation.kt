package com.example.ool_mobile.ui.form.employee

import com.example.ool_mobile.model.Employee
import com.example.ool_mobile.model.ImmutableEmployee
import com.example.ool_mobile.ui.form.employee.EmployeeViewModel.Event
import com.example.ool_mobile.ui.util.form.FormCheck
import com.example.ool_mobile.ui.util.form.FormCheck.failIf
import com.example.ool_mobile.ui.util.form.ValidationResult
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observer

class EmployeeValidation(
        private val events: Observer<Event>
) {

    fun validate(input: EmployeeInput): Maybe<Employee> {

        return FormCheck.validate(getChecks(input), events::onNext)
                .filter { it == ValidationResult.Success }
                .map {
                    ImmutableEmployee
                            .builder()
                            .cpf(input.cpf.get()!!)
                            .name(input.name.get()!!)
                            .socialName(input.socialName.get()!!)
                            .birthDate(input.birthDate.get()!!)
                            .phone(input.phone.get()!!)
                            .accessLevel(
                                    input.lists.accessLevels[
                                            input.accessLevelSelection.get()
                                    ].id
                            )
                            .email(input.email.get()!!)
                            .gender(input.gender.get()!!)
                            .occupationId(
                                    input.lists.occupations[
                                            input.occupationSelection.get()
                                    ].id()
                            )
                            .build()
                }
    }

    private fun getChecks(input: EmployeeInput): List<FormCheck<Event>> {

        normalize(input)

        return listOf(
                { input.name.get()?.isEmpty() }
                        to Event.MissingName,

                { input.socialName.get()?.isEmpty() }
                        to Event.MissingSocialName,

                { input.birthDate.get() == null }
                        to Event.MissingBirthDate,

                { input.phone.get()?.isEmpty() }
                        to Event.MissingPhone,

                { input.email.get()?.isEmpty() }
                        to Event.MissingEmail,

                { input.password.get()?.isEmpty() }
                        to Event.MissingPassword,

                { input.passwordConfirmation.get()?.isEmpty() }
                        to Event.MissingPasswordConfirmation,

                { input.accessLevelSelection.get() == -1 }
                        to Event.MissingAccessLevel,

                { input.gender.get()?.isEmpty() }
                        to Event.MissingGender,

                { input.occupationSelection.get() == -1 }
                        to Event.MissingOccupation,

                { input.password.get()?.equals(input.passwordConfirmation.get())?.not() }
                        to Event.PasswordsDoNotMatch,

                { input.phone.get()?.isEmpty() }
                        to Event.MissingPhone

        ).map {
            failIf({ it.first() ?: false }, it.second)
        }
    }

    private fun normalize(input: EmployeeInput) {
        input.cpf.set(input.cpf.get()?.trim())
        input.name.set(input.name.get()?.trim())
        input.socialName.set(input.socialName.get()?.trim())
        input.phone.set(input.phone.get()?.trim())
        input.email.set(input.email.get()?.trim())
        input.password.set(input.password.get()?.trim())
        input.passwordConfirmation.set(input.passwordConfirmation.get()?.trim())
        input.gender.set(input.gender.get()?.trim())
    }
}