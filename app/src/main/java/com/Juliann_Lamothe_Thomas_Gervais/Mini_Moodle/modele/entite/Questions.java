package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite;

public class Questions {

    private String id, question;
    private String[] options;
    private int correctOption;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String[] getOptions() { return options; }
    public void setOptions(String[] options) { this.options = options; }

    public int getCorrectOption() { return correctOption; }
    public void setCorrectOption(int correctOption) { this.correctOption = correctOption; }
}
