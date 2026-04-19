package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL.SessionDao;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.ListeAnnoncesActivity;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.ListeTravauxActivity;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.ListeQuizActivity;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Assignments;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.TravailAvecCours;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Courses;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.TableauDeBordViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.DetailCours;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.DetailQuizActivity;

import java.util.ArrayList;
import java.util.List;

public class TableauDeBord extends AppCompatActivity {

    TableauDeBordViewModel viewModel;
    TextView tvBonjour, tvStatCours, tvStatTravaux, tvStatQuiz;
    LinearLayout llTravaux, llQuizzes, llAnnonces;
    Button btnVoirCours, btnProfil, btnDeconnexion;
    TextView tvTitreAnnonces, tvTitreTravaux, tvTitreQuiz;
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
        btnProfil       = findViewById(R.id.btnProfil);
        btnDeconnexion  = findViewById(R.id.btnDeconnexion);
        tvTitreAnnonces = findViewById(R.id.tvTitreAnnonces);
        tvTitreTravaux  = findViewById(R.id.tvTitreTravaux);
        tvTitreQuiz     = findViewById(R.id.tvTitreQuiz);

        enrolledIds = getIntent().getStringArrayListExtra("enrolledCourseIds");
        if (enrolledIds == null) enrolledIds = new ArrayList<>();

        btnVoirCours.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListeDesCours.class);
            intent.putStringArrayListExtra("enrolledCourseIds", new ArrayList<>(enrolledIds));
            startActivity(intent);
        });

        btnProfil.setOnClickListener(v ->
                startActivity(new Intent(this, ProfilUtilisateur.class)));

        btnDeconnexion.setOnClickListener(v -> seDeconnecter());

        tvTitreAnnonces.setOnClickListener(v -> {
            Intent i = new Intent(this, ListeAnnoncesActivity.class);
            i.putStringArrayListExtra("enrolledCourseIds", new ArrayList<>(enrolledIds));
            startActivity(i);
        });
        tvTitreTravaux.setOnClickListener(v -> {
            Intent i = new Intent(this, ListeTravauxActivity.class);
            i.putStringArrayListExtra("enrolledCourseIds", new ArrayList<>(enrolledIds));
            startActivity(i);
        });
        tvTitreQuiz.setOnClickListener(v -> {
            Intent i = new Intent(this, ListeQuizActivity.class);
            i.putStringArrayListExtra("enrolledCourseIds", new ArrayList<>(enrolledIds));
            startActivity(i);
        });

        viewModel = new ViewModelProvider(this).get(TableauDeBordViewModel.class);

        viewModel.getCourses().observe(this, courses -> {
            tvStatCours.setText(String.valueOf(courses.size()));
            llAnnonces.removeAllViews();
            for (Courses c : courses) {
                if (c.getAnnonces() != null) {
                    for (String annonce : c.getAnnonces()) {
                        ajouterAnnonce(c, annonce);
                    }
                }
            }
            if (llAnnonces.getChildCount() == 0) {
                int px16 = (int) (16 * getResources().getDisplayMetrics().density);
                TextView tv = new TextView(this);
                tv.setText("Aucune annonce récente.");
                tv.setTextSize(14);
                tv.setTextColor(Color.parseColor("#9E9E9E"));
                tv.setPadding(px16, px16, px16, px16);
                llAnnonces.addView(tv);
            }
        });

        viewModel.getAssignments().observe(this, assignments -> {
            tvStatTravaux.setText(String.valueOf(assignments.size()));
            llTravaux.removeAllViews();
            if (assignments.isEmpty()) {
                ajouterItem(llTravaux, "Aucun travail à remettre.");
            } else {
                for (TravailAvecCours t : assignments) {
                    ajouterTravailCliquable(t);
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
                    ajouterQuizCliquable(q);
                }
            }
        });

        viewModel.getMessage().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());

        viewModel.getPrenom().observe(this, p -> {
            if (p != null && !p.isEmpty()) {
                tvBonjour.setText("Bonjour, " + p + " !");
            }
        });

        viewModel.chargerDonnees(enrolledIds);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void seDeconnecter() {
        SessionDao.supprimerSession(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.chargerPrenom();
    }

    private void ajouterAnnonce(Courses cours, String texte) {
        String courseCode = cours.getCode();
        float density = getResources().getDisplayMetrics().density;
        int px4  = (int) (4  * density);
        int px12 = (int) (12 * density);
        int px16 = (int) (16 * density);

        // Separator before each item except the first
        if (llAnnonces.getChildCount() > 0) {
            View sep = new View(this);
            LinearLayout.LayoutParams sepParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
            sep.setLayoutParams(sepParams);
            sep.setBackgroundColor(Color.parseColor("#EEEEEE"));
            llAnnonces.addView(sep);
        }

        // Row: colored accent bar | course code + text
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, px12, px16, px12);

        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackground, value, true);
        row.setBackgroundResource(value.resourceId);
        row.setClickable(true);
        row.setFocusable(true);
        row.setOnClickListener(v -> {
            Intent intent = new Intent(this, DetailCours.class);
            intent.putExtra("id", cours.getId());
            startActivity(intent);
        });

        // Left accent bar
        View accent = new View(this);
        LinearLayout.LayoutParams accentParams = new LinearLayout.LayoutParams(px4, LinearLayout.LayoutParams.MATCH_PARENT);
        accentParams.setMarginEnd(px12);
        accent.setLayoutParams(accentParams);
        accent.setBackgroundColor(Color.parseColor("#1A237E"));
        accent.setMinimumHeight((int) (40 * density));

        // Content
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView tvCode = new TextView(this);
        tvCode.setText(courseCode);
        tvCode.setTextSize(11);
        tvCode.setTypeface(null, Typeface.BOLD);
        tvCode.setTextColor(Color.parseColor("#1A237E"));
        tvCode.setLetterSpacing(0.05f);

        TextView tvTexte = new TextView(this);
        tvTexte.setText(texte);
        tvTexte.setTextSize(13);
        tvTexte.setTextColor(Color.parseColor("#37474F"));
        int px2 = (int) (2 * density);
        tvTexte.setPadding(0, px2, 0, 0);

        content.addView(tvCode);
        content.addView(tvTexte);

        row.addView(accent);
        row.addView(content);

        llAnnonces.addView(row);
    }

    private void ajouterItem(LinearLayout parent, String texte) {
        TextView tv = new TextView(this);
        tv.setText("• " + texte);
        tv.setTextSize(14);
        tv.setPadding(0, 6, 0, 6);
        parent.addView(tv);
    }

    private void ajouterTravailCliquable(TravailAvecCours item) {
        Assignments a = item.travail;
        String statut = a.getStatutCalcule() != null ? a.getStatutCalcule() : "À faire";
        int couleur = couleurStatut(statut);

        LinearLayout row = creerLigneCliquable();

        LinearLayout left = new LinearLayout(this);
        left.setOrientation(LinearLayout.VERTICAL);
        left.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        if (item.codeCours != null && !item.codeCours.isEmpty()) {
            TextView tvCode = new TextView(this);
            tvCode.setText(item.codeCours);
            tvCode.setTextSize(11);
            tvCode.setTypeface(null, android.graphics.Typeface.BOLD);
            tvCode.setTextColor(Color.parseColor("#1A237E"));
            tvCode.setLetterSpacing(0.05f);
            left.addView(tvCode);
        }

        TextView tvTitre = new TextView(this);
        tvTitre.setText(a.getTitle());
        tvTitre.setTextSize(14);
        tvTitre.setTextColor(Color.parseColor("#212121"));

        TextView tvInfo = new TextView(this);
        tvInfo.setText("Échéance : " + a.getDueDate() + "  •  " + statut);
        tvInfo.setTextSize(12);
        tvInfo.setTextColor(couleur);

        left.addView(tvTitre);
        left.addView(tvInfo);

        TextView tvChevron = creerChevron();

        row.addView(left);
        row.addView(tvChevron);

        row.setOnClickListener(v -> {
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

        llTravaux.addView(row);
    }

    private void ajouterQuizCliquable(Quizzes q) {
        LinearLayout row = creerLigneCliquable();

        LinearLayout left = new LinearLayout(this);
        left.setOrientation(LinearLayout.VERTICAL);
        left.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvTitre = new TextView(this);
        tvTitre.setText(q.getTitle());
        tvTitre.setTextSize(14);
        tvTitre.setTextColor(Color.parseColor("#212121"));

        int nbQ = q.getQuestions() != null ? q.getQuestions().size() : 0;
        String info = nbQ + " question(s)";
        if (q.getDuration() > 0) info += "  •  " + q.getDuration() + " min";

        TextView tvInfo = new TextView(this);
        tvInfo.setText(info);
        tvInfo.setTextSize(12);
        tvInfo.setTextColor(Color.parseColor("#546E7A"));

        left.addView(tvTitre);
        left.addView(tvInfo);

        TextView tvChevron = creerChevron();

        row.addView(left);
        row.addView(tvChevron);

        row.setOnClickListener(v -> {
            try {
                String quizJson = new ObjectMapper().writeValueAsString(q);
                Intent intent = new Intent(this, DetailQuizActivity.class);
                intent.putExtra("quizJson", quizJson);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        llQuizzes.addView(row);
    }

    private LinearLayout creerLigneCliquable() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        int px8 = (int) (8 * getResources().getDisplayMetrics().density);
        int px12 = (int) (12 * getResources().getDisplayMetrics().density);
        row.setPadding(px8, px12, px8, px12);

        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackground, value, true);
        row.setBackgroundResource(value.resourceId);
        row.setClickable(true);
        row.setFocusable(true);
        return row;
    }

    private TextView creerChevron() {
        TextView tv = new TextView(this);
        tv.setText("›");
        tv.setTextSize(20);
        tv.setTextColor(Color.parseColor("#BDBDBD"));
        int px8 = (int) (8 * getResources().getDisplayMetrics().density);
        tv.setPadding(px8, 0, 0, 0);
        return tv;
    }

    private int couleurStatut(String statut) {
        switch (statut) {
            case "Corrigé":   return Color.parseColor("#E65100");
            case "En retard": return Color.parseColor("#C62828");
            default:          return Color.parseColor("#546E7A");
        }
    }
}
