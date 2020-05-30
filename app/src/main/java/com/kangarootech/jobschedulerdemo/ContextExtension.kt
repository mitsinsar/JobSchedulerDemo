package com.kangarootech.jobschedulerdemo

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(@StringRes messageResId: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, getString(messageResId), length).show()
}
