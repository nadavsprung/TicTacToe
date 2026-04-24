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
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextInputEditText editUsername, editEmail, editPassword;
    private TextView tvMessage;
    private Button btnRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();

        editUsername = view.findViewById(R.id.editUsername);
        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        tvMessage = view.findViewById(R.id.tvMessage);
        btnRegister = view.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> handleRegister());

        view.findViewById(R.id.tvToggle).setOnClickListener(v ->
            getParentFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragmentContainer, new LoginFragment())
                .commit()
        );

        return view;
    }

    private void handleRegister() {
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        btnRegister.setEnabled(false);
        btnRegister.setText("Creating account...");
        tvMessage.setVisibility(View.GONE);

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build();
                    user.updateProfile(profileUpdate)
                        .addOnCompleteListener(t -> ((OpenActivity) requireActivity()).goToMain(username));
                } else {
                    btnRegister.setEnabled(true);
                    btnRegister.setText("Register");
                    showError(task.getException().getMessage());
                }
            });
    }

    private void showError(String message) {
        tvMessage.setText(message);
        tvMessage.setVisibility(View.VISIBLE);
    }
}
