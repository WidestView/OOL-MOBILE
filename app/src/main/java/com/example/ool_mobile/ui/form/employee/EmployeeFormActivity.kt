package com.example.ool_mobile.ui.form.employee

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ool_mobile.R
import com.example.ool_mobile.databinding.ActivityEmployeeFormBinding
import com.example.ool_mobile.model.AccessLevel
import com.example.ool_mobile.model.Occupation
import com.example.ool_mobile.service.Dependencies
import com.example.ool_mobile.ui.form.employee.EmployeeViewModelImpl.Companion.create
import com.example.ool_mobile.ui.util.DisposedFromLifecycle
import com.example.ool_mobile.ui.util.image.ImageSelectionHandler
import com.example.ool_mobile.ui.util.image.LegacySelectionHandler
import com.example.ool_mobile.ui.util.snack
import com.example.ool_mobile.ui.util.swalError
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber

@RuntimePermissions
class EmployeeFormActivity : AppCompatActivity(), EmployeeViewModel.Event.Visitor {

    private lateinit var binding: ActivityEmployeeFormBinding

    private lateinit var employeeViewModel: EmployeeViewModel

    private lateinit var imageSelectionHandler: ImageSelectionHandler

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_form)

        employeeViewModel = create(
                this,
                Dependencies.from(this)
        )

        binding.activity = this
        binding.lifecycleOwner = this
        binding.errors = EmployeeFormErrors()
        binding.viewModel = employeeViewModel

        imageSelectionHandler = LegacySelectionHandler(this)
    }

    override fun onStart() {
        super.onStart()

        employeeViewModel.events
                .to(DisposedFromLifecycle.of(this))
                .subscribe { event -> event.accept(this) }

        imageSelectionHandler.bitmapResults
                .observeOn(AndroidSchedulers.mainThread())
                .to(DisposedFromLifecycle.of(this))
                .subscribe { bitmap -> employeeViewModel.imageBitmap.setValue(bitmap) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // shut up android.
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        imageSelectionHandler.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imageSelectionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // PermissionDispatcher delegate
        onRequestPermissionsResult(requestCode, grantResults)
    }

    fun onGalleryClick() {
        imageSelectionHandler.requestGallery()
                .to(DisposedFromLifecycle.of<Any>(this))
                .subscribe()
    }

    fun onCameraClick() {
        openCameraWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun openCamera() {

        imageSelectionHandler.requestCamera()
                .to(DisposedFromLifecycle.of<Any>(this))
                .subscribe({}) { error ->
                    Timber.e(error)
                    visitError()
                }
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun onPermissionDenied() {
        snack(this, R.string.message_permission_required)
    }

    fun formatAccessLevels(accessLevels: List<AccessLevel>?): List<String>? {
        return accessLevels?.map { accessLevel ->
            String.format(getString(R.string.format_id_name),
                    accessLevel.id, accessLevel.name)

        }
    }

    fun formatOccupations(occupations: List<Occupation>?): List<String>? {
        return occupations?.map { occupation: Occupation ->
            String.format(getString(R.string.format_id_name),
                    occupation.id(), occupation.name())
        }
    }

    override fun visitError() {
        swalError(this)
    }

    override fun visitSuccess() {
        finish()
    }

    override fun visitMissingName() {
        binding.errors!!.nameError
                .set(getString(R.string.error_missing_employee_name))
    }

    override fun visitMissingBirthDate() {
        binding.errors!!.birthDateError
                .set(getString(R.string.error_missing_employee_birth_date))
        snack(this, R.string.error_missing_employee_birth_date)
    }

    override fun visitMissingPhone() {
        binding.errors!!.phoneError
                .set(getString(R.string.error_missing_employee_phone))
    }

    override fun visitMissingEmail() {
        binding.errors!!.emailError
                .set(getString(R.string.error_missing_employee))
    }

    override fun visitMissingPassword() {
        binding.errors!!.passwordError
                .set(getString(R.string.error_missing_employee_password))
    }

    override fun visitMissingPasswordConfirmation() {
        binding.errors!!.passwordConfirmationError
                .set(getString(R.string.error_missing_employee_password_confirmation))
    }

    override fun visitMissingAccessLevel() {
        binding.errors!!.accessLevelError
                .set(getString(R.string.error_missing_employee_access_level))
    }

    override fun visitMissingGender() {
        binding.errors!!.genderError
                .set(getString(R.string.error_missing_employee_gender))
    }

    override fun visitMissingOccupation() {
        binding.errors!!.occupationError
                .set(getString(R.string.error_missing_employee_occupation))
    }

    override fun visitPasswordsDoNotMatch() {
        binding.errors!!.passwordConfirmationError
                .set(getString(R.string.error_passwords_not_match))
    }

    override fun visitInvalidPhone() {
        binding.errors!!.phoneError
                .set(getString(R.string.error_invalid_employeee_phone))
    }

    override fun visitMissingRg() {
        binding.errors!!.rgError
                .set(getString(R.string.invalid_employee_rg))
    }
}