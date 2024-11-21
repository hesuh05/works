package com.example.localstorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SchoolControlDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "schoolcontroldb";
    public static final int DATABASE_VERSION=1;
    public static final String SQL_CREATE_STUDENTS="CREATE TABLE "+
            SchoolControlContract.Student.TABLE_NAME+" ( "+
            SchoolControlContract.Student.COLUMN_NAME_ID+" TEXT PRIMARY KEY,"+
            SchoolControlContract.Student.COLUMN_NAME_NAME+" TEXT,"+
            SchoolControlContract.Student.COLUMN_NAME_LASTNAME+" TEXT,"+
            SchoolControlContract.Student.COLUMN_NAME_GRADE+" INTEGER,"+
            SchoolControlContract.Student.COLUMN_NAME_GROUP+" TEXT, "+
            SchoolControlContract.Student.COLUMN_NAME_AVERAGE+" REAL,"+
            SchoolControlContract.Student.COLUMN_NAME_CAREER+" TEXT);";
    public static final String SQL_DELETE_STUDENTS="DROP TABLE IF EXISTS "+ SchoolControlContract.Student.TABLE_NAME;
    public SchoolControlDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STUDENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_STUDENTS);
        onCreate(db);
    }
}
