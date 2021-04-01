package com.volboy.course_project.ui.channel_fragments.tab_layout_fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.volboy.course_project.App
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentStreamsBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.model.ObservableStreams
import com.volboy.course_project.model.SendedMessage
import com.volboy.course_project.model.StreamJSON
import com.volboy.course_project.model.StreamsJSON
import internet.ZulipApi
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StreamsFragment : Fragment(), UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentStreamsBinding
    private val loaderStreams = ObservableStreams()
    private var listStreams = listOf<ViewTyped>()
    private lateinit var commonAdapter: CommonAdapter<ViewTyped>
    private lateinit var searchText: Observable<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getStreams()
        val holderFactory = UiHolderFactory(this)
        commonAdapter = CommonAdapter(holderFactory)
        val observableStreams = loaderStreams.getStreams().subscribe(
            { item ->
                listStreams = item
                commonAdapter.items = listStreams
            },
            { error -> Snackbar.make(binding.root, error.toString(), Snackbar.LENGTH_LONG).show() }
        )
        commonAdapter.items = listStreams
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

    private fun getStreams() {
        App.instance.zulipApi.getStreams().enqueue(object : Callback<StreamJSON> {
            override fun onResponse(call: Call<StreamJSON>, response: Response<StreamJSON>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, " $response.code()", Toast.LENGTH_LONG).show();
                    val streamsJsonList = mutableListOf<StreamJSON>()
                    response.body()?.let { streamsJsonList.add(it) }
                } else {
                    Toast.makeText(context, Toast.LENGTH_LONG, response.code()).show();
                }
            }

            override fun onFailure(call: Call<StreamJSON>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show();
            }
        });
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
}
