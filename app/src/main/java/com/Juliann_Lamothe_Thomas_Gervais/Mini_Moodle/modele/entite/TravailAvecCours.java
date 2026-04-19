package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite;

public class TravailAvecCours {
    public final Assignments travail;
    public final String nomCours;
    public final String codeCours;

    public TravailAvecCours(Assignments travail, String nomCours, String codeCours) {
        this.travail = travail;
        this.nomCours = nomCours;
        this.codeCours = codeCours;
    }
}
