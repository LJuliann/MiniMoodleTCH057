package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite;

public class QuizAvecStatut {
    public final Quizzes quiz;
    public final ResultatQuiz resultat; // null si non complété

    public QuizAvecStatut(Quizzes quiz, ResultatQuiz resultat) {
        this.quiz = quiz;
        this.resultat = resultat;
    }
}
