package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao;

import android.util.Log;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Assignments;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Courses;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class HttpJsonService {

    private static String URL_POINT_ENTRER = "http://10.0.2.2:3000";
   // private static String URL_POINT_ENTRER = "http://10.0.0.251:3000";


    //Recuperation des users
    public List<Users> getUsers() throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL_POINT_ENTRER)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        ResponseBody responseBody = response.body();
        String jsonStr = responseBody.string();
        List<Users> users = null;

        Log.d("HttpJsonService",jsonStr);

        if(jsonStr.length() > 0){
            ObjectMapper mapper = new ObjectMapper();
            try{
                users = Arrays.asList(mapper.readValue(jsonStr, Users[].class));
            }catch (JsonProcessingException e){
                throw new RuntimeException(e);
            }
            return users;
        }
        return null;
    }


    //Recupere la liste des cours
    public List<Courses> getCourses() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL_POINT_ENTRER + "/courses")
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String jsonStr = response.body().string();
        try {
            return Arrays.asList(new ObjectMapper().readValue(jsonStr, Courses[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //Passe un utilisateur a la methode et envois un post pour le creer.
    public boolean enregistrerUser(Users user) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        JSONObject obj = new JSONObject();
        obj.put("username", user.getUsername());
        obj.put("email", user.getEmail());
        obj.put("password", user.getPassword());
        obj.put("nom", user.getNom());
        obj.put("prenom", user.getPrenom());
        obj.put("telephone", user.getTelephone());
        obj.put("photoUrl", user.getPhotoUrl());

        RequestBody corpsRequete = RequestBody.create(String.valueOf(obj), JSON);
        String url = URL_POINT_ENTRER + "/users";

        Request request = new Request.Builder()
                .url(url)
                .post(corpsRequete)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.code() == 201;
    }

    public Courses getCourseById(String id) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        okhttp3.HttpUrl url = okhttp3.HttpUrl.parse(URL_POINT_ENTRER + "/courses")
                .newBuilder()
                .addQueryParameter("id", id)
                .build();
        Request request = new Request.Builder().url(url).get().build();
        String body = okHttpClient.newCall(request).execute().body().string();
        Courses[] result = new ObjectMapper().readValue(body, Courses[].class);
        return result.length > 0 ? result[0] : null;
    }

    public List<Assignments> getAssignmentsByCourseId(String courseId) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        okhttp3.HttpUrl url = okhttp3.HttpUrl.parse(URL_POINT_ENTRER + "/assignments")
                .newBuilder()
                .addQueryParameter("courseId", courseId)
                .build();
        Request request = new Request.Builder().url(url).get().build();
        String body = okHttpClient.newCall(request).execute().body().string();
        return Arrays.asList(new ObjectMapper().readValue(body, Assignments[].class));
    }

    public List<Quizzes> getQuizzesByCourseId(String courseId) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        okhttp3.HttpUrl url = okhttp3.HttpUrl.parse(URL_POINT_ENTRER + "/quizzes")
                .newBuilder()
                .addQueryParameter("courseId", courseId)
                .build();
        Request request = new Request.Builder().url(url).get().build();
        String body = okHttpClient.newCall(request).execute().body().string();
        return Arrays.asList(new ObjectMapper().readValue(body, Quizzes[].class));
    }

    public List<Assignments> getAssignments() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL_POINT_ENTRER + "/assignments")
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String jsonStr = response.body().string();
        try {
            return Arrays.asList(new ObjectMapper().readValue(jsonStr, Assignments[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Quizzes> getQuizzes() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL_POINT_ENTRER + "/quizzes")
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String jsonStr = response.body().string();
        try {
            return Arrays.asList(new ObjectMapper().readValue(jsonStr, Quizzes[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Users connexion(String courriel, String password) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        okhttp3.HttpUrl url = okhttp3.HttpUrl.parse(URL_POINT_ENTRER + "/users")
                .newBuilder()
                .addQueryParameter("email", courriel)
                .addQueryParameter("password", password)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String body = response.body().string();
        if (body.equals("[]")) return null;
        Users[] users = new ObjectMapper().readValue(body, Users[].class);
        return users.length > 0 ? users[0] : null;
    }




}
