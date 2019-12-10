package com.example.secondtry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "reminder_db";
    public static final String TABLE_NAME = "reminder_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "MESSAGE";
    public static final String COL_4 = "DATE";
    public static final String COL_5 = "TIME";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_2 + " TEXT NOT NULL, "
                + COL_3 + " TEXT NOT NULL, "
                + COL_4 + "  TEXT NOT NULL, "
                + COL_5 + " TEXT NOT NULL " + " );";
        db.execSQL(SQL_CREATE_TABLE);//create table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String message, String date, String time){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, message);
        contentValues.put(COL_4, date);
        contentValues.put(COL_5, time);
        long result = db.insert(TABLE_NAME, null, contentValues);//returns -1 if failed
        if(result == -1)
            return false;
        else
            return true;

    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public boolean updateData(String id, String name, String message, String date, String time){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, message);
        contentValues.put(COL_4, date);
        contentValues.put(COL_5, time);

        db.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[] { id });
        return true;
    }

    public Integer deleteData(String id){//param String or int??
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME, COL_1 + " = ?", new String[] { id });
    }
}
