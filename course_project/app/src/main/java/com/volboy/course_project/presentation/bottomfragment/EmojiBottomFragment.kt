package com.volboy.course_project.presentation.bottomfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.volboy.course_project.databinding.DialogBottomEmojiBinding
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.model.Emoji

class EmojiBottomFragment : BottomSheetDialogFragment(), EmojiHolderFactory.BottomEmojiInterface {
    private lateinit var binding: DialogBottomEmojiBinding
    private lateinit var emojiCodes: List<Emoji>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogBottomEmojiBinding.inflate(inflater, container, false)
        emojiCodes = listOf(
            Emoji("grinning", 0x1f600),
            Emoji("smiley", 0x1f603),
            Emoji("big_smile", 0x1f604),
            Emoji("grinning_face_with_smiling_eyes", 0x1f601),
            Emoji("laughing", 0x1f606),
            Emoji("sweat_smile", 0x1f605),
            Emoji("rolling_on_the_floor_laughing", 0x1f923),
            Emoji("joy", 0x1f602),
            Emoji("smile", 0x1f642),
            Emoji("upside_down", 0x1f643),
            Emoji("wink", 0x1f609),
            Emoji("blush", 0x1f60a),
            Emoji("innocent", 0x1f607),
            Emoji("heart_eyes", 0x1f60d),
            Emoji("heart_kiss", 0x1f618),
            Emoji("kiss", 0x1f617),
            Emoji("smiling_face", 0x263a),
            Emoji("kiss_with_blush", 0x1f61a),
            Emoji("kiss_smiling_eyes", 0x1f619),
            Emoji("yum", 0x1f60b),
            Emoji("stuck_out_tongue", 0x1f61b),
            Emoji("stuck_out_tongue_wink", 0x1f61c),
            Emoji("stuck_out_tongue_closed_eyes", 0x1f61d),
            Emoji("money_face", 0x1f911),
            Emoji("hug", 0x1f917),
            Emoji("thinking", 0x1f914),
            Emoji("silence", 0x1f910),
            Emoji("neutral", 0x1f610),
            Emoji("expressionless", 0x1f611),
            Emoji("speechless", 0x1f636),
            Emoji("smirk", 0x1f60f),
            Emoji("unamused", 0x1f612),
            Emoji("rolling_eyes", 0x1f644),
            Emoji("grimacing", 0x1f62c),
            Emoji("lying", 0x1f925),
            Emoji("relieved", 0x1f60c),
            Emoji("pensive", 0x1f614),
            Emoji("sleepy", 0x1f62a),
            Emoji("drooling", 0x1f924),
            Emoji("sleeping", 0x1f634),
            Emoji("cant_talk", 0x1f637),
            Emoji("sick", 0x1f912),
            Emoji("hurt", 0x1f915),
            Emoji("nauseated", 0x1f922),
            Emoji("sneezing", 0x1f927),
            Emoji("dizzy", 0x1f635),
            Emoji("cowboy", 0x1f920),
            Emoji("sunglasses", 0x1f60e),
            Emoji("nerd", 0x1f913),
            Emoji("oh_no", 0x1f615),
            Emoji("worried", 0x1f61f),
            Emoji("frown", 0x1f641),
            Emoji("sad", 0x2639),
            Emoji("open_mouth", 0x1f62e),
            Emoji("hushed", 0x1f62f),
            Emoji("astonished", 0x1f632),
            Emoji("flushed", 0x1f633),
            Emoji("frowning", 0x1f626),
            Emoji("anguished", 0x1f627),
            Emoji("fear", 0x1f628),
            Emoji("cold_sweat", 0x1f630),
            Emoji("exhausted", 0x1f625),
            Emoji("cry", 0x1f622),
            Emoji("sob", 0x1f62d),
            Emoji("scream", 0x1f631),
            Emoji("confounded", 0x1f616),
            Emoji("persevere", 0x1f623),
            Emoji("disappointed", 0x1f61e),
            Emoji("sweat", 0x1f613),
            Emoji("weary", 0x1f629),
            Emoji("anguish", 0x1f62b),
            Emoji("triumph", 0x1f624),
            Emoji("rage", 0x1f621),
            Emoji("angry", 0x1f620),
            Emoji("smiling_devil", 0x1f608),
            Emoji("devil", 0x1f47f),
            Emoji("skull", 0x1f480),
            Emoji("skull_and_crossbones", 0x2620),
            Emoji("poop", 0x1f4a9),
            Emoji("clown", 0x1f921),
            Emoji("ogre", 0x1f479),
            Emoji("goblin", 0x1f47a),
            Emoji("ghost", 0x1f47b),
            Emoji("alien", 0x1f47d),
            Emoji("space_invader", 0x1f47e),
            Emoji("robot", 0x1f916),
            Emoji("smiley_cat", 0x1f63a),
            Emoji("smile_cat", 0x1f638),
            Emoji("joy_cat", 0x1f639),
            Emoji("heart_eyes_cat", 0x1f63b),
            Emoji("smirk_cat", 0x1f63c),
            Emoji("kissing_cat", 0x1f63d),
            Emoji("scream_cat", 0x1f640),
            Emoji("crying_cat", 0x1f63f),
            Emoji("angry_cat", 0x1f63e),
            Emoji("see_no_evil", 0x1f648),
            Emoji("hear_no_evil", 0x1f649),
            Emoji("speak_no_evil", 0x1f64a),
            Emoji("lipstick_kiss", 0x1f48b),
            Emoji("love_letter", 0x1f48c),
            Emoji("cupid", 0x1f498),
            Emoji("gift_heart", 0x1f49d),
            Emoji("sparkling_heart", 0x1f496),
            Emoji("heart_pulse", 0x1f497),
            Emoji("heartbeat", 0x1f493),
            Emoji("revolving_hearts", 0x1f49e),
            Emoji("two_hearts", 0x1f495),
            Emoji("heart_box", 0x1f49f),
            Emoji("heart_exclamation", 0x2763),
            Emoji("broken_heart", 0x1f494),
            Emoji("heart", 0x2764),
            Emoji("yellow_heart", 0x1f49b),
            Emoji("green_heart", 0x1f49a),
            Emoji("blue_heart", 0x1f499),
            Emoji("purple_heart", 0x1f49c),
            Emoji("black_heart", 0x1f5a4),
            Emoji("100", 0x1f4af),
            Emoji("anger", 0x1f4a2),
            Emoji("boom", 0x1f4a5),
            Emoji("seeing_stars", 0x1f4ab),
            Emoji("sweat_drops", 0x1f4a6),
            Emoji("dash", 0x1f4a8),
            Emoji("hole", 0x1f573),
            Emoji("bomb", 0x1f4a3),
            Emoji("umm", 0x1f4ac),
            Emoji("speech_bubble", 0x1f5e8),
            Emoji("anger_bubble", 0x1f5ef),
            Emoji("thought", 0x1f4ad),
            Emoji("zzz", 0x1f4a4)
        )

        val emojiViewTyped = mutableListOf<ViewTyped>()
        emojiCodes.forEach { emoji ->
            emojiViewTyped.add(EmojiUi(String(Character.toChars(emoji.emojiCode))))
        }
        val holderFactory = EmojiHolderFactory(this)
        val emojiAdapter = EmojiAdapter<ViewTyped>(holderFactory)
        emojiAdapter.items = emojiViewTyped
        binding.recyclerEmoji.adapter = emojiAdapter
        return binding.root
    }

    override fun getClickedView(view: View, position: Int) {
        val emojiCode = String(Character.toChars(emojiCodes[position].emojiCode))
        val emojiName = emojiCodes[position].emojiName
        setFragmentResult(
            ARG_BOTTOM_FRAGMENT, bundleOf(
                ARG_EMOJI to arrayListOf(emojiName, emojiCode)
            )
        )
        dismiss()
    }

    companion object {
        const val ARG_BOTTOM_FRAGMENT = "BottomSheetDialogFragment"
        const val ARG_EMOJI = "emoji"
    }
}
