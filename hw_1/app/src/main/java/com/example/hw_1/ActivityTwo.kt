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
            val intentForReceiver = Intent(LOADED_CONTACTS_ACTION)
            intentForReceiver.putExtra(DATA_CONTACTS_KEY, "DONE")
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentForReceiver)

        }

        private fun getContacts(): MutableList<ContactsData> {
            var id: String
            var name: String
            var number = "Номер отсутствует"
            var hasNumber: Int

            if (contacts.isNotEmpty()) contacts.clear()

            val cursor =
                contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    hasNumber =
                        cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    if (hasNumber > 0) {
                        val cursorNumber =
                            contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                arrayOf(id),
                                null
                            )
                        if (cursorNumber != null && cursorNumber.moveToFirst()) {
                            number =
                                cursorNumber.getString(cursorNumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            cursorNumber.close()
                        }

                    }
                    if (name.isNotEmpty() && number.isNotEmpty())
                        contacts.add(ContactsData(name, number))

                } while (cursor.moveToNext())
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
            finish()

        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(applicationContext)
            .unregisterReceiver(localBroadcastReceiver)
        super.onDestroy()

    }


}