package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Questions;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.ResultatQuiz;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.QuizViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DetailQuizActivity extends AppCompatActivity {

    private QuizViewModel viewModel;
    private TextView tvTitre, tvProgression, tvQuestion;
    private RadioGroup rgOptions;
    private ProgressBar progressBar;
    private Button btnSuivant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_quiz);

        tvTitre = findViewById(R.id.tvQuizTitreHeader);
        tvProgression = findViewById(R.id.tvQuizProgression);
        tvQuestion = findViewById(R.id.tvQuestion);
        rgOptions = findViewById(R.id.rgOptions);
        progressBar = findViewById(R.id.progressBarQuiz);
        btnSuivant = findViewById(R.id.btnQuizSuivant);

        findViewById(R.id.btnQuizRetour).setOnClickListener(v -> confirmerAbandon());

        // Désérialiser le quiz depuis l'intent
        String quizJson = getIntent().getStringExtra("quizJson");
        Quizzes quiz;
        try {
            quiz = new ObjectMapper().readValue(quizJson, Quizzes.class);
        } catch (Exception e) {
            Toast.makeText(this, "Erreur de chargement du quiz", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTitre.setText(quiz.getTitle());

        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        viewModel.initialiser(quiz);

        viewModel.getIndexCourant().observe(this, index -> {
            int total = viewModel.getNbQuestions();
            tvProgression.setText("Question " + (index + 1) + " / " + total);
            progressBar.setProgress((int) (((index + 1) * 100.0) / total));
            btnSuivant.setText(index + 1 == total ? "Terminer" : "Suivant");
        });

        viewModel.getQuestionCourante().observe(this, question -> afficherQuestion(question));

        viewModel.getQuizTermine().observe(this, termine -> {
            if (Boolean.TRUE.equals(termine)) {
                viewModel.getResultat().observe(this, resultat -> {
                    if (resultat != null) ouvrirResultat(quiz, resultat);
                });
            }
        });

        btnSuivant.setOnClickListener(v -> {
            int checkedId = rgOptions.getCheckedRadioButtonId();
            if (checkedId == -1) {
                Toast.makeText(this, "Veuillez choisir une réponse", Toast.LENGTH_SHORT).show();
                return;
            }

            int index = viewModel.getIndexCourant().getValue() != null ? viewModel.getIndexCourant().getValue() : 0;
            int reponse = rgOptions.indexOfChild(rgOptions.findViewById(checkedId));
            viewModel.enregistrerReponse(index, reponse);

            if (index + 1 == viewModel.getNbQuestions()) {
                viewModel.terminer();
            } else {
                viewModel.questionSuivante();
                rgOptions.clearCheck();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void afficherQuestion(Questions question) {
        tvQuestion.setText(question.getQuestion());
        rgOptions.removeAllViews();
        String[] options = question.getOptions();
        if (options == null) return;
        for (String option : options) {
            RadioButton rb = new RadioButton(this);
            rb.setText(option);
            rb.setTextSize(16);
            rb.setPadding(8, 16, 8, 16);
            rgOptions.addView(rb);
        }
    }

    private void ouvrirResultat(Quizzes quiz, ResultatQuiz resultat) {
        Intent intent = new Intent(this, ResultatQuizActivity.class);
        intent.putExtra("quizTitre", quiz.getTitle());
        intent.putExtra("score", resultat.getScore());
        intent.putExtra("total", resultat.getTotal());
        intent.putExtra("date", resultat.getDateCompletion());
        startActivity(intent);
        finish();
    }

    private void confirmerAbandon() {
        new AlertDialog.Builder(this)
                .setTitle("Quitter le quiz")
                .setMessage("Vos réponses seront perdues. Voulez-vous quitter ?")
                .setPositiveButton("Quitter", (d, w) -> finish())
                .setNegativeButton("Continuer", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        confirmerAbandon();
    }
}
