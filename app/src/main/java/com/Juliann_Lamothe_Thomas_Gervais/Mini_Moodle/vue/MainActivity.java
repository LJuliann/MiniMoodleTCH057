package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL.SessionDao;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Users;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.UsersViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnConnect,btnInscription;
    EditText etAdresse, etMotDePasse;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Intent intent;

    CheckBox cbConnexion;

    UsersViewModel usersViewModel = new UsersViewModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Button
        btnConnect = findViewById(R.id.btnLoginConnection);
        btnConnect.setOnClickListener(this);

        btnInscription = findViewById(R.id.btnLoginInscription);
        btnInscription.setOnClickListener(this);

        //Editext
        etAdresse = findViewById(R.id.etLoginAdresse);
        etMotDePasse = findViewById(R.id.etPassword);

        //CheckBox
        cbConnexion = findViewById(R.id.cbConnexion);

        String[] session = SessionDao.getSession(this);
        if (session != null) {
            etAdresse.setText(session[0]);
            etMotDePasse.setText(session[1]);
            cbConnexion.setChecked(true);
        }


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {

            }
        });

        usersViewModel.getSuccess().observe(this, success -> {
            Toast.makeText(this, usersViewModel.getMessage().getValue(), Toast.LENGTH_SHORT).show();
            if (success) {
                if (cbConnexion.isChecked()) {
                    SessionDao.sauvegarderSession(this, etAdresse.getText().toString(), etMotDePasse.getText().toString());
                } else {
                    SessionDao.supprimerSession(this);
                }
                Users user = usersViewModel.getConnectedUser().getValue();

                // Persister le profil localement pour l'écran Profil utilisateur
                if (user != null) {
                    DbUtil db = new DbUtil(this);
                    db.sauvegarderProfil(
                            user.getPrenom(), user.getNom(), user.getEmail(),
                            user.getTelephone(), user.getPhotoUrl(), user.getPassword());
                    db.close();
                }

                intent = new Intent(this, TableauDeBord.class);
                if (user != null && user.getEnrolledCourseIds() != null) {
                    intent.putStringArrayListExtra("enrolledCourseIds", new ArrayList<>(user.getEnrolledCourseIds()));
                }
                if (user != null && user.getPrenom() != null) {
                    intent.putExtra("prenom", user.getPrenom());
                }
                activityResultLauncher.launch(intent);
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

      //Quand la personne se connecte.
        if(v == btnConnect){
            if(checkSelfPermission("android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED){
                usersViewModel.connexion(etAdresse.getText().toString(),etMotDePasse.getText().toString());

            } else {
                requestPermissions(new String[]{"android.permission.INTERNET"},1);
            }

        }

        //Si la personne n'a pas de compte et veut s'inscrire
        if(v == btnInscription){
            intent = new Intent(this, Inscription.class);
            activityResultLauncher.launch(intent);
        }

    }
}
