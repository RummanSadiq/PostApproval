package com.firebaseapp.rummansadiq.postapproval;


public class Post {

    public String jsonFileName;
    public String postKey;
    public String postApproved;
    public String postURL;
    public String[] postTitle;
    public String[] postTags;

    public Post(String jsonFileName, String postKey, String postApproved, String postURL, String[] postTitle, String[] postTags) {
        this.jsonFileName = jsonFileName;
        this.postKey = postKey;
        this.postApproved = postApproved;
        this.postURL = postURL;
        this.postTitle = postTitle;
        this.postTags = postTags;
    }
}
