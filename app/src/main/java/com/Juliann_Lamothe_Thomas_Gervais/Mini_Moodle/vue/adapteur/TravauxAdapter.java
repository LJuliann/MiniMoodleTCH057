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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TravauxAdapter extends RecyclerView.Adapter<TravauxAdapter.ViewHolder> {

    private List<Assignments> listeTravaux;

    public TravauxAdapter(List<Assignments> listeTravaux) {
        this.listeTravaux = listeTravaux;
    }

    public void updateList(List<Assignments> nouvelle) {
        this.listeTravaux = nouvelle;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row_travail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Assignments travail = listeTravaux.get(position);
        holder.tvTitre.setText(travail.getTitle());
        holder.tvEcheance.setText("Échéance : " + travail.getDueDate());

        String statut = calculerStatut(travail);
        holder.tvStatut.setText(statut);
        holder.tvStatut.setTextColor(couleurStatut(statut));

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

    private String calculerStatut(Assignments a) {
        if (a.getGrade() != null && a.getGrade() >= 0) return "Corrigé";
        try {
            Date limite = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(a.getDueDate());
            if (limite != null && limite.before(new Date())) return "En retard";
        } catch (Exception ignored) {}
        return "À faire";
    }

    private int couleurStatut(String statut) {
        switch (statut) {
            case "Corrigé":   return Color.parseColor("#E65100");
            case "En retard": return Color.parseColor("#C62828");
            default:          return Color.parseColor("#2E7D32");
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre, tvEcheance, tvStatut;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitre    = itemView.findViewById(R.id.tvTitreTravail);
            tvEcheance = itemView.findViewById(R.id.tvEcheance);
            tvStatut   = itemView.findViewById(R.id.tvStatut);
        }
    }
}
