package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL;

import android.provider.BaseColumns;

public class ProfilSQLite {

    public static final String TABLE_NAME = "Profil";

    public static class Colonnes {
        public static final String ID       = BaseColumns._ID;
        public static final String PRENOM   = "PRENOM";
        public static final String NOM      = "NOM";
        public static final String EMAIL    = "EMAIL";
        public static final String TELEPHONE = "TELEPHONE";
        public static final String PHOTO_URL = "PHOTO_URL";
        public static final String PASSWORD = "PASSWORD";
    }
}
