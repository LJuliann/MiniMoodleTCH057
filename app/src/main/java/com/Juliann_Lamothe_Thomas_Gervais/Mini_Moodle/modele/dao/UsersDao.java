package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Users;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class UsersDao {

    public static Users connexion(String courriel, String password) throws IOException {
        return new HttpJsonService().connexion(courriel, password);
    }

    public static boolean enregistrerUser(Users user) throws IOException, JSONException{
        return new HttpJsonService().enregistrerUser(user);
    }



}
