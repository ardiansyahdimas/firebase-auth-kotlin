package com.test.firebase_auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.test.firebase_auth.databinding.ActivityMainBinding
import com.test.firebase_auth.shared_preference.SharedPrefManager
import com.test.firebase_auth.ui.screen.auth.AuthActivity
import com.test.firebase_auth.ui.screen.home.HomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 2000
    private val sharedPref = SharedPrefManager(App.context)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startHandlerLoop()
    }

    private fun startHandlerLoop() {
        handler.postDelayed({
            val activity = if (sharedPref.isLogin()) HomeActivity() else AuthActivity()
            startActivity(Intent(this@MainActivity, activity::class.java))
            finish()
        }, delayMillis)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}