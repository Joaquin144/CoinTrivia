package com.joaquin.quizapp.cointrivia.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joaquin.quizapp.cointrivia.R;
import com.joaquin.quizapp.cointrivia.databinding.ActivitySettingsBinding;
import com.joaquin.quizapp.cointrivia.utils.Constants;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    private static final String TAG = "##@@SettingsAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupUI();
    }

    private void setupUI() {
        binding.privacyPolicyLayout.setOnClickListener(v -> {
            Intent privacyPolicyIntent = new Intent(SettingsActivity.this, PrivacyPolicyActivity.class);
            startActivity(privacyPolicyIntent);
        });

        binding.sourceCodeLayout.setOnClickListener(v -> {
            Intent sourceCodeIntent = new Intent(Intent.ACTION_VIEW);
            sourceCodeIntent.setData(Uri.parse(Constants.SOURCE_CODE_URL));
            startActivity(sourceCodeIntent);
        });

        binding.logoutLayout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent loginActivityIntent = new Intent(SettingsActivity.this, LoginActivity.class);
            loginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginActivityIntent);
            finish();
        });

        binding.contactLayout.setOnClickListener(v -> {
            Intent contactIntent = new Intent(Intent.ACTION_VIEW).setType("plain/text");
            contactIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"care.devcommop@gmail.com"});
            contactIntent.setData(Uri.parse("mailto:"));
            contactIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback/ Query about Coin Trivia App");
            contactIntent.putExtra(Intent.EXTRA_TEXT, "Email sent from Coin Trivia Android App");
            try {
                startActivity(contactIntent);
            } catch (Exception e) {
                Log.d(TAG, "Failed to open gmail app: " + e.getMessage());
                Toast.makeText(this, "Failed to open gmail app: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.deleteMyDataLayout.setOnClickListener(v -> {
            showDeleteAccountDialog();

        });


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void showDeleteAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Delete Account");
        builder.setCancelable(false);
        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone");

        // Set positive button
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleDeleteAccountConfirmation();
            }
        });

        // Set negative button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancelled, do nothing
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void handleDeleteAccountConfirmation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        if(user == null){
            Toast.makeText(this, "The user account does not exists.", Toast.LENGTH_SHORT).show();
            return;
        }
        String uid = user.getUid();
        firestore.collection(Constants.FS_USERS_COLLECTION).document(uid).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SettingsActivity.this, "Your account and data has been deleted.", Toast.LENGTH_LONG).show();
                                Intent loginActivityIntent = new Intent(SettingsActivity.this, LoginActivity.class);
                                loginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(loginActivityIntent);
                                finish();
                            } else {
                                //Toast.makeText(SettingsActivity.this, "Failed to delete user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                showExceptionDialog("Failed to delete user: " + task.getException().getMessage());
                                Log.d(TAG, "Failed to delete user: " + task.getException().getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void showExceptionDialog(String exceptionMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Some Error Occurred");
        builder.setMessage(exceptionMessage);

        // Set neutral button
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}