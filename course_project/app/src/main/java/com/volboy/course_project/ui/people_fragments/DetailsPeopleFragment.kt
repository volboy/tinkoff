package com.volboy.course_project.ui.people_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.volboy.course_project.databinding.FragmentPeoplesDetailBinding
import com.volboy.course_project.ui.people_fragments.PeopleFragment.Companion.ARG_IMAGE
import com.volboy.course_project.ui.people_fragments.PeopleFragment.Companion.ARG_NAME

class DetailsPeopleFragment : Fragment() {
    private lateinit var binding: FragmentPeoplesDetailBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPeoplesDetailBinding.inflate(inflater, container, false)
        binding.profileName.text=requireArguments().getString(ARG_NAME)
        binding.profileImage.setImageResource(requireArguments().getInt(ARG_IMAGE))
        return binding.root
    }

}