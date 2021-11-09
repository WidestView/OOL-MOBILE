package com.example.ool_mobile.ui.form.employee

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.example.ool_mobile.model.AccessLevel
import com.example.ool_mobile.model.Employee
import com.example.ool_mobile.model.Occupation

class EmployeeInput(
        employee: Employee,
        val lists: Lists
) {

    val cpf = ObservableField(employee.cpf())

    val name = ObservableField(employee.name())

    val socialName = ObservableField(employee.socialName())

    val birthDate = ObservableField(employee.birthDate())

    val phone = ObservableField(employee.phone())

    val email = ObservableField(employee.email())

    val password = ObservableField<String>()

    val passwordConfirmation = ObservableField<String>()

    val gender = ObservableField(employee.gender())

    val accessLevelSelection = ObservableInt(
            lists.accessLevels.first { it.id == employee.accessLevel() }.id
    )

    val occupationSelection = ObservableInt(
            lists.occupations.first { it.id() == employee.occupationId() }.id()
    )

    class Lists(
            val accessLevels: List<AccessLevel>,
            val occupations: List<Occupation>
    )
}