package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbUtil extends SQLiteOpenHelper {
    public DbUtil(@Nullable Context context) {
        super(context, UserSQLite.DB_NAME, null, UserSQLite.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String requeteCreation = String.format(
                "create table %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s text, %s text)",
                UserSQLite.TABLE_NAME,
                UserSQLite.Colonnes.ID,
                UserSQLite.Colonnes.EMAIL,
                UserSQLite.Colonnes.PASSWORD
        );
        db.execSQL(requeteCreation);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String requeteModification = String.format(
                "alter table %s add column %s text",
                UserSQLite.TABLE_NAME,
                UserSQLite.Colonnes.PASSWORD
        );
        db.execSQL(requeteModification);
    }

}
