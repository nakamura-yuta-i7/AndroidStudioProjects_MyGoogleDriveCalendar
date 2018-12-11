package com.nkmr.mygoogledrivecalendar

import kotlinx.android.synthetic.main.activity_main.*

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.api.services.calendar.model.Event

class MainActivity: GoogleSignInActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sign_in_button.setOnClickListener { signIn() }
        sign_out_button.setOnClickListener { signOut().setResultCallback {
            textView.text = ""
            sign_in_button.visibility = View.VISIBLE
            sign_out_button.visibility = View.GONE
            disconnect_button.visibility = View.GONE
        } }
        disconnect_button.setOnClickListener { revokeAccess().setResultCallback {
            textView.text = ""
            sign_in_button.visibility = View.VISIBLE
            sign_out_button.visibility = View.GONE
            disconnect_button.visibility = View.GONE
        } }

        sign_out_button.visibility = View.GONE
        disconnect_button.visibility = View.GONE
    }

    override fun handleSignInResult(
        result: GoogleSignInResult,
        callback: (isSuccess: Boolean, signInAccount: GoogleSignInAccount?) -> Unit
    ) {
        super.handleSignInResult(result) { isSuccess, signInAccount ->
            if (isSuccess) {
                textView.text = signInAccount?.email

                sign_in_button.visibility = View.GONE
                sign_out_button.visibility = View.VISIBLE
                disconnect_button.visibility = View.VISIBLE

            } else {
                textView.text = ""

                sign_in_button.visibility = View.VISIBLE
                sign_out_button.visibility = View.GONE
                disconnect_button.visibility = View.GONE
            }
        }
    }

    fun tappedCreateFolderButton(v: View) {

        MyGoogleDrive(application as MyApplication).createRootFolder("New Folder") {
            driveFolderResult ->
            Log.d("###", driveFolderResult.driveFolder.driveId.toString())
        }
    }

    fun tappedGetEventsButton(v: View) {

        MyGoogleCalendar(application as MyApplication).getEvents {
            events -> Log.d("events", events.toString() )
            events.forEach { event: Event ->
                Log.d("event.summary", event.summary)
            }
        }
    }
    fun tappedGetSheetsButton(v: View) {

        MyGoogleSpreadSheets(application as MyApplication).getData(
            sheetId = "1mYA5bZzLLG_WvPio-3iXC9gQm3rTEUdXtATM7PLcjjU",
            range = "シート1!A2:E") {
            data ->
            data.forEach { row ->
                Log.d("row.get(3).toString()", row.get(3).toString() )
            }
        }
    }
}
