package com.volboy.courseproject.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.volboy.courseproject.R
import com.volboy.courseproject.recyclerview.simpleitems.EmptyView
import com.volboy.courseproject.recyclerview.simpleitems.ErrorItem
import com.volboy.courseproject.recyclerview.simpleitems.ProgressItem

abstract class HolderFactory : (ViewGroup, Int) -> BaseViewHolder<ViewTyped> {

    abstract fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>?

    final override fun invoke(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<ViewTyped> {
        val view: View = viewGroup.inflate(viewType)
        return when (viewType) {
            R.layout.item_progress -> BaseViewHolder<ProgressItem>(view)
            R.layout.item_error -> BaseViewHolder<ErrorItem>(view)
            R.layout.empty_view -> BaseViewHolder<EmptyView>(view)
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