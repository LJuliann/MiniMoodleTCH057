package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Courses;
import android.widget.TextView;


public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {
    private final List<Courses> listeCours;

    public CoursesAdapter(List<Courses> listeCours) {
        this.listeCours = listeCours;
    }

    @NonNull
    @Override
    public CoursesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new CoursesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesAdapter.ViewHolder holder, int position) {
        Courses cours = listeCours.get(position);
        holder.tvNomCours.setText(cours.getTitle());
        holder.tvNomProf.setText(cours.getTeacher());
    }

    @Override
    public int getItemCount() {
        return listeCours.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomCours;
        TextView tvNomProf;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNomCours = itemView.findViewById(R.id.tvRecyclerNomCours);
            tvNomProf = itemView.findViewById(R.id.tvRecyclerViewNomProf);
        }
    }
}
