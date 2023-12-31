package com.joaquin.quizapp.cointrivia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.joaquin.quizapp.cointrivia.R;
import com.joaquin.quizapp.cointrivia.databinding.RowLeaderboardsBinding;
import com.joaquin.quizapp.cointrivia.model.User;

import java.util.ArrayList;

public class LeaderboardsAdapter extends RecyclerView.Adapter<LeaderboardsAdapter.LeaderboardViewHolder> {
    Context context;
    ArrayList<User> users;

    public LeaderboardsAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public LeaderboardsAdapter.LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_leaderboards, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardsAdapter.LeaderboardViewHolder holder, int position) {
        User user = users.get(position);

        holder.binding.name.setText(user.getName());
        holder.binding.coins.setText(String.valueOf(user.getCoins()));
        holder.binding.index.setText(String.format("#%d", position + 1));
        setBckgColor(holder.binding, position + 1);

//        Glide.with(context)
//                .load(user.getProfile())
//                .into(holder.binding.imageView7);
    }

    private void setBckgColor(RowLeaderboardsBinding binding, int rank) {
        if (rank == 1) {
            binding.imageView7.setImageResource(R.drawable.rank_one_gold_medal);
        } else if (rank == 2) {
            binding.imageView7.setImageResource(R.drawable.rank_two_silver_medal);
        } else if (rank == 3) {
            binding.imageView7.setImageResource(R.drawable.rank_three_bronze_medal);
        } else {
            binding.imageView7.setImageResource(R.drawable.certificate);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        RowLeaderboardsBinding binding;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowLeaderboardsBinding.bind(itemView);
        }
    }
}
