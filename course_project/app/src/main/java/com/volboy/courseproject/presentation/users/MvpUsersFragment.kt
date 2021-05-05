package com.volboy.courseproject.presentation.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.FragmentPeopleBinding
import com.volboy.courseproject.presentation.details.MvpDetailsFragment
import com.volboy.courseproject.presentation.mvp.presenter.MvpFragment
import com.volboy.courseproject.presentation.streams.UiHolderFactory
import com.volboy.courseproject.recyclerview.CommonAdapter
import com.volboy.courseproject.recyclerview.CommonDiffUtilCallback
import com.volboy.courseproject.recyclerview.ViewTyped
import javax.inject.Inject

class MvpUsersFragment : UsersView, MvpFragment<UsersView, UsersPresenter>(), UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentPeopleBinding
    private lateinit var adapter: CommonAdapter<ViewTyped>

    @Inject
    lateinit var usersPresenter: UsersPresenter

    init {
        component.injectUsersPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPeopleBinding.inflate(inflater, container, false)
        val holderFactory = UiHolderFactory(this)
        adapter = CommonAdapter(holderFactory, CommonDiffUtilCallback(), null)
        binding.rwPeople.adapter = adapter
        getPresenter().getUsers()
        val searchEdit = requireActivity().findViewById<EditText>(R.id.searchEditText)
        getPresenter().setSearchObservable(searchEdit)
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
        val user = adapter.items[position] as PeopleUi
        detailsPeopleFragment.arguments = bundleOf(
            ARG_NAME to user.name,
            ARG_EMAIL to user.email,
            ARG_IMAGE to user.imageURL,
            ARG_USER_ID to user.uid.toInt()
        )
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.container, detailsPeopleFragment)
            .addToBackStack(FROM_USERS_TO_USERPROFILE)
            .commit()
    }

    override fun getCheckedSwitch(isChecked: Boolean, position: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        const val ARG_NAME = "name"
        const val ARG_EMAIL = "email"
        const val ARG_IMAGE = "image"
        const val ARG_USER_ID = "id"
        const val FROM_USERS_TO_USERPROFILE = "FromUsersFragmentToDetailFragment"
    }
}

