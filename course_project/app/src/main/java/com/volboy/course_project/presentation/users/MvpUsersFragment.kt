package com.volboy.course_project.presentation.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.volboy.course_project.App.Companion.usersPresenter
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentPeopleBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.CommonDiffUtilCallback
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.presentation.mvp.presenter.MvpFragment
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.UiHolderFactory
import com.volboy.course_project.ui.people_fragments.DetailsPeopleFragment
import com.volboy.course_project.ui.people_fragments.PeopleUi

class MvpUsersFragment : UsersView, MvpFragment<UsersView, UsersPresenter>(), UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentPeopleBinding
    private lateinit var rwUsers: RecyclerView
    private lateinit var errorView: AppCompatImageView
    private lateinit var loading: CircularProgressIndicator
    private lateinit var adapter: CommonAdapter<ViewTyped>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPeopleBinding.inflate(inflater, container, false)
        val holderFactory = UiHolderFactory(this)
        adapter = CommonAdapter(holderFactory, CommonDiffUtilCallback())
        rwUsers = binding.rwPeople
        rwUsers.adapter = adapter
        errorView = binding.error
        loading = binding.loading
        getPresenter().getUsers()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun getPresenter(): UsersPresenter = usersPresenter

    override fun getMvpView(): UsersView = this

    override fun showData(data: List<ViewTyped>) {
        rwUsers.isVisible = true
        loading.isGone = true
        errorView.isGone = true
        adapter.items = data
    }

    override fun showError(error: String?) {
        rwUsers.isGone = true
        loading.isGone = true
        errorView.isVisible = true
    }

    override fun showLoading(msg: String) {
        rwUsers.isGone = true
        loading.isVisible = true
        errorView.isGone = true
    }

    override fun getClickedView(view: View, position: Int, viewType: Int) {
        val detailsPeopleFragment = DetailsPeopleFragment()
        val arguments = Bundle()
        val user = adapter.items[position] as PeopleUi
        arguments.putString(ARG_NAME, user.name)
        arguments.putString(ARG_EMAIL, user.email)
        arguments.putString(ARG_IMAGE, user.imageURL)
        detailsPeopleFragment.arguments = arguments
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.addToBackStack(FROM_USERS_TO_USERPROFILE)
        transaction.add(R.id.container, detailsPeopleFragment)
        transaction.commit()
    }

    companion object {
        const val ARG_NAME = "name"
        const val ARG_EMAIL = "email"
        const val ARG_IMAGE = "image"
        const val FROM_USERS_TO_USERPROFILE = "FromUsersFragmentToDetailFragment"
    }
}

