package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Assignments;

import java.util.List;

public class TravauxAdapter extends RecyclerView.Adapter<TravauxAdapter.ViewHolder> {

    private final List<Assignments> listeTravaux;

    public TravauxAdapter(List<Assignments> listeTravaux) {
        this.listeTravaux = listeTravaux;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Assignments travail = listeTravaux.get(position);
        holder.tvTitre.setText(travail.getTitle());
        holder.tvDateLimite.setText("Date limite : " + travail.getDueDate());
        holder.tvStatut.setText(travail.getStatus());
        holder.tvPoints.setText(travail.getTotalPoints() + " pts");
    }

    @Override
    public int getItemCount() { return listeTravaux.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre, tvDateLimite, tvStatut, tvPoints;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitre = itemView.findViewById(R.id.tvTravailTitre);
            tvDateLimite = itemView.findViewById(R.id.tvTravailDate);
            tvStatut = itemView.findViewById(R.id.tvTravailStatut);
            tvPoints = itemView.findViewById(R.id.tvTravailPoints);
        }
    }
}
