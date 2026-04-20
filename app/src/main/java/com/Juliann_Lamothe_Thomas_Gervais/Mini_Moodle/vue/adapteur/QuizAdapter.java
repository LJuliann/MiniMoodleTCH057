package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur;

import android.annotation.SuppressLint;
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
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.QuizAvecStatut;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.ResultatQuiz;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.DetailQuizActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    private List<QuizAvecStatut> liste;

    public QuizAdapter(List<QuizAvecStatut> liste) {
        this.liste = liste;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<QuizAvecStatut> nouvelle) {
        this.liste = nouvelle;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row_quiz, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizAvecStatut item = liste.get(position);
        holder.tvTitre.setText(item.quiz.getTitle());

        int nbQ = item.quiz.getQuestions() != null ? item.quiz.getQuestions().size() : 0;
        String info = nbQ + " question(s)";
        if (item.quiz.getDuration() > 0) info += "  •  " + item.quiz.getDuration() + " min";
        holder.tvInfo.setText(info);

        ResultatQuiz res = item.resultat;
        if (res != null) {
            int pct = res.getTotal() > 0 ? (res.getScore() * 100 / res.getTotal()) : 0;
            holder.tvStatut.setText("✓ Complété  •  " + res.getScore() + "/" + res.getTotal() + "  (" + pct + "%)");
            holder.tvStatut.setTextColor(pct >= 60
                    ? Color.parseColor("#2E7D32")
                    : Color.parseColor("#C62828"));
            holder.tvStatut.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatut.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            try {
                Context ctx = v.getContext();
                String json = new ObjectMapper().writeValueAsString(item.quiz);
                Intent intent = new Intent(ctx, DetailQuizActivity.class);
                intent.putExtra("quizJson", json);
                ctx.startActivity(intent);
            } catch (Exception ignored) {}
        });
    }

    @Override
    public int getItemCount() { return liste.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre, tvInfo, tvStatut;

        ViewHolder(View v) {
            super(v);
            tvTitre  = v.findViewById(R.id.tvTitreQuiz);
            tvInfo   = v.findViewById(R.id.tvInfoQuiz);
            tvStatut = v.findViewById(R.id.tvStatutQuiz);
        }
    }
}
