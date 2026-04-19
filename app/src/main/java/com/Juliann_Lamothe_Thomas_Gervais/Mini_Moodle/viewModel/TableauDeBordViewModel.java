package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL.DbUtil;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao.HttpJsonService;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Users;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Assignments;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Courses;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TableauDeBordViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Courses>> courses = new MutableLiveData<>();
    private final MutableLiveData<List<Assignments>> assignments = new MutableLiveData<>();
    private final MutableLiveData<List<Quizzes>> quizzes = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<String> prenom = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TableauDeBordViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Courses>> getCourses() { return courses; }
    public LiveData<List<Assignments>> getAssignments() { return assignments; }
    public LiveData<List<Quizzes>> getQuizzes() { return quizzes; }
    public LiveData<String> getMessage() { return message; }
    public LiveData<String> getPrenom() { return prenom; }

    public void chargerPrenom() {
        executorService.execute(() -> {
            DbUtil db = new DbUtil(getApplication());
            Users user = db.getProfil();
            db.close();
            if (user != null && user.getPrenom() != null) {
                prenom.postValue(user.getPrenom());
            }
        });
    }

    public void chargerDonnees(List<String> enrolledIds) {
        executorService.execute(() -> {
            try {
                HttpJsonService service = new HttpJsonService();

                List<Courses> coursInscrits = new ArrayList<>();
                for (Courses c : service.getCourses()) {
                    if (enrolledIds.contains(c.getId())) coursInscrits.add(c);
                }

                List<Assignments> assignmentsInscrits = new ArrayList<>();
                for (Assignments a : service.getAssignments()) {
                    if (enrolledIds.contains(a.getCourseId())) assignmentsInscrits.add(a);
                }

                List<Quizzes> quizzesInscrits = new ArrayList<>();
                for (Quizzes q : service.getQuizzes()) {
                    if (enrolledIds.contains(q.getCourseId())) quizzesInscrits.add(q);
                }

                courses.postValue(coursInscrits);
                assignments.postValue(assignmentsInscrits);
                quizzes.postValue(quizzesInscrits);
            } catch (IOException e) {
                message.postValue("Erreur de chargement");
            }
        });
    }
}
