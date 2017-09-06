package com.example.android.pets.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by adityav on 8/30/17.
 */

public class PetDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Pets.db";

    public static final String SQL_CREATE_ENTERIES =
            "CREATE TABLE " + PetEntry.TABLE_NAME + " (" +
                    PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +
                    PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, " +
                    PetEntry.COLUMN_PET_BREED + " TEXT, " +
                    PetEntry.COLUMN_PET_GENDER + " Integer NOT NULL," +
                    PetEntry.COLUMN_PET_WEIGHT + " Integer NOT NULL DEFAULT 0)";

    public static final String SQL_DELET_ENTERIES =
            "DROP TABLE IF EXISTS " + PetEntry.TABLE_NAME ;


    public PetDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTERIES);
    }

    public long insert(Uri uri, ContentValues contentValues){
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(PetEntry.TABLE_NAME,null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELET_ENTERIES);
        onCreate(db);
    }
}
