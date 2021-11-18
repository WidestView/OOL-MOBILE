package com.example.ool_mobile.ui.log_export

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.ool_mobile.service.Dependencies
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel
import com.example.ool_mobile.ui.util.view_model.viewModelFactory

class LogExportViewModel : SubscriptionViewModel() {

    private val _isLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean> = _isLoading


    private val _isEnabled = MutableLiveData(true)

    val isEnabled: LiveData<Boolean> = Transformations.switchMap(_isLoading) { loading ->
        Transformations.map(_isEnabled) { enabled ->
            (loading ?: false) && (enabled ?: false)
        }
    }

    val chosenDirectoryPath = MutableLiveData<Uri>()

    fun export() {

    }

    companion object {

        @JvmStatic
        fun create(dependencies: Dependencies) = viewModelFactory {

            TODO("Implement this")
        }
    }
}