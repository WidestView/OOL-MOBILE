package com.example.ool_mobile.ui.form.employee

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ool_mobile.service.Dependencies
import com.example.ool_mobile.service.EmployeeRepository
import com.example.ool_mobile.service.api.EmployeeApi
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel
import io.reactivex.rxjava3.core.Single

class EmployeeViewModelImpl(
        employeeRepository: EmployeeRepository,
        private val employeeApi: EmployeeApi
) : SubscriptionViewModel(), EmployeeViewModel {

    override val input: LiveData<EmployeeInput> by lazy {
        MutableLiveData<EmployeeInput>().also { input ->
            employeeRepository.currentEmployee
                    .toSingle()
                    .zipWith(listsSingle, ::EmployeeInput)
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

    override fun save() {

    }

    private fun handleError(error: Throwable) {
        error.printStackTrace()
    }

    private val listsSingle: Single<EmployeeInput.Lists> by lazy {
        employeeApi.listAccessLevels()
                .zipWith(employeeApi.listOccupations(), EmployeeInput::Lists)
                .cache()
    }

    companion object {
        @JvmStatic
        fun create(dependencies: Dependencies) {
            EmployeeViewModelImpl(
                    dependencies.employeeRepository,
                    dependencies.apiProvider.employeeApi
            )
        }
    }
}