package com.example.ool_mobile.ui.form.employee

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.ool_mobile.ui.form.employee.EmployeeViewModel.Event
import io.reactivex.rxjava3.core.Observable

interface EmployeeViewModel {

    val input: LiveData<EmployeeInput>

    val imageUrl: LiveData<Uri>

    val events: Observable<Event>

    fun save()

    fun interface Event {

        fun accept(visitor: Visitor)

        companion object {

            @JvmField
            val Error = Event { it.visitError() }

            @JvmField
            val Success = Event { it.visitSuccess() }
        }

        interface Visitor {
            fun visitError()
            fun visitSuccess()
        }
    }
}