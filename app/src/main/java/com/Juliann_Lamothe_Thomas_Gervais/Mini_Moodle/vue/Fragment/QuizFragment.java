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
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.SQL.DbUtil;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.ResultatQuiz;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur.QuizzesAdapter;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.DetailCoursViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizFragment extends Fragment {

    public QuizFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    private RecyclerView recyclerView;
    private List<Quizzes> derniersQuizzes;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvQuizzes);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        DetailCoursViewModel viewModel = new ViewModelProvider(requireActivity()).get(DetailCoursViewModel.class);
        viewModel.getQuizzes().observe(getViewLifecycleOwner(), quizzes -> {
            derniersQuizzes = quizzes;
            chargerAvecResultats(recyclerView, quizzes);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (derniersQuizzes != null) {
            chargerAvecResultats(recyclerView, derniersQuizzes);
        }
    }

    private void chargerAvecResultats(RecyclerView recyclerView, List<Quizzes> quizzes) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            DbUtil db = new DbUtil(requireContext());
            Map<String, ResultatQuiz> resultats = new HashMap<>();
            for (Quizzes q : quizzes) {
                ResultatQuiz r = db.getResultatQuiz(q.getId());
                if (r != null) resultats.put(q.getId(), r);
            }
            db.close();

            requireActivity().runOnUiThread(() ->
                    recyclerView.setAdapter(new QuizzesAdapter(quizzes, resultats))
            );
        });
    }
}
