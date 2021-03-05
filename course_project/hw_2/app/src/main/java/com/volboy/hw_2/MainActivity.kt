package com.volboy.hw_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.volboy.hw_2.CustomViews.EmojiView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clickEmojiiView(view: View) {
        //view.isSelected = !view.isSelected
    }
}