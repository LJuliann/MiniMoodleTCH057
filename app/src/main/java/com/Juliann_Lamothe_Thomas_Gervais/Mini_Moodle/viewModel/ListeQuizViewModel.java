package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL.DbUtil;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao.HttpJsonService;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.QuizAvecStatut;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.ResultatQuiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListeQuizViewModel extends AndroidViewModel {

    private final MutableLiveData<List<QuizAvecStatut>> quiz = new MutableLiveData<>();
    private final MutableLiveData<Boolean> chargement = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ListeQuizViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<QuizAvecStatut>> getQuiz() { return quiz; }
    public LiveData<Boolean> getChargement() { return chargement; }

    public void chargerQuiz(List<String> enrolledIds) {
        chargement.postValue(true);
        executor.execute(() -> {
            try {
                HttpJsonService service = new HttpJsonService();
                List<Quizzes> tous = service.getQuizzes();
                List<QuizAvecStatut> filtres = new ArrayList<>();
                DbUtil db = new DbUtil(getApplication());
                for (Quizzes q : tous) {
                    if (enrolledIds.contains(q.getCourseId())) {
                        ResultatQuiz res = db.getResultatQuiz(q.getId());
                        filtres.add(new QuizAvecStatut(q, res));
                    }
                }
                db.close();
                quiz.postValue(filtres);
            } catch (IOException e) {
                quiz.postValue(new ArrayList<>());
            } finally {
                chargement.postValue(false);
            }
        });
    }
}
