package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    Spinner spinner;
    EditText editTextRecherche;
    CoursesAdapter adapter;


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

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Tous", "Actifs", "Terminés"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        adapter = new CoursesAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        coursesViewModel = new ViewModelProvider(this).get(CoursesViewModel.class);

        coursesViewModel.getCourses().observe(this, courses -> {
            adapter.updateList(courses);
        });

        coursesViewModel.getMessage().observe(this, msg -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        editTextRecherche = findViewById(R.id.editTextText);
        editTextRecherche.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                coursesViewModel.rechercher(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                coursesViewModel.filtrer(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
