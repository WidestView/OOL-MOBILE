package com.example.ool_mobile.ui.form.equipment_details

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.ool_mobile.R
import com.example.ool_mobile.service.Dependencies
import com.example.ool_mobile.ui.util.DisposedFromLifecycle
import com.example.ool_mobile.ui.util.form.FormModeValue
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
class EquipmentDetailsFormActivity : AppCompatActivity(), DetailsViewModel.Event.Visitor {

    private lateinit var binding: EquipmentDetailsFormBinding
    private lateinit var imageHandler: ImageSelectionHandler
    private lateinit var viewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_equipment_details_form)

        binding.activity = this
        binding.lifecycleOwner = this

        setupViewModel()

        imageHandler = LegacySelectionHandler(this)
    }

    override fun onStart() {
        super.onStart()

        imageHandler
                .bitmapResults
                .observeOn(AndroidSchedulers.mainThread())
                .to(DisposedFromLifecycle.of(this))
                .subscribe({ bitmap -> viewModel.setSelectedBitmap(bitmap) }) { error ->
                    Timber.e(error)
                    visitError()
                }

        viewModel
                .events
                .to(DisposedFromLifecycle.of(this))
                .subscribe({ event: DetailsViewModel.Event -> event.accept(this) }) { error ->
                    Timber.e(error)
                }
    }

    fun onCameraButtonClick() {
        openCameraWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun openCamera() {
        imageHandler
                .requestCamera()
                .to(DisposedFromLifecycle.of<Any>(this))
                .subscribe({}) { t: Throwable? -> Timber.e(t) }
    }

    fun onGalleryButtonClick() {
        imageHandler
                .requestGallery()
                .to(DisposedFromLifecycle.of<Any>(this))
                .subscribe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // shut up android
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        imageHandler.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imageHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)

        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun onPermissionDenied() {
        snack(this, R.string.message_permission_required)
    }

    private fun setupViewModel() {
        val args = EquipmentDetailsFormActivityArgs
                .fromBundle(intent.extras!!)

        viewModel = ViewModelProvider(this, DetailsViewModel.create(
                FormModeValue.convert(args.formMode),
                Dependencies.from(this).equipmentApi,

                if (args.resourceId == -1)
                    null
                else args.resourceId
        )).get(CommonDetailsViewModel::class.java)

        binding.viewModel = viewModel
    }

    override fun visitError() {
        swalError(this)
    }

    override fun visitMissingName() {
        snack(this, R.string.error_empty_name)
    }

    override fun visitInvalidEquipmentKind() {
        snack(this, R.string.error_invalid_equipment_kind)
    }

    override fun visitMissingEquipmentKind() {
        snack(this, R.string.error_missing_equipment_kind)
    }

    override fun visitMissingPrice() {
        snack(this, R.string.error_missing_price)
    }

    override fun visitInvalidPrice() {
        snack(this, R.string.error_invalid_price)
    }

    override fun visitNotPositivePrice() {
        snack(this, R.string.error_not_negative_price)
    }

    override fun visitSuccess() {
        finish()
    }
}