package com.example.makeanote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileUpdate extends AppCompatActivity {


    private EditText editName;
    private EditText editContacts;
    private ImageView editImage;
    private Button saveDataButton;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String CurrentUser;
    private String previousName;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        Initalize();
        getPriorExistingDatas();
        previousName = editName.getText().toString();
        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewData(previousName);
            }
        });
    }

    private void Initalize()
    {
        editName = findViewById(R.id.userNameView2);
        editContacts = findViewById(R.id.contactNumber2);
        editImage = findViewById(R.id.setImageView2);
        saveDataButton = findViewById(R.id.saveDataButton2);
        progressBar = findViewById(R.id.progressBar22);
        auth = FirebaseAuth.getInstance();
        CurrentUser = auth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
    }

    private void getPriorExistingDatas() {

        progressBar.setVisibility(View.VISIBLE);

        DocumentReference dr = db.collection("users").document(CurrentUser);
        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                editName.setText(value.getString("userName"));
                editContacts.setText(value.getString("contactNumber"));
                editImage.setImageResource(Integer.parseInt(value.getString("image_id")));

            }
        });

        progressBar.setVisibility(View.GONE);
    }

    private void setNewData(String previousName)
    {
        progressBar.setVisibility(View.VISIBLE);
        DocumentReference dr = db.collection("users").document(CurrentUser);
        User user = new User(editName.getText().toString(), editContacts.getText().toString(), String.valueOf(R.drawable.ic_baseline_account_box_24));
        dr.set(user);

        //update all previous data as well
        FirebaseFirestore dbb = FirebaseFirestore.getInstance();
        CollectionReference cr2 = dbb.collection("posts");
        Query query = cr2.whereEqualTo("profileName", previousName);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      for(DocumentSnapshot documentSnapshot: task.getResult()) {
                          DocumentReference dr1= documentSnapshot.getReference();
                          String nameOfUser = previousName;
                          String userImage = documentSnapshot.getString("profileImage");
                          String currentPostTime = documentSnapshot.getString("timeOfPost");
                          String postContentPosted = documentSnapshot.getString("postToBeUpdated");
                          Long timeMillisIn = documentSnapshot.getLong("timeMillis");
                          Long likedCounts = documentSnapshot.getLong("likedByCount");
                          ArrayList<String> likedByUsers = (ArrayList<String>) documentSnapshot.get("likedBy");

                          postUpdated postNew = new postUpdated(postContentPosted, userImage, nameOfUser, timeMillisIn, currentPostTime, likedCounts, likedByUsers);
                          dr1.set(postNew);

                      }
            }
        });



        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(ProfileUpdate.this, Posts.class);
        startActivity(intent);
    }
}