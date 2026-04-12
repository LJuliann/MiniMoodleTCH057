package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur.DetailCoursAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DetailCours extends AppCompatActivity {

    Button btnDetailRetour;
    TextView tvDetailNomCours;
    TabLayout tlDetail;
    ViewPager2 vpDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_cours);

        DetailCoursAdapter detailCoursAdapter = new DetailCoursAdapter(this);

        btnDetailRetour = findViewById(R.id.btnDetailRetour);
        btnDetailRetour.setOnClickListener(v -> finish());

        tvDetailNomCours = findViewById(R.id.tvDetailNomCours);
        tlDetail = findViewById(R.id.tlDetail);
        vpDetail = findViewById(R.id.vpDetail);

        vpDetail.setAdapter(detailCoursAdapter);

        new TabLayoutMediator(tlDetail, vpDetail, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Description"); break;
                case 1: tab.setText("Travaux"); break;
                case 2: tab.setText("Quiz"); break;
                case 3: tab.setText("Annonces"); break;
            }
        }).attach();


        String id = getIntent().getStringExtra("id");
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
