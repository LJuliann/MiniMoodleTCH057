package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Users;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.UsersViewModel;

public class Inscription extends AppCompatActivity implements View.OnClickListener {

    EditText etNom,etPrenom,etCourriel,etTelephone;
    EditText motDePasse;
    Button btnInscription, btnLogin;

    ActivityResultLauncher<Intent> activityResultLauncher;
    Intent intent;
    UsersViewModel usersViewModel = new UsersViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inscription);

        //EditText
        etNom = findViewById(R.id.etInscriptionNom);
        etPrenom = findViewById(R.id.etInscriptionPrenom);
        etTelephone = findViewById(R.id.etInscriptionNumeroTelephone);
        etCourriel = findViewById(R.id.etInscriptionCourriel);
        motDePasse = findViewById(R.id.etInscriptionMotDePasse);

        //Button
        btnInscription = findViewById(R.id.btnInscriptionInscrire);
        btnInscription.setOnClickListener(this);

        btnLogin = findViewById(R.id.btnInscriptionConnection);
        btnLogin.setOnClickListener(this);

        //ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {

            }
        });

        usersViewModel.getSuccess().observe(this, success -> {
                    if (success) {
                        Toast.makeText(this, usersViewModel.getMessage().getValue(), Toast.LENGTH_SHORT).show();
                        intent = new Intent(this, ListeDesCours.class);
                        activityResultLauncher.launch(intent);
                    } else {
                        Toast.makeText(this, usersViewModel.getMessage().getValue(), Toast.LENGTH_SHORT).show();
                    }
                });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View v) {

        //Quand la personne s'inscrit.
        if(v == btnInscription){
            if (checkSelfPermission("android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED) {
                if(etNom.getText().toString().isEmpty() || etPrenom.getText().toString().isEmpty() || etCourriel.getText().toString().isEmpty() ||
                        etTelephone.getText().toString().isEmpty() || motDePasse.getText().toString().isEmpty()){
                    Toast.makeText(this,"veuilliez de remplir tous les champs",Toast.LENGTH_SHORT).show();
                    return;
                }
                String nom = etNom.getText().toString();
                String prenom = etPrenom.getText().toString();
                String courriel = etCourriel.getText().toString();
                String telephone = etTelephone.getText().toString();
                String password = motDePasse.getText().toString();
                Users users = new Users("", courriel, password, nom, prenom, telephone, "", "");
                usersViewModel.enregistereUser(users);
            } else {
                requestPermissions(new String[]{"android.permission.INTERNET"},1);
            }
        }

        //Si la personne veut revenir au login.
        if(v == btnLogin){
            intent = new Intent(this, MainActivity.class);
            activityResultLauncher.launch(intent);
        }

    }
}
