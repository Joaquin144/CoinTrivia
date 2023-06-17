package com.joaquin.quizapp.cointrivia.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.joaquin.quizapp.cointrivia.R;
import com.joaquin.quizapp.cointrivia.adapter.LeaderboardsAdapter;
import com.joaquin.quizapp.cointrivia.databinding.FragmentLeaderboardsBinding;
import com.joaquin.quizapp.cointrivia.model.User;
import com.joaquin.quizapp.cointrivia.utils.Constants;

import java.util.ArrayList;

public class LeaderboardsFragment extends Fragment {
    private FragmentLeaderboardsBinding binding;
    private FirebaseFirestore database;

    public LeaderboardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLeaderboardsBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();

        setupUI();

        return binding.getRoot();
    }

    private void setupUI() {
        final ArrayList<User> users = new ArrayList<>();
        final LeaderboardsAdapter adapter = new LeaderboardsAdapter(getContext(), users);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database.collection(Constants.FS_USERS_COLLECTION)
                .orderBy("coins", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            User user = snapshot.toObject(User.class);
                            users.add(user);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}