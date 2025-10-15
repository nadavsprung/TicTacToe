package com.nadavsprung.tictactoe;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OpenActivity extends AppCompatActivity {


    // **** SQLite database
    public DatabaseHelper dbHelper;

    // **** SharedPreferences save user name in this phone
    private SharedPreferences sharedPreferences;
    private String savedUsername;

    Intent intent;
    // for popup players card
    private Dialog dialog;

    // edittext in Dialog
    private EditText editdUsername,
            editDEmail,
            editDPassword;
    // text view message
    private TextView tvDMessage;
    //Btton in Dialog
    private Button btnDRegister, btnDlogin;


    // %%  post delay   פסיקות
    private Handler handler = new Handler();

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


        // **** SQLite database
        dbHelper = new DatabaseHelper(this);

        // Retrieve the username from SharedPreferences
        this.sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        this.savedUsername = sharedPreferences.getString("USERNAME", "DefaultUser");  // "DefaultUser" is a fallback value


        // Show the loader briefly, then go to MainActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            //  startActivity(new Intent(OpenActivity.this, MainActivity.class));
            createLoginDialog(); // prevent back to splash
        }, 1500); // 1.5s—tweak as you like

    }


    public void createRegistrationDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popupregister);
        dialog.setTitle("Registration");
        dialog.setCancelable(true);


        tvDMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        tvDMessage.setVisibility(View.INVISIBLE);

        editdUsername = (EditText) dialog.findViewById(R.id.editUsername);
        editDPassword = (EditText) dialog.findViewById(R.id.editPassword);
        editDEmail = (EditText) dialog.findViewById(R.id.editEmail);

        btnDRegister = (Button) dialog.findViewById(R.id.btnRegister);
        btnDRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = editdUsername.getText().toString().trim();
                String email = editDEmail.getText().toString().trim();
                String password = editDPassword.getText().toString().trim();


                // Validate fields
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    tvDMessage.setVisibility(View.VISIBLE);
                    tvDMessage.setText("Please fill all fields");

                    return;
                }

                // Validate email format (contains '@' and '.com')
                if (!email.contains("@") || !email.contains(".com")) {
                    tvDMessage.setVisibility(View.VISIBLE);
                    tvDMessage.setText("Invalid email format. Email must contain '@' and '.com'");
                    return;
                }

                boolean isRegistered = dbHelper.registerUser(username, email, password);
                tvDMessage.setVisibility(View.VISIBLE);
                if (isRegistered) {
                    // Save the username in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USERNAME", username);  // 'username' is a variable holding the user's name
                    editor.apply(); // or editor.commit();

                    tvDMessage.setText("Registration successful");
                    handler.postDelayed(new Runnable()



                    {

                        @Override
                        public void run() {
                            intent = new Intent(OpenActivity.this,MainActivity.class);
                            startActivity(intent);

                        }
                    }, 1500);

                    // Optionally, navigate to LoginActivity here.
                } else {
                    tvDMessage.setText("Registration failed user/main exist");
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            createLoginDialog();

                        }
                    }, 1500);

                }

            }
        });

        dialog.show();
    }


    public void createLoginDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popuplogin);
        dialog.setTitle("Login");
        dialog.setCancelable(true);

        tvDMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        tvDMessage.setVisibility(View.INVISIBLE);

        editdUsername = (EditText) dialog.findViewById(R.id.editUsername);
        editDPassword = (EditText) dialog.findViewById(R.id.editPassword);

        btnDlogin = (Button) dialog.findViewById(R.id.btnLogin);
        btnDlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = editdUsername.getText().toString().trim();

                String password = editDPassword.getText().toString().trim();


                // Validate fields
                if (username.isEmpty() || password.isEmpty()) {
                    tvDMessage.setVisibility(View.VISIBLE);
                    tvDMessage.setText("Please fill all fields");

                    return;
                }


                boolean isRegistered = dbHelper.loginUserByUsername(username, password);
                tvDMessage.setVisibility(View.VISIBLE);
                if (isRegistered) {

                    tvDMessage.setText("Login successful");
                    // Save the username in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USERNAME", username);  // 'username' is a variable holding the user's name
                    editor.apply(); // or editor.commit();

                    intent = new Intent(OpenActivity.this, MainActivity.class);
                    //  intent.putExtra("USERNAME", username);

                    startActivity(intent);

                    // Optionally, navigate to LoginActivity here.
                } else {

                    tvDMessage.setText("Login failed please sign in first");
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            createRegistrationDialog();

                        }
                    }, 1500);

                }
            }
        });


        dialog.show();
    }

}


