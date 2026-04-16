package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL;

import android.provider.BaseColumns;

public class SoumissionSQLite {

    public static final String TABLE_NAME = "Soumissions";

    public class Colonnes {
        public static final String ID = BaseColumns._ID;
        public static final String ASSIGNMENT_ID = "ASSIGNMENT_ID";
        public static final String CONTENU = "CONTENU";
        public static final String DATE_REMISE = "DATE_REMISE";
    }
}
