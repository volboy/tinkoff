package com.volboy.course_project.presentation.users

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.BaseViewHolder
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.presentation.streams.UiHolderFactory

class PeopleUi(
    val name: String,
    val email: String?,
    val imageURL: String,
    override val viewType: Int = R.layout.item_people_list,
    override val uid: String
) : ViewTyped

class PeopleViewHolder(val view: View, private val channelsInterface: UiHolderFactory.ChannelsInterface) :
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
        Glide.with(image.context)
            .load(item.imageURL)
            .fitCenter()
            .error(R.drawable.ic_profile)
            .into(image)
    }
}