package com.volboy.course_project.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentProfileBinding
import com.volboy.course_project.message_recycler_view.simple_items.ErrorItem
import com.volboy.course_project.model.Loader

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val loader = Loader()
        val ownUser = loader.getOwnUser()
        val disposableMessages = ownUser.subscribe(
            { result ->
                binding.profileName.text = result.full_name
                Glide.with(requireContext())
                    .load(result.avatar_url)
                    .fitCenter()
                    .error(R.drawable.ic_profile)
                    .into(binding.profileImage)
            },
            { error ->
                Toast.makeText(context, "Ошибка ${error.message}", Toast.LENGTH_LONG).show()
                Log.d("ZULIP", error.message.toString())
            }
        )
        val mActionBar = (requireActivity() as AppCompatActivity).supportActionBar;
        mActionBar?.hide()
        return binding.root
    }
}