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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bparks1271 on 5/8/15.
 */
public class CalendarHelper {

    private static final String TAG = "CalendarHelper";

    private static Context context;

    static final String[] CALENDAR_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT,                 // 3
            CalendarContract.Calendars.ACCOUNT_TYPE                   // 4
    };

    static final int PROJECTION_ID_INDEX = 0;
    static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    static final int PROJECTION_ACCOUNT_TYPE_INDEX = 3;



    public CalendarHelper(Context c) {
        context = c;
    }

    public void doCalendarLogDump() {

        // Run query
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        cur = cr.query(uri, CALENDAR_PROJECTION, null, null, null);

        // Use the cursor to step through the returned records
        while (cur.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;
            String accountType = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            accountType = cur.getString(PROJECTION_ACCOUNT_TYPE_INDEX);

            // Do something with the values...
            Log.d(TAG, "Calendar found: " + calID + " \nDisplay name: "
                    + displayName + " \nAccount name: "
                    + accountName + " \nOwner name: "
                    + ownerName + " \nAccount type: "
                    + accountType);
        }

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

    public void addEventToCalendar(Items event, int calID) {
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2012, 9, 14, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2012, 9, 14, 8, 45);
        endMillis = endTime.getTimeInMillis();

        long eventTime = getEventCstTimeInMillis(event);

        if (eventTime < 0) {
            Log.e(TAG,"addEventToCalendar(): Could not add event to phone calendar due to" +
                    " invalid date and time");
            return;
        }

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, calID);

//        values.put(Events.DTSTART, eventTime);
//        values.put(Events.DTEND, eventTime + 3600000);

        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, event.getName());
        values.put(CalendarContract.Events.DESCRIPTION, event.getDescription());
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "US/Central");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        long eventID = Long.parseLong(uri.getLastPathSegment());

        Log.d(TAG, "Event " + eventID + " added to calendar!!");

        CalendarTest.lookupEvent(event, context);
    }



}
