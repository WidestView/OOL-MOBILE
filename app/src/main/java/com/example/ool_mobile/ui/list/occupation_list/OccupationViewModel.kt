package com.example.ool_mobile.ui.list.occupation_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ool_mobile.model.Occupation
import com.example.ool_mobile.service.Dependencies
import com.example.ool_mobile.service.api.EmployeeApi
import com.example.ool_mobile.ui.util.ErrorEvent
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel
import com.example.ool_mobile.ui.util.view_model.viewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class OccupationViewModel(
        private val api: EmployeeApi
) : SubscriptionViewModel() {

    private val _events = PublishSubject.create<ErrorEvent>()

    val events: Observable<ErrorEvent> = _events

    val occupations: LiveData<List<Occupation>> by lazy {
        MutableLiveData<List<Occupation>>().also { occupations ->

            api.listOccupations().observeOn(AndroidSchedulers.mainThread())
                    .to(disposedWhenCleared())
                    .subscribe({
                        occupations.value = it
                    }, {
                        Timber.e(it)
                        _events.onNext(ErrorEvent.Error)
                    })
        }
    }

    companion object {
        @JvmStatic
        fun create(dependencies: Dependencies) = viewModelFactory {
            OccupationViewModel(dependencies.apiProvider.employeeApi)
        }
    }
}