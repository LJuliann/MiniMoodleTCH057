package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite;

public class Soumission {

    private String assignmentId, contenu, dateRemise;

    public Soumission(String assignmentId, String contenu, String dateRemise) {
        this.assignmentId = assignmentId;
        this.contenu = contenu;
        this.dateRemise = dateRemise;
    }

    public String getAssignmentId() { return assignmentId; }
    public String getContenu() { return contenu; }
    public String getDateRemise() { return dateRemise; }
}
