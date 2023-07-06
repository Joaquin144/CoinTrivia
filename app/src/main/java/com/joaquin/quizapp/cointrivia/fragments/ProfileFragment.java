package com.joaquin.quizapp.cointrivia.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joaquin.quizapp.cointrivia.R;
import com.joaquin.quizapp.cointrivia.databinding.FragmentProfileBinding;
import com.joaquin.quizapp.cointrivia.utils.Constants;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private FirebaseFirestore database;
    private FirebaseUser firebaseUser;
    private static final String TAG = "##@@ProfileFrag";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        setupUI();

        return binding.getRoot();
    }

    private void setupUI() {
        binding.updateBtn.setOnClickListener(v -> {
            String name = binding.nameBox.getText().toString();
            String uid = firebaseUser.getUid();

            database
                    .collection(Constants.FS_USERS_COLLECTION)
                    .document(uid)
                    .update("name", name).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(requireContext(), "Details Updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, task.getException().getLocalizedMessage());
                                Toast.makeText(requireContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }
}