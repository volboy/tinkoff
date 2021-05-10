package com.volboy.courseproject.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.R
import com.volboy.courseproject.common.ResourceProvider
import com.volboy.courseproject.databinding.FragmentPeoplesDetailBinding
import com.volboy.courseproject.presentation.mvp.presenter.MvpFragment
import com.volboy.courseproject.presentation.users.MvpUsersFragment
import javax.inject.Inject

class MvpDetailsFragment : DetailsView, MvpFragment<DetailsView, DetailsPresenter>() {
    private lateinit var binding: FragmentPeoplesDetailBinding
    private var userId = 0

    @Inject
    lateinit var detailsPresenter: DetailsPresenter

    @Inject
    lateinit var res: ResourceProvider

    init {
        component.injectDetailPresenter(this)
        component.injectResourceProvider(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPeoplesDetailBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        userId = requireArguments().getInt(MvpUsersFragment.ARG_USER_ID)
        getPresenter().getUserStatus(userId)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun getPresenter(): DetailsPresenter = detailsPresenter

    override fun getMvpView(): DetailsView = this

    override fun showStatus(status: String) {
        when (status) {
            ACTIVE -> binding.profileStatus.setTextColor(res.getColor(R.color.profile_status_color_green))
            IDLE -> binding.profileStatus.setTextColor(res.getColor(R.color.profile_status_color_orange))
            OFFLINE -> binding.profileStatus.setTextColor(res.getColor(R.color.profile_status_color_red))
        }
        binding.profileStatus.text = status
        binding.profileName.text = requireArguments().getString(MvpUsersFragment.ARG_NAME)
        val imageURL = requireArguments().getString(MvpUsersFragment.ARG_IMAGE)
        Glide.with(requireContext())
            .load(imageURL)
            .fitCenter()
            .error(R.drawable.ic_profile)
            .into(binding.profileImage)
        binding.profileName.isVisible = true
        binding.profileImage.isVisible = true
        binding.profileStatus.isVisible = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
    }

    override fun showLoading(msg: String) {
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isVisible = true
        binding.profileName.isGone = true
        binding.profileImage.isGone = true
        binding.profileStatus.isGone = true
    }

    override fun showError(error: String?) {
        binding.fragmentError.root.isVisible = true
        binding.fragmentLoading.root.isGone = true
        binding.profileName.isGone = true
        binding.profileImage.isGone = true
        binding.profileStatus.isGone = true
        binding.fragmentError.errorText.text = error
        binding.fragmentError.retryText.setOnClickListener { getPresenter().getUserStatus(userId) }
    }


    companion object {
        var ACTIVE = "active"
        var IDLE = "idle"
        var OFFLINE = "offline"
    }

}
