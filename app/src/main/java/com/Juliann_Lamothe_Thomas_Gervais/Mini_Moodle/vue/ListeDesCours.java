package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur.CoursesAdapter;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vuewModel.CoursesViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListeDesCours extends AppCompatActivity {

    CoursesViewModel coursesViewModel;
    RecyclerView recyclerView;
    Button btnListeRetour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liste_des_cours);

        btnListeRetour = findViewById(R.id.btnListeRetour);
        btnListeRetour.setOnClickListener(v -> finish());


        recyclerView = findViewById(R.id.rvListeCours);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        coursesViewModel = new ViewModelProvider(this).get(CoursesViewModel.class);

        coursesViewModel.getCourses().observe(this, courses -> {
            CoursesAdapter adapter = new CoursesAdapter(courses);
            recyclerView.setAdapter(adapter);
        });

        coursesViewModel.getMessage().observe(this, msg -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        List<String> enrolledIds = getIntent().getStringArrayListExtra("enrolledCourseIds");
        coursesViewModel.chargerCourses(enrolledIds != null ? enrolledIds : new ArrayList<>());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
