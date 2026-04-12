package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.DetailCoursViewModel;

public class DescriptionFragment extends Fragment {

    public DescriptionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_description, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvCode = view.findViewById(R.id.tvDescCode);
        TextView tvTeacher = view.findViewById(R.id.tvDescTeacher);
        TextView tvSession = view.findViewById(R.id.tvDescSession);
        TextView tvDescription = view.findViewById(R.id.tvDescDescription);

        DetailCoursViewModel viewModel = new ViewModelProvider(requireActivity()).get(DetailCoursViewModel.class);
        viewModel.getCours().observe(getViewLifecycleOwner(), cours -> {
            tvCode.setText(cours.getCode());
            tvTeacher.setText(cours.getTeacher());
            tvSession.setText(cours.getSession());
            tvDescription.setText(cours.getDescription());
        });
    }
}
