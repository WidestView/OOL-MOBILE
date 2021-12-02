@file:JvmName("SnackMessage")

package com.example.ool_mobile.ui.util

import android.app.Activity
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.ool_mobile.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

@JvmOverloads
fun snack(
        fragment: Fragment,
        @StringRes message: Int,
        length: Int = Snackbar.LENGTH_LONG) {
    snack(fragment.requireView(), message, length)
}

@JvmOverloads
fun snack(
        activity: Activity,
        @StringRes message: Int,
        length: Int = Snackbar.LENGTH_LONG) {
    snack(activity.findViewById<View>(R.id.content), message, length)
}

private fun snack(
        view: View,
        @StringRes message: Int,
        @BaseTransientBottomBar.Duration length: Int) {
    Snackbar.make(view, message, length).show()
}
