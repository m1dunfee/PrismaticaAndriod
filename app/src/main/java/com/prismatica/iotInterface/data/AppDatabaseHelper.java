package com.prismatica.iotInterface.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabaseHelper extends SQLiteOpenHelper {
    // Variables
    private static final String DB_NAME = "prismatica.db";
    private static final int DB_VERSION = 1;

    // Users table
    public static final String T_USERS = "users";
    public static final String C_USER_USERNAME = "username";
    public static final String C_USER_PASSWORD = "password";

    // Items table (for inventory or events or weights; keep generic)
    public static final String T_ITEMS = "items";
    public static final String C_ITEM_ID = "id";
    public static final String C_ITEM_NAME = "name";
    public static final String C_ITEM_QTY = "qty"; // repurpose as needed

    public AppDatabaseHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_USERS + " (" +
                C_USER_USERNAME + " TEXT PRIMARY KEY, " +
                C_USER_PASSWORD + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + T_ITEMS + " (" +
                C_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                C_ITEM_NAME + " TEXT NOT NULL, " +
                C_ITEM_QTY + " INTEGER NOT NULL DEFAULT 0)");
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + T_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + T_USERS);
        onCreate(db);
    }
}