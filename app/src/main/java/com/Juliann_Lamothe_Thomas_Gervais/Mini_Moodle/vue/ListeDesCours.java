package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur.CoursesAdapter;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vuewModel.CoursesViewModel;

public class ListeDesCours extends AppCompatActivity {

    CoursesViewModel coursesViewModel;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liste_des_cours);

        recyclerView = findViewById(R.id.rvListeCours);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        coursesViewModel = new ViewModelProvider(this).get(CoursesViewModel.class);

        coursesViewModel.getCourses().observe(this, courses -> {
            CoursesAdapter adapter = new CoursesAdapter(courses);
            recyclerView.setAdapter(adapter);
        });

        coursesViewModel.getMessage().observe(this, msg -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        coursesViewModel.chargerCourses();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
