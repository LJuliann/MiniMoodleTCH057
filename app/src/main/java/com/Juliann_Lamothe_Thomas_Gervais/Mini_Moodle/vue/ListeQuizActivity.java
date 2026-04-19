package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.ListeQuizViewModel;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur.QuizAdapter;

import java.util.ArrayList;

public class ListeQuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liste_quiz);

        Button btnRetour = findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(v -> finish());

        ProgressBar progressBar = findViewById(R.id.progressBar);
        RecyclerView rv = findViewById(R.id.rvQuiz);
        rv.setLayoutManager(new LinearLayoutManager(this));

        QuizAdapter adapter = new QuizAdapter(new ArrayList<>());
        rv.setAdapter(adapter);

        ArrayList<String> enrolledIds = getIntent().getStringArrayListExtra("enrolledCourseIds");
        if (enrolledIds == null) enrolledIds = new ArrayList<>();

        ListeQuizViewModel viewModel = new ViewModelProvider(this).get(ListeQuizViewModel.class);

        viewModel.getChargement().observe(this, loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            rv.setVisibility(loading ? View.GONE : View.VISIBLE);
        });

        viewModel.getQuiz().observe(this, quiz -> {
            adapter.updateList(quiz);
            if (quiz.isEmpty()) {
                Toast.makeText(this, "Aucun quiz disponible.", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.chargerQuiz(enrolledIds);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }
}
