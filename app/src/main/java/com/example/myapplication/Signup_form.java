package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;


public class Signup_form<FirebaseStorage, StorageReference> extends AppCompatActivity {
    String url;
    EditText Description;
    Button button_submit;
    Spinner type_of_crime;
    private Button button_image, upld_button, button_store;
    private ImageView imageView;

    int a = 0;
    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    DatabaseReference mydatabase;
    FirebaseAuth mauth;
    com.google.firebase.storage.FirebaseStorage storage = com.google.firebase.storage.FirebaseStorage.getInstance();
    private String status = "Reported";
    com.google.firebase.storage.StorageReference storageRef = storage.getReferenceFromUrl("gs://myapplication2-91004.appspot.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);
        getSupportActionBar().setTitle("Report a Crime");

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00DFFC"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        mydatabase = FirebaseDatabase.getInstance().getReference().child("Report");
        mauth = FirebaseAuth.getInstance();

        Description = findViewById(R.id.Description);
        button_submit = (Button) findViewById(R.id.button_submit);
        type_of_crime = (Spinner) findViewById(R.id.type_of_crime);
        button_image = (Button) findViewById(R.id.button_image);
        upld_button = (Button) findViewById(R.id.upld_button);
        imageView = (ImageView) findViewById(R.id.imgView);
        button_store = (Button) findViewById(R.id.button_store);

        GPSTracker gps = new GPSTracker(this);
        final double latitude = gps.getLatitude();
        final double longitude = gps.getLongitude();

        button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }

            private void chooseImage() {
                a = 1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        upld_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                a=2;
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePicture.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePicture, 1);
                }
            }
        });

        button_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(Description.getText().toString().equals(""))) {
                    HashMap<String, String> reports = new HashMap<String, String>();
                    String CurrentDateTime = DateFormat.getDateTimeInstance().format(new Date());
                    reports.put("Description", Description.getText().toString());
                    reports.put("Type of Crime", type_of_crime.getSelectedItem().toString());
                    reports.put("Latitude", Double.toString(latitude));
                    reports.put("Longitude", Double.toString(longitude));
                    reports.put("Status", status);
                    reports.put("URL", url);

                    mydatabase.child(mauth.getCurrentUser().getPhoneNumber()).child(run()).setValue(reports).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Signup_form.this, "Your Report has been Submitted", Toast.LENGTH_LONG).show();
                            Intent inte = new Intent(Signup_form.this, MapsActivity.class);
                            startActivity(inte);
                            finish();

                        }
                    });
                }
                else{
                    Toast.makeText(Signup_form.this, "Please fill the Description.", Toast.LENGTH_SHORT).show();
                    Description.requestFocus();
                }
            }

            public String run() {
                Calendar calc = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance().format(calc.getTime());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                return (currentDate + " " + currentTime);
            }

        });

    }

    private void startPosting() {
        if (filePath != null) {
            Calendar calc = Calendar.getInstance();
            String currentDate = DateFormat.getDateInstance().format(calc.getTime());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            com.google.firebase.storage.StorageReference childRef = storageRef.child(mauth.getCurrentUser().getPhoneNumber()).child((currentDate + " " + currentTime));
            //uploading the image
            UploadTask uploadTask = childRef.putFile(filePath);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Signup_form.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                    url = taskSnapshot.getStorage().getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Signup_form.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(Signup_form.this, "Select an image", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (a == 2) {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap image = (Bitmap) extras.get("data");
                imageView.setImageBitmap(image);


            }
        }
        else
        {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    }

