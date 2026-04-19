package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.ResultatQuiz;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Soumission;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Users;

public class DbUtil extends SQLiteOpenHelper {

    public DbUtil(@Nullable Context context) {
        super(context, UserSQLite.DB_NAME, null, UserSQLite.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)",
                UserSQLite.TABLE_NAME,
                UserSQLite.Colonnes.ID,
                UserSQLite.Colonnes.EMAIL,
                UserSQLite.Colonnes.PASSWORD
        ));

        db.execSQL(String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT UNIQUE, %s TEXT, %s TEXT)",
                SoumissionSQLite.TABLE_NAME,
                SoumissionSQLite.Colonnes.ID,
                SoumissionSQLite.Colonnes.ASSIGNMENT_ID,
                SoumissionSQLite.Colonnes.CONTENU,
                SoumissionSQLite.Colonnes.DATE_REMISE
        ));

        db.execSQL(String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT UNIQUE, %s INTEGER, %s INTEGER, %s TEXT)",
                ResultatQuizSQLite.TABLE_NAME,
                ResultatQuizSQLite.Colonnes.ID,
                ResultatQuizSQLite.Colonnes.QUIZ_ID,
                ResultatQuizSQLite.Colonnes.SCORE,
                ResultatQuizSQLite.Colonnes.TOTAL,
                ResultatQuizSQLite.Colonnes.DATE_COMPLETION
        ));

        db.execSQL(String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                ProfilSQLite.TABLE_NAME,
                ProfilSQLite.Colonnes.ID,
                ProfilSQLite.Colonnes.PRENOM,
                ProfilSQLite.Colonnes.NOM,
                ProfilSQLite.Colonnes.EMAIL,
                ProfilSQLite.Colonnes.TELEPHONE,
                ProfilSQLite.Colonnes.PHOTO_URL,
                ProfilSQLite.Colonnes.PASSWORD
        ));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    // ── Connexion ──────────────────────────────────────────────────────────

    public void sauvegarderConnexion(String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues donnees = new ContentValues();
        donnees.put(UserSQLite.Colonnes.EMAIL, email);
        donnees.put(UserSQLite.Colonnes.PASSWORD, password);
        db.insert(UserSQLite.TABLE_NAME, null, donnees);
        db.close();
    }

    public String[] getConnexion() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor curseur = db.query(UserSQLite.TABLE_NAME,
                new String[]{UserSQLite.Colonnes.EMAIL, UserSQLite.Colonnes.PASSWORD},
                null, null, null, null, null);
        if (curseur != null && curseur.moveToFirst()) {
            String email = curseur.getString(curseur.getColumnIndexOrThrow(UserSQLite.Colonnes.EMAIL));
            String password = curseur.getString(curseur.getColumnIndexOrThrow(UserSQLite.Colonnes.PASSWORD));
            curseur.close();
            db.close();
            return new String[]{email, password};
        }
        db.close();
        return null;
    }

    // ── Soumissions ────────────────────────────────────────────────────────

    public void sauvegarderSoumission(String assignmentId, String contenu, String dateRemise) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues donnees = new ContentValues();
        donnees.put(SoumissionSQLite.Colonnes.ASSIGNMENT_ID, assignmentId);
        donnees.put(SoumissionSQLite.Colonnes.CONTENU, contenu);
        donnees.put(SoumissionSQLite.Colonnes.DATE_REMISE, dateRemise);
        db.insertWithOnConflict(SoumissionSQLite.TABLE_NAME, null, donnees, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    // ── Résultats Quiz ─────────────────────────────────────────────────────

    public void sauvegarderResultatQuiz(String quizId, int score, int total, String dateCompletion) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues donnees = new ContentValues();
        donnees.put(ResultatQuizSQLite.Colonnes.QUIZ_ID, quizId);
        donnees.put(ResultatQuizSQLite.Colonnes.SCORE, score);
        donnees.put(ResultatQuizSQLite.Colonnes.TOTAL, total);
        donnees.put(ResultatQuizSQLite.Colonnes.DATE_COMPLETION, dateCompletion);
        db.insertWithOnConflict(ResultatQuizSQLite.TABLE_NAME, null, donnees, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ResultatQuiz getResultatQuiz(String quizId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor curseur = db.query(ResultatQuizSQLite.TABLE_NAME,
                new String[]{ResultatQuizSQLite.Colonnes.QUIZ_ID,
                        ResultatQuizSQLite.Colonnes.SCORE,
                        ResultatQuizSQLite.Colonnes.TOTAL,
                        ResultatQuizSQLite.Colonnes.DATE_COMPLETION},
                ResultatQuizSQLite.Colonnes.QUIZ_ID + " = ?",
                new String[]{quizId},
                null, null, null);
        if (curseur != null && curseur.moveToFirst()) {
            ResultatQuiz resultat = new ResultatQuiz(
                    curseur.getString(curseur.getColumnIndexOrThrow(ResultatQuizSQLite.Colonnes.QUIZ_ID)),
                    curseur.getInt(curseur.getColumnIndexOrThrow(ResultatQuizSQLite.Colonnes.SCORE)),
                    curseur.getInt(curseur.getColumnIndexOrThrow(ResultatQuizSQLite.Colonnes.TOTAL)),
                    curseur.getString(curseur.getColumnIndexOrThrow(ResultatQuizSQLite.Colonnes.DATE_COMPLETION))
            );
            curseur.close();
            db.close();
            return resultat;
        }
        db.close();
        return null;
    }

    // ── Profil utilisateur ─────────────────────────────────────────────────

    public void sauvegarderProfil(String prenom, String nom, String email,
                                   String telephone, String photoUrl, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues donnees = new ContentValues();
        donnees.put(ProfilSQLite.Colonnes.ID, 1); // une seule ligne : l'utilisateur connecté
        donnees.put(ProfilSQLite.Colonnes.PRENOM,    prenom    != null ? prenom    : "");
        donnees.put(ProfilSQLite.Colonnes.NOM,       nom       != null ? nom       : "");
        donnees.put(ProfilSQLite.Colonnes.EMAIL,     email     != null ? email     : "");
        donnees.put(ProfilSQLite.Colonnes.TELEPHONE, telephone != null ? telephone : "");
        donnees.put(ProfilSQLite.Colonnes.PHOTO_URL, photoUrl  != null ? photoUrl  : "");
        donnees.put(ProfilSQLite.Colonnes.PASSWORD,  password  != null ? password  : "");
        db.insertWithOnConflict(ProfilSQLite.TABLE_NAME, null, donnees, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public Users getProfil() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor curseur = db.query(ProfilSQLite.TABLE_NAME, null, null, null, null, null, null, "1");
        if (curseur != null && curseur.moveToFirst()) {
            Users user = new Users();
            user.setPrenom(curseur.getString(curseur.getColumnIndexOrThrow(ProfilSQLite.Colonnes.PRENOM)));
            user.setNom(curseur.getString(curseur.getColumnIndexOrThrow(ProfilSQLite.Colonnes.NOM)));
            user.setEmail(curseur.getString(curseur.getColumnIndexOrThrow(ProfilSQLite.Colonnes.EMAIL)));
            user.setTelephone(curseur.getString(curseur.getColumnIndexOrThrow(ProfilSQLite.Colonnes.TELEPHONE)));
            user.setPhotoUrl(curseur.getString(curseur.getColumnIndexOrThrow(ProfilSQLite.Colonnes.PHOTO_URL)));
            user.setPassword(curseur.getString(curseur.getColumnIndexOrThrow(ProfilSQLite.Colonnes.PASSWORD)));
            curseur.close();
            db.close();
            return user;
        }
        if (curseur != null) curseur.close();
        db.close();
        return null;
    }

    public Soumission getSoumission(String assignmentId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor curseur = db.query(SoumissionSQLite.TABLE_NAME,
                new String[]{SoumissionSQLite.Colonnes.ASSIGNMENT_ID,
                        SoumissionSQLite.Colonnes.CONTENU,
                        SoumissionSQLite.Colonnes.DATE_REMISE},
                SoumissionSQLite.Colonnes.ASSIGNMENT_ID + " = ?",
                new String[]{assignmentId},
                null, null, null);
        if (curseur != null && curseur.moveToFirst()) {
            Soumission soumission = new Soumission(
                    curseur.getString(curseur.getColumnIndexOrThrow(SoumissionSQLite.Colonnes.ASSIGNMENT_ID)),
                    curseur.getString(curseur.getColumnIndexOrThrow(SoumissionSQLite.Colonnes.CONTENU)),
                    curseur.getString(curseur.getColumnIndexOrThrow(SoumissionSQLite.Colonnes.DATE_REMISE))
            );
            curseur.close();
            db.close();
            return soumission;
        }
        db.close();
        return null;
    }
}
