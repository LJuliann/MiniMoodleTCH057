package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao.HttpJsonService;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListeQuizViewModel extends ViewModel {

    private final MutableLiveData<List<Quizzes>> quiz = new MutableLiveData<>();
    private final MutableLiveData<Boolean> chargement = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LiveData<List<Quizzes>> getQuiz() { return quiz; }
    public LiveData<Boolean> getChargement() { return chargement; }

    public void chargerQuiz(List<String> enrolledIds) {
        chargement.postValue(true);
        executor.execute(() -> {
            try {
                HttpJsonService service = new HttpJsonService();
                List<Quizzes> tous = service.getQuizzes();
                List<Quizzes> filtres = new ArrayList<>();
                for (Quizzes q : tous) {
                    if (enrolledIds.contains(q.getCourseId())) filtres.add(q);
                }
                quiz.postValue(filtres);
            } catch (IOException e) {
                quiz.postValue(new ArrayList<>());
            } finally {
                chargement.postValue(false);
            }
        });
    }
}
