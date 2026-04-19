package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao.HttpJsonService;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.AnnonceItem;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Courses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListeAnnoncesViewModel extends ViewModel {

    private final MutableLiveData<List<AnnonceItem>> annonces = new MutableLiveData<>();
    private final MutableLiveData<Boolean> chargement = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LiveData<List<AnnonceItem>> getAnnonces() { return annonces; }
    public LiveData<Boolean> getChargement() { return chargement; }

    public void chargerAnnonces(List<String> enrolledIds) {
        chargement.postValue(true);
        executor.execute(() -> {
            try {
                HttpJsonService service = new HttpJsonService();
                List<AnnonceItem> items = new ArrayList<>();
                for (Courses c : service.getCourses()) {
                    if (enrolledIds.contains(c.getId()) && c.getAnnonces() != null) {
                        for (String texte : c.getAnnonces()) {
                            items.add(new AnnonceItem(c, texte));
                        }
                    }
                }
                annonces.postValue(items);
            } catch (IOException e) {
                annonces.postValue(new ArrayList<>());
            } finally {
                chargement.postValue(false);
            }
        });
    }
}
