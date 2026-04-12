package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.Fragment.AnnoncesFragment;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.Fragment.DescriptionFragment;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.Fragment.QuizFragment;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.Fragment.TravauxFragment;

public class DetailCoursAdapter extends FragmentStateAdapter {

    int nbrFragment = 4; //nombre de fragment à afficher


    public DetailCoursAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Nullable
    @Override
    public Fragment createFragment(int position) {
        if(position == 0){
            return new DescriptionFragment();
        }
        else if(position == 1){
            return new TravauxFragment();
        }
        else if(position == 2){
            return new QuizFragment();
        }
        else if(position == 3){
            return new AnnoncesFragment();
        }
        else
            return null;
    }

    @Override
    public int getItemCount() {
        return nbrFragment;
    }
}
