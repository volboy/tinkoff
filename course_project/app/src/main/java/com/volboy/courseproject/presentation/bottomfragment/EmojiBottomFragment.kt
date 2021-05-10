package com.volboy.courseproject.presentation.bottomfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.DialogBottomEmojiBinding
import com.volboy.courseproject.model.Emoji
import com.volboy.courseproject.recyclerview.ViewTyped

class EmojiBottomFragment : BottomSheetDialogFragment(), EmojiHolderFactory.BottomEmojiInterface, View.OnClickListener {
    private lateinit var binding: DialogBottomEmojiBinding
    private var emojiList = mutableListOf<Emoji>()
    private var prevTopic = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogBottomEmojiBinding.inflate(inflater, container, false)
        binding.dialogEdit.editMessage.setText(requireArguments().getString(ARG_MESSAGE))
        prevTopic = requireArguments().getString(ARG_TOPIC).toString()
        binding.dialogChange.editNewTopic.setText(prevTopic)
        val emojiName = resources.getStringArray(R.array.emojiName)
        val emojiCode = resources.getStringArray(R.array.emojiCode)
        for (i in 0 until emojiCode.size.coerceAtMost(emojiName.size)) {
            emojiList.add(Emoji(emojiName[i], emojiCode[i]))
        }
        val emojiViewTyped = mutableListOf<ViewTyped>()
        emojiList.forEach { emoji ->
            emojiViewTyped.add(
                EmojiUi(
                    String(Character.toChars(emoji.emojiCode.toInt(16)))
                )
            )
        }
        val holderFactory = EmojiHolderFactory(this)
        val emojiAdapter = EmojiAdapter<ViewTyped>(holderFactory)
        emojiAdapter.items = emojiViewTyped
        binding.recyclerEmoji.adapter = emojiAdapter
        binding.dialogNavigation.setOnNavigationItemSelectedListener {
            allHide()
            when (it.itemId) {
                R.id.emoji -> {
                    binding.recyclerEmoji.isVisible = true
                }
                R.id.delete -> {
                    binding.dialogDelete.root.isVisible = true
                }
                R.id.edit -> {
                    binding.dialogEdit.root.isVisible = true
                }
                R.id.change_topic -> {
                    binding.dialogChange.root.isVisible = true
                }
                R.id.copy -> {
                    setFragmentResult(ACTION_COPY, bundleOf())
                    dismiss()
                }
            }
            true
        }
        binding.dialogDelete.btnYes.setOnClickListener(this)
        binding.dialogDelete.btnCancel.setOnClickListener(this)
        binding.dialogEdit.btnYes.setOnClickListener(this)
        binding.dialogEdit.btnCancel.setOnClickListener(this)
        binding.dialogChange.btnYes.setOnClickListener(this)
        binding.dialogChange.btnCancel.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.dialogDelete.btnYes -> {
                setFragmentResult(ACTION_DELETE, bundleOf())
                dismiss()
            }
            binding.dialogDelete.btnCancel -> {
                dismiss()
            }
            binding.dialogEdit.btnYes -> {
                setFragmentResult(
                    ACTION_EDIT, bundleOf(
                        ARG_MESSAGE to binding.dialogEdit.editMessage.text.toString()
                    )
                )
                dismiss()
            }
            binding.dialogEdit.btnCancel -> {
                dismiss()
            }
            binding.dialogChange.btnYes -> {
                if (prevTopic != binding.dialogChange.editNewTopic.text.toString()) {
                    setFragmentResult(
                        ACTION_CHANGE, bundleOf(
                            ARG_NEW_TOPIC to binding.dialogChange.editNewTopic.text.toString()
                        )
                    )
                    dismiss()
                } else {
                    dismiss()
                }
            }
            binding.dialogChange.btnCancel -> {
                dismiss()
            }
        }
    }

    override fun getClickedView(view: View, position: Int) {
        val emojiCode = emojiList[position].emojiCode
        val emojiName = emojiList[position].emojiName
        setFragmentResult(ACTION_EMOJI, bundleOf(ARG_EMOJI to arrayListOf(emojiName, emojiCode)))
        dismiss()
    }

    private fun allHide() {
        binding.recyclerEmoji.isGone = true
        binding.dialogDelete.root.isGone = true
        binding.dialogEdit.root.isGone = true
        binding.dialogChange.root.isGone = true
    }

    companion object {
        const val ACTION_EMOJI = "action_emoji"
        const val ACTION_DELETE = "action_delete"
        const val ACTION_EDIT = "action_edit"
        const val ACTION_CHANGE = "action_change"
        const val ACTION_COPY = "action_copy"
        const val ARG_EMOJI = "emoji"
        const val ARG_MESSAGE = "message"
        const val ARG_TOPIC = "topic"
        const val ARG_NEW_TOPIC = "new_topic_for_message"
    }
}
