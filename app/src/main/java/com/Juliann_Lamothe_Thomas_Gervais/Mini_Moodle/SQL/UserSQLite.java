package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL;

import android.provider.BaseColumns;

public class UserSQLite {

    public static final String DB_NAME = "Mini_Moodle.db";
    public static final int DB_VERSION = 3;
    public static final String TABLE_NAME = "Users";

    public class Colonnes{
        public static final String ID = BaseColumns._ID;
        public static final String EMAIL = "EMAIL";
        public static final String PASSWORD = "PASSWORD";
    }

}
