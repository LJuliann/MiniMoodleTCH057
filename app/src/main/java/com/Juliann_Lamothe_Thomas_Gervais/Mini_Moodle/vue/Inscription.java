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
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL.DbUtil;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Users;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.UsersViewModel;

import java.util.ArrayList;

public class Inscription extends AppCompatActivity implements View.OnClickListener {

    EditText etNom, etPrenom, etCourriel, etTelephone, etUsername;
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
        etUsername = findViewById(R.id.etInscriptionUsername);
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
            Toast.makeText(this, usersViewModel.getMessage().getValue(), Toast.LENGTH_SHORT).show();
            if (success) {
                DbUtil db = new DbUtil(this);
                db.sauvegarderProfil(
                        etPrenom.getText().toString(),
                        etNom.getText().toString(),
                        etCourriel.getText().toString(),
                        etTelephone.getText().toString(),
                        "", "");
                db.close();

                Intent i = new Intent(this, TableauDeBord.class);
                i.putStringArrayListExtra("enrolledCourseIds", new ArrayList<>());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
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
                if(etNom.getText().toString().isEmpty() || etPrenom.getText().toString().isEmpty() ||
                        etCourriel.getText().toString().isEmpty() || etTelephone.getText().toString().isEmpty() ||
                        motDePasse.getText().toString().isEmpty() || etUsername.getText().toString().isEmpty()){
                    Toast.makeText(this,"Veuillez remplir tous les champs",Toast.LENGTH_SHORT).show();
                    return;
                }
                String nom = etNom.getText().toString();
                String prenom = etPrenom.getText().toString();
                String courriel = etCourriel.getText().toString();
                String telephone = etTelephone.getText().toString();
                String password = motDePasse.getText().toString();
                String username = etUsername.getText().toString();
                Users users = new Users(username, courriel, password, nom, prenom, telephone, "", "");
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
