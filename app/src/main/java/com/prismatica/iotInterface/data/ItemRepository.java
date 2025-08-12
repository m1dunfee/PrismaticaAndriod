package com.prismatica.iotInterface.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    private final AppDatabaseHelper dbh;

    public ItemRepository(Context ctx) {
        dbh = new AppDatabaseHelper(ctx);
    }

    public long create(String name, int qty, String notes) {
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(AppDatabaseHelper.C_ITEM_NAME, name);
        cv.put(AppDatabaseHelper.C_ITEM_QTY, qty);
        cv.put(AppDatabaseHelper.C_ITEM_NOTES, notes);
        return db.insert(AppDatabaseHelper.T_ITEMS, null, cv);
    }

    public List<Item> readAll() {
        SQLiteDatabase db = dbh.getReadableDatabase();
        String[] cols = {
                AppDatabaseHelper.C_ITEM_ID,
                AppDatabaseHelper.C_ITEM_NAME,
                AppDatabaseHelper.C_ITEM_QTY,
                AppDatabaseHelper.C_ITEM_NOTES
        };
        Cursor c = db.query(AppDatabaseHelper.T_ITEMS, cols, null, null, null, null,
                AppDatabaseHelper.C_ITEM_NAME + " ASC");
        List<Item> out = new ArrayList<>();
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            int qty = c.getInt(2);
            String notes = c.getString(3);
            out.add(new Item(id, name, qty, notes));
        }
        c.close();
        return out;
    }

    public boolean updateQty(int id, int newQty) {
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(AppDatabaseHelper.C_ITEM_QTY, newQty);
        int rows = db.update(AppDatabaseHelper.T_ITEMS, cv,
                AppDatabaseHelper.C_ITEM_ID + "=?", new String[]{ String.valueOf(id) });
        return rows > 0;
    }

    public boolean update(int id, String newName, int newQty, String newNotes) {
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(AppDatabaseHelper.C_ITEM_NAME, newName);
        cv.put(AppDatabaseHelper.C_ITEM_QTY, newQty);
        cv.put(AppDatabaseHelper.C_ITEM_NOTES, newNotes);
        int rows = db.update(AppDatabaseHelper.T_ITEMS, cv,
                AppDatabaseHelper.C_ITEM_ID + "=?", new String[]{ String.valueOf(id) });
        return rows > 0;
    }

    public boolean delete(int id) {
        SQLiteDatabase db = dbh.getWritableDatabase();
        int rows = db.delete(AppDatabaseHelper.T_ITEMS,
                AppDatabaseHelper.C_ITEM_ID + "=?", new String[]{ String.valueOf(id) });
        return rows > 0;
    }
}