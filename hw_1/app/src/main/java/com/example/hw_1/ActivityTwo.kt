package com.example.hw_1

import android.Manifest
import android.app.Activity
import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.hw_1.ContactsHolder.contacts
import com.example.hw_1.databinding.ActivityTwoBinding


class ActivityTwo : AppCompatActivity() {

    lateinit var localBroadcastReceiver: LocalBroadcastReceiver
    lateinit var intentFilter: IntentFilter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityTwoBinding>(this, R.layout.activity_two)

        localBroadcastReceiver = LocalBroadcastReceiver()
        intentFilter = IntentFilter(LOADED_CONTACTS_ACTION)
        val localBroadcastManager = LocalBroadcastManager.getInstance(applicationContext)
        localBroadcastManager.registerReceiver(localBroadcastReceiver, intentFilter)

        binding.btnLoadData.setOnClickListener {
            val intentForService = Intent(applicationContext, LoadContactService::class.java)
            startService(intentForService)
        }
    }

    @Suppress("DEPRECATION")
    class LoadContactService() : IntentService("") {
        override fun onHandleIntent(intent: Intent?) {
            getContacts()
            Log.i("info_hw1", "Service was worked")
            val intentForReceiver = Intent(LOADED_CONTACTS_ACTION)
            intentForReceiver.putExtra(DATA_CONTACTS_KEY, "DONE")
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentForReceiver)

        }

        private fun getContacts(): MutableList<ContactsData> {
            val cursor =
                contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                    val number =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                    ContactsHolder.contacts.add(ContactsData(name, number))
                }
                cursor.close()
            }
            return contacts
        }
    }

    inner class LocalBroadcastReceiver() : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val intentForMainActivity = Intent()
            intentForMainActivity.putExtra(
                DATA_CONTACTS_KEY,
                intent?.getStringExtra(DATA_CONTACTS_KEY)
            )
            setResult(Activity.RESULT_OK, intentForMainActivity)
            Log.i("info_hw1", "BroadcastReceiver was worked")
            finish()

        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(applicationContext)
            .unregisterReceiver(localBroadcastReceiver);
        super.onDestroy()

    }


}