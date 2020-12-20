package com.example.makeanote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreatePosts extends AppCompatActivity {
    //in this class work on correctly getting the data of the provider user

    private EditText postWritten;
    private ProgressBar progressBar;
    private Button saveButton;
    private postUpdated postToBeUpdated;
    public String name;
    public String image;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db1;
    private FirebaseFirestore db2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_posts);

        Initalize();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getCurrentUserData();
            }
        });


    }

    private void Initalize() {

        postWritten = findViewById(R.id.editPost);
        progressBar = findViewById(R.id.progressBar3);
        saveButton = findViewById(R.id.postButton);
        mAuth = FirebaseAuth.getInstance();
        db1 = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
    }
    private void getCurrentUserData() {

        String currentUserId = mAuth.getCurrentUser().getUid().toString();
        db1.collection("users").document(currentUserId).addSnapshotListener(CreatePosts.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                 name = value.getString("userName");
                 image = value.getString("image_id");

                  setData(name, image);
            }
        });

       //end of function
    }

    private String getCurrentTime()
    {
        Calendar cal = Calendar.getInstance();
        DateFormat outputFormat = new SimpleDateFormat("KK:mm a");
        String formattedTime = outputFormat.format(cal.getTime());
        return formattedTime;
    }

    private void setData(String name, String image)
    {

        String userId = mAuth.getCurrentUser().getUid().toString();
        String time = getCurrentTime();
        String textOfPost = postWritten.getText().toString();
          Calendar calendar = Calendar.getInstance();

        long timeMillis = calendar.getTimeInMillis();
        long likedByCount =0;
        ArrayList<String> likedBy = new ArrayList<>();

       postToBeUpdated = new postUpdated(textOfPost, image, name, timeMillis, time, likedByCount, likedBy);

        db2.collection("posts").document().set(postToBeUpdated).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    progressBar.setVisibility(View.GONE);
                    Intent intent  = new Intent(CreatePosts.this, Posts.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CreatePosts.this, "error ! Not posted", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}