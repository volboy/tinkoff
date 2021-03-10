package com.example.hw_1

import android.app.Activity
import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.provider.ContactsContract
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.hw_1.ContactsHolder.contacts
import com.example.hw_1.databinding.ActivityTwoBinding


class ActivityTwo : AppCompatActivity() {
    lateinit var localBroadcastReceiver: LocalBroadcastReceiver
    lateinit var intentFilter: IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityTwoBinding>(this, R.layout.activity_two)
        localBroadcastReceiver = LocalBroadcastReceiver()
        intentFilter = IntentFilter(LoadContactService.LOADED_CONTACTS_ACTION)
        val localBroadcastManager = LocalBroadcastManager.getInstance(applicationContext)
        localBroadcastManager.registerReceiver(localBroadcastReceiver, intentFilter)
        binding.btnLoadData.setOnClickListener {
            val intentForService = Intent(applicationContext, LoadContactService::class.java)
            startService(intentForService)
        }
    }

    inner class LocalBroadcastReceiver() : BroadcastReceiver() {
        private val intentForMainActivity = Intent()

        override fun onReceive(context: Context?, intent: Intent?) {
            intentForMainActivity.putExtra(LoadContactService.DATA_CONTACTS_KEY, intent?.getStringExtra(LoadContactService.DATA_CONTACTS_KEY))
            setResult(Activity.RESULT_OK, intentForMainActivity)
            finish()
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(localBroadcastReceiver)
        super.onDestroy()
    }
}