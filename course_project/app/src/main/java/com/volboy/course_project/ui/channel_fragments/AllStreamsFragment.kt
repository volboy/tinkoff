package com.volboy.course_project.ui.channel_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.volboy.course_project.databinding.FragmentAllStreamsBinding
import com.volboy.course_project.databinding.FragmentSubscribedBinding

class AllStreamsFragment : Fragment() {
    private lateinit var binding: FragmentAllStreamsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAllStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }
}