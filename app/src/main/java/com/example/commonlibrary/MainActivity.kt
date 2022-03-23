package com.example.commonlibrary

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.audioRecording).setOnClickListener {
            startActivity(Intent(this, AudioRecordActivity::class.java))
        }
        findViewById<View>(R.id.camera_btn).setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }
}