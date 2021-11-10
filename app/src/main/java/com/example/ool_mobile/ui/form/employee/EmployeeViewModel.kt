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

            @JvmField
            val MissingName = Event { it.visitMissingName() }

            @JvmField
            val MissingSocialName = Event { it.visitMissingSocialName() }

            @JvmField
            val MissingBirthDate = Event { it.visitMissingBirthDate() }

            @JvmField
            val MissingPhone = Event { it.visitMissingPhone() }

            @JvmField
            val MissingEmail = Event { it.visitMissingEmail() }

            @JvmField
            val MissingPassword = Event { it.visitMissingPassword() }

            @JvmField
            val MissingPasswordConfirmation = Event { it.visitMissingPasswordConfirmation() }

            @JvmField
            val MissingAccessLevel = Event { it.visitMissingAccessLevel() }

            @JvmField
            val MissingGender = Event { it.visitMissingGender() }

            @JvmField
            val MissingOccupation = Event { it.visitMissingOccupation() }

            @JvmField
            val PasswordsDoNotMatch = Event { it.visitPasswordsDoNotMatch() }

            @JvmField
            val InvalidPhone = Event { it.visitInvalidPhone() }

            @JvmField
            val MissingRg = Event { it.visitMissingRg() }

        }

        interface Visitor {
            fun visitError()
            fun visitSuccess()
            fun visitMissingName()
            fun visitMissingSocialName()
            fun visitMissingBirthDate()
            fun visitMissingPhone()
            fun visitMissingEmail()
            fun visitMissingPassword()
            fun visitMissingPasswordConfirmation()
            fun visitMissingAccessLevel()
            fun visitMissingGender()
            fun visitMissingOccupation()
            fun visitPasswordsDoNotMatch()
            fun visitInvalidPhone()
            fun visitMissingRg()
        }
    }
}