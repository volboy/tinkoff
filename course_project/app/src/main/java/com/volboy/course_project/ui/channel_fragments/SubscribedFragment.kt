package com.volboy.course_project.ui.channel_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.volboy.course_project.databinding.FragmentSubscribedBinding

class SubscribedFragment : Fragment() {
    private lateinit var binding: FragmentSubscribedBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSubscribedBinding.inflate(inflater, container, false)
        return binding.root
    }
}