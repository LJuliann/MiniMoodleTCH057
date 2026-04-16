package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao.CoursesDao;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Courses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CoursesViewModel extends ViewModel {

    private final MutableLiveData<List<Courses>> coursesFiltrees = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<Boolean> chargement = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Courses> tousLesCours = new ArrayList<>();
    private int filtreSpinner = 0;
    private String rechercheQuery = "";

    public LiveData<List<Courses>> getCourses() {
        return coursesFiltrees;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public LiveData<Boolean> getChargement() {
        return chargement;
    }

    public void chargerCourses(List<String> enrolledIds) {
        chargement.postValue(true);
        executorService.execute(() -> {
            try {
                List<Courses> tous = CoursesDao.getCourses();
                List<Courses> coursInscrits = new ArrayList<>();
                for (Courses cours : tous) {
                    if (enrolledIds.contains(cours.getId())) {
                        coursInscrits.add(cours);
                    }
                }
                tousLesCours = coursInscrits;
                appliquerFiltres();
            } catch (IOException e) {
                message.postValue("Erreur de chargement des cours");
            } finally {
                chargement.postValue(false);
            }
        });
    }

    // 0 = Tous, 1 = Actifs, 2 = Terminés
    public void filtrer(int position) {
        filtreSpinner = position;
        appliquerFiltres();
    }

    public void rechercher(String query) {
        rechercheQuery = query.toLowerCase().trim();
        appliquerFiltres();
    }

    private void appliquerFiltres() {
        List<Courses> résultat = new ArrayList<>();
        for (Courses cours : tousLesCours) {
            boolean actif = isSessionActive(cours.getSession());
            boolean passeFiltreSpinner = filtreSpinner == 0
                    || (filtreSpinner == 1 && actif)
                    || (filtreSpinner == 2 && !actif);
            boolean passeRecherche = rechercheQuery.isEmpty()
                    || cours.getTitle().toLowerCase().contains(rechercheQuery)
                    || cours.getTeacher().toLowerCase().contains(rechercheQuery);
            if (passeFiltreSpinner && passeRecherche) {
                résultat.add(cours);
            }
        }
        coursesFiltrees.postValue(résultat);
    }

    private boolean isSessionActive(String session) {
        if (session == null) return false;
        int anneeActuelle = Calendar.getInstance().get(Calendar.YEAR);
        int moisActuel = Calendar.getInstance().get(Calendar.MONTH) + 1;

        String[] parties = session.split(" ");
        if (parties.length < 2) return false;
        String saison = parties[0];
        int annee;
        try {
            annee = Integer.parseInt(parties[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (annee > anneeActuelle) return true;
        if (annee < anneeActuelle) return false;

        if (saison.equalsIgnoreCase("Hiver") && moisActuel <= 4) return true;
        if (saison.equalsIgnoreCase("Été") && moisActuel <= 8) return true;
        if (saison.equalsIgnoreCase("Automne") && moisActuel <= 12) return true;
        return false;
    }

}
