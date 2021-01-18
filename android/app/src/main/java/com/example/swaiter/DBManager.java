package com.example.swaiter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    static final String DB_MENU = "Menu.db";
    static final String TABLE_SELMENU = "Selmenus";
    static final int DB_VERSION = 1;

    Context mContext = null;

    private static DBManager dbManager = null;
    private SQLiteDatabase database = null;

    public static DBManager getInstance(Context context) {
        if (dbManager == null) {
            dbManager = new DBManager(context);
        }
        return dbManager;
    }

    private DBManager (Context context) {
        mContext = context;
        database = context.openOrCreateDatabase(DB_MENU, context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SELMENU + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, imgurl TEXT, title TEXT, option TEXT, price TEXT);");
    }

    public long insert(ContentValues addRowValue) {
        return database.insert(TABLE_SELMENU, null, addRowValue);
    }

    public Cursor query (String[] colums, String selection, String[] selectionArgs, String groupBy, String having, String orderby) {
        return database.query(TABLE_SELMENU, colums, selection, selectionArgs, groupBy, having, orderby);
    }

    public int delete(String whereClause, String[] whereArgs) {
        return database.delete(TABLE_SELMENU, whereClause, whereArgs);
    }

    public int update(ContentValues updateRowValue, String whereClause, String[] whereArgs) {
        return database.update(TABLE_SELMENU, updateRowValue, whereClause, whereArgs);
    }
}
