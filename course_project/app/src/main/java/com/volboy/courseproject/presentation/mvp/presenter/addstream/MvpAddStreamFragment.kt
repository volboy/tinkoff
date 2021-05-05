package com.volboy.courseproject.presentation.mvp.presenter.addstream

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.volboy.courseproject.App
import com.volboy.courseproject.databinding.FragmentAddStreamBinding
import com.volboy.courseproject.model.OwnUser
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
            addStreamPresenter.createNewStream(binding.editName.text.toString(), binding.editDescription.text.toString())
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun showData(user: OwnUser) {
        TODO("Not yet implemented")
    }

    override fun showLoading(msg: String) {
        TODO("Not yet implemented")
    }

    override fun showError(error: String?) {
        TODO("Not yet implemented")
    }

    override fun getPresenter(): AddStreamPresenter = addStreamPresenter

    override fun getMvpView(): AddStreamView = this
}