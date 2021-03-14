package com.volboy.hw_2.message_recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.volboy.hw_2.R
import com.volboy.hw_2.message_recycler_view.simple_items.ErrorItem
import com.volboy.hw_2.message_recycler_view.simple_items.ProgressItem

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