package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Assignments;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Courses;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.TableauDeBordViewModel;

import java.util.ArrayList;
import java.util.List;

public class TableauDeBord extends AppCompatActivity {

    TableauDeBordViewModel viewModel;
    TextView tvBonjour, tvStatCours, tvStatTravaux, tvStatQuiz;
    LinearLayout llTravaux, llQuizzes, llAnnonces;
    Button btnVoirCours;
    List<String> enrolledIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tableau_de_bord);

        tvBonjour = findViewById(R.id.tvBonjour);
        tvStatCours = findViewById(R.id.tvStatCours);
        tvStatTravaux = findViewById(R.id.tvStatTravaux);
        tvStatQuiz = findViewById(R.id.tvStatQuiz);
        llTravaux = findViewById(R.id.llTravaux);
        llQuizzes = findViewById(R.id.llQuizzes);
        llAnnonces = findViewById(R.id.llAnnonces);
        btnVoirCours = findViewById(R.id.btnVoirCours);

        String prenom = getIntent().getStringExtra("prenom");
        enrolledIds = getIntent().getStringArrayListExtra("enrolledCourseIds");
        if (enrolledIds == null) enrolledIds = new ArrayList<>();

        if (prenom != null && !prenom.isEmpty()) {
            tvBonjour.setText("Bonjour, " + prenom + " !");
        }

        btnVoirCours.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListeDesCours.class);
            intent.putStringArrayListExtra("enrolledCourseIds", new ArrayList<>(enrolledIds));
            startActivity(intent);
        });

        viewModel = new ViewModelProvider(this).get(TableauDeBordViewModel.class);

        viewModel.getCourses().observe(this, courses -> {
            tvStatCours.setText(String.valueOf(courses.size()));
            llAnnonces.removeAllViews();
            for (Courses c : courses) {
                if (c.getAnnonces() != null) {
                    for (String annonce : c.getAnnonces()) {
                        ajouterItem(llAnnonces, "[" + c.getCode() + "] " + annonce);
                    }
                }
            }
            if (llAnnonces.getChildCount() == 0) {
                ajouterItem(llAnnonces, "Aucune annonce récente.");
            }
        });

        viewModel.getAssignments().observe(this, assignments -> {
            tvStatTravaux.setText(String.valueOf(assignments.size()));
            llTravaux.removeAllViews();
            if (assignments.isEmpty()) {
                ajouterItem(llTravaux, "Aucun travail à remettre.");
            } else {
                for (Assignments a : assignments) {
                    ajouterTravailCliquable(a);
                }
            }
        });

        viewModel.getQuizzes().observe(this, quizzes -> {
            tvStatQuiz.setText(String.valueOf(quizzes.size()));
            llQuizzes.removeAllViews();
            if (quizzes.isEmpty()) {
                ajouterItem(llQuizzes, "Aucun quiz disponible.");
            } else {
                for (Quizzes q : quizzes) {
                    ajouterItem(llQuizzes, q.getTitle());
                }
            }
        });

        viewModel.getMessage().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());

        viewModel.chargerDonnees(enrolledIds);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void ajouterItem(LinearLayout parent, String texte) {
        TextView tv = new TextView(this);
        tv.setText("• " + texte);
        tv.setTextSize(14);
        tv.setPadding(0, 6, 0, 6);
        parent.addView(tv);
    }

    private void ajouterTravailCliquable(Assignments a) {
        String statut = calculerStatutSimple(a);

        TextView tv = new TextView(this);
        tv.setText("• " + a.getTitle() + "  —  " + a.getDueDate());
        tv.setTextSize(14);
        tv.setPadding(0, 6, 0, 6);
        tv.setTextColor(couleurStatut(statut));
        tv.setOnClickListener(v -> {
            Intent intent = new Intent(this, DetailTravail.class);
            intent.putExtra("assignmentId", a.getId());
            intent.putExtra("title", a.getTitle());
            intent.putExtra("description", a.getDescription());
            intent.putExtra("dueDate", a.getDueDate());
            intent.putExtra("instructions", a.getInstructions());
            intent.putExtra("totalPoints", a.getTotalPoints());
            intent.putExtra("statut", statut);
            intent.putExtra("grade", a.getGrade() != null ? a.getGrade() : -1);
            intent.putExtra("comment", a.getComment() != null ? a.getComment() : "");
            startActivity(intent);
        });
        llTravaux.addView(tv);
    }

    private String calculerStatutSimple(Assignments a) {
        if (a.getGrade() != null && a.getGrade() >= 0) return "Corrigé";
        try {
            Date dateLimite = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(a.getDueDate());
            if (dateLimite != null && dateLimite.before(new Date())) return "En retard";
        } catch (Exception ignored) {}
        return "À faire";
    }

    private int couleurStatut(String statut) {
        switch (statut) {
            case "Corrigé":   return Color.parseColor("#E65100");
            case "En retard": return Color.parseColor("#C62828");
            default:          return Color.parseColor("#546E7A");
        }
    }
}
