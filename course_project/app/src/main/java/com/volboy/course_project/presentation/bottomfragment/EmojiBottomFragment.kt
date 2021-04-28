package com.volboy.course_project.presentation.bottomfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.volboy.course_project.R
import com.volboy.course_project.databinding.DialogBottomEmojiBinding
import com.volboy.course_project.model.Emoji
import com.volboy.course_project.recyclerview.ViewTyped

class EmojiBottomFragment : BottomSheetDialogFragment(), EmojiHolderFactory.BottomEmojiInterface {
    private lateinit var binding: DialogBottomEmojiBinding
    private var emojiList = mutableListOf<Emoji>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogBottomEmojiBinding.inflate(inflater, container, false)
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
        return binding.root
    }

    override fun getClickedView(view: View, position: Int) {
        val emojiCode = emojiList[position].emojiCode
        val emojiName = emojiList[position].emojiName
        setFragmentResult(ARG_BOTTOM_FRAGMENT, bundleOf(ARG_EMOJI to arrayListOf(emojiName, emojiCode)))
        dismiss()
    }

    companion object {
        const val ARG_BOTTOM_FRAGMENT = "BottomSheetDialogFragment"
        const val ARG_EMOJI = "emoji"
    }
}
