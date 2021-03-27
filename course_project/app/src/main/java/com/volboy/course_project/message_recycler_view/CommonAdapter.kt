package com.volboy.course_project.message_recycler_view

import android.util.Log
import androidx.recyclerview.widget.AsyncListDiffer

class CommonAdapter<T : ViewTyped>(holderFactory: HolderFactory) : BaseAdapter<T>(holderFactory) {

    private val differ = AsyncListDiffer(this, CommonDiffUtilCallback())

    override var items: List<T>
        get() = differ.currentList as List<T>
        set(newItems) {
            differ.submitList(newItems)
            Log.i("MyMes", newItems[0].uid)
        }
}