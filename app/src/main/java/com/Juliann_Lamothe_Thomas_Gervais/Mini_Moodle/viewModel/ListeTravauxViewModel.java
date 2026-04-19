package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao.HttpJsonService;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Assignments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListeTravauxViewModel extends ViewModel {

    private final MutableLiveData<List<Assignments>> travaux = new MutableLiveData<>();
    private final MutableLiveData<Boolean> chargement = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LiveData<List<Assignments>> getTravaux() { return travaux; }
    public LiveData<Boolean> getChargement() { return chargement; }

    public void chargerTravaux(List<String> enrolledIds) {
        chargement.postValue(true);
        executor.execute(() -> {
            try {
                HttpJsonService service = new HttpJsonService();
                List<Assignments> tous = service.getAssignments();
                List<Assignments> filtres = new ArrayList<>();
                for (Assignments a : tous) {
                    if (enrolledIds.contains(a.getCourseId())) filtres.add(a);
                }
                travaux.postValue(filtres);
            } catch (IOException e) {
                travaux.postValue(new ArrayList<>());
            } finally {
                chargement.postValue(false);
            }
        });
    }
}
