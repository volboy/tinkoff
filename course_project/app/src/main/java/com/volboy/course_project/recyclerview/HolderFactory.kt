package com.volboy.course_project.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.volboy.course_project.R
import com.volboy.course_project.recyclerview.simple_items.ErrorItem
import com.volboy.course_project.recyclerview.simple_items.ProgressItem

abstract class HolderFactory : (ViewGroup, Int) -> BaseViewHolder<ViewTyped> {

    abstract fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>?

    final override fun invoke(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<ViewTyped> {
        val view: View = viewGroup.inflate(viewType)
        return when (viewType) {
            R.layout.item_progress -> BaseViewHolder<ProgressItem>(view)
            R.layout.item_error -> BaseViewHolder<ErrorItem>(view)
            else -> checkNotNull(createViewHolder(view, viewType)) {
                "unknown viewType" + viewGroup.resources.getResourceName(viewType)
            }
        } as BaseViewHolder<ViewTyped>
    }
}

fun <T : View> View.inflate(
    @LayoutRes
    layout: Int,
    root: ViewGroup? = this as? ViewGroup,
    attachToRoot: Boolean = false
): T {
    return LayoutInflater.from(context).inflate(layout, root, attachToRoot) as T
}