package com.example.ool_mobile.ui.form.employee

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.example.ool_mobile.model.Occupation
import java.util.*

class EmployeeInput(
        val lists: Lists
) {

    val cpf = ObservableField<String>()
    val name = ObservableField<String>()
    val socialName = ObservableField<String>()
    val birthDate = ObservableField<Date>()
    val phone = ObservableField<String>()
    val email = ObservableField<String>()
    val password = ObservableField<String>()
    val passwordConfirmation = ObservableField<String>()
    val gender = ObservableField<String>()

    val accessLevelSelection = ObservableInt(-1)

    val occupationSelection = ObservableInt(-1)


    class Lists(
            val accessLevels: List<String>,
            val occupations: List<Occupation>
    )
}