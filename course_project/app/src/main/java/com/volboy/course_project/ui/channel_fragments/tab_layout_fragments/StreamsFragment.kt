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
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentStreamsBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.model.Loader
import com.volboy.course_project.ui.channel_fragments.MessagesFragment
import io.reactivex.Observable


class StreamsFragment : Fragment(), UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentStreamsBinding
    private lateinit var loader: Loader
    private var listStreams = listOf<ViewTyped>()
    private lateinit var commonAdapter: CommonAdapter<ViewTyped>
    private lateinit var searchText: Observable<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStreamsBinding.inflate(inflater, container, false)
        loader = Loader()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val holderFactory = UiHolderFactory(this)
        commonAdapter = CommonAdapter(holderFactory)
        val streams = loader.getRemoteStreams()
        val disposableStreams = streams.subscribe(
            { result ->
                commonAdapter.items = result
                listStreams = result
            },
            { error ->
                Toast.makeText(context, "Ошибка ${error.message}", Toast.LENGTH_LONG).show()
                Log.d("ZULIP", error.message.toString())
            }
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
                            commonAdapter.items = listOf(
                                TitleUi(
                                    "Ничего не найдено", 0, false, null, 0,
                                    R.layout.item_collapse, ""
                                )
                            )
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
        val oldStreamsList: MutableList<ViewTyped> = commonAdapter.items.toMutableList()
        var topicsOfStream = mutableListOf<ViewTyped>()
        val clickedStream = oldStreamsList[position] as TitleUi
        clickedStream.isSelected = !clickedStream.isSelected
        when (viewType) {
            R.layout.item_collapse -> {
                if (clickedStream.isSelected) {
                    topicsOfStream.clear()
                    clickedStream.imageId = R.drawable.ic_arrow_up
                    val topic = loader.getTopicsOfStreams(clickedStream.uid.toInt())
                    val disposableStreams = topic.subscribe(
                        { result ->
                            topicsOfStream = result
                            clickedStream.count = topicsOfStream.size
                            oldStreamsList.removeAt(position)
                            oldStreamsList.add(position, clickedStream)
                            oldStreamsList.addAll(position + 1, topicsOfStream)
                            commonAdapter.items = oldStreamsList
                            commonAdapter.notifyItemChanged(position)
                        },
                        { error ->
                            Toast.makeText(context, "Ошибка ${error.message}", Toast.LENGTH_LONG).show()
                            Log.d("ZULIP", error.message.toString())
                        }
                    )
                } else {
                    clickedStream.imageId = R.drawable.ic_arrow_down
                    oldStreamsList.removeAt(position)
                    oldStreamsList.add(position, clickedStream)
                    for (i: Int in clickedStream.count downTo 1) {
                        oldStreamsList.removeAt(position + i)
                    }
                    commonAdapter.items = oldStreamsList
                    commonAdapter.notifyItemChanged(position)
                }
            }
            R.layout.item_expand -> {
                val messagesFragment = MessagesFragment()
                val arguments = Bundle()
                arguments.putString(ARG_TITLE, (commonAdapter.items[position] as TitleUi).title)
                messagesFragment.arguments = arguments
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.addToBackStack("FromSubscribedFragment")
                transaction.add(R.id.container, messagesFragment)
                transaction.commit()
            }
        }
    }

    companion object {
        const val ARG_TITLE = "title"
    }
}
