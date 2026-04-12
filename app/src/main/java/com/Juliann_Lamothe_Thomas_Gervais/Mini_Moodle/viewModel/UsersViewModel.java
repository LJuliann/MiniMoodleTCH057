package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao.UsersDao;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Users;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsersViewModel extends ViewModel {

    private final MutableLiveData<List<Users>> users = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<Boolean> success = new MutableLiveData<>();
    private final MutableLiveData<Users> connectedUser = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public LiveData<List<Users>> getUsers() {
        return users;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public LiveData<Boolean> getSuccess() {
        return success;
    }

    public LiveData<Users> getConnectedUser() {
        return connectedUser;
    }

    public void connexion(String courriel, String password) {
        if (courriel.isEmpty() || password.isEmpty()) {
            message.postValue("Veuillez remplir tous les champs");
            return;
        }
        executorService.execute(() -> {
            try {
                Users user = UsersDao.connexion(courriel, password);
                if (user != null) {
                    connectedUser.postValue(user);
                    message.postValue("Connexion réussie");
                    success.postValue(true);
                } else {
                    message.postValue("Courriel ou mot de passe incorrect");
                    success.postValue(false);
                }
            } catch (IOException e) {
                message.postValue("Erreur de connexion au serveur");
                success.postValue(false);
            }
        });
    }

    public void enregistereUser(Users user) {
        if (user.getNom().isEmpty() || user.getPrenom().isEmpty() || user.getTelephone().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            message.postValue("Veuillez remplir tous les champs");
            return;
        }

        executorService.execute(() -> {
            try{
                boolean reussite = UsersDao.enregistrerUser(user);
                if (reussite){
                    message.postValue("Inscription réussie");
                    success.postValue(true);
                } else {
                    message.postValue("Inscription échouée");
                    success.postValue(false);
                }
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
        });

    }


}
