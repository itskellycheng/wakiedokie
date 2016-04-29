package com.wakiedokie.waikiedokie.util.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;
import java.util.HashMap;

/**
 * DBHelper - handles all database CRUD operations
 *
 * */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    private static final int DATABASE_VERSION = 2;

    // user_info Table
    public static final String USER_INFO_TABLE_NAME = "user_info";
    public static final String USER_INFO_COLUMN_ID = "id";
    public static final String USER_INFO_COLUMN_FACEBOOK_ID = "facebook_id";
    public static final String USER_INFO_COLUMN_FIRST_NAME = "first_name";
    public static final String USER_INFO_COLUMN_LAST_NAME = "last_name";


    // alarm Table
    public static final String ALARM_TABLE_NAME = "alarm";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS user_info");
        db.execSQL(
                "create table user_info " +
                        "(id integer primary key, facebook_id text, first_name text, last_name text)"
        );
        // Create alarm table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ALARM_TABLE_NAME +
                        "(id INTEGER primary key, alarm_time TEXT, wakie_buddy TEXT, is_active INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS user_info");
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertInfo(int id, String facebook_id, String first_name, String last_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("facebook_id", facebook_id);
        contentValues.put("first_name", first_name);
        contentValues.put("last_name", last_name);
        db.insert("user_info", null, contentValues);
        return true;
    }


    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from user_info where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, USER_INFO_TABLE_NAME);
        return numRows;
    }

    /* getAllUsers - get all users in user table */
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + USER_INFO_TABLE_NAME, null);
        return res;
    }

    /* addAlarm - add row into alarm table */
    public long addAlarm(String alarmTime, String wakieBuddy, int isActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("alarm_time", alarmTime);
        contentValues.put("wakie_buddy", wakieBuddy);
        contentValues.put("is_active", isActive);
        long pk = db.insert(ALARM_TABLE_NAME, null, contentValues);
        return pk;
    }

    /* updateAlarm - update alarm with id */
    public boolean updateAlarm(int id, String alarmTime, String wakieBuddy, int isActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("alarm_time", alarmTime);
        contentValues.put("wakie_buddy", wakieBuddy);
        contentValues.put("is_active", isActive);
        String whereStr = "id=" + Integer.toString(id);
        db.update(ALARM_TABLE_NAME, contentValues, whereStr, null);
        return true;
    }

    /* getAllAlarms - get all data in alarm table */
    public Cursor getAllAlarms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + ALARM_TABLE_NAME, null);
        return res;
    }

    /* getAlarm - get alarm according to id */
    public Cursor getAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + ALARM_TABLE_NAME + " where id=" + id + "", null);
        return res;
    }

    /* getTimeString - get formatted string of time eg. 10:31 AM */
    public String getTimeString(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + ALARM_TABLE_NAME + " where id=" + id + "", null);

        String alarmTime = res.getString(res.getColumnIndex("alarm_time"));
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(alarmTime));
        final Calendar finalCal = cal;
        String hour = Integer.toString(cal.get(Calendar.HOUR));
        String minute = Integer.toString(cal.get(Calendar.MINUTE));
        if (minute.length()==1){
            minute = "0" + minute;
        }
        String amPm;
        if (cal.get(Calendar.AM_PM) == 0)
            amPm = "AM";
        else
            amPm = "PM";
        String timeStr = hour + ":" + minute + " " + amPm;
        return timeStr;
    }

    /* setAlarmToActive - with alarm id, set alarm active column to active (1) */
    public void setAlarmToActive(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + ALARM_TABLE_NAME +
                " SET IS_ACTIVE = 1 WHERE ID = " + id);
    }

    /* setAlarmToActive - set alarm active column to inactive (0) */
    public void setAlarmToInactive(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + ALARM_TABLE_NAME +
                " SET IS_ACTIVE = 0 WHERE ID = " + id);
    }

    /* deleteAlarm - deletes row in alarm table with alarm id */
    public void deleteAlarm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "id=" + Integer.toString(id);
        db.delete(ALARM_TABLE_NAME, whereClause, null);
    }



//    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("name", name);
//        contentValues.put("phone", phone);
//        contentValues.put("email", email);
//        contentValues.put("street", street);
//        contentValues.put("place", place);
//        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
//        return true;
//    }

//    public Integer deleteContact (Integer id)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete("contacts",
//                "id = ? ",
//                new String[] { Integer.toString(id) });
//    }

//    public ArrayList<String> getAllCotacts()
//    {
//        ArrayList<String> array_list = new ArrayList<String>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
//            res.moveToNext();
//        }
//        return array_list;
//    }
}

