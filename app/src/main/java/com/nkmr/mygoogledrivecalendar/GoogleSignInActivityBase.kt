package com.nkmr.mygoogledrivecalendar

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.Drive
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.sheets.v4.SheetsScopes

open class GoogleSignInActivityBase: AppCompatActivity(),
    GoogleApiClient.OnConnectionFailedListener {

    lateinit var mGoogleApiClient: GoogleApiClient

    val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.DRIVE_FILE))
            .requestScopes(Scope(SheetsScopes.SPREADSHEETS_READONLY))
            .requestScopes(Scope(CalendarScopes.CALENDAR_READONLY))
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .addApi(Drive.API)
            .build()
    }

    public override fun onStart() {
        super.onStart()
        val opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient)
        if (opr.isDone) {
            Log.d("###", "Got cached sign-in")
            val result = opr.get()
            handleSignInResult(result)
        } else {
            opr.setResultCallback { googleSignInResult ->
                handleSignInResult(googleSignInResult)
            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d("onConnectionFailed:", connectionResult.toString())
    }

    protected fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    protected fun revokeAccess(): PendingResult<Status> {
        return Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient)
    }

    protected fun signOut(): PendingResult<Status> {
        return Auth.GoogleSignInApi.signOut(mGoogleApiClient)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result) {isSuccess, signInAccount ->  }
        }
    }

    open protected fun handleSignInResult(result: GoogleSignInResult) {
        handleSignInResult(result) { _, _ -> }
    }

    open protected fun handleSignInResult(
        result: GoogleSignInResult,
        callback: (isSuccess: Boolean, signInAccount: GoogleSignInAccount?) -> Unit
    ) {
        Log.d("### handleSignInResult -> result.isSuccess ### :", result.isSuccess.toString() )

        if (result.isSuccess) {
            val acct = result.signInAccount!!
            val email = acct.email.toString()
            (application as MyApplication).login(email, mGoogleApiClient)
        }
        callback(result.isSuccess, result.signInAccount)
    }
}