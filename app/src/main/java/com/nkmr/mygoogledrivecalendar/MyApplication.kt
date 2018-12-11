package com.nkmr.mygoogledrivecalendar

import android.app.Application
import android.content.SharedPreferences
import com.google.android.gms.common.api.GoogleApiClient

class MyApplication : Application() {
    lateinit var preferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        preferences = getSharedPreferences( packageName + "_preferences", MODE_PRIVATE)
    }

    class LoginUser(var email: String)
    var loginUser: LoginUser? = null
    var googleApiClient: GoogleApiClient? = null
    fun login(email: String, mGoogleApiClient: GoogleApiClient) {
        loginUser = LoginUser(email)
        googleApiClient = mGoogleApiClient
    }
}