package com.volboy.course_project.ui.channel_fragments.tab_layout_fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.volboy.course_project.App
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentSubscribedBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.model.ObservableStreams
import com.volboy.course_project.model.SendedMessage
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscribedFragment : Fragment(), UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentSubscribedBinding
    private lateinit var loaderStreams: ObservableStreams
    private var listStreams = listOf<ViewTyped>()
    private lateinit var commonAdapter: CommonAdapter<ViewTyped>
    private lateinit var searchText: Observable<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSubscribedBinding.inflate(inflater, container, false)
        loaderStreams = ObservableStreams(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val holderFactory = UiHolderFactory(this)
        commonAdapter = CommonAdapter(holderFactory)
        val streams = loaderStreams.getRemoteStreams()
        val disposableStreams = streams.subscribe(
            { result -> commonAdapter.items = result },
            { error -> Toast.makeText(context, "Ошибка ${error.message}", Toast.LENGTH_LONG).show(); Log.d("ZULIP", error.message.toString()) }
        )
        binding.rwAllStreams.adapter = commonAdapter
        val mActionBar = (requireActivity() as AppCompatActivity).supportActionBar;
        mActionBar?.show()
        val searchEdit = requireActivity().findViewById<EditText>(R.id.searchEditText)
        searchEdit.addTextChangedListener { text ->
            searchText = Observable.create { emitter ->
                emitter.onNext(text.toString())
            }
            searchText
                .filter { inputText -> inputText.isNotEmpty() }
                .filter { inputText -> inputText[0] != ' ' }
                .distinctUntilChanged()
                .subscribe(
                    { inputText ->
                        val filteredStreams = listStreams.filter { stream ->
                            val item = stream as TitleUi
                            item.title.contains(inputText)
                        }
                        if (filteredStreams.isEmpty()) {
                            commonAdapter.items = listOf(TitleUi("Ничего не найдено", null, null, 0, R.layout.item_collapse, ""))
                        } else {
                            commonAdapter.items = filteredStreams
                        }
                    },
                    { error -> Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show() }
                )
            if (text.isNullOrEmpty()) {
                commonAdapter.items = listStreams
            }
        }
    }

    override fun getClickedView(view: View, position: Int, viewType: Int) {
        sendMessage()
        val items: MutableList<ViewTyped> = commonAdapter.items.toMutableList()
        val item = items[position] as TitleUi
        val topics = item.topics
        view.isSelected = !view.isSelected
        when (viewType) {
            R.layout.item_collapse -> {
                if (view.isSelected) {
                    item.imageId = R.drawable.ic_arrow_up
                    item.uid = "UP"
                    topics?.forEach { topic ->
                        items.add(position + 1, TitleUi(topic.first, topic.second.toString() + " mes", null, 0, R.layout.item_expand, topic.first))
                    }
                } else {
                    item.imageId = R.drawable.ic_arrow_down
                    item.uid = "DOWN"
                    var topicSize = item.topics?.size
                    topics?.forEach { _ ->
                        topicSize = topicSize?.minus(1)
                        items.removeAt(position + topicSize!! + 1)
                    }
                }
                commonAdapter.items = items
                commonAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun sendMessage() {
        App.instance.zulipApi.sendMessage("stream", "general", "Hello from Volgograd)", "test_topic").enqueue(object : Callback<SendedMessage> {
            override fun onResponse(call: Call<SendedMessage>, response: Response<SendedMessage>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, " $response.code()", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, " $response.code()", Toast.LENGTH_LONG).show();
                }
            }

            override fun onFailure(call: Call<SendedMessage>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show();
            }
        });
    }

    companion object {
        const val ARG_TITLE = "title"
    }
}
