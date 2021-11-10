package com.example.ool_mobile.ui.list.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ool_mobile.model.Employee
import com.example.ool_mobile.service.api.EmployeeApi
import com.example.ool_mobile.ui.util.ErrorEvent
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel
import com.example.ool_mobile.ui.util.view_model.viewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class EmployeeListViewModel(
        employeeApi: EmployeeApi
) : SubscriptionViewModel() {

    private val _events = PublishSubject.create<ErrorEvent>()

    val events: Observable<ErrorEvent> = _events


    private val _employees = MutableLiveData<List<Employee>>()

    val employees: LiveData<List<Employee>> = _employees


    init {
        employeeApi
                .listEmployees()
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared())
                .subscribe({ result ->
                    _employees.value = result
                }, this::handleError)


    }


    private fun handleError(error: Throwable) {
        Timber.e(error)
        _events.onNext(ErrorEvent.Error)
    }

    companion object {
        @JvmStatic
        fun create(employeeApi: EmployeeApi) = viewModelFactory {
            EmployeeListViewModel(employeeApi)
        }
    }
}