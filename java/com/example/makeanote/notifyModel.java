package com.example.makeanote;

import android.widget.ImageView;
import android.widget.TextView;

public class notifyModel {

    private String profileImage;
    private String profileName;
    private String posts;

          public notifyModel(String profileImage, String profileName, String posts){
              this.profileImage = profileImage;
              this.profileName = profileName;
              this.posts = posts;
          }

          public String getProfileImage(){return profileImage;}
          public String getProfileName(){return profileName;}
          public String getPosts(){return posts;}

}
