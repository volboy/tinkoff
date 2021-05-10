package com.volboy.courseproject.presentation.addstream

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.volboy.courseproject.App
import com.volboy.courseproject.databinding.FragmentAddStreamBinding
import com.volboy.courseproject.presentation.mvp.presenter.MvpFragment
import javax.inject.Inject

class MvpAddStreamFragment : AddStreamView, MvpFragment<AddStreamView, AddStreamPresenter>() {
    lateinit var binding: FragmentAddStreamBinding

    @Inject
    lateinit var addStreamPresenter: AddStreamPresenter

    init {
        App.component.injectAddStreamPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddStreamBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        binding.createStream.setOnClickListener {
            addStreamPresenter.createNewStream(
                binding.editName.text.toString(),
                binding.editDescription.text.toString(),
                binding.inviteOnly.isChecked
            )
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun showData(title: String, msg: String?) {
        hideViews()
        binding.fragmentLoading.root.isGone = true
        binding.fragmentSuccess.root.isVisible = true
        binding.fragmentSuccess.successTitle.text = title
        binding.fragmentSuccess.successText.text = msg
        binding.fragmentSuccess.okText.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun showLoading() {
        hideViews()
        binding.fragmentSuccess.root.isGone = true
        binding.fragmentLoading.root.isVisible = true
    }

    private fun hideViews() {
        binding.editDescription.isGone = true
        binding.txtDescription.isGone = true
        binding.inviteOnly.isGone = true
        binding.createStream.isGone = true
        binding.txtLock.isGone = true
        binding.txtName.isGone = true
        binding.editName.isGone = true
    }

    override fun getPresenter(): AddStreamPresenter = addStreamPresenter

    override fun getMvpView(): AddStreamView = this
}