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
import com.google.android.material.snackbar.Snackbar
import com.volboy.course_project.App
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentStreamsBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.message_recycler_view.simple_items.ErrorItem
import com.volboy.course_project.message_recycler_view.simple_items.ProgressItem
import com.volboy.course_project.model.Loader
import com.volboy.course_project.ui.channel_fragments.MessagesFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class StreamsFragment : Fragment(), UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentStreamsBinding
    private lateinit var loader: Loader
    private var listStreams = listOf<ViewTyped>()
    private lateinit var commonAdapter: CommonAdapter<ViewTyped>
    private lateinit var searchText: Observable<String>
    private var streamName = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStreamsBinding.inflate(inflater, container, false)
        loader = Loader()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val holderFactory = UiHolderFactory(this)
        commonAdapter = CommonAdapter(holderFactory)
        binding.rwAllStreams.adapter = commonAdapter
        val appDatabase = App.appDatabase
        val streamsDao = appDatabase.streamsDao()
        val disposable = streamsDao.getAllStreams()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { streams -> loader.viewTypedStreams(streams) }
            .subscribe(
                { viewTypedStreams ->
                    showSnackbar(resources.getString(R.string.msg_database_ok) + " , размер БД " + viewTypedStreams?.size?.toString())
                    if (viewTypedStreams.size == 0) {
                        commonAdapter.items = listOf(ProgressItem)
                    } else {
                        commonAdapter.items = viewTypedStreams
                    }
                },
                { error -> showSnackbar(resources.getString(R.string.msg_database_error) + error.message) },
                {
                    showSnackbar(resources.getString(R.string.msg_database_empty))
                    commonAdapter.items = listOf(ProgressItem)
                })

        val streams = loader.getRemoteStreams()
        val disposableStreams = streams.subscribe(
            { result ->
                commonAdapter.items = result
                listStreams = result
                showSnackbar(resources.getString(R.string.msg_network_ok))
            },
            { error ->
                commonAdapter.items = listOf(ErrorItem)
                showSnackbar(resources.getString(R.string.msg_network_error) + error.message)
            }
        )
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
                streamName = clickedStream.title
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
                arguments.putString(SubscribedFragment.ARG_TOPIC, (commonAdapter.items[position] as TitleUi).title)
                arguments.putString(SubscribedFragment.ARG_STREAM, streamName)
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

    private fun showSnackbar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
        Log.i(resources.getString(R.string.log_string), msg)
    }
}