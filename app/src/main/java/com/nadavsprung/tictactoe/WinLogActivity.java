package com.nadavsprung.tictactoe;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WinLogActivity extends AppCompatActivity {
    MyDatabaseHelper myDB;
    RecyclerView recyclerView;
    Log[] arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.win_track);

        // Handle window insets (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myDB = new MyDatabaseHelper(this);
        displayData(); // Load data from the database

        // Set up RecyclerView
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewCustomAdapter(arr));
    }

    // Reads data from the database and fills the 'arr' array
    void displayData() {
        int index = 0;
        Cursor cursor = myDB.readAllData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            arr = new Log[cursor.getCount()];
            while (cursor.moveToNext()) {
                Log log = new Log(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3)
                );
                arr[index++] = log;
            }
        }
    }
}
