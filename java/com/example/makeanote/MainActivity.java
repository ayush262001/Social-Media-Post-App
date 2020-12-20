package com.example.makeanote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText emailView;
    private EditText passwordView;
    private Button signInButton;
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private TextView changeView;
    private TextView createView;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            Intent intent = new Intent(MainActivity.this, Posts.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        emailView = findViewById(R.id.editemail);
        passwordView = findViewById(R.id.passwordedit);
        signInButton = findViewById(R.id.buttonSignin);
        changeView = findViewById(R.id.changePassword);
        createView = findViewById(R.id.createNewAccount);


        changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailView.getText().toString();
                password = passwordView.getText().toString();
                sendEmail();
            }
        });

        createView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(createView.getText().toString().equals("Create a new Account"))
                {
                    createView.setText("Already have an account SignIn here");
                    signInButton.setText("SignUp");
                    changeView.setVisibility(View.GONE);
                }
                else
                {
                    createView.setText("Create a new Account");
                    signInButton.setText("SignIn");
                    changeView.setVisibility(View.VISIBLE);
                }

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailView.getText().toString();
                password = passwordView.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                emailView.setVisibility(View.GONE);
                passwordView.setVisibility(View.GONE);
                signInButton.setVisibility(View.GONE);
                if(signInButton.getText().toString().equals("SignIn"))
                {
                    SigninUser();
                }
                else
                {
                    SignUpUser();
                }


            }
        });
    }

    private void SignUpUser() {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful())
                         {
                             Intent intent = new Intent(MainActivity.this, ProfileActivity.class);

                             startActivity(intent);
                             finish();
                         }
                         else
                         {
                             Toast.makeText(MainActivity.this, "Task failed", Toast.LENGTH_SHORT).show();
                             progressBar.setVisibility(View.GONE);
                             emailView.setVisibility(View.VISIBLE);
                             passwordView.setVisibility(View.VISIBLE);
                             signInButton.setVisibility(View.VISIBLE);
                         }
            }
        });


    }

    private void sendEmail() {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "Check your mail to change password", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                emailView.setVisibility(View.VISIBLE);
                passwordView.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void SigninUser() {


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent intent = new Intent(MainActivity.this, Posts.class);

                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this, "Failed to signIn", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    emailView.setVisibility(View.VISIBLE);
                    passwordView.setVisibility(View.VISIBLE);
                    signInButton.setVisibility(View.VISIBLE);
                }
            }
        });



    }

}