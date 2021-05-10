package com.volboy.courseproject.common

import android.content.Context
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

class ResourceProvider(val context: Context) {
    fun getString(@StringRes resId: Int): String = context.getString(resId)
    fun getColor(@ColorRes resId: Int): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getColor(resId)
    } else {
        context.resources.getColor(resId)

    }
}
