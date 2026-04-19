package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Users;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.ProfilViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

public class ProfilUtilisateur extends AppCompatActivity {

    ImageView ivAvatar;
    TextView tvNomComplet;
    TextInputEditText etPrenom, etNom, etCourriel, etTelephone, etPhotoUrl,
                      etPassword, etPasswordConfirm;
    Button btnSauvegarder, btnPrevisualiserPhoto, btnRetour;
    private final Handler debounceHandler = new Handler(Looper.getMainLooper());
    private Runnable debounceRunnable;
    private ProfilViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil_utilisateur);

        ivAvatar            = findViewById(R.id.ivAvatar);
        tvNomComplet        = findViewById(R.id.tvNomComplet);
        etPrenom            = findViewById(R.id.etProfilPrenom);
        etNom               = findViewById(R.id.etProfilNom);
        etCourriel          = findViewById(R.id.etProfilCourriel);
        etTelephone         = findViewById(R.id.etProfilTelephone);
        etPhotoUrl          = findViewById(R.id.etProfilPhotoUrl);
        etPassword          = findViewById(R.id.etProfilPassword);
        etPasswordConfirm   = findViewById(R.id.etProfilPasswordConfirm);
        btnSauvegarder      = findViewById(R.id.btnSauvegarder);
        btnPrevisualiserPhoto = findViewById(R.id.btnPrevisualiserPhoto);
        btnRetour           = findViewById(R.id.btnProfilRetour);

        ivAvatar.setBackground(cercleBleue());
        ivAvatar.setClipToOutline(true);

        viewModel = new ViewModelProvider(this).get(ProfilViewModel.class);

        viewModel.getProfil().observe(this, user -> {
            if (user == null) return;
            etPrenom.setText(user.getPrenom());
            etNom.setText(user.getNom());
            etCourriel.setText(user.getEmail());
            etTelephone.setText(user.getTelephone());
            etPhotoUrl.setText(user.getPhotoUrl());
            String prenom = user.getPrenom() != null ? user.getPrenom() : "";
            String nom    = user.getNom()    != null ? user.getNom()    : "";
            tvNomComplet.setText((prenom + " " + nom).trim());
            chargerAvatar(user.getPhotoUrl());
        });

        viewModel.getMessage().observe(this, msg -> {
            if (msg == null) return;
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            finish();
        });

        viewModel.chargerProfil();

        etPhotoUrl.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (debounceRunnable != null) debounceHandler.removeCallbacks(debounceRunnable);
                debounceRunnable = () -> chargerAvatar(s.toString().trim());
                debounceHandler.postDelayed(debounceRunnable, 1000);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnPrevisualiserPhoto.setOnClickListener(v -> {
            String url = etPhotoUrl.getText() != null ? etPhotoUrl.getText().toString().trim() : "";
            chargerAvatar(url);
        });

        btnSauvegarder.setOnClickListener(v -> sauvegarder());

        btnRetour.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void chargerAvatar(String url) {
        if (url != null && !url.trim().isEmpty()) {
            Glide.with(this)
                 .load(url.trim())
                 .circleCrop()
                 .error(cercleBleue())   // cercle bleu si l'URL est invalide
                 .into(ivAvatar);
        } else {
            // URL vide : remettre le fond bleu par défaut
            ivAvatar.setImageDrawable(null);
            ivAvatar.setBackground(cercleBleue());
        }
    }

    private GradientDrawable cercleBleue() {
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.OVAL);
        d.setColor(0xFF1A237E);
        return d;
    }

    private void sauvegarder() {
        String prenom    = texte(etPrenom);
        String nom       = texte(etNom);
        String courriel  = texte(etCourriel);
        String telephone = texte(etTelephone);
        String photoUrl  = texte(etPhotoUrl);
        String mdp       = texte(etPassword);
        String mdpConf   = texte(etPasswordConfirm);

        if (prenom.isEmpty() || nom.isEmpty()) {
            Toast.makeText(this, "Le prénom et le nom sont obligatoires.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mdp.isEmpty() && !mdp.equals(mdpConf)) {
            Toast.makeText(this, "Les mots de passe ne correspondent pas.", Toast.LENGTH_SHORT).show();
            return;
        }

        tvNomComplet.setText((prenom + " " + nom).trim());
        chargerAvatar(photoUrl);

        viewModel.sauvegarderProfil(prenom, nom, courriel, telephone, photoUrl, mdp);
    }

    private String texte(TextInputEditText champ) {
        return champ.getText() != null ? champ.getText().toString().trim() : "";
    }
}
