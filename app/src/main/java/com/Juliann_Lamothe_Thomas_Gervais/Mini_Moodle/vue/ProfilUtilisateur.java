package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;

public class ProfilUtilisateur extends AppCompatActivity implements View.OnClickListener {

    EditText prenom, nom, courriel, telephone, password;
    Button btnAccepter, btnRetour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil_utilisateur);

        prenom = findViewById(R.id.etProfilPrenom);
        nom = findViewById(R.id.etProfilNom);
        courriel = findViewById(R.id.etProfilCourriel);
        telephone = findViewById(R.id.etProfilTelephone);
        password = findViewById(R.id.etProfilPassword);

        btnAccepter = findViewById(R.id.btnProfilAccepter);
        btnAccepter.setOnClickListener(this);

        btnRetour = findViewById(R.id.btnProfilRetour);
        btnRetour.setOnClickListener(this);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View v) {

    }
}
