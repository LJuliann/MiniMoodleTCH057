package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;

public class ResultatQuizActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resultat_quiz);

        String titre = getIntent().getStringExtra("quizTitre");
        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        String date = getIntent().getStringExtra("date");

        TextView tvTitre = findViewById(R.id.tvResultatTitre);
        TextView tvScore = findViewById(R.id.tvResultatScore);
        TextView tvSurTotal = findViewById(R.id.tvResultatSurTotal);
        TextView tvMention = findViewById(R.id.tvResultatMention);
        TextView tvDate = findViewById(R.id.tvResultatDate);
        Button btnRetour = findViewById(R.id.btnResultatRetour);

        tvTitre.setText(titre);
        tvScore.setText(String.valueOf(score));
        tvSurTotal.setText("sur " + total);
        tvDate.setText("Complété le " + date);

        double pourcentage = total > 0 ? (score * 100.0 / total) : 0;
        if (pourcentage >= 80) {
            tvMention.setText("Excellent !");
            tvMention.setTextColor(Color.parseColor("#2E7D32"));
            tvScore.setTextColor(Color.parseColor("#2E7D32"));
        } else if (pourcentage >= 60) {
            tvMention.setText("Bien !");
            tvMention.setTextColor(Color.parseColor("#1565C0"));
            tvScore.setTextColor(Color.parseColor("#1565C0"));
        } else if (pourcentage >= 40) {
            tvMention.setText("Passable");
            tvMention.setTextColor(Color.parseColor("#E65100"));
            tvScore.setTextColor(Color.parseColor("#E65100"));
        } else {
            tvMention.setText("À améliorer");
            tvMention.setTextColor(Color.parseColor("#C62828"));
            tvScore.setTextColor(Color.parseColor("#C62828"));
        }

        btnRetour.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
