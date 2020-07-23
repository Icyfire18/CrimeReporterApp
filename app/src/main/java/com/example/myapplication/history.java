package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {

    private ListView listview;
    DatabaseReference databasereference;
    List<Report>list;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle("REPORTED CRIMES");

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00DFFC"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);


        mauth = FirebaseAuth.getInstance();
        listview = findViewById(R.id.list_view);
        databasereference = FirebaseDatabase.getInstance().getReference("Report").child(mauth.getCurrentUser().getPhoneNumber());

        list = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        databasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot student : dataSnapshot.getChildren()){
                    Report rep = student.getValue(Report.class);
                    list.add(rep);
                }
                info inf = new info(history.this,list);

                listview.setAdapter(inf);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
