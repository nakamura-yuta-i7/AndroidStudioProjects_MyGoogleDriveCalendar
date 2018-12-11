package com.nkmr.mygoogledrivecalendar

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event

class MyGoogleCalendar(application: MyApplication):
    MyGoogleApiBaseLegacy(application) {

    override fun SCOPE(): String { return CalendarScopes.CALENDAR_READONLY }

    private var mService = com.google.api.services.calendar.Calendar.Builder(
        transport, jsonFactory, credential)
        .setApplicationName("Google API Android")
        .build()

    fun getEvents(onSuccess: (events: List<Event>) -> Unit) {

        object : MyAsyncTask() {
            override fun doInBackground(vararg params: Void): String? {

                val now = DateTime(System.currentTimeMillis())
                val events = mService.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute()

                onSuccess(events.items)

                return null
            }
        }.execute()
    }
}