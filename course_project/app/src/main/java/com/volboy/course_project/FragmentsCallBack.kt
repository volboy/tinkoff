package com.volboy.course_project

import android.view.View
import com.volboy.course_project.message_recycler_view.ViewTyped

interface FragmentsCallBack {
    fun searchToolbar(list: List<ViewTyped>):List<ViewTyped>
}