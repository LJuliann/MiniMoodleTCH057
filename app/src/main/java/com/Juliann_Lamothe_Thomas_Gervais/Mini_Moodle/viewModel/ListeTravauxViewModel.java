package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL.DbUtil;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao.HttpJsonService;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Assignments;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Courses;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Soumission;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.TravailAvecCours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListeTravauxViewModel extends AndroidViewModel {

    private final MutableLiveData<List<TravailAvecCours>> travaux = new MutableLiveData<>();
    private final MutableLiveData<Boolean> chargement = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ListeTravauxViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<TravailAvecCours>> getTravaux() { return travaux; }
    public LiveData<Boolean> getChargement() { return chargement; }

    public void chargerTravaux(List<String> enrolledIds) {
        chargement.postValue(true);
        executor.execute(() -> {
            try {
                HttpJsonService service = new HttpJsonService();

                Map<String, Courses> coursParId = new HashMap<>();
                for (Courses c : service.getCourses()) {
                    coursParId.put(c.getId(), c);
                }

                DbUtil db = new DbUtil(getApplication());
                List<TravailAvecCours> filtres = new ArrayList<>();
                for (Assignments a : service.getAssignments()) {
                    if (enrolledIds.contains(a.getCourseId())) {
                        Soumission s = db.getSoumission(a.getId());
                        a.setStatutCalcule(TravauxViewModel.calculerStatut(a, s));
                        Courses cours = coursParId.get(a.getCourseId());
                        filtres.add(new TravailAvecCours(a,
                                cours != null ? cours.getTitle() : "",
                                cours != null ? cours.getCode()  : ""));
                    }
                }
                db.close();
                travaux.postValue(filtres);
            } catch (IOException e) {
                travaux.postValue(new ArrayList<>());
            } finally {
                chargement.postValue(false);
            }
        });
    }
}
