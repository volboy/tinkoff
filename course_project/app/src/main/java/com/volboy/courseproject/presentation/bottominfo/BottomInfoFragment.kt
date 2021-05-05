package com.volboy.courseproject.presentation.bottominfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.volboy.courseproject.databinding.DialogBottomInfoBinding

class BottomInfoFragment : BottomSheetDialogFragment() {
    private lateinit var binding: DialogBottomInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogBottomInfoBinding.inflate(inflater, container, false)
        binding.title.text = requireArguments().getString(ARG_INFO_TITLE)
        binding.message.text = requireArguments().getString(ARG_INFO_TEXT)
        return binding.root
    }

    companion object {
        const val ARG_INFO_TITLE = "BottomInfoFragmentTitle"
        const val ARG_INFO_TEXT = "BottomInfoFragmentText"
    }
}
