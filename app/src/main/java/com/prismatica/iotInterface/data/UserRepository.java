package com.prismatica.iotInterface.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserRepository {
    private final AppDatabaseHelper dbh;

    public UserRepository(Context ctx) {
        dbh = new AppDatabaseHelper(ctx);
    }

    public boolean createUser(String username, String password) {
        if (username == null || username.isEmpty()) return false;
        if (password == null || password.isEmpty()) return false;

        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(AppDatabaseHelper.C_USER_USERNAME, username);
        cv.put(AppDatabaseHelper.C_USER_PASSWORD, password);
        long id = db.insert(AppDatabaseHelper.T_USERS, null, cv);
        return id != -1;
    }

    public boolean checkLogin(String username, String password) {
        if (username == null || username.isEmpty()) return false;
        if (password == null || password.isEmpty()) return false;

        SQLiteDatabase db = dbh.getReadableDatabase();
        String[] cols = { AppDatabaseHelper.C_USER_USERNAME };
        String sel = AppDatabaseHelper.C_USER_USERNAME + "=? AND " + AppDatabaseHelper.C_USER_PASSWORD + "=?";
        String[] args = { username, password };
        Cursor c = db.query(AppDatabaseHelper.T_USERS, cols, sel, args, null, null, null);
        boolean ok = c.moveToFirst();
        c.close();
        return ok;
    }
}