package com.joaquin.quizapp.cointrivia.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joaquin.quizapp.cointrivia.databinding.FragmentWalletBinding;
import com.joaquin.quizapp.cointrivia.model.User;
import com.joaquin.quizapp.cointrivia.model.WithdrawRequest;
import com.joaquin.quizapp.cointrivia.utils.Constants;

public class WalletFragment extends Fragment {

    private FragmentWalletBinding binding;
    private FirebaseFirestore database;
    private User user;

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWalletBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();

        setupUI();

        return binding.getRoot();
    }

    private void setupUI() {
        database.collection(Constants.FS_USERS_COLLECTION).document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                binding.currentCoins.setText(String.valueOf(user.getCoins()));

                //binding.currentCoins.setText(user.getCoins() + "");

            }
        });

        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getCoins() > Constants.MINIMUM_COINS_BEFORE_MONEY_WITHDRAW) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    String payPal = binding.emailBox.getText().toString();
                    WithdrawRequest request = new WithdrawRequest(uid, payPal, user.getName());
                    database.collection(Constants.FS_WITHDRAWS_COLLECTION).document(uid).set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Request sent successfully.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "You need more coins to get withdraw.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}