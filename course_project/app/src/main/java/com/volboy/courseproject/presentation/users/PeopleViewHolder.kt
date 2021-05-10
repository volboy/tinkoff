package com.volboy.courseproject.presentation.users

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.ItemPeopleListBinding
import com.volboy.courseproject.presentation.details.MvpDetailsFragment
import com.volboy.courseproject.presentation.streams.UiHolderFactory
import com.volboy.courseproject.recyclerview.BaseViewHolder
import com.volboy.courseproject.recyclerview.ViewTyped

class PeopleUi(
    val name: String,
    val email: String?,
    val imageURL: String,
    var statusString: String,
    override val viewType: Int = R.layout.item_people_list,
    override val uid: String
) : ViewTyped

class PeopleViewHolder(val view: View, private val channelsInterface: UiHolderFactory.ChannelsInterface) :
    BaseViewHolder<PeopleUi>(view) {
    private val binding = ItemPeopleListBinding.bind(view)
    private val name: TextView = binding.name
    private val email: TextView = binding.email
    private val image: ImageView = binding.profileImage
    private val status: View = binding.userStatus

    override fun bind(item: PeopleUi) {
        status.isVisible = true
        view.setOnClickListener {
            channelsInterface.getClickedView(view, adapterPosition, item.viewType)
        }
        when (item.statusString) {
            MvpDetailsFragment.ACTIVE -> status.setBackgroundResource(R.drawable.user_status_on_drw)
            MvpDetailsFragment.IDLE -> status.setBackgroundResource(R.drawable.user_status_idle_drw)
            MvpDetailsFragment.OFFLINE -> status.setBackgroundResource(R.drawable.user_status_off_drw)
            else -> status.isGone = true
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