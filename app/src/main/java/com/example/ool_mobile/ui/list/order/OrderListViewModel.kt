package com.example.ool_mobile.ui.list.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ool_mobile.model.Order
import com.example.ool_mobile.service.Dependencies
import com.example.ool_mobile.service.api.OrderApi
import com.example.ool_mobile.ui.util.ErrorEvent
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel
import com.example.ool_mobile.ui.util.view_model.viewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class OrderListViewModel(
        private val orderApi: OrderApi
) : SubscriptionViewModel() {

    private val _events = PublishSubject.create<ErrorEvent>()

    val events: Observable<ErrorEvent> = _events

    val orders: LiveData<List<Order>> by lazy {
        MutableLiveData<List<Order>>().also { things ->

            orderApi.listAllOrders()
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(disposedWhenCleared())
                    .subscribe({
                        things.value = it
                    }, this::handleError)
        }
    }

    fun handleError(throwable: Throwable) {
        Timber.e(throwable)
        _events.onNext(ErrorEvent.Error)
    }

    companion object {

        @JvmStatic
        fun create(dependencies: Dependencies) = viewModelFactory {
            OrderListViewModel(dependencies.apiProvider.orderApi)
        }
    }


}