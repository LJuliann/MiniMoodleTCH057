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
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.ResultatQuiz;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur.QuizzesAdapter;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.DetailCoursViewModel;

import java.util.Collections;
import java.util.Map;

public class QuizFragment extends Fragment {

    public QuizFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.rvQuizzes);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        DetailCoursViewModel viewModel = new ViewModelProvider(requireActivity()).get(DetailCoursViewModel.class);

        viewModel.getQuizzes().observe(getViewLifecycleOwner(), quizzes -> {
            viewModel.chargerResultatsQuiz(quizzes);
        });

        viewModel.getResultatsQuiz().observe(getViewLifecycleOwner(), resultats -> {
            if (viewModel.getQuizzes().getValue() != null) {
                Map<String, ResultatQuiz> r = resultats != null ? resultats : Collections.emptyMap();
                recyclerView.setAdapter(new QuizzesAdapter(viewModel.getQuizzes().getValue(), r));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        DetailCoursViewModel viewModel = new ViewModelProvider(requireActivity()).get(DetailCoursViewModel.class);
        if (viewModel.getQuizzes().getValue() != null) {
            viewModel.chargerResultatsQuiz(viewModel.getQuizzes().getValue());
        }
    }
}
