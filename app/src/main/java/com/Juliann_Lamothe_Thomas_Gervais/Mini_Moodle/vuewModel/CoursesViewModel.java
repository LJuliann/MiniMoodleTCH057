package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vuewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao.CoursesDao;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Courses;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CoursesViewModel extends ViewModel {

    private final MutableLiveData<List<Courses>> courses = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public LiveData<List<Courses>> getCourses() {
        return courses;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void chargerCourses() {
        executorService.execute(() -> {
            try {
                List<Courses> liste = CoursesDao.getCourses();
                courses.postValue(liste);
            } catch (IOException e) {
                message.postValue("Erreur de chargement des cours");
            }
        });
    }

}
