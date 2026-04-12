package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao.HttpJsonService;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Assignments;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Courses;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailCoursViewModel extends ViewModel {

    private final MutableLiveData<Courses> cours = new MutableLiveData<>();
    private final MutableLiveData<List<Assignments>> travaux = new MutableLiveData<>();
    private final MutableLiveData<List<Quizzes>> quizzes = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public LiveData<Courses> getCours() { return cours; }
    public LiveData<List<Assignments>> getTravaux() { return travaux; }
    public LiveData<List<Quizzes>> getQuizzes() { return quizzes; }
    public LiveData<String> getMessage() { return message; }

    public void charger(String courseId) {
        executorService.execute(() -> {
            try {
                HttpJsonService service = new HttpJsonService();
                cours.postValue(service.getCourseById(courseId));
                travaux.postValue(service.getAssignmentsByCourseId(courseId));
                quizzes.postValue(service.getQuizzesByCourseId(courseId));
            } catch (IOException e) {
                message.postValue("Erreur de chargement du cours");
            }
        });
    }
}
