package com.example.commonlibrary

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.text1)

        val viewModelProvider = ViewModelProvider(this)
        val mainViewModel = viewModelProvider.get(MainViewModel::class.java)
        mainViewModel.weChatPublicAccountBeanMutableLiveData.observe(this) {
            Log.e(TAG, "onCreate: 收到数据...")
            textView.text = Gson().toJson(it)
        }

        findViewById<View>(R.id.btn).setOnClickListener {
            Log.e(TAG, "onCreate: 发出网络请求...")
            mainViewModel.getWeChatPublicAccount(this)
            val handler = Handler(Looper.getMainLooper())
            Log.e(TAG, "onCreate: " + SystemClock.uptimeMillis())
            handler.post {
                Log.e(TAG, "onCreate: ")
            }
        }
    }
}