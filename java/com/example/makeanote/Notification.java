package com.example.makeanote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {

    private ImageView goToHomeButton;
    private RecyclerView recyclerView;
    private ArrayList<notifyModel> mlist;
    FirebaseFirestore db;
    private NotifyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Initalize();
        getNotified();

        goToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notification.this, Posts.class);
                startActivity(intent);
            }
        });
    }

    public void Initalize(){
        goToHomeButton = findViewById(R.id.goToHome);
        recyclerView = findViewById(R.id.recyclerViewNotify);
        db = FirebaseFirestore.getInstance();
        mlist = new ArrayList<notifyModel>();

        recyclerView.setLayoutManager(new LinearLayoutManager(Notification.this));
        adapter = new NotifyAdapter(Notification.this, mlist);
        recyclerView.setAdapter(adapter);
    }

    public void getNotified()
    {
        mlist.clear();
        CollectionReference cr= db.collection("posts");
        Query query = cr.orderBy("timeMillis", Query.Direction.DESCENDING).limit(10);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            String ImageId = documentSnapshot.getString("profileImage");
                            String name = documentSnapshot.getString("profileName");
                            String post = documentSnapshot.getString("postToBeUpdated");
                            mlist.add(new notifyModel(ImageId, name,post));

                            if(mlist.size()>10)
                          {
                              mlist.remove(0);
                          }
                           adapter.notifyDataSetChanged();
                        }
                    }
            }
        });

//
    }
}