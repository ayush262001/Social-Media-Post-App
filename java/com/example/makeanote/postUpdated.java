package com.example.makeanote;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class postUpdated {

         private String profileImage;
         private String profileName;
         private String timeOfPost;
         private String postToBeUpdated;
         private long timeMillis;
         public ArrayList<String> likedBy = new ArrayList<>();
         public long likedByCount;
         public postUpdated()
         {
             //default constructor
         }

         public postUpdated(String postToBeUpdated, String profileImage, String profileName, long timeMillis, String timeOfPost, long likedByCount, ArrayList<String> likedBy)
         {
             this.profileImage= profileImage;
             this.profileName = profileName;
             this.timeOfPost = timeOfPost;
             this.timeMillis = timeMillis;
             this.postToBeUpdated = postToBeUpdated;
             this.likedBy = likedBy;
             this.likedByCount = likedByCount;
         }

         public String getProfileImage(){return profileImage;}
         public String getProfileName(){return profileName;}
         public String getTimeOfPost(){return timeOfPost;}
         public String getPostToBeUpdated(){return postToBeUpdated;}
         public int getLikedByCount(){return likedBy.size();}
         public long getTimeMillis(){return timeMillis;}
        public ArrayList<String> getLikedBy(){return likedBy;}

         public void setProfileImage(String id){this.profileImage = id;}
         public void setProfileName(String name){this.profileName = name;}
         public  void setTimeOfPost(String time){this.timeOfPost = time;}
         public void setPostToBeUpdated(String post){this.postToBeUpdated = post;}
         public void setLikedBy(String userName){this.likedBy.add(userName);}

         public void removeUser(String userName){ this.likedBy.remove(userName);}
}
