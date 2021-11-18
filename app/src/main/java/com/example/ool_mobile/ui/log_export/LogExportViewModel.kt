package com.example.ool_mobile.ui.log_export

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.ool_mobile.service.Dependencies
import com.example.ool_mobile.service.log.LogDatabase
import com.example.ool_mobile.ui.log_export.LogExportViewModel.Event
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel
import com.example.ool_mobile.ui.util.view_model.viewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class LogExportViewModel(
        private val database: LogDatabase,
        private val logWriter: LogWriter
) : SubscriptionViewModel() {

    private val _events = PublishSubject.create<Event>()

    val events: Observable<Event> = _events

    private val _isLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean> = _isLoading


    private val _isEnabled = MutableLiveData(true)

    val isEnabled: LiveData<Boolean> = Transformations.switchMap(_isLoading) { loading ->
        Transformations.map(_isEnabled) { enabled ->
            !(loading ?: false) && (enabled ?: false)
        }
    }

    fun export() {

        database.listEntries()
                .flatMapCompletable { entries ->
                    logWriter.writeLogEntries(entries)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared<Unit>())
                .subscribe {
                    _events.onNext(Event.Success)
                }

    }

    companion object {

        @JvmStatic
        fun create(dependencies: Dependencies) = viewModelFactory {

            LogExportViewModel(dependencies.logDatabase, dependencies.logWriter)
        }
    }

    fun interface Event {

        fun accept(visitor: Visitor)

        interface Visitor {
            fun visitError()
            fun visitSuccess()
        }

        companion object {
            @JvmField
            val Error = Event { it.visitError() }

            @JvmField
            val Success = Event { it.visitSuccess() }
        }
    }

}