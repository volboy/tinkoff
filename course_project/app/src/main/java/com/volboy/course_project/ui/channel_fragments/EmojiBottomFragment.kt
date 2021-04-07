package com.volboy.course_project.ui.channel_fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.volboy.course_project.R
import com.volboy.course_project.databinding.DialogBottomEmojiBinding
import com.volboy.course_project.emoji_recycler_view.EmojiAdapter
import com.volboy.course_project.emoji_recycler_view.EmojiUi
import com.volboy.course_project.emoji_recycler_view.EmojiHolderFactory
import com.volboy.course_project.message_recycler_view.*

class EmojiBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: DialogBottomEmojiBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogBottomEmojiBinding.inflate(inflater, container, false)
        val clickListener: (View) -> Unit = { view ->
            val emojiText = view.findViewById<TextView>(R.id.emoji)
            val emojiCode = emojiText.text.toString()
            setFragmentResult(ARG_BOTTOM_FRAGMENT, bundleOf(ARG_EMOJI_CODE to emojiCode))
            dismiss()
            true
        }
        val emojiCode = listOf<Int>(
            0x1F60E,
            0x1F60C,
            0x1F60C,
            0x1F614,
            0x1F60B,
            0x1F61B,
            0x1F60D,
            0x1F601,
            0x1F44D,
            0x1F618,
            0x1F383,
            0x1F60A,
            0x1F61A,
            0x1F643,
            0x263A,
            0x1F605,
            0x1F602,
            0x1F607,
            0x1F609,
            0x1F60C,
            0x1F617,
            0x1F61C,
            0x1F60E,
            0x1F60F,
            0x1F61C,
            0x1F617,
            0x1F617,
            0x1F602,
            0x1F60E,
            0x1F60C,
            0x1F60C,
            0x1F614,
            0x1F60B,
            0x1F61B,
            0x1F60D,
            0x1F601,
            0x1F44D,
            0x1F618,
            0x1F383,
            0x1F60A,
            0x1F61A,
            0x1F643,
            0x263A,
            0x1F605,
            0x1F602,
            0x1F607,
            0x1F609,
            0x1F60C,
            0x1F617,
            0x1F61C,
            0x1F60E,
            0x1F60F,
            0x1F61C,
            0x1F617,
            0x1F617,
            0x1F602
        )

        var emojiViewTyped = mutableListOf<ViewTyped>()
        emojiCode.forEach { code ->
            emojiViewTyped.add(EmojiUi(String(Character.toChars(code))))
        }
        val holderFactory = EmojiHolderFactory(clickListener)
        val emojiAdapter = EmojiAdapter<ViewTyped>(holderFactory)
        emojiAdapter.items = emojiViewTyped
        binding.recyclerEmoji.adapter = emojiAdapter
        return binding.root
    }

    companion object {
        const val ARG_BOTTOM_FRAGMENT = "BottomSheetDialogFragment"
        const val ARG_EMOJI_CODE = "emoji_code"
    }
}