package com.ss.android.allepyfish.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.ss.android.allepyfish.model.ContactInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dell on 3/3/2017.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_STATE = "state";
    private static final String KEY_DISTRICT = "district";
    private static final String KEY_CITY = "city";
    private static final String KEY_PROFILE_PIC = "profile_pic_url";
    private static final String KEY_LOGIN_TYPE = "loginType";
    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"+ KEY_LOGIN_TYPE + " TEXT,"+ KEY_STATE + " TEXT,"+ KEY_DISTRICT + " TEXT,"+ KEY_CITY + " TEXT,"+ KEY_PROFILE_PIC + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * name, email, phoneNo, , created_at
     * */
    public void addUser(String name, String email, String uid, String loginType, String stateSQL, String districtSQL, String citySQL, String created_at, String profile_pic) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_LOGIN_TYPE, loginType); // Login Type
        values.put(KEY_STATE, stateSQL); // state Type
        values.put(KEY_DISTRICT, districtSQL); // district Type
        values.put(KEY_CITY, citySQL); // district Type
        values.put(KEY_LOGIN_TYPE, loginType); // district
        values.put(KEY_PROFILE_PIC, profile_pic); // Created At
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("loginType", cursor.getString(4));
            user.put("created_at", cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


    public List<ContactInfo> getAllContacts() {
        List<ContactInfo> contactList = new ArrayList<ContactInfo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactInfo contact = new ContactInfo();
                contact.setId(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contact.setPhone_no(cursor.getString(3));
                contact.setEmail(cursor.getString(2));
                contact.setLoginType(cursor.getString(4));
                contact.setState(cursor.getString(5));
                contact.setDistrict(cursor.getString(6));
                contact.setCity(cursor.getString(7));
                contact.setprofile_pic_url(cursor.getString(8));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public void upfateUserPhno(String phoneNo) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("", phoneNo);

    }
}
