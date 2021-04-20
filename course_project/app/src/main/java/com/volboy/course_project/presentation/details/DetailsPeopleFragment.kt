package com.volboy.course_project.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentPeoplesDetailBinding
import com.volboy.course_project.presentation.users.MvpUsersFragment.Companion.ARG_IMAGE
import com.volboy.course_project.presentation.users.MvpUsersFragment.Companion.ARG_NAME

class DetailsPeopleFragment : Fragment() {
    private lateinit var binding: FragmentPeoplesDetailBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPeoplesDetailBinding.inflate(inflater, container, false)
        val mActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        mActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileName.text = requireArguments().getString(ARG_NAME)
        val imageURL = requireArguments().getString(ARG_IMAGE)
        Glide.with(requireContext())
            .load(imageURL)
            .fitCenter()
            .error(R.drawable.ic_profile)
            .into(binding.profileImage)
    }
}