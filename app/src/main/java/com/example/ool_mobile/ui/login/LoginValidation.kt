package com.example.ool_mobile.ui.login

import com.example.ool_mobile.ui.login.LoginViewModel.Event
import com.example.ool_mobile.ui.util.form.FormCheck
import com.example.ool_mobile.ui.util.form.ValidationResult
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observer

class LoginValidation(
        private val events: Observer<Event>
) {

    fun validate(loginInput: LoginInput): Maybe<LoginInput> {

        val normalized = LoginInput().apply {
            email = loginInput.email.trim()
            password = loginInput.password
        }

        return FormCheck.validate(check(normalized), events::onNext)
                .filter { it == ValidationResult.Success }
                .map { normalized }

    }

    fun check(loginInput: LoginInput): List<FormCheck<Event>> {

        return listOf(

                { loginInput.email.isEmpty() }
                        to Event.MissingLogin,

                { loginInput.password.isEmpty() }
                        to Event.MissingPassword

        ).map { (validation, event) -> FormCheck.failIf(validation, event) }


    }

}