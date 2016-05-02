package com.wakiedokie.waikiedokie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * DBHelper - handles all database CRUD operations
 *
 * */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";

    private static final int DATABASE_VERSION = 108;

    // user_info Table
    public static final String USER_INFO_TABLE_NAME = "user_info";
    public static final String USER_INFO_COLUMN_FACEBOOK_ID = "facebook_id";
    public static final String USER_INFO_COLUMN_FIRST_NAME = "first_name";
    public static final String USER_INFO_COLUMN_LAST_NAME = "last_name";

    // alarm Table
    public static final String ALARM_TABLE_NAME = "alarm";
    public static final String ALARM_COLUMN_OWNER_ID = "owner_fb_id";
    public static final String ALARM_COLUMN_USER2_ID = "user2_fb_id";

    public static final String ME_TABLE_NAME = "me_info";
    public static final String ME_TABLE_COLUMN_FB_ID = "facebook_id";

    public static final int ALARM_LOCAL_INACTIVE = 0;
    public static final int ALARM_LOCAL_ACTIVE = 1;
    public static final int ALARM_PENDING = 2;
    public static final int ALARM_TYPE_NOT_SET = 3;

    private static final String TAG = "dbHelperBooboo";


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
                        "(facebook_id TEXT primary key, first_name text, last_name text)"
        );
        // Create alarm table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ALARM_TABLE_NAME +
                        "(id INTEGER primary key, owner_fb_id TEXT, user2_fb_id TEXT, alarm_time TEXT, is_active INTEGER, alarm_server_id TEXT NOT NULL DEFAULT '-1')"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS user_info");
        db.execSQL("DROP TABLE IF EXISTS " + ME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE_NAME);
        onCreate(db);
    }


    public boolean insertInfo(String facebook_id, String first_name, String last_name) {
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

    /* get all users that you have not yet set an alarm with */
    public ArrayList<String> getUsersExclude() {
        ArrayList<String> excludes = new ArrayList<String>();
        Cursor res = getAllAlarms();
        while(res.moveToNext()) {
            String fb_id = res.getString(res.getColumnIndex(ALARM_COLUMN_USER2_ID));
            excludes.add(fb_id);
        }
        return excludes;
    }

    /* getFullNameWithID - looks up user table for user with XX ID and returns full name */
    public String getFullNameWithID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from user_info where " + USER_INFO_COLUMN_FACEBOOK_ID + " = \"" + id + "\"", null);
        res.moveToFirst();
        String fName = res.getString(res.getColumnIndex(USER_INFO_COLUMN_FIRST_NAME));
        String lName = res.getString(res.getColumnIndex(USER_INFO_COLUMN_LAST_NAME));
        String fullName = fName + " " + lName;
        return fullName;
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

    /* getMyID - returns my facebook id from me_info table */
    public String getMyIDFromMeTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + ME_TABLE_NAME, null);
        res.moveToFirst();
        String myID = res.getString(res.getColumnIndex(ME_TABLE_COLUMN_FB_ID));
        return myID;
    }

    /* Prints alarm table in console */
    public void printFullAlarmTable() {
        Cursor c = getAllAlarms();
        while(c.moveToNext()) {
            int alarmPK = c.getInt(c.getColumnIndex("id"));
            String ownerID = c.getString(c.getColumnIndex(ALARM_COLUMN_OWNER_ID));
            String user2ID = c.getString(c.getColumnIndex(ALARM_COLUMN_USER2_ID));
            String myID = getMyIDFromMeTable();
            Log.d(TAG, "Alarm(pk=" + alarmPK + "): " +
                    "ownerID: " + ownerID +
                    ", user2ID: " + user2ID +
                    ", myID: " + myID);

        }
    }

    public long addOrUpdateAlarm(int id, String alarmTime, String owner_fb_id, String user2_fb_id, int isActive) {
        long pk = -1;
        if (id == -1) {
            // add alarm
            pk = addAlarm(alarmTime, owner_fb_id, user2_fb_id, isActive);
            Log.d(TAG,"Add alarm" + user2_fb_id);
        } else {
            // update field: user2_fb_id, alarmTime
            updateAlarm(id, alarmTime, user2_fb_id, isActive);
            pk = (long) id;
            Log.d(TAG,"Update alarm" + user2_fb_id);
        }
        return pk;
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


    public int getAlarmId(String owner_fb_id, String user2_fb_id) {
        System.out.println("inside getAlarmId()");
        int alarmID = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + ALARM_TABLE_NAME + " WHERE owner_fb_id = " + owner_fb_id + " AND user2_fb_id = " + user2_fb_id, null);
        res.moveToFirst();
        alarmID = res.getInt(res.getColumnIndex("id"));
        return alarmID;
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

        return timeStr;
    }

    /* Returns name of buddy */
    public String getBuddyName(int alarmID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + ALARM_TABLE_NAME + " where id=" + alarmID + "", null);
        res.moveToFirst();
        String ownerID = res.getString(res.getColumnIndex(ALARM_COLUMN_OWNER_ID));
        String user2ID = res.getString(res.getColumnIndex(ALARM_COLUMN_USER2_ID));
        String myID = getMyIDFromMeTable();
        Log.d(TAG, "Alarm("+alarmID+"): "+ownerID + ", " + user2ID +  ", " + myID);
        String buddyName;
        if (myID.equals(ownerID)) {
            buddyName = getFullNameWithID(user2ID);
        }
        else {
            buddyName = getFullNameWithID(ownerID);
        }
        return buddyName;
    }

    public String getOwnerFbId(int alarmID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + ALARM_TABLE_NAME + " where id=" + alarmID + "", null);
        res.moveToFirst();
        String ownerID = res.getString(res.getColumnIndex(ALARM_COLUMN_OWNER_ID));
        System.out.println("Inside getOwnerFbId: " + ownerID);
        return ownerID;
    }

    public String getUser2FbId(int alarmID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + ALARM_TABLE_NAME + " where id=" + alarmID + "", null);
        res.moveToFirst();
        String user2ID = res.getString(res.getColumnIndex(ALARM_COLUMN_USER2_ID));
        System.out.println("Inside getUser2FbId: " + user2ID);
        return user2ID;
    }

    public boolean imOwnerOfAlarm(int alarmID, String my_fb_id) {
        System.out.println("Checking owner ship");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + ALARM_TABLE_NAME + " where id=" + alarmID + "", null);
        res.moveToFirst();
        String ownerID = res.getString(res.getColumnIndex(ALARM_COLUMN_OWNER_ID));
        if (ownerID.equals(my_fb_id)) {
            System.out.println("yes. I own this alarm");
            return true;
        } else{
            System.out.println("I am user2 of this alarm");
            return false;
        }
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

    /* setAlarmStatus - set alarm is_active column */
    public  void setAlarmStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + ALARM_TABLE_NAME +
                " SET IS_ACTIVE = " + status + " WHERE ID = " + id);
    }

    /* setAlarmStatus - set alarm is_active column */
    public  void setAlarmStatus(String owner_fb_id, String user2_fb_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + ALARM_TABLE_NAME +
                " SET IS_ACTIVE = " + status + " WHERE owner_fb_id = " + owner_fb_id + " AND user2_fb_id = " + user2_fb_id);
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


}

