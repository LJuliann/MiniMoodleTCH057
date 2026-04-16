package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Courses;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.DetailCours;

import android.widget.TextView;


public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {
    private List<Courses> listeCours;

    public CoursesAdapter(List<Courses> listeCours) {
        this.listeCours = listeCours;
    }

    public void updateList(List<Courses> nouvelleListe) {
        this.listeCours = nouvelleListe;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoursesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row_liste_cours, parent, false);
        return new CoursesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesAdapter.ViewHolder holder, int position) {
        Courses cours = listeCours.get(position);
        holder.tvNomCours.setText(cours.getTitle());
        holder.tvNomProf.setText(cours.getTeacher());
        holder.tvSession.setText(cours.getSession());
        holder.coursId = cours.getId();

    }

    @Override
    public int getItemCount() {
        return listeCours.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNomCours;
        TextView tvNomProf;
        TextView tvSession;
        String coursId = "";

        public ViewHolder(View itemView) {
            super(itemView);


            tvNomCours = itemView.findViewById(R.id.tvRecyclerNomCours);
            tvNomProf = itemView.findViewById(R.id.tvRecyclerViewNomProf);
            tvSession = itemView.findViewById(R.id.tvRecyclerViewSession);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            Intent intent = new Intent(context, DetailCours.class);
            intent.putExtra("id", coursId);
            v.getContext().startActivity(intent);
        }
    }
}
