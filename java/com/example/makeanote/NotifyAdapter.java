package com.example.makeanote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder>{

    Context context;
    ArrayList<notifyModel> list = new ArrayList<>();

    public NotifyAdapter(Context context, ArrayList<notifyModel>lists){
        this.context = context; this.list= lists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.notify_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        notifyModel current = list.get(position);
        holder.profileName.setText(current.getProfileName());
        holder.profileImage.setImageResource(Integer.parseInt(current.getProfileImage()));
        holder.posts.setText(current.getPosts());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profileImage;
        TextView profileName;
        TextView posts;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImageNotify);
            profileName = itemView.findViewById(R.id.profileNameNotify);
            posts = itemView.findViewById(R.id.postNotify);

        }
    }
}
