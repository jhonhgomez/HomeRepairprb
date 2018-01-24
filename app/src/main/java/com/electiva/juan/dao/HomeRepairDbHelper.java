package com.electiva.juan.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Juan on 01/11/2015.
 */
public class HomeRepairDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HomeRepair.db";
    

    public HomeRepairDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HomeRepairContract.SQL_CREATE_ECONOMIC);
        db.execSQL(HomeRepairContract.SQL_CREATE_ROLE);
        db.execSQL(HomeRepairContract.SQL_CREATE_USER);
        db.execSQL(HomeRepairContract.SQL_CREATE_COMPANY);
        db.execSQL(HomeRepairContract.SQL_CREATE_CLIENT);
        db.execSQL(HomeRepairContract.SQL_CREATE_REQUEST);
        db.execSQL(HomeRepairContract.SQL_CREATE_QUOTE);
        db.execSQL(HomeRepairContract.SQL_INSERT_ECONOMIC);
        db.execSQL(HomeRepairContract.SQL_INSERT_ROLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(HomeRepairContract.SQL_DELETE_ECONOMIC);
        db.execSQL(HomeRepairContract.SQL_DELETE_USER);
        db.execSQL(HomeRepairContract.SQL_DELETE_ROLE);
        db.execSQL(HomeRepairContract.SQL_DELETE_COMPANY);
        db.execSQL(HomeRepairContract.SQL_DELETE_CLIENT);
        db.execSQL(HomeRepairContract.SQL_DELETE_REQUEST);
        db.execSQL(HomeRepairContract.SQL_DELETE_QUOTE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
