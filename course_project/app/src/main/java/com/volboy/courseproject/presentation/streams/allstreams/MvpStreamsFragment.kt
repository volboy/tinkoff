package com.volboy.courseproject.presentation.streams.allstreams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.FragmentStreamsBinding
import com.volboy.courseproject.presentation.addstream.MvpAddStreamFragment
import com.volboy.courseproject.presentation.bottominfo.BottomInfoFragment
import com.volboy.courseproject.presentation.messages.MvpMessagesFragment
import com.volboy.courseproject.presentation.mvp.presenter.MvpFragment
import com.volboy.courseproject.presentation.streams.UiHolderFactory
import com.volboy.courseproject.presentation.streams.mystreams.TitleUi
import com.volboy.courseproject.recyclerview.CommonAdapter
import com.volboy.courseproject.recyclerview.CommonDiffUtilCallback
import com.volboy.courseproject.recyclerview.ViewTyped
import javax.inject.Inject

class MvpStreamsFragment : AllStreamsView, MvpFragment<AllStreamsView, AllStreamsPresenter>(),
    UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentStreamsBinding
    private lateinit var adapter: CommonAdapter<ViewTyped>
    private lateinit var clickedStream: TitleUi

    @Inject
    lateinit var allStreamsPresenter: AllStreamsPresenter

    init {
        component.injectStreamsPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStreamsBinding.inflate(inflater, container, false)
        val holderFactory = UiHolderFactory(this)
        adapter = CommonAdapter(holderFactory, CommonDiffUtilCallback(), null)
        val searchEdit = requireActivity().findViewById<EditText>(R.id.searchEditText)
        binding.rwAllStreams.adapter = adapter
        binding.createStream.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.container, MvpAddStreamFragment())
                .addToBackStack("MvpUsersFragment.FROM_USERS_TO_USERPROFILE")
                .commit()
        }
        getPresenter().getStreams()
        getPresenter().setSearchObservable(searchEdit)
        return binding.root
    }

    override fun getPresenter(): AllStreamsPresenter = allStreamsPresenter

    override fun getMvpView(): AllStreamsView = this

    override fun showData(data: List<ViewTyped>) {
        binding.rwAllStreams.isVisible = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
        adapter.items = data
    }

    override fun updateData(data: List<ViewTyped>, position: Int) {
        binding.rwAllStreams.isVisible = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
        adapter.items = data
        adapter.notifyItemInserted(position)
    }

    override fun hideData(data: List<ViewTyped>) {
        adapter.items = data
    }

    override fun showMessage(title: String, msg: String) {
        val bottomInfoFragment = BottomInfoFragment()
        bottomInfoFragment.arguments = bundleOf(
            BottomInfoFragment.ARG_INFO_TITLE to title,
            BottomInfoFragment.ARG_INFO_TEXT to msg
        )
        bottomInfoFragment.show(parentFragmentManager, bottomInfoFragment.tag)
    }

    override fun showError(error: String?) {
        binding.rwAllStreams.isGone = true
        binding.fragmentError.root.isVisible = true
        binding.fragmentLoading.root.isGone = true
        binding.fragmentError.errorText.text = error
        binding.fragmentError.retryText.setOnClickListener { getPresenter().getStreams() }
    }

    override fun showLoading(msg: String) {
        binding.rwAllStreams.isGone = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isVisible = true
    }

    override fun getClickedView(view: View, position: Int, viewType: Int) {
        when (viewType) {
            R.layout.item_collapse -> {
                clickedStream = adapter.items[position] as TitleUi
                clickedStream.isSelected = !clickedStream.isSelected
                if (clickedStream.isSelected) {

                } else {

                }
            }
            R.layout.item_expand -> {
                val messagesFragment = MvpMessagesFragment()
                messagesFragment.arguments = bundleOf(
                    MvpMessagesFragment.ARG_TOPIC to (adapter.items[position] as TitleUi).title,
                    MvpMessagesFragment.ARG_LAST_MSG_ID_IN_TOPIC to (adapter.items[position] as TitleUi).uid,
                    MvpMessagesFragment.ARG_STREAM to clickedStream.title
                )
                requireActivity().supportFragmentManager.beginTransaction()
                    .addToBackStack(MvpMessagesFragment.FROM_TOPIC_TO_MESSAGE)
                    .add(R.id.container, messagesFragment)
                    .commit()
            }
        }
    }

    override fun getClickedSwitch(view: SwitchCompat, streamName: String) {
        if (view.isChecked) {
            allStreamsPresenter.subscribeToStream(streamName)
        } else {
            allStreamsPresenter.unSubscribeToStream(streamName)
        }
    }
}