package com.example.makeanote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Posts extends AppCompatActivity {

    private FirebaseAuth authNew;

    private FloatingActionButton notifyButton;
    private FloatingActionButton Fab;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private ArrayList<postUpdated> mPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        mPost = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewForPost);
        Fab = findViewById(R.id.createAPost);
        notifyButton = findViewById(R.id.notifyPageMovement);

        getPosts();
        SetRecycler();

        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Posts.this, Notification.class);
                startActivity(intent);
            }
        });

        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Posts.this, CreatePosts.class);
                startActivity(intent);
            }
        });

        adapter.setOnItemClickListener(new PostAdapter.onItemClickListener() {
            @Override
            public void onLikedClick(int Position) {
                OnLikedByUser(Position);
            }
        });

    }

    public void getPosts()
    {
        mPost.clear();
           FirebaseFirestore db = FirebaseFirestore.getInstance();
           CollectionReference collectionReference = db.collection("posts");

           Query query = collectionReference.orderBy("timeMillis", Query.Direction.DESCENDING);

           query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if(task.isSuccessful())
                      {
                          for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                          {
                                 String nameOfUser = documentSnapshot.getString("profileName");
                                 String userImage = documentSnapshot.getString("profileImage");
                                 String currentPostTime = documentSnapshot.getString("timeOfPost");
                                 String postContentPosted = documentSnapshot.getString("postToBeUpdated");
                                 Long LikedByUsers = documentSnapshot.getLong("likedByCount");
                                 Long timeMillisIn = documentSnapshot.getLong("timeMillis");
                                 Long likedCounts = documentSnapshot.getLong("likedByCount");
                                 ArrayList<String> likedByUsers = (ArrayList<String>) documentSnapshot.get("likedBy");

                                 postUpdated postNew = new postUpdated(postContentPosted, userImage, nameOfUser, timeMillisIn, currentPostTime, likedCounts, likedByUsers);
                                 mPost.add(postNew);
                          }
                          adapter.notifyDataSetChanged();
                      }
                      else
                      {
                          Toast.makeText(Posts.this, "You try to practice it more", Toast.LENGTH_SHORT).show();
                      }
               }
           });
    }

    public void SetRecycler()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(Posts.this));
        adapter = new PostAdapter(Posts.this, mPost);
        recyclerView.setAdapter(adapter);
    }


    public void OnLikedByUser(int Position){
        postUpdated postCurrent = mPost.get(Position);

        //follow background thread
        class LikedBySomeOne extends AsyncTask<Void, Void, Void>{


            @Override
            protected Void doInBackground(Void... voids) {

                FirebaseAuth mAuthNew = FirebaseAuth.getInstance();
                String userId = mAuthNew.getCurrentUser().getUid();

                FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                DocumentReference dr1 = db1.collection("users").document(userId);

                dr1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String name = value.getString("userName");

                        if(!postCurrent.likedBy.contains(name))
                        {
                            postCurrent.setLikedBy(name);
                            postCurrent.likedByCount = postCurrent.likedBy.size();
                            mPost.remove(Position);
                            mPost.add(Position, postCurrent);
                            updateDataInFirebase(postCurrent);
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            postCurrent.removeUser(name);
                            postCurrent.likedByCount = postCurrent.likedBy.size();
                            mPost.remove(Position);
                            mPost.add(Position, postCurrent);
                            updateDataInFirebase(postCurrent);
                            adapter.notifyDataSetChanged();
                        }

                        // this is an end
                    }
                });

                return null;//end with background one
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
            }
        }//background class ended here
        new LikedBySomeOne().execute();
        adapter.notifyDataSetChanged();



    }

    private void updateDataInFirebase(postUpdated postCurrent) {

        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        CollectionReference cr2 = db2.collection("posts");

        long timeMillis = postCurrent.getTimeMillis();
        Query query = cr2.whereEqualTo("timeMillis", timeMillis);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        DocumentReference dr2 = documentSnapshot.getReference();

                       dr2.set(postCurrent);

                    }
                }
                else
                {
                    Toast.makeText(Posts.this, "error in fetching data from firebaseFirestore", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_xml_options, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logout_Button)
        {
             authNew = FirebaseAuth.getInstance();

            authNew.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.moveToProfileSection)
        {
            Intent intent = new Intent(Posts.this, ProfileUpdate.class);
            startActivity(intent);
        }

        return true;
    }



}