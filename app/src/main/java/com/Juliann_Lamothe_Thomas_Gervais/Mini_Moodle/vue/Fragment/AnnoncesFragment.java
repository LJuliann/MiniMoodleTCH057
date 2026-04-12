package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.viewModel.DetailCoursViewModel;

import java.util.Arrays;

public class AnnoncesFragment extends Fragment {

    public AnnoncesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_annonces, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.lvAnnonces);

        DetailCoursViewModel viewModel = new ViewModelProvider(requireActivity()).get(DetailCoursViewModel.class);
        viewModel.getCours().observe(getViewLifecycleOwner(), cours -> {
            String[] annonces = cours.getAnnonces();
            if (annonces != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_list_item_1,
                        Arrays.asList(annonces));
                listView.setAdapter(adapter);
            }
        });
    }
}
