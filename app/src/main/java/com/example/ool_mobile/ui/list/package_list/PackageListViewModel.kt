package com.example.ool_mobile.ui.list.package_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ool_mobile.model.PackageModel
import com.example.ool_mobile.service.Dependencies
import com.example.ool_mobile.service.api.PackageApi
import com.example.ool_mobile.ui.util.ErrorEvent
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel
import com.example.ool_mobile.ui.util.view_model.viewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class PackageListViewModel(
        private val api: PackageApi
) : SubscriptionViewModel() {

    private val _events = PublishSubject.create<ErrorEvent>()

    val events: Observable<ErrorEvent> = _events

    val packages: LiveData<List<PackageModel>> by lazy {

        MutableLiveData<List<PackageModel>>().also { packages ->
            api.listPackages()
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(disposedWhenCleared())
                    .subscribe({
                        packages.value = it
                    }, this::handleError)
        }
    }

    private fun handleError(error: Throwable) {
        Timber.e(error)
        _events.onNext(ErrorEvent.Error)
    }

    companion object {
        @JvmStatic
        fun create(dependencies: Dependencies) = viewModelFactory {
            PackageListViewModel(dependencies.apiProvider.packageApi)
        }
    }
}