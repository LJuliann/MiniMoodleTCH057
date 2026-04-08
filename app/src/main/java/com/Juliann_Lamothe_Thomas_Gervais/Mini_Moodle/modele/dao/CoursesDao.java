package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.dao;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Courses;

import java.io.IOException;
import java.util.List;

public class CoursesDao {

    public static List<Courses> getCourses() throws IOException {
        return new HttpJsonService().getCourses();
    }

}
