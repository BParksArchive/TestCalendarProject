package com.example.bparks1271.testcalendarproject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by bparks1271 on 5/8/15.
 */
public class CalendarHelper {

    private static final String TAG = "CalendarHelper";

    private static Context context;

    public CalendarHelper(Context c) {
        context = c;
    }


    public long getEventCstTimeInMillis(Items event) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-MMM-yy_h:mma");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("US/Central"));
        long result = -1;

        try {
            String groupHeader = event.getDate() + "_" + event.getTime();
            Date keyDate = simpleDateFormat.parse(groupHeader);
            result = keyDate.getTime();
        } catch (ParseException e) {
            Log.d(TAG, "getEventTimeInMillis(): Error creating date for event", e);
        }

        return result;
    }

//    public void storeEventReferenceInSharedPrefs()

    public void addEventToCalendar(Items event, int calID) {
        long eventTime = getEventCstTimeInMillis(event);

        if (eventTime < 0) {
            Log.e(TAG,"addEventToCalendar(): Could not add event to phone calendar due to" +
                    " invalid date and time");
            return;
        }
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.CALENDAR_ID, calID);

        values.put(CalendarContract.Events.DTSTART, eventTime);
        values.put(CalendarContract.Events.DTEND, eventTime + 3600000);

        values.put(CalendarContract.Events.TITLE, event.getName());
        values.put(CalendarContract.Events.DESCRIPTION, event.getDescription());
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "US/Central");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        long eventID = Long.parseLong(uri.getLastPathSegment());

        Log.d(TAG, "Event " + eventID + " added to calendar!!");
    }
}
