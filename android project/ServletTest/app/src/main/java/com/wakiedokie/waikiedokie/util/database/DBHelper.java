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
    private static final int DATABASE_VERSION = 60;

    // user_info Table
    public static final String USER_INFO_TABLE_NAME = "user_info";
    public static final String USER_INFO_COLUMN_ID = "id";
    public static final String USER_INFO_COLUMN_FACEBOOK_ID = "facebook_id";
    public static final String USER_INFO_COLUMN_FIRST_NAME = "first_name";
    public static final String USER_INFO_COLUMN_LAST_NAME = "last_name";

    // alarm Table
    public static final String ALARM_TABLE_NAME = "alarm";
//    public static final String ALARM_MAIN_ACTIVITY_TABLE_NAME = "alarm_main_activity";
    public static final String ME_TABLE_NAME = "me_info";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS user_info");
        db.execSQL(
                "create table user_info " +
                        "(facebook_id text primary key, first_name text, last_name text)"
        );
        db.execSQL("DROP TABLE IF EXISTS " + ME_TABLE_NAME);
        db.execSQL(
                "create table " + ME_TABLE_NAME +
                        "(facebook_id text primary key, first_name text, last_name text)"
        );
        // Create alarm table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ALARM_TABLE_NAME +
                        "(id INTEGER primary key, owner_fb_id TEXT, user2_fb_id TEXT, alarm_time TEXT, is_active INTEGER, alarm_server_id TEXT NOT NULL DEFAULT '-1')"
        );


        // Create run alarm main activity first time table
//        db.execSQL("DROP TABLE IF EXISTS alarm_main_activity");
//        db.execSQL(
//                "create table " + ALARM_MAIN_ACTIVITY_TABLE_NAME +
//                        "(facebook_id text primary key, first_name text, last_name text)"
//        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS user_info");
        db.execSQL("DROP TABLE IF EXISTS " + ME_TABLE_NAME);
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

    public String getMyFbId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from me_info", null);
        res.moveToFirst();
        String fb_id = res.getString(res.getColumnIndex(DBHelper.USER_INFO_COLUMN_FACEBOOK_ID));
//        if (!res.isClosed()){
//            res.close();
//        }
        return fb_id;

    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, USER_INFO_TABLE_NAME);
        return numRows;
    }


    public int numberOfRowsAlarmTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ALARM_TABLE_NAME);
        return numRows;
    }

    /* getAllUsers - get all users in user table */
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + USER_INFO_TABLE_NAME, null);
        return res;
    }

    /* insertMe - insert row into me table. Me table only has one row. */
    public boolean insertMe(String facebook_id, String first_name, String last_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ME_TABLE_NAME, null, null); // delete all rows
        ContentValues contentValues = new ContentValues();
        contentValues.put("facebook_id", facebook_id);
        contentValues.put("first_name", first_name);
        contentValues.put("last_name", last_name);
        db.insert(ME_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getMe() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + ME_TABLE_NAME, null);
        return res;
    }

    /* addAlarm - add row into alarm table */
    public long addAlarm(String alarmTime, String owner_fb_id, String user2_fb_id, int isActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("alarm_time", alarmTime);
        contentValues.put("owner_fb_id", owner_fb_id);
        contentValues.put("user2_fb_id", user2_fb_id);
        contentValues.put("is_active", isActive);
        long pk = db.insert(ALARM_TABLE_NAME, null, contentValues);
        return pk;
    }


    public long addOrUpdateAlarm(int id, String alarmTime, String owner_fb_id, String user2_fb_id, int isActive) {
        long pk = -1;
        if (id == -1) {
            // add alarm
            pk = addAlarm(alarmTime, owner_fb_id, user2_fb_id, isActive);
        } else {
            // update field: user2_fb_id, alarmTime
            updateAlarm(id, alarmTime, user2_fb_id, isActive);
            pk = (long) id;
        }
        return pk;
    }


    /* addAlarm - add row into alarm table */
    public long addAlarm(String alarmTime, String user2_fb_id, int isActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("alarm_time", alarmTime);
        contentValues.put("is_active", isActive);
        long pk = db.insert(ALARM_TABLE_NAME, null, contentValues);
        db.close();
        return pk;
    }



    /* updateAlarm - update alarm with id */
    public boolean updateAlarm(int id, String alarmTime, String user2_fb_id, int isActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("alarm_time", alarmTime);
        contentValues.put("user2_fb_id", user2_fb_id);
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

    public void printAllAlarms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + ALARM_TABLE_NAME, null);
        System.out.println("Printing local alarm table..");
        while(res.moveToNext()) {
            System.out.println("alarm table is not empty");
            String alarmId = res.getString(res.getColumnIndex("id"));
            String time = res.getString(res.getColumnIndex("alarm_time"));
            String owner_fb_id = res.getString(res.getColumnIndex("owner_fb_id"));
            String user2_fb_id = res.getString(res.getColumnIndex("user2_fb_id"));
            String alarm_server_id = res.getString(res.getColumnIndex("alarm_server_id"));
            int isActive = res.getInt(res.getColumnIndex("is_active"));
            System.out.println("Alarm id: " + alarmId + ", time: " + time + ", alarm_server_id: " + alarm_server_id);
            System.out.println("Owner_fb_id: " + owner_fb_id + ", user2_fb_id: " + user2_fb_id + ", isActive: " + isActive);

        }
//        if (!res.isClosed()) {
//            res.close();
//        }
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

//        if (!res.isClosed()) {
//            res.close();
//        }
        return timeStr;
    }


    /* setAlarmToActive - with alarm id, set alarm active column to active (1) */
    public void setAlarmToActive(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + ALARM_TABLE_NAME +
                " SET IS_ACTIVE = 1 WHERE ID = " + id);
    }

    /* setAlarmToActive - set alarm active column to active (1) with owner fb_id & user2 fb_id */
    public void setAlarmToActive(String owner_fb_id, String user2_fb_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + ALARM_TABLE_NAME +
                " SET IS_ACTIVE = 1 WHERE owner_fb_id = " + owner_fb_id + " AND user2_fb_id = " + user2_fb_id);
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
    /* deleteAlarm - deletes row in alarm table with owner & user2's fb id*/
    public void deleteAlarm(String owner_fb_id, String user2_fb_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "owner_fb_id ="+owner_fb_id+" AND user2_fb_id=" + user2_fb_id;
        db.delete(ALARM_TABLE_NAME, whereClause, null);
    }


    public void setAlarmServerId(String owner_fb_id, String user2_fb_id, String alarm_server_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("alarm_server_id", alarm_server_id);
        String whereStr = "owner_fb_id = \"" + owner_fb_id +"\" and user2_fb_id = \"" + user2_fb_id + "\"";
        db.update(ALARM_TABLE_NAME, contentValues, whereStr, null);
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

