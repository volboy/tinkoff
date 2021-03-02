package com.example.hw_1

import android.Manifest
import android.Manifest.permission.READ_CONTACTS
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw_1.databinding.ActivityMainBinding

const val REQUEST_CODE_ACTIVITY_TWO = 1
const val REQUEST_CODE_PERMISSION = 1
const val LOADED_CONTACTS_ACTION = "LOADED_CONTACT_ACTION"
const val DATA_CONTACTS_KEY = "DATA_CONTACTS_KEY"

class MainActivity : AppCompatActivity() {

    var readContactGranted = false
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        getPermission()
        getContacts()
        binding.btnToLoadActivity.setOnClickListener {
            if (readContactGranted) {
                val intent = Intent(this, ActivityTwo::class.java)
                startActivityForResult(intent, REQUEST_CODE_ACTIVITY_TWO)
            } else {
                Toast.makeText(this, "Необходимо разрешить доступ к контактам", Toast.LENGTH_LONG)
                    .show()
                getPermission()
            }
        }
    }


    private fun getContacts() {
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        if (ContactsHolder.contacts.isNotEmpty()) {
            binding.recyclerView.adapter = ContactItemAdapter(ContactsHolder.contacts)
        }
    }

    private fun getPermission() {
        val readContactPermission =
            ContextCompat.checkSelfPermission(
                applicationContext,
                READ_CONTACTS
            )
        if (readContactPermission == PackageManager.PERMISSION_GRANTED) {
            readContactGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(READ_CONTACTS),
                REQUEST_CODE_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContactGranted = true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ACTIVITY_TWO) {
            if (resultCode == Activity.RESULT_OK) {
                if (data?.getStringExtra(DATA_CONTACTS_KEY).equals("DONE")) {
                    getContacts()
                } else {
                    Toast.makeText(applicationContext, "Контакты не загрузились", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, "Что-то пошло не так", Toast.LENGTH_LONG).show()
            }
        }
    }
}