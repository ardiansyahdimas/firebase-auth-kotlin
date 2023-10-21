package com.test.firebase_auth.utils

import android.app.Activity
import android.text.TextUtils.replace
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.test.firebase_auth.R
import org.aviran.cookiebar2.CookieBar

object Utils {
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun errorEditText(editText: EditText, message:String){
        editText.error = message
        editText.requestFocus()
    }

    fun isPasswordValid(password: String): Boolean {
        val pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}\$".toRegex()
        return pattern.matches(password)
    }

    fun showNotif(activity: Activity, title:String, message: String){
        CookieBar.build(activity)
            .setTitle(title)
            .setBackgroundColor(R.color.primary_color)
            .setMessage(message)
            .show()
    }

    fun isLoading(isLoading:Boolean, progressBar: ProgressBar, button: Button? = null) {
        progressBar.isVisible = isLoading
        if (button != null) {
            button.isEnabled = !isLoading
        }
    }
}