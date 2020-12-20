package com.example.makeanote;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public interface onItemClickListener{
        void onLikedClick(int Position);
    }

    onItemClickListener mListener;

    public void setOnItemClickListener(onItemClickListener listener){
            mListener = listener;
    }
     Context context;
     ArrayList<postUpdated> listOfPosts = new ArrayList<>();

     public PostAdapter(Context context , ArrayList<postUpdated>list)
     {
         this.context = context;
         this.listOfPosts = list;
     }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostViewHolder viewHolder =  new PostViewHolder(LayoutInflater.from(context).inflate(R.layout.post_model_be_like, parent, false));
           return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
              postUpdated listItem = listOfPosts.get(position);
              holder.ProfileImage.setImageResource(Integer.parseInt(listItem.getProfileImage()));
              holder.profileName.setText(listItem.getProfileName());
              holder.PostTime.setText(listItem.getTimeOfPost());
              holder.PostData.setText(listItem.getPostToBeUpdated());
              holder.LikedUsers.setText(String.valueOf(listItem.getLikedByCount()));

    }

    @Override
    public int getItemCount() {
        return listOfPosts.size();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder{

        TextView profileName;
        TextView PostTime;
        ImageView ProfileImage;
        TextView PostData;
        TextView LikedUsers;
        ImageView LikedBy;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            profileName = itemView.findViewById(R.id.creatorName);
            PostTime = itemView.findViewById(R.id.cretaePostTime);
            ProfileImage = itemView.findViewById(R.id.postCreatorModelImageView);
            PostData = itemView.findViewById(R.id.postCreate);
            LikedUsers = itemView.findViewById(R.id.likedByDetails);
            LikedBy = itemView.findViewById(R.id.likePostButton);

            LikedBy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null)
                    {
                              int Position = getAdapterPosition();
                                if(Position != RecyclerView.NO_POSITION)
                                {
                                   mListener.onLikedClick(Position);
                                }
                    }
                }
            });
        }
    }



}


