package com.volboy.courseproject.presentation.users

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.volboy.courseproject.R
import com.volboy.courseproject.presentation.streams.UiHolderFactory
import com.volboy.courseproject.recyclerview.BaseViewHolder
import com.volboy.courseproject.recyclerview.ViewTyped

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