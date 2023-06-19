package com.joaquin.quizapp.cointrivia.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import com.joaquin.quizapp.cointrivia.R;
import com.joaquin.quizapp.cointrivia.databinding.ActivityPrivacyPolicyBinding;

public class PrivacyPolicyActivity extends AppCompatActivity {
    ActivityPrivacyPolicyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupUI();
    }

    private void setupUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //privacyPolicyTv.setText(Html.fromHtml(getString(R.string.privacy_policy_text), Html.FROM_HTML_MODE_LEGACY));
            binding.privacyPolicyTv.setText(Html.fromHtml(getString(R.string.privacy_policy_text), Html.FROM_HTML_MODE_LEGACY));
        } else {
            //privacyPolicyTv.setText(Html.fromHtml(getString(R.string.privacy_policy_text)));
            binding.privacyPolicyTv.setText(Html.fromHtml(getString(R.string.privacy_policy_text)));
        }

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