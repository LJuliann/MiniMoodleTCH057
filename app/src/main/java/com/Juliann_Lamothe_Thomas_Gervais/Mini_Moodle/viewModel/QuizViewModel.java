package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL.DbUtil;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Questions;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.ResultatQuiz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizViewModel extends AndroidViewModel {

    private final MutableLiveData<Questions> questionCourante = new MutableLiveData<>();
    private final MutableLiveData<Integer> indexCourant = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> quizTermine = new MutableLiveData<>(false);
    private final MutableLiveData<ResultatQuiz> resultat = new MutableLiveData<>();

    private Quizzes quiz;
    private int[] reponsesUtilisateur;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public QuizViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Questions> getQuestionCourante() { return questionCourante; }
    public LiveData<Integer> getIndexCourant() { return indexCourant; }
    public LiveData<Boolean> getQuizTermine() { return quizTermine; }
    public LiveData<ResultatQuiz> getResultat() { return resultat; }

    public void initialiser(Quizzes quiz) {
        this.quiz = quiz;
        List<Questions> questions = quiz.getQuestions();
        if (questions == null || questions.isEmpty()) return;
        reponsesUtilisateur = new int[questions.size()];
        for (int i = 0; i < reponsesUtilisateur.length; i++) reponsesUtilisateur[i] = -1;
        indexCourant.setValue(0);
        questionCourante.setValue(questions.get(0));
    }

    public int getNbQuestions() {
        return quiz != null && quiz.getQuestions() != null ? quiz.getQuestions().size() : 0;
    }

    public void enregistrerReponse(int index, int reponse) {
        if (reponsesUtilisateur != null && index < reponsesUtilisateur.length) {
            reponsesUtilisateur[index] = reponse;
        }
    }

    public void questionSuivante() {
        List<Questions> questions = quiz.getQuestions();
        int index = indexCourant.getValue() != null ? indexCourant.getValue() : 0;
        int prochainIndex = index + 1;
        if (prochainIndex < questions.size()) {
            indexCourant.setValue(prochainIndex);
            questionCourante.setValue(questions.get(prochainIndex));
        }
    }

    public void terminer() {
        List<Questions> questions = quiz.getQuestions();
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (reponsesUtilisateur[i] == questions.get(i).getCorrectOption()) {
                score++;
            }
        }
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        int scoreFinal = score;

        executorService.execute(() -> {
            DbUtil db = new DbUtil(getApplication());
            db.sauvegarderResultatQuiz(quiz.getId(), scoreFinal, questions.size(), date);
            db.close();
            resultat.postValue(new ResultatQuiz(quiz.getId(), scoreFinal, questions.size(), date));
            quizTermine.postValue(true);
        });
    }
}
