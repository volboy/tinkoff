package com.example.hw_1

import android.app.Activity
import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.hw_1.databinding.ActivityTwoBinding

class ActivityTwo : AppCompatActivity() {
    val contacts= arrayOf("")
    lateinit var localBroadcastReceiver: LocalBroadcastReceiver
    lateinit var intentResult:Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityTwoBinding>(this, R.layout.activity_two)
        localBroadcastReceiver= LocalBroadcastReceiver(contacts)

        binding.btnLoadData.setOnClickListener {
            val intentForService = Intent(this, LoadContactService::class.java)
            startService(intentForService)
        }
    }
    @Suppress("DEPRECATION")
    class LoadContactService() : IntentService("") {
        override fun onHandleIntent(intent: Intent?) {
            //грузим контакты
            Log.i("QWERTY", "YES")
            /*val intentForReceiver=Intent("LOADED_CONTACT_ACTION")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentForReceiver)*/
        }
    }

     class LocalBroadcastReceiver(contacts_param:Array<String>): BroadcastReceiver(){
        private val contacts=contacts_param
        override fun onReceive(context: Context?, intent: Intent?) {
            contacts.plus("Кря")
            Toast.makeText(context, "Кря", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter("LOADED_CONTACT_ACTION")
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(localBroadcastReceiver, intentFilter)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastReceiver);
        super.onDestroy()

    }


}