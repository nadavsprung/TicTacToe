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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });


        myDB = new MyDatabaseHelper(WinLogActivity.this);
        displayData();

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewCustomAdapter adapter = new RecyclerViewCustomAdapter(arr);
        recyclerView.setAdapter(adapter);





    }
    void displayData(){
        int index=0;
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount()==0){
            Toast.makeText(this,"No Data",Toast.LENGTH_SHORT).show();
        }else{
            arr=new Log[cursor.getCount()];
            while (cursor.moveToNext()){
                long id=cursor.getLong(0);
                String winner =cursor.getString(1);
                int moves =cursor.getInt(2);
                String date=cursor.getString(3);
                Log log=new Log(id,winner,moves,date);
                arr[index]=log;
                index++;


            }

        }




    }


}