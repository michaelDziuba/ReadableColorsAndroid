package com.michael.readablecolors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ReadableColors.db";  //database name
    private static final int DATABASE_VERSION = 1;  //database version

    private static final String ID = "_id";  //common column name for all tables

    /**
     * table name and its column names
     */
    private static final String SETTINGS = "settings";
    private static final String TEXT_COLOR_CODE = "text_color_code";
    private static final String TEXT_R = "text_r";
    private static final String TEXT_G = "text_g";
    private static final String TEXT_B = "text_b";
    private static final String INFO_COLOR_CODE = "info_color_code";
    private static final String CONTRAST_CODE = "contrast_code";
    private static final String SORT_CODE = "sort_code";
    private static final String SEEK_BAR_POSITION = "seek_bar_position";

    /**
     * create table statement
     */
    private static final String CREATE_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS " + SETTINGS + " (" +
                                                        ID + " INTEGER PRIMARY KEY, " +
                                                        TEXT_COLOR_CODE + " INTEGER, " +
                                                        TEXT_R + " INTEGER, " +
                                                        TEXT_G + " INTEGER, " +
                                                        TEXT_B + " INTEGER, " +
                                                        INFO_COLOR_CODE + " INTEGER, " +
                                                        CONTRAST_CODE + " INTEGER, " +
                                                        SORT_CODE + " INTEGER, " +
                                                        SEEK_BAR_POSITION + " INTEGER" +
                                                        ")";


    /**
     *
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Runs when database is created (database is created by this.getWritableDatabase();)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SETTINGS_TABLE);
    }


    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SETTINGS);
        onCreate(db);
    }

    public long insertSettings(Settings settings){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEXT_COLOR_CODE, settings.getTextColorCode());
        values.put(TEXT_R, settings.getTextR());
        values.put(TEXT_G, settings.getTextG());
        values.put(TEXT_B, settings.getTextB());
        values.put(INFO_COLOR_CODE, settings.getInfoColorCode());
        values.put(CONTRAST_CODE, settings.getContrastCode());
        values.put(SORT_CODE, settings.getSortCode());
        values.put(SEEK_BAR_POSITION, settings.getSeekbarPosition());

        long insertId = db.insert(SETTINGS, null, values);
        db.close();

        return insertId;
    }


    /**
     *
     * @param settings
     * @return id of the updated table row
     */
    public long updateSettingsTable(Settings settings){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEXT_COLOR_CODE, settings.getTextColorCode());
        values.put(TEXT_R, settings.getTextR());
        values.put(TEXT_G, settings.getTextG());
        values.put(TEXT_B, settings.getTextB());
        values.put(INFO_COLOR_CODE, settings.getInfoColorCode());
        values.put(CONTRAST_CODE, settings.getContrastCode());
        values.put(SORT_CODE, settings.getSortCode());
        values.put(SEEK_BAR_POSITION, settings.getSeekbarPosition());

        long updateId =  db.update(SETTINGS, values, ID + " = ?", new String[]{String.valueOf(settings.getId())});
        db.close();

        return updateId;
    }


    /**
     *
     * @return settings object populated with retrieved settings values
     */
    public Settings getSettings(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                SETTINGS,
                new String[]{ID, TEXT_COLOR_CODE, TEXT_R, TEXT_G, TEXT_B, INFO_COLOR_CODE, CONTRAST_CODE, SORT_CODE, SEEK_BAR_POSITION},
                null,
                null,
                null,
                null,
                null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        Settings settings = new Settings(
                                cursor.getInt(0),
                                cursor.getInt(1),
                                cursor.getInt(2),
                                cursor.getInt(3),
                                cursor.getInt(4),
                                cursor.getInt(5),
                                cursor.getInt(6),
                                cursor.getInt(7),
                                cursor.getInt(8));
        db.close();
        return settings;
    }



    /**
     *
     * @param context
     * @return
     */
    public boolean isDatabase(Context context) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    /**
     * Method closes the database connection, if it's open
     */
    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null && db.isOpen()){
            db.close();
        }
    }
}