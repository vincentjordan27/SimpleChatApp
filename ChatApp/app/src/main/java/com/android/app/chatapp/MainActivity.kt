package com.android.app.chatapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),10)
        }

        val editText = findViewById<EditText>(R.id.editText)
        findViewById<Button>(R.id.enterBtn).setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("name", editText.text.toString().trim())
            startActivity(intent)
        }

    }
}