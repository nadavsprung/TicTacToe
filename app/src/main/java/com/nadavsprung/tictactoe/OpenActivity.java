package com.nadavsprung.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OpenActivity extends AppCompatActivity {

    private View splashLayout;
    private View fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.openactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Skip login if already signed in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String name = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : currentUser.getEmail();
            goToMain(name);
            return;
        }

        splashLayout = findViewById(R.id.splashLayout);
        fragmentContainer = findViewById(R.id.fragmentContainer);

        new Handler(Looper.getMainLooper()).postDelayed(this::showLoginFragment, 1500);
    }

    private void showLoginFragment() {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragmentContainer, new LoginFragment())
            .commitNow();

        splashLayout.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);
        fragmentContainer.setTranslationY(600f);
        fragmentContainer.animate().translationY(0f).setDuration(450).start();
    }

    public void goToMain(String username) {
        getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            .edit().putString("USERNAME", username).apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
