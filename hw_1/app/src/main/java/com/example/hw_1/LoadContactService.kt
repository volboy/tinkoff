package com.example.hw_1

import android.app.IntentService
import android.content.Intent
import android.provider.ContactsContract
import androidx.localbroadcastmanager.content.LocalBroadcastManager

@Suppress("DEPRECATION")
class LoadContactService() : IntentService(LoadContactService::class.java.simpleName) {
    private val intentForReceiver = Intent(LOADED_CONTACTS_ACTION)

    override fun onHandleIntent(intent: Intent?) {
        getContacts()
        intentForReceiver.putExtra(DATA_CONTACTS_KEY, "DONE")
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentForReceiver)
    }

    private fun getContacts(): MutableList<ContactsData> {
        var id: String
        var name: String
        var number = getString(R.string.no_number_string)
        var hasNumber: Int
        if (ContactsHolder.contacts.isNotEmpty()) {
            ContactsHolder.contacts.clear()
        }
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                hasNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
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
                        number = cursorNumber.getString(cursorNumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        cursorNumber.close()
                    }
                }
                if (name.isNotEmpty() && number.isNotEmpty()) {
                    ContactsHolder.contacts.add(ContactsData(name, number))
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
        return ContactsHolder.contacts
    }

    companion object {
        const val LOADED_CONTACTS_ACTION = "LOADED_CONTACT_ACTION"
        const val DATA_CONTACTS_KEY = "DATA_CONTACTS_KEY"
    }
}
