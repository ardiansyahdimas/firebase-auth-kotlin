package com.test.firebase_auth.shared_preference

import android.content.Context

class SharedPrefManager(context: Context) {
    companion object {
        private const val PREFS_NAME = "petra_pref"
        private const val IS_LOGIN = "is_login"
        private const val ACCESS_TOKEN = "access_token"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    fun setLogin(value: Boolean){
        editor?.putBoolean(IS_LOGIN,value)
        editor?.commit()
    }

    fun isLogin(): Boolean {
        return preferences.getBoolean(IS_LOGIN, false)
    }

    fun setAccessToken(value: String){
        editor?.putString(ACCESS_TOKEN,value)
        editor?.commit()
    }

    fun getAccessToken(): String {
        return preferences.getString(ACCESS_TOKEN, "").toString()
    }

    fun removeData() {
        editor?.clear()
    }
}