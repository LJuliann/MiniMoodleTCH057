package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.vue.adapteur;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.R;
import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Quizzes;

import java.util.List;

public class QuizzesAdapter extends RecyclerView.Adapter<QuizzesAdapter.ViewHolder> {

    private final List<Quizzes> listeQuizzes;

    public QuizzesAdapter(List<Quizzes> listeQuizzes) {
        this.listeQuizzes = listeQuizzes;
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
        holder.tvTitre.setText(quiz.getTitle());
        int nbQuestions = quiz.getQuestions() != null ? quiz.getQuestions().size() : 0;
        holder.tvNbQuestions.setText(nbQuestions + " question(s)");
    }

    @Override
    public int getItemCount() { return listeQuizzes.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre, tvNbQuestions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitre = itemView.findViewById(R.id.tvQuizTitre);
            tvNbQuestions = itemView.findViewById(R.id.tvQuizNbQuestions);
        }
    }
}
