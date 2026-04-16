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
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.ResultatQuiz;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.DetailQuizActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizzesAdapter extends RecyclerView.Adapter<QuizzesAdapter.ViewHolder> {

    private final List<Quizzes> listeQuizzes;
    private final Map<String, ResultatQuiz> resultats;

    public QuizzesAdapter(List<Quizzes> listeQuizzes, Map<String, ResultatQuiz> resultats) {
        this.listeQuizzes = listeQuizzes;
        this.resultats = resultats != null ? resultats : new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quizzes quiz = listeQuizzes.get(position);
        int nbQuestions = quiz.getQuestions() != null ? quiz.getQuestions().size() : 0;

        holder.tvTitre.setText(quiz.getTitle());
        holder.tvNbQuestions.setText(nbQuestions + " question(s)");

        if (quiz.getDuration() > 0) {
            holder.tvDuree.setText(quiz.getDuration() + " min");
        } else {
            holder.tvDuree.setVisibility(View.GONE);
        }

        ResultatQuiz resultat = resultats.get(quiz.getId());
        if (resultat != null) {
            holder.tvStatut.setText("Terminé");
            holder.tvStatut.setTextColor(Color.parseColor("#2E7D32"));
            holder.tvScore.setVisibility(View.VISIBLE);
            holder.tvScore.setText("Score : " + resultat.getScore() + " / " + resultat.getTotal());
            holder.tvScore.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            holder.tvStatut.setText("Non commencé");
            holder.tvStatut.setTextColor(Color.parseColor("#546E7A"));
            holder.tvScore.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            try {
                String quizJson = new ObjectMapper().writeValueAsString(quiz);
                Intent intent = new Intent(context, DetailQuizActivity.class);
                intent.putExtra("quizJson", quizJson);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() { return listeQuizzes.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre, tvNbQuestions, tvDuree, tvStatut, tvScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitre = itemView.findViewById(R.id.tvQuizTitre);
            tvNbQuestions = itemView.findViewById(R.id.tvQuizNbQuestions);
            tvDuree = itemView.findViewById(R.id.tvQuizDuree);
            tvStatut = itemView.findViewById(R.id.tvQuizStatut);
            tvScore = itemView.findViewById(R.id.tvQuizScore);
        }
    }
}
