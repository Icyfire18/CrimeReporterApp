package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class history extends AppCompatActivity {


    ArrayList<String> myarray = new ArrayList<>();
    ListView mylistview;
    FirebaseDatabase myfirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ListView mylistview = (ListView)findViewById(R.id.listview);
        ArrayAdapter<String> s = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myarray);

        myfirebase.getReference();

    }
}
