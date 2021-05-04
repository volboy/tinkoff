package com.volboy.courseproject.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.FragmentProfileBinding
import com.volboy.courseproject.model.OwnUser
import com.volboy.courseproject.presentation.mvp.presenter.MvpFragment
import javax.inject.Inject

class MvpProfileFragment : ProfileView, MvpFragment<ProfileView, ProfilePresenter>() {
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var profilePresenter: ProfilePresenter

    init {
        component.injectProfilePresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        getPresenter().getOwnUser()
        return binding.root
    }

    override fun getPresenter(): ProfilePresenter = profilePresenter

    override fun getMvpView(): ProfileView = this

    override fun showData(user: OwnUser) {
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
        binding.profileName.isVisible = true
        binding.profileImage.isVisible = true
        binding.profileName.text = user.full_name
        Glide.with(requireContext())
            .load(user.avatar_url)
            .fitCenter()
            .error(R.drawable.ic_profile)
            .into(binding.profileImage)
    }

    override fun showLoading(msg: String) {
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isVisible = true
        binding.profileName.isGone = true
        binding.profileImage.isGone = true
    }

    override fun showError(error: String?) {
        binding.fragmentError.root.isVisible = true
        binding.fragmentLoading.root.isGone = true
        binding.profileName.isGone = true
        binding.profileImage.isGone = true
        binding.fragmentError.errorText.text = error
        binding.fragmentError.retryText.setOnClickListener { getPresenter().getOwnUser() }
    }
}