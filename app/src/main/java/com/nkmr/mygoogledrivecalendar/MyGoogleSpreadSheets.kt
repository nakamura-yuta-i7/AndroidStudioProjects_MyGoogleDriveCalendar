package com.nkmr.mygoogledrivecalendar

import com.google.api.services.sheets.v4.SheetsScopes

class MyGoogleSpreadSheets(application: MyApplication):
    MyGoogleApiBaseLegacy(application) {

    override fun SCOPE(): String { return SheetsScopes.SPREADSHEETS_READONLY }

    private var mService = com.google.api.services.sheets.v4.Sheets.Builder(
        transport, jsonFactory, credential)
        .setApplicationName("Google API Android")
        .build()

    fun getData(sheetId: String, range: String, onSuccess: (result: List<MutableList<Any>>) -> Unit) {
        object : MyAsyncTask() {
            override fun doInBackground(vararg params: Void): String? {
                val response = mService.spreadsheets().values().get(sheetId, range)
                    .execute()
                onSuccess(response.getValues())
                return null
            }
        }.execute()
    }
}