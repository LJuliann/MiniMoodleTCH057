package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SessionDao {

    public static void sauvegarderSession(Context context, String email, String password) {
        SQLiteDatabase db = new DbUtil(context).getWritableDatabase();
        db.delete(UserSQLite.TABLE_NAME, null, null);
        ContentValues values = new ContentValues();
        values.put(UserSQLite.Colonnes.EMAIL, email);
        values.put(UserSQLite.Colonnes.PASSWORD, password);
        db.insert(UserSQLite.TABLE_NAME, null, values);
        db.close();
    }

    public static String[] getSession(Context context) {
        SQLiteDatabase db = new DbUtil(context).getReadableDatabase();
        Cursor cursor = db.query(UserSQLite.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow(UserSQLite.Colonnes.EMAIL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(UserSQLite.Colonnes.PASSWORD));
            cursor.close();
            db.close();
            return new String[]{email, password};
        }
        cursor.close();
        db.close();
        return null;
    }

    public static void supprimerSession(Context context) {
        SQLiteDatabase db = new DbUtil(context).getWritableDatabase();
        db.delete(UserSQLite.TABLE_NAME, null, null);
        db.close();
    }

}
