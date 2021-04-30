package com.volboy.courseproject.recyclerview.simpleitems

import com.volboy.courseproject.R
import com.volboy.courseproject.recyclerview.ViewTyped

object ProgressItem : ViewTyped {
    override val uid: String = "PROGRESS_ITEM_ID"
    override val viewType: Int = R.layout.item_progress
}

object ErrorItem : ViewTyped {
    override val uid: String = "ERROR_ITEM_ID"
    override val viewType: Int = R.layout.item_error
}

object EmptyView : ViewTyped {
    override val uid: String = "EMPTY_ITEM_ID"
    override val viewType: Int = R.layout.empty_view
}