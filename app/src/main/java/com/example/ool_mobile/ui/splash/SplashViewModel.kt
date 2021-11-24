package com.example.ool_mobile.ui.splash

import com.example.ool_mobile.service.EmployeeRepository
import com.example.ool_mobile.ui.splash.SplashViewModel.Event
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel
import com.example.ool_mobile.ui.util.view_model.viewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SplashViewModel(
        private val repository: EmployeeRepository
) : SubscriptionViewModel() {

    private val _events = PublishSubject.create<Event>()

    val events: Observable<Event> = _events

    fun checkAlreadyLogged() {

        repository.currentEmployee
                .map { true }
                .switchIfEmpty(Single.just(false))
                .delay(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread(), true)
                .to(disposedWhenCleared())
                .subscribe({ isLogged ->
                    if (isLogged) {
                        _events.onNext(Event.AlreadyLogged)
                    } else {
                        _events.onNext(Event.NotLogged)
                    }
                }, { error ->
                    _events.onNext(Event.Error)

                    Timber.e(error)
                })
    }


    companion object {

        @JvmStatic
        fun create(repository: EmployeeRepository) = viewModelFactory {
            SplashViewModel(repository)
        }
    }

    fun interface Event {

        fun accept(visitor: Visitor)

        companion object {

            @JvmField
            val Error = Event { it.visitError() }

            @JvmField
            val NotLogged = Event { it.visitNotLogged() }

            @JvmField
            val AlreadyLogged = Event { it.visitAlreadyLogged() }
        }

        interface Visitor {
            fun visitError()
            fun visitNotLogged()
            fun visitAlreadyLogged()
        }

    }
}