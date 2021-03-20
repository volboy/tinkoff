package com.volboy.course_project.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.volboy.course_project.databinding.FragmentChannelsBinding

class ChannelsFragment : Fragment() {
    private lateinit var binding: FragmentChannelsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }
}