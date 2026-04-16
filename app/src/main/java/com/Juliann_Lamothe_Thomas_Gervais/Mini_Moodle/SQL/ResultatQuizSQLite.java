package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL;

import android.provider.BaseColumns;

public class ResultatQuizSQLite {

    public static final String TABLE_NAME = "ResultatsQuiz";

    public class Colonnes {
        public static final String ID = BaseColumns._ID;
        public static final String QUIZ_ID = "QUIZ_ID";
        public static final String SCORE = "SCORE";
        public static final String TOTAL = "TOTAL";
        public static final String DATE_COMPLETION = "DATE_COMPLETION";
    }
}
