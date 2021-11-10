package com.example.ool_mobile.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ool_mobile.model.Photoshoot
import com.example.ool_mobile.service.api.PhotoshootApi
import com.example.ool_mobile.ui.util.ErrorEvent
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel
import com.example.ool_mobile.ui.util.view_model.viewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

/**
 * Handles all logic related operations of the CalendarFragment
 */
class CalendarViewModel(private val photoshootApi: PhotoshootApi) : SubscriptionViewModel() {

    private val photoshootList = MutableLiveData<List<Photoshoot>>()

    private val _events = PublishSubject.create<ErrorEvent>()

    /**
     * The exposed error events
     */
    val events: Observable<ErrorEvent> = _events


    /**
     * Fetches the photoshoot list from the api
     */
    fun fetchPhotoshootList(): LiveData<List<Photoshoot>> {
        photoshootApi.listFromCurrentEmployee()
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared())
                .subscribe(photoshootList::setValue, this::handleError)

        return photoshootList
    }

    private fun handleError(error: Throwable) {
        Timber.e(error)
        _events.onNext(ErrorEvent.Error)
    }

    companion object {

        /**
         * Creates a new CalendarViewModel.
         */
        @JvmStatic
        fun create(photoshootApi: PhotoshootApi) = viewModelFactory {
            CalendarViewModel(photoshootApi)
        }
    }
}