package com.volboy.course_project.presentation.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.volboy.course_project.App.Companion.usersPresenter
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentPeopleBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.CommonDiffUtilCallback
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.presentation.details.MvpDetailsFragment
import com.volboy.course_project.presentation.mvp.presenter.MvpFragment
import com.volboy.course_project.presentation.streams.UiHolderFactory

class MvpUsersFragment : UsersView, MvpFragment<UsersView, UsersPresenter>(), UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentPeopleBinding
    private lateinit var adapter: CommonAdapter<ViewTyped>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPeopleBinding.inflate(inflater, container, false)
        val holderFactory = UiHolderFactory(this)
        adapter = CommonAdapter(holderFactory, CommonDiffUtilCallback())
        binding.rwPeople.adapter = adapter
        val mActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        mActionBar?.show()
        getPresenter().getUsers()
        return binding.root
    }

    override fun getPresenter(): UsersPresenter = usersPresenter

    override fun getMvpView(): UsersView = this

    override fun showData(data: List<ViewTyped>) {
        binding.rwPeople.isVisible = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
        adapter.items = data
    }

    override fun showError(error: String?) {
        binding.rwPeople.isGone = true
        binding.fragmentError.root.isVisible = true
        binding.fragmentLoading.root.isGone = true
        binding.fragmentError.errorText.text = error
        binding.fragmentError.retryText.setOnClickListener { getPresenter().getUsers() }

    }

    override fun showLoading(msg: String) {
        binding.rwPeople.isGone = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isVisible = true
    }

    override fun getClickedView(view: View, position: Int, viewType: Int) {
        val detailsPeopleFragment = MvpDetailsFragment()
        val arguments = Bundle()
        val user = adapter.items[position] as PeopleUi
        arguments.putString(ARG_NAME, user.name)
        arguments.putString(ARG_EMAIL, user.email)
        arguments.putString(ARG_IMAGE, user.imageURL)
        arguments.putInt(ARG_USER_ID, user.uid.toInt())
        detailsPeopleFragment.arguments = arguments
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, detailsPeopleFragment)
        transaction.addToBackStack(FROM_USERS_TO_USERPROFILE)
        transaction.commit()
    }

    companion object {
        const val ARG_NAME = "name"
        const val ARG_EMAIL = "email"
        const val ARG_IMAGE = "image"
        const val ARG_USER_ID = "id"
        const val FROM_USERS_TO_USERPROFILE = "FromUsersFragmentToDetailFragment"
    }
}

