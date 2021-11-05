package com.example.ool_mobile.ui.util.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


inline fun <reified VM : ViewModel> viewModelFactory(crossinline factory: () -> VM): ViewModelProvider.Factory {
    return ViewModelFactory.create(VM::class.java) {
        factory()
    }
}