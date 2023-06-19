package com.joaquin.quizapp.cointrivia.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.joaquin.quizapp.cointrivia.activities.SpinnerActivity;
import com.joaquin.quizapp.cointrivia.adapter.CategoryAdapter;
import com.joaquin.quizapp.cointrivia.databinding.FragmentHomeBinding;
import com.joaquin.quizapp.cointrivia.model.CategoryModel;
import com.joaquin.quizapp.cointrivia.utils.Constants;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FirebaseFirestore database;
    private SharedPreferences sharedPreferences;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        sharedPreferences = requireContext().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        setupUI();

        return binding.getRoot();
    }

    private void setupUI() {
        final ArrayList<CategoryModel> categories = new ArrayList<>();

        final CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);

        database.collection(Constants.FS_CATEGORIES_COLLECTION)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        categories.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            CategoryModel model = snapshot.toObject(CategoryModel.class);
                            model.setCategoryId(snapshot.getId());
                            categories.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });


        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.categoryList.setAdapter(adapter);

        binding.spinwheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCooldownOver()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("lastPlayedTime", System.currentTimeMillis());
                    editor.apply();
                    startActivity(new Intent(getContext(), SpinnerActivity.class));
                }else{
                    Toast.makeText(requireContext(), "You can only spin wheel once in every 24 hours", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isCooldownOver() {
        long lastTimePlayed = sharedPreferences.getLong("lastPlayedTime", 0);
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastTimePlayed;
        long hoursElapsed = elapsedTime / (1000 * 60 * 60); // Convert milliseconds to hours

        return hoursElapsed >= 24;
    }
}