package com.joaquin.quizapp.cointrivia.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.joaquin.quizapp.cointrivia.R;
import com.joaquin.quizapp.cointrivia.databinding.ActivitySettingsBinding;
import com.joaquin.quizapp.cointrivia.utils.Constants;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;

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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}