package com.volboy.course_project.ui.channel_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.volboy.course_project.databinding.FragmentChannelsBinding

private lateinit var pageViewModel: PageViewModel

class ChannelsFragment : Fragment() {
    private lateinit var binding: FragmentChannelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChannelsBinding.inflate(inflater, container, false)
        pageViewModel.text.observe(this, Observer<String> {
            var i=it.toInt()
            if (i == 1) {
                //как то передать адрес раздела LATEST
            } else if (i == 2) {
                //как то передать адрес раздела HOT
            } else {
                //как то передать адрес раздела TOP
            }
        })
        return binding.root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): ChannelsFragment {
            return ChannelsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}