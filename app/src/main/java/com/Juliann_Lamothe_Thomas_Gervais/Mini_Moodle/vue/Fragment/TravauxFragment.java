package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Assignments;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Courses;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.TravailAvecCours;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur.TravauxAdapter;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.DetailCoursViewModel;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.TravauxViewModel;

import java.util.ArrayList;
import java.util.List;

public class TravauxFragment extends Fragment {

    public TravauxFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_travaux, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.rvTravaux);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        TravauxViewModel traVauxViewModel = new ViewModelProvider(requireActivity()).get(TravauxViewModel.class);
        DetailCoursViewModel detailCoursViewModel = new ViewModelProvider(requireActivity()).get(DetailCoursViewModel.class);

        traVauxViewModel.getTravaux().observe(getViewLifecycleOwner(), travaux -> {
            Courses cours = detailCoursViewModel.getCours().getValue();
            String nom  = cours != null ? cours.getTitle() : "";
            String code = cours != null ? cours.getCode()  : "";
            List<TravailAvecCours> liste = new ArrayList<>();
            for (Assignments a : travaux) liste.add(new TravailAvecCours(a, nom, code));
            recyclerView.setAdapter(new TravauxAdapter(liste));
        });

        detailCoursViewModel.getTravaux().observe(getViewLifecycleOwner(), travaux ->
                traVauxViewModel.chargerAvecStatuts(travaux)
        );
    }
}
