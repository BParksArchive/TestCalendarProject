package com.example.bparks1271.testcalendarproject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.provider.CalendarContract.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

public class CalendarTestActivity extends AppCompatActivity {

    private static final String TAG = "CalendarTestActivity";

    private static final String ESSENCE_OWNER_ACCOUNT = "testOwnerAccount@timeinc.com";

    private CalendarHelper calHelper = new CalendarHelper(this);

    long calendarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_test);
        createNewCalendarIfNotExistsAndSetId(this);


    }

    public void createNewCalendar(Context context) {
        ContentValues values = new ContentValues();
        values.put(Calendars.ACCOUNT_NAME, "Essence Festival 2015_v2");
        values.put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(Calendars.NAME, "ESSENCE_FESTIVAL_2015_v2");
        values.put(Calendars.CALENDAR_DISPLAY_NAME, "My Events");
        values.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
        values.put(Calendars.CALENDAR_COLOR, 0x601F61);
        values.put(Calendars.OWNER_ACCOUNT, "some.account@googlemail.com");
        values.put(Calendars.CALENDAR_TIME_ZONE, "US/Central");
        values.put(Calendars.SYNC_EVENTS, 1);
        Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(Calendars.ACCOUNT_NAME, "com.example");
        builder.appendQueryParameter(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,
                "true");
        Uri uri = context.getContentResolver().insert(builder.build(), values);

        calendarId = Long.parseLong(uri.getLastPathSegment());
    }

    private void setCalendarId(long id) {
        calendarId = id;
    }


    private long createNewCalendarIfNotExistsAndSetId(Context context) {
        long existingId = getEssenceCalendarIdIfExists(context);

        if (existingId != -1) {
            setCalendarId(existingId);
            return existingId;
        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.ACCOUNT_NAME, "TEST Festival 2015");
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(CalendarContract.Calendars.NAME, "TEST_FESTIVAL_2015");
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "My Test Events");
        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0x601F61);
        values.put(CalendarContract.Calendars.OWNER_ACCOUNT, ESSENCE_OWNER_ACCOUNT);
        values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, "US/Central");
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "TesT Festival 2015");
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,
                "true");
        Uri uri = context.getContentResolver().insert(builder.build(), values);

        long calendarId = Long.parseLong(uri.getLastPathSegment());
        setCalendarId(calendarId);
        return calendarId;
    }

    private long getEssenceCalendarIdIfExists(Context context) {
        final String[] CAL_PROJECTION = new String[] {
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
        };

        Cursor cur = null;

        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        cur = cr.query(uri, CAL_PROJECTION, null, null, null);

        StringBuilder builder = new StringBuilder();
        builder.append("Calendar IDs: ");

        while (cur.moveToNext()) {
            builder.append(cur.getString(0) + ", ");
            if (cur.getString(3).equals(ESSENCE_OWNER_ACCOUNT)) {
                Log.d(TAG, "Essence Calendar Found ==> " + cur.getString(1)
                        + " with ID " + Long.parseLong( cur.getString(0) ));
                return Long.parseLong(cur.getString(0));
            }
        }

        Log.d(TAG, "Essence calendar NOT found in calendars database.");
        return -1;
    }

    @Override
    protected void onStart() {
        super.onStart();
        CalendarTest.doCalendarLogDump(this);

        Items event = CalendarTest.createFakeItem();
//        CalendarTest.lookupEvent(event, this);
        calHelper.addEventToCalendar(event, (int) calendarId);
//        addNewEventToCalendar(event);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
