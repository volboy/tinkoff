package com.volboy.course_project.ui.people_fragments

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.BaseViewHolder
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.uiHolderFactory

class PeopleUi(
    val name: String,
    val email: String?,
    val imageId: Int?,
    override val viewType: Int = R.layout.people_list_item,
    override val uid: String = ""
) : ViewTyped

class PeopleViewHolder(val view: View, private val channelsInterface: uiHolderFactory.ChannelsInterface) :
    BaseViewHolder<PeopleUi>(view) {
    private val name: TextView = view.findViewById(R.id.name)
    private val email: TextView = view.findViewById(R.id.email)
    private val image: ImageView = view.findViewById(R.id.profileImage)

    override fun bind(item: PeopleUi) {
        view.setOnClickListener {
            channelsInterface.getClickedView(view, adapterPosition, item.viewType)
        }
        name.text = item.name
        email.text = item.email.toString()
        item.imageId?.let {
            image.setImageResource(it)
        }
    }
}