package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Assignments;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.DetailTravail;

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
        holder.tvPoints.setText(travail.getTotalPoints() + " pts");

        String statut = travail.getStatutCalcule() != null ? travail.getStatutCalcule() : "À faire";
        holder.tvStatut.setText(statut);
        holder.tvStatut.setTextColor(couleurStatut(statut));

        if ("Corrigé".equals(statut) && travail.getGrade() != null) {
            holder.tvStatut.setText("Corrigé — " + travail.getGrade() + "/" + travail.getTotalPoints());
        }

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailTravail.class);
            intent.putExtra("assignmentId", travail.getId());
            intent.putExtra("title", travail.getTitle());
            intent.putExtra("description", travail.getDescription());
            intent.putExtra("dueDate", travail.getDueDate());
            intent.putExtra("instructions", travail.getInstructions());
            intent.putExtra("totalPoints", travail.getTotalPoints());
            intent.putExtra("statut", statut);
            intent.putExtra("grade", travail.getGrade() != null ? travail.getGrade() : -1);
            intent.putExtra("comment", travail.getComment() != null ? travail.getComment() : "");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return listeTravaux.size(); }

    private int couleurStatut(String statut) {
        switch (statut) {
            case "Remis":    return Color.parseColor("#2E7D32"); // vert
            case "En retard": return Color.parseColor("#C62828"); // rouge
            case "Corrigé":  return Color.parseColor("#E65100"); // orange
            default:         return Color.parseColor("#546E7A"); // gris
        }
    }

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
