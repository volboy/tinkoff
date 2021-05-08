package com.volboy.courseproject.recyclerview

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

class CommonAdapter<T : ViewTyped>(
    holderFactory: HolderFactory,
    diffItemCallback: DiffUtil.ItemCallback<T>,
    private val paginationAdapterHelper: PaginationAdapterHelper?
) : BaseAdapter<T>(holderFactory) {

    private val differ = AsyncListDiffer(this, diffItemCallback)
    override var items: List<T>
        get() = differ.currentList
        set(newItems) = differ.submitList(newItems)

    override fun onBindViewHolder(holder: BaseViewHolder<ViewTyped>, position: Int) {
        super.onBindViewHolder(holder, position)

        paginationAdapterHelper?.onBind(position, items.size)

    }
}