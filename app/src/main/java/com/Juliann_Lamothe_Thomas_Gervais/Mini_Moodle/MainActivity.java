package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnConnect,btnInscription;
    EditText etAdresse, etMotDePasse;

    ActivityResultLauncher<Intent> activityResultLauncher;
    Intent intent;

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



        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {

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

        }

        //Si la personne n'a pas de compte et veut se connecter
        if(v == btnInscription){
            intent = new Intent(this, Inscription.class);
            activityResultLauncher.launch(intent);
        }

    }
}
