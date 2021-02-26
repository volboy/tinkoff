package com.example.hw_1

import android.Manifest
import android.Manifest.permission.READ_CONTACTS
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.hw_1.databinding.ActivityMainBinding

const val REQUEST_CODE_ACTIVITY_TWO = 1
const val REQUEST_CODE_PERMISSION = 1
const val LOADED_CONTACTS_ACTION = "LOADED_CONTACT_ACTION"
const val DATA_CONTACTS_KEY = "DATA_CONTACTS_KEY"

class MainActivity : AppCompatActivity() {

    var readContactGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        getPermission()
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

    private fun getPermission() {
        val readContactPermission =
            ContextCompat.checkSelfPermission(
                applicationContext,
                READ_CONTACTS
            );
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
                readContactGranted = true;
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ACTIVITY_TWO) {
            if (resultCode == Activity.RESULT_OK) {
                val someData = data?.getStringExtra(DATA_CONTACTS_KEY)
                Toast.makeText(this, someData, Toast.LENGTH_LONG).show()
            }
        }
    }
}