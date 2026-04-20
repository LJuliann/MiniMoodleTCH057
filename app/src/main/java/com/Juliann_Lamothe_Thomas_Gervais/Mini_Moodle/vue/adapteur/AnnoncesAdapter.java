package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.AnnonceItem;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.DetailCours;

import java.util.List;

public class AnnoncesAdapter extends RecyclerView.Adapter<AnnoncesAdapter.ViewHolder> {

    private List<AnnonceItem> liste;

    public AnnoncesAdapter(List<AnnonceItem> liste) {
        this.liste = liste;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<AnnonceItem> nouvelle) {
        this.liste = nouvelle;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row_annonce, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnnonceItem item = liste.get(position);
        holder.tvCode.setText(item.cours.getCode());
        holder.tvTexte.setText(item.texte);

        holder.itemView.setOnClickListener(v -> {
            Context ctx = v.getContext();
            Intent intent = new Intent(ctx, DetailCours.class);
            intent.putExtra("id", item.cours.getId());
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return liste.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvTexte;

        ViewHolder(View v) {
            super(v);
            tvCode  = v.findViewById(R.id.tvCodeCours);
            tvTexte = v.findViewById(R.id.tvTexteAnnonce);
        }
    }
}
