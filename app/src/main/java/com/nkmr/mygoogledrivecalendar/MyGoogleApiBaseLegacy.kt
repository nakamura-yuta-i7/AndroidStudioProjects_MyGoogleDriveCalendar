package com.nkmr.mygoogledrivecalendar

import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.ExponentialBackOff
import java.util.*

abstract class MyGoogleApiBaseLegacy(application: MyApplication) {

    protected val transport = AndroidHttp.newCompatibleTransport()!!
    protected val jsonFactory = JacksonFactory.getDefaultInstance()!!

    protected val credential = GoogleAccountCredential.usingOAuth2(
        application.applicationContext, Arrays.asList(SCOPE()))
        .setBackOff(ExponentialBackOff())
        .setSelectedAccountName(application.loginUser?.email)!!

    protected abstract fun SCOPE(): String
}