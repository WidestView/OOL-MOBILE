package com.example.ool_mobile.ui.util.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Initializes a ViewModelFactory from the given ViewModelProvider
 *
 * This is an inline function, the compiler will place its executable code inside the caller
 * when possible, allowing certain capabilities that cannot be normally achieved.
 *
 * One of this capabilities,are reified type argument which can get their Class objects extracted.
 * This is an important feature for making this utility class without requiring the caller to
 * pass the class object as its parameter.
 *
 * The callback parameters have to be crossinline to be called within an inline function.
 */
inline fun <reified VM : ViewModel> viewModelFactory(crossinline factory: () -> VM): ViewModelProvider.Factory {
    return ViewModelFactory.create(VM::class.java) {
        factory()
    }
}