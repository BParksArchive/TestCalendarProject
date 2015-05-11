package com.example.bparks1271.testcalendarproject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Brendan on 5/10/2015.
 */
public class CalendarTest {

    private static final String TAG = "CalendarTest";

    public static void lookupCalendars(Context context) {
        // Projection array. Creating indices for this array instead of doing
        // dynamic lookups improves performance.
        final String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
        };

        // The indices for the projection array above.
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        final int PROJECTION_DISPLAY_NAME_INDEX = 2;
        final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;





        Cursor cur = null;

        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);



        Log.d(TAG, "====DISPLAYING LIST OF CALENDARS===");
        while (cur.moveToNext()) {
            Log.d(TAG, "Calendar ==> " + cur.getString(PROJECTION_DISPLAY_NAME_INDEX));
        }
    }


    private static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Events.CALENDAR_ID,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.EVENT_TIMEZONE
    };

    public static void lookupEvent(Items event, Context context) {
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;



        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);

        int ID_INDEX = cur.getColumnIndex(CalendarContract.Events._ID);
        int NAME_INDEX = cur.getColumnIndex(CalendarContract.Events.TITLE);

        Log.d(TAG, "LookupEvent(): Found " + cur.getCount() + " entries in calendar events.");

        while (cur.moveToNext()) {
            Log.d(TAG, cur.getString(NAME_INDEX));
        }
    }

    static AtomicInteger fakeItemCounter = new AtomicInteger();

    public static Items createFakeItem() {
        Items item = new Items();
        item.setDate("2-Jul-15");
        item.setDescription("Test description");
        item.setName("Super Fake Test Event! Aww yeah");
        item.setTime("10:00am");
        item.setType("Event"); //Use this to color event
        item.setId(fakeItemCounter.getAndIncrement());

        return item;
    }


}
