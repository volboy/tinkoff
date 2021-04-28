package com.volboy.course_project.presentation.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.volboy.course_project.App.Companion.streamsPresenter
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentStreamsBinding
import com.volboy.course_project.presentation.messages.MvpMessagesFragment
import com.volboy.course_project.presentation.mvp.presenter.MvpFragment
import com.volboy.course_project.recyclerview.CommonAdapter
import com.volboy.course_project.recyclerview.CommonDiffUtilCallback
import com.volboy.course_project.recyclerview.ViewTyped
import com.volboy.course_project.recyclerview.simple_items.EmptyView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MvpSubscribedFragment : StreamsView, MvpFragment<StreamsView, StreamsPresenter>(), UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentStreamsBinding
    private lateinit var rwStreams: RecyclerView
    private lateinit var adapter: CommonAdapter<ViewTyped>
    private lateinit var clickedStream: TitleUi
    private lateinit var searchText: Observable<String>
    private var compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStreamsBinding.inflate(inflater, container, false)
        val holderFactory = UiHolderFactory(this)
        adapter = CommonAdapter(holderFactory, CommonDiffUtilCallback(), null)
        rwStreams = binding.rwAllStreams
        rwStreams.adapter = adapter
        getPresenter().getStreams()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listStreams = adapter.items
        val searchEdit = requireActivity().findViewById<EditText>(R.id.searchEditText)
        searchEdit.addTextChangedListener { text ->
            searchText = Observable.create { emitter ->
                emitter.onNext(text.toString())
            }
            searchText
                .filter { inputText -> inputText.isNotEmpty() && inputText[0] != ' ' }
                .distinctUntilChanged()
                .debounce(TIME_SEARCH_DELAY, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())

            compositeDisposable.add(searchText.subscribe(
                { inputText ->
                    val filteredStreams = listStreams.filter { stream ->
                        val item = stream as TitleUi
                        item.title.contains(inputText, ignoreCase = true)
                    }
                    if (filteredStreams.isEmpty()) {
                        adapter.items = listOf(EmptyView)
                    } else {
                        adapter.items = filteredStreams
                    }
                },
                { error -> Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show() }
            ))
            if (text.isNullOrEmpty()) {
                adapter.items = listStreams
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override fun getPresenter(): StreamsPresenter = streamsPresenter

    override fun getMvpView(): StreamsView = this

    override fun showData(data: List<ViewTyped>) {
        binding.rwAllStreams.isVisible = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
        adapter.items = data
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
        const val TIME_SEARCH_DELAY = 1L
    }
}