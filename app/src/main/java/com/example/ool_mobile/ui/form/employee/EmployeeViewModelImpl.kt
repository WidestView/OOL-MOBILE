package com.example.ool_mobile.ui.form.employee

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.ool_mobile.service.Dependencies
import com.example.ool_mobile.service.EmployeeRepository
import com.example.ool_mobile.service.api.ApiUtil
import com.example.ool_mobile.service.api.EmployeeApi
import com.example.ool_mobile.ui.form.employee.EmployeeViewModel.Event
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel
import com.example.ool_mobile.ui.util.view_model.viewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class EmployeeViewModelImpl(
        employeeRepository: EmployeeRepository,
        private val employeeApi: EmployeeApi
) : SubscriptionViewModel(), EmployeeViewModel {

    override val input: LiveData<EmployeeInput> by lazy {
        MutableLiveData<EmployeeInput>().also { input ->
            employeeRepository.currentEmployee
                    .toSingle()
                    .zipWith(listsSingle, ::EmployeeInput)
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(disposedWhenCleared())
                    .subscribe({ result ->
                        input.value = result
                    }, { error ->
                        handleError(error)
                    })
        }
    }

    override val imageUrl: LiveData<Uri> = MutableLiveData(
            Uri.parse(employeeRepository.currentEmployeePictureURL.toString())
    )

    private val _events = PublishSubject.create<Event>()

    override val events: Observable<Event> = _events

    override val imageBitmap: MutableLiveData<Bitmap> = MutableLiveData()


    private val validation = EmployeeValidation(_events)


    override fun save() {

        val input = input.value ?: return

        validation.validate(input)
                .flatMapSingle { employee ->
                    employeeApi.updateCurrentEmployee(employee)
                }
                .flatMapSingle {
                    uploadImage().toSingleDefault(true)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared())
                .subscribe({
                    _events.onNext(Event.Success)
                }, this::handleError)
    }

    private fun uploadImage() = Completable.defer {

        val bitmap = imageBitmap.value

        if (bitmap == null) {
            Completable.complete()
        } else {
            ApiUtil.multiPartFromBitmap(bitmap)
                    .flatMapCompletable { file ->
                        employeeApi.setCurrentUserImage(file)
                    }
        }
    }

    private fun handleError(error: Throwable) {
        Timber.e(error)
        _events.onNext(Event.Error)
    }

    private val listsSingle: Single<EmployeeInput.Lists> by lazy {
        employeeApi.listAccessLevels()
                .zipWith(employeeApi.listOccupations(), EmployeeInput::Lists)
                .cache()
    }

    companion object {

        @JvmStatic
        fun create(owner: ViewModelStoreOwner, dependencies: Dependencies): EmployeeViewModel {
            return ViewModelProvider(owner, create(dependencies))
                    .get(EmployeeViewModelImpl::class.java)
        }

        @JvmStatic
        private fun create(dependencies: Dependencies) = viewModelFactory {
            EmployeeViewModelImpl(
                    dependencies.employeeRepository,
                    dependencies.apiProvider.employeeApi
            )
        }
    }
}