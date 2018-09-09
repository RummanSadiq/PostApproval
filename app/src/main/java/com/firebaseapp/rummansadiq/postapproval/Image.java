package com.firebaseapp.rummansadiq.postapproval;

public class Image {

    public Post parentPost;
    public int imageIndex;
    public String imgBefore1;
    public String imgBefore2;
    public String imgAfter;
    public String imgApproved;
    public String imgSource;

    public Image(Post parentPost, int imageIndex, String imgBefore1, String imgBefore2, String imgAfter, String imgApproved, String imgSource) {
        this.parentPost = parentPost;
        this.imageIndex = imageIndex;
        this.imgBefore1 = imgBefore1;
        this.imgBefore2 = imgBefore2;
        this.imgAfter = imgAfter;
        this.imgApproved = imgApproved;
        this.imgSource = imgSource;
    }
}
