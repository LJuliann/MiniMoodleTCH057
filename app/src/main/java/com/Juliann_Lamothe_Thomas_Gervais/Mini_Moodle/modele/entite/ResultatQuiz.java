package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite;

public class ResultatQuiz {

    private String quizId;
    private int score;
    private int total;
    private String dateCompletion;

    public ResultatQuiz(String quizId, int score, int total, String dateCompletion) {
        this.quizId = quizId;
        this.score = score;
        this.total = total;
        this.dateCompletion = dateCompletion;
    }

    public String getQuizId() { return quizId; }
    public int getScore() { return score; }
    public int getTotal() { return total; }
    public String getDateCompletion() { return dateCompletion; }
}
