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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity {

    public EditText userNameView;
    public EditText userContactView;
    public ImageView userImageView;
    public Button saveBtn;
    private ProgressBar progressBar;
    private User user;
    public String userName;
    public String userContact;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isOkay ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        userNameView = findViewById(R.id.userNameView);
        userContactView = findViewById(R.id.contactNumber);
        userImageView = findViewById(R.id.setImageView);
        saveBtn = findViewById(R.id.saveDataButton);
        progressBar = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        isOkay = false;


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                userName = userNameView.getText().toString();
                userContact = userContactView.getText().toString();
                user = new User(userName, userContact, String.valueOf(R.drawable.ic_baseline_account_box_24));
                setDataInFirebase();
            }
        });

    }

    private void setDataInFirebase() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId = firebaseUser.getUid();

        DocumentReference reference = db.collection("users").document(userId);
        reference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                      if (task.isSuccessful())
                      {
                          progressBar.setVisibility(View.GONE);
                          Intent intent = new Intent(ProfileActivity.this, Posts.class);
                          startActivity(intent);
                          finish();
                      }
                      else
                      {
                          progressBar.setVisibility(View.GONE);
                          String message = task.getException().toString();
                          Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();
                      }
            }
        });


    }

    // check this one this is giving an error when I'm login with a new id

}