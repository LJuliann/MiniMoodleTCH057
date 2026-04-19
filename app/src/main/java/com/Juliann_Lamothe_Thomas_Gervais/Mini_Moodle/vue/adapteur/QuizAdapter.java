package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.DetailQuizActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    private List<Quizzes> liste;

    public QuizAdapter(List<Quizzes> liste) {
        this.liste = liste;
    }

    public void updateList(List<Quizzes> nouvelle) {
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
        Quizzes q = liste.get(position);
        holder.tvTitre.setText(q.getTitle());

        int nbQ = q.getQuestions() != null ? q.getQuestions().size() : 0;
        String info = nbQ + " question(s)";
        if (q.getDuration() > 0) info += "  •  " + q.getDuration() + " min";
        holder.tvInfo.setText(info);

        holder.itemView.setOnClickListener(v -> {
            try {
                Context ctx = v.getContext();
                String json = new ObjectMapper().writeValueAsString(q);
                Intent intent = new Intent(ctx, DetailQuizActivity.class);
                intent.putExtra("quizJson", json);
                ctx.startActivity(intent);
            } catch (Exception ignored) {}
        });
    }

    @Override
    public int getItemCount() { return liste.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre, tvInfo;

        ViewHolder(View v) {
            super(v);
            tvTitre = v.findViewById(R.id.tvTitreQuiz);
            tvInfo  = v.findViewById(R.id.tvInfoQuiz);
        }
    }
}
