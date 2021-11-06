package com.example.ool_mobile.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ool_mobile.service.EmployeeRepository
import com.example.ool_mobile.ui.login.LoginViewModel.Event
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel
import com.example.ool_mobile.ui.util.view_model.viewModelFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class LoginViewModel(private val repository: EmployeeRepository) : SubscriptionViewModel() {

    val input = LoginInput()


    private val _events = PublishSubject.create<Event>()

    val events: Observable<Event> = _events


    private val _isEnabled = MutableLiveData(true)

    val isEnabled: LiveData<Boolean> = _isEnabled

    fun initializeErrorState(isError: Boolean, visitor: Event.Visitor) {
        _isEnabled.value = !isError

        if (isError) {
            Event.ReportApiUnavailable.accept(visitor)
        }
    }


    fun login() {

        repository.login(input.email, input.password)
                .to(disposedWhenCleared())
                .subscribe({ success ->
                    if (success) {
                        _events.onNext(Event.StartContent)
                    } else {
                        _events.onNext(Event.ReportFailedLogin)
                    }
                }) { error ->
                    error.printStackTrace()
                    _events.onNext(Event.ReportApiUnavailable)
                    _isEnabled.value = false
                }
    }

    fun interface Event {
        fun accept(visitor: Visitor)

        interface Visitor {
            fun visitStartContent()
            fun visitReportFailedLogin()
            fun visitReportApiUnavailable()
        }

        companion object {
            @JvmField
            val StartContent = Event { it.visitStartContent() }

            @JvmField
            val ReportFailedLogin = Event { it.visitReportFailedLogin() }

            @JvmField
            val ReportApiUnavailable = Event { it.visitReportApiUnavailable() }
        }
    }

    companion object {
        @JvmStatic
        fun create(repository: EmployeeRepository) = viewModelFactory {
            LoginViewModel(repository)
        }
    }
}