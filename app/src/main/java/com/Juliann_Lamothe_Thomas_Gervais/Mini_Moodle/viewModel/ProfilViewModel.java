package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL.DbUtil;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao.UsersDao;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Users;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfilViewModel extends AndroidViewModel {

    private final MutableLiveData<Users> profil = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ProfilViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Users> getProfil() {
        return profil;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void chargerProfil() {
        executor.execute(() -> {
            DbUtil db = new DbUtil(getApplication());
            Users user = db.getProfil();
            db.close();
            profil.postValue(user);
        });
    }

    public void sauvegarderProfil(String prenom, String nom, String courriel,
                                   String telephone, String photoUrl, String motDePasse) {
        executor.execute(() -> {
            DbUtil db = new DbUtil(getApplication());

            String mdpFinal = motDePasse;
            if (mdpFinal.isEmpty()) {
                Users current = db.getProfil();
                if (current != null && current.getPassword() != null) {
                    mdpFinal = current.getPassword();
                }
            }

            db.sauvegarderProfil(prenom, nom, courriel, telephone, photoUrl, mdpFinal);
            db.close();

            final String mdpPourServeur = mdpFinal;
            try {
                Users serveurUser = UsersDao.getUserByEmail(courriel);
                if (serveurUser != null && serveurUser.getId() != null) {
                    UsersDao.mettreAJourProfil(serveurUser.getId(), prenom, nom,
                            telephone, photoUrl, mdpPourServeur);
                }
                message.postValue("Profil mis à jour.");
            } catch (Exception e) {
                android.util.Log.e("ProfilViewModel", "Erreur mise à jour serveur", e);
                message.postValue("Profil sauvegardé localement (serveur indisponible).");
            }
        });
    }
}
