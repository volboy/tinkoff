package com.volboy.course_project.presentation.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.volboy.course_project.App.Companion.streamsPresenter
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentStreamsBinding
import com.volboy.course_project.presentation.messages.MvpMessagesFragment
import com.volboy.course_project.presentation.mvp.presenter.MvpFragment
import com.volboy.course_project.recyclerview.CommonAdapter
import com.volboy.course_project.recyclerview.CommonDiffUtilCallback
import com.volboy.course_project.recyclerview.ViewTyped

class MvpSubscribedFragment : StreamsView, MvpFragment<StreamsView, StreamsPresenter>(), UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentStreamsBinding
    private lateinit var rwStreams: RecyclerView
    private lateinit var adapter: CommonAdapter<ViewTyped>
    private lateinit var clickedStream: TitleUi

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStreamsBinding.inflate(inflater, container, false)
        val holderFactory = UiHolderFactory(this)
        adapter = CommonAdapter(holderFactory, CommonDiffUtilCallback(), null)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        val searchEdit = requireActivity().findViewById<EditText>(R.id.searchEditText)
        rwStreams = binding.rwAllStreams
        rwStreams.adapter = adapter
        getPresenter().getStreams()
        getPresenter().setSearchObservable(searchEdit)
        return binding.root
    }

    override fun getPresenter(): StreamsPresenter = streamsPresenter

    override fun getMvpView(): StreamsView = this

    override fun showData(data: List<ViewTyped>) {
        binding.rwAllStreams.isVisible = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
        adapter.items = data
        adapter.notifyDataSetChanged()
    }

    override fun hideData(data: List<ViewTyped>) {
        adapter.items = data
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
                    getPresenter().getTopics(position)
                } else {
                    getPresenter().removeTopics(position)
                }
            }
            R.layout.item_expand -> {
                val messagesFragment = MvpMessagesFragment()
                val arguments = Bundle()
                arguments.putString(ARG_TOPIC, (adapter.items[position] as TitleUi).title)
                arguments.putString(ARG_LAST_MSG_ID_IN_TOPIC, (adapter.items[position] as TitleUi).uid)
                arguments.putString(ARG_STREAM, clickedStream.title)
                messagesFragment.arguments = arguments
                requireActivity().supportFragmentManager.beginTransaction()
                    .addToBackStack(FROM_TOPIC_TO_MESSAGE)
                    .add(R.id.container, messagesFragment)
                    .commit()
            }
        }
    }

    companion object {
        const val ARG_TOPIC = "topic"
        const val ARG_STREAM = "stream"
        const val ARG_LAST_MSG_ID_IN_TOPIC = "lastId"
        const val FROM_TOPIC_TO_MESSAGE = "FromSubscribedFragmentToMessageFragment"
    }
}