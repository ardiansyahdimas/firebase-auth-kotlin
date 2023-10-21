package com.test.firebase_auth.ui.screen.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.firebase_auth.R
import com.test.firebase_auth.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val loginFragment = LoginFragment()
        val fragment = fragmentManager.findFragmentByTag(LoginFragment::class.java.simpleName)
        if (fragment !is LoginFragment) {
            fragmentManager
                .beginTransaction()
                .add(R.id.frame_container, loginFragment, LoginFragment::class.java.simpleName)
                .commit()
        }
    }
}