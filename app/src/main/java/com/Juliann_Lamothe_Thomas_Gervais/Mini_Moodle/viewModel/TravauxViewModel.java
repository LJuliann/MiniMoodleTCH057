package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL.DbUtil;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Assignments;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Soumission;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TravauxViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Assignments>> travaux = new MutableLiveData<>();
    private final MutableLiveData<Soumission> soumissionCourante = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TravauxViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Assignments>> getTravaux() { return travaux; }
    public LiveData<Soumission> getSoumissionCourante() { return soumissionCourante; }
    public LiveData<String> getMessage() { return message; }

    public void chargerAvecStatuts(List<Assignments> liste) {
        executorService.execute(() -> {
            DbUtil db = new DbUtil(getApplication());
            for (Assignments assignment : liste) {
                Soumission soumission = db.getSoumission(assignment.getId());
                assignment.setStatutCalcule(calculerStatut(assignment, soumission));
            }
            db.close();
            travaux.postValue(liste);
        });
    }

    public void chargerSoumission(String assignmentId) {
        executorService.execute(() -> {
            DbUtil db = new DbUtil(getApplication());
            soumissionCourante.postValue(db.getSoumission(assignmentId));
            db.close();
        });
    }

    public void soumettre(String assignmentId, String contenu) {
        executorService.execute(() -> {
            String dateRemise = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
            DbUtil db = new DbUtil(getApplication());
            db.sauvegarderSoumission(assignmentId, contenu, dateRemise);
            db.close();
            soumissionCourante.postValue(new Soumission(assignmentId, contenu, dateRemise));
            message.postValue("Travail remis le " + dateRemise);
        });
    }

    public static String calculerStatut(Assignments assignment, Soumission soumission) {
        if (soumission != null) {
            if (assignment.getGrade() != null) return "Corrigé";
            return "Remis";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date dateLimite = sdf.parse(assignment.getDueDate());
            if (dateLimite != null && dateLimite.before(new Date())) return "En retard";
        } catch (ParseException e) {
            // date invalide, on retourne À faire
        }
        return "À faire";
    }
}
