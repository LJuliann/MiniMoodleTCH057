package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.TravauxViewModel;

public class DetailTravail extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_travail);

        // Récupérer les données de l'Intent
        String assignmentId = getIntent().getStringExtra("assignmentId");
        String title        = getIntent().getStringExtra("title");
        String description  = getIntent().getStringExtra("description");
        String dueDate      = getIntent().getStringExtra("dueDate");
        String instructions = getIntent().getStringExtra("instructions");
        String statut       = getIntent().getStringExtra("statut");
        int totalPoints     = getIntent().getIntExtra("totalPoints", 0);
        int grade           = getIntent().getIntExtra("grade", -1);
        String comment      = getIntent().getStringExtra("comment");

        // Vues
        TextView tvTitre         = findViewById(R.id.tvTravailDetailTitre);
        TextView tvStatut        = findViewById(R.id.tvTravailDetailStatut);
        TextView tvDate          = findViewById(R.id.tvTravailDetailDate);
        TextView tvDescription   = findViewById(R.id.tvTravailDetailDescription);
        TextView tvConsignes     = findViewById(R.id.tvTravailDetailConsignes);
        LinearLayout layoutNote  = findViewById(R.id.layoutNote);
        TextView tvNote          = findViewById(R.id.tvTravailDetailNote);
        TextView tvCommentaire   = findViewById(R.id.tvTravailDetailCommentaire);
        LinearLayout layoutSoumissionExistante = findViewById(R.id.layoutSoumissionExistante);
        TextView tvSoumissionDate    = findViewById(R.id.tvSoumissionDate);
        TextView tvSoumissionContenu = findViewById(R.id.tvSoumissionContenu);
        EditText etSoumission    = findViewById(R.id.etSoumission);
        Button btnSoumettre      = findViewById(R.id.btnSoumettre);
        Button btnRetour         = findViewById(R.id.btnTravailRetour);

        btnRetour.setOnClickListener(v -> finish());

        // Remplir les champs
        tvTitre.setText(title);
        tvDate.setText(dueDate);
        tvDescription.setText(description);
        tvConsignes.setText(instructions != null && !instructions.isEmpty() ? instructions : "Aucune consigne");
        tvStatut.setText(statut);
        tvStatut.setTextColor(couleurStatut(statut));

        // Note (si corrigé)
        if ("Corrigé".equals(statut) && grade >= 0) {
            layoutNote.setVisibility(View.VISIBLE);
            tvNote.setText(grade + " / " + totalPoints);
            tvCommentaire.setText(comment != null && !comment.isEmpty() ? comment : "Aucun commentaire");
        }

        // ViewModel pour soumission
        TravauxViewModel viewModel = new ViewModelProvider(this).get(TravauxViewModel.class);

        viewModel.getSoumissionCourante().observe(this, soumission -> {
            if (soumission != null) {
                layoutSoumissionExistante.setVisibility(View.VISIBLE);
                tvSoumissionDate.setText(soumission.getDateRemise());
                tvSoumissionContenu.setText(soumission.getContenu());
                etSoumission.setText(soumission.getContenu());
                btnSoumettre.setText("Modifier la soumission");
            }
        });

        viewModel.getMessage().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        );

        viewModel.chargerSoumission(assignmentId);

        btnSoumettre.setOnClickListener(v -> {
            String contenu = etSoumission.getText().toString().trim();
            if (contenu.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer un lien ou une réponse", Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(this)
                    .setTitle("Confirmer la soumission")
                    .setMessage("Voulez-vous soumettre ce travail ?")
                    .setPositiveButton("Soumettre", (dialog, which) -> viewModel.soumettre(assignmentId, contenu))
                    .setNegativeButton("Annuler", null)
                    .show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private int couleurStatut(String statut) {
        if (statut == null) return Color.parseColor("#546E7A");
        switch (statut) {
            case "Remis":     return Color.parseColor("#2E7D32");
            case "En retard": return Color.parseColor("#C62828");
            case "Corrigé":   return Color.parseColor("#E65100");
            default:          return Color.parseColor("#546E7A");
        }
    }
}
