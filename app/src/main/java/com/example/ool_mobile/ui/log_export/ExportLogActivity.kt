package com.example.ool_mobile.ui.log_export

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.ool_mobile.R
import com.example.ool_mobile.databinding.ActivityExportLogBinding
import com.example.ool_mobile.service.Dependencies
import com.example.ool_mobile.ui.log_export.LogExportViewModel.Companion.create
import com.example.ool_mobile.ui.util.DisposedFromLifecycle
import com.example.ool_mobile.ui.util.SnackMessage.snack
import permissions.dispatcher.*

@RuntimePermissions
class ExportLogActivity : AppCompatActivity(), LogExportViewModel.Event.Visitor {

    private lateinit var viewModel: LogExportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
                this,
                create(Dependencies.from(this))
        ).get(LogExportViewModel::class.java)

        val binding: ActivityExportLogBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_export_log)
        binding.activity = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // PermissionDispatcher delegate
        onRequestPermissionsResult(requestCode, grantResults)
    }

    fun onExportClick() {
        exportWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun export() {
        viewModel.export()
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showRationale() {
        snack(this, R.string.message_write_permission_required)
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionDenied() {
        snack(this, R.string.message_write_permission_required)
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onNeverAskAgain() {
        snack(this, R.string.message_write_permission_required)
        finish()
    }

    override fun onStart() {
        super.onStart()
        viewModel.events
                .to(DisposedFromLifecycle.of(this))
                .subscribe { event: LogExportViewModel.Event -> event.accept(this) }
    }

    override fun visitError() {
        snack(this, R.string.error_operation_failed)
    }

    override fun visitSuccess() {
        finish()
    }
}