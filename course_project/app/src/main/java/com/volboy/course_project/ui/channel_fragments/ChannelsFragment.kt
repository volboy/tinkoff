package com.volboy.course_project.ui.channel_fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentChannelsBinding

class ChannelsFragment : Fragment() {
    private lateinit var binding: FragmentChannelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(activity, newText, Toast.LENGTH_SHORT).show()
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().actionBar?.setDisplayShowTitleEnabled(false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager
        val viewPagerAdapter = ChannelsFragmentAdapter(this)
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.tab_channels_1)
                1 -> tab.text = resources.getString(R.string.tab_channels_2)
            }
        }.attach()
    }
}