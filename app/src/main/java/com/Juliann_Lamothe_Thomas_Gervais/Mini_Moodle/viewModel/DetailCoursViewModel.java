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
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.ResultatQuiz;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailCoursViewModel extends AndroidViewModel {

    private final MutableLiveData<Courses> cours = new MutableLiveData<>();
    private final MutableLiveData<List<Assignments>> travaux = new MutableLiveData<>();
    private final MutableLiveData<List<Quizzes>> quizzes = new MutableLiveData<>();
    private final MutableLiveData<Map<String, ResultatQuiz>> resultatsQuiz = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<Boolean> chargement = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public DetailCoursViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Courses> getCours() { return cours; }
    public LiveData<List<Assignments>> getTravaux() { return travaux; }
    public LiveData<List<Quizzes>> getQuizzes() { return quizzes; }
    public LiveData<Map<String, ResultatQuiz>> getResultatsQuiz() { return resultatsQuiz; }
    public LiveData<String> getMessage() { return message; }
    public LiveData<Boolean> getChargement() { return chargement; }

    public void charger(String courseId) {
        chargement.postValue(true);
        executorService.execute(() -> {
            try {
                HttpJsonService service = new HttpJsonService();
                cours.postValue(service.getCourseById(courseId));
                travaux.postValue(service.getAssignmentsByCourseId(courseId));
                quizzes.postValue(service.getQuizzesByCourseId(courseId));
            } catch (IOException e) {
                message.postValue("Erreur de chargement du cours");
            } finally {
                chargement.postValue(false);
            }
        });
    }

    public void chargerResultatsQuiz(List<Quizzes> liste) {
        executorService.execute(() -> {
            DbUtil db = new DbUtil(getApplication());
            Map<String, ResultatQuiz> resultats = new HashMap<>();
            for (Quizzes q : liste) {
                ResultatQuiz r = db.getResultatQuiz(q.getId());
                if (r != null) resultats.put(q.getId(), r);
            }
            db.close();
            resultatsQuiz.postValue(resultats);
        });
    }
}
