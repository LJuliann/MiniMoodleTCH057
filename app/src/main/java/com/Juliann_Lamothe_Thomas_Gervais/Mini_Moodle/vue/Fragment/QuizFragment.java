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
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur.QuizzesAdapter;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.DetailCoursViewModel;

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
            recyclerView.setAdapter(new QuizzesAdapter(quizzes));
        });
    }
}
