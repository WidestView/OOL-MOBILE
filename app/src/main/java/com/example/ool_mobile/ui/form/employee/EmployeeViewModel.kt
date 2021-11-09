package com.example.ool_mobile.ui.form.employee

import android.net.Uri
import androidx.lifecycle.LiveData

interface EmployeeViewModel {

    val input: LiveData<EmployeeInput>

    val imageUrl: LiveData<Uri>

    fun save()
}