package com.nadavsprung.tictactoe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextInputEditText editEmail, editPassword;
    private TextView tvMessage;
    private Button btnLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        tvMessage = view.findViewById(R.id.tvMessage);
        btnLogin = view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> handleLogin());

        view.findViewById(R.id.tvToggle).setOnClickListener(v ->
            getParentFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragmentContainer, new RegisterFragment())
                .commit()
        );

        return view;
    }

    private void handleLogin() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Signing in...");
        tvMessage.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                btnLogin.setEnabled(true);
                btnLogin.setText("Login");
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String name = user.getDisplayName() != null ? user.getDisplayName() : user.getEmail();
                    ((OpenActivity) requireActivity()).goToMain(name);
                } else {
                    showError("Login failed. Check your email and password.");
                }
            });
    }

    private void showError(String message) {
        tvMessage.setText(message);
        tvMessage.setVisibility(View.VISIBLE);
    }
}
