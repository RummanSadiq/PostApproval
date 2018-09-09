package com.firebaseapp.rummansadiq.postapproval;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomSwipeAdaptor extends PagerAdapter {

    private int[] image_resources = {R.mipmap.s1, R.mipmap.s2, R.mipmap.s3, R.mipmap.s4, R.mipmap.s5, R.mipmap.s6, R.mipmap.s7};
    public  Image[] images;
    private ViewPager viewPager;
    private boolean allGood;

    public DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


    public Context ctx;

    private LayoutInflater layoutInflater;


    public CustomSwipeAdaptor(Context ctx, Image[] images, ViewPager view) {
        this.images = images;
        this.viewPager = view;
        this.ctx = ctx;

    }
    @Override
    public int getCount() {

        ArrayList<Image> imgList = new ArrayList<>();
        for (int i = 0; i < images.length ; i++) {
            if(images[i].parentPost.postApproved.equalsIgnoreCase("true")) {
                if(images[i].imgApproved.equalsIgnoreCase("true")) {
                    imgList.add(images[i]);
                }
            }
        }

        images = imgList.toArray( new Image[imgList.size()]);
        return images.length;
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (ScrollView) o);
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        allGood = false;
        String src = images[position].imgSource;

        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.swipe_layout, null);

        final ImageView imageView = view.findViewById(R.id.image_view);

        TextView postTitle = (TextView) ((Activity) ctx).findViewById(R.id.post_title);
        postTitle.setText(images[viewPager.getCurrentItem()].parentPost.postTitle[0]);


        LinearLayout imgDel = (LinearLayout) ((Activity) ctx).findViewById(R.id.img_del);
        imgDel.setOnClickListener(new LinearLayout.OnClickListener() {

            @Override
            public void onClick(View view) {
                // write code to change this attribute of every image in the given post
                delImage();

            }
        });

        ImageButton postDel = (ImageButton) ((Activity) ctx).findViewById(R.id.post_del);
        postDel.setOnClickListener(new ImageButton.OnClickListener() {

            @Override
            public void onClick(View view) {
                // write code to change this attribute of every image in the given post
                delPost();

            }
        });





        TextView beforeText1 = view.findViewById(R.id.before_text1);
        TextView beforeText2 = view.findViewById(R.id.before_text2);
        TextView afterText = view.findViewById(R.id.after_text);

        if(images[position].imgBefore1 != null) {
            if(!images[position].imgBefore1.equalsIgnoreCase("")) {

                beforeText1.setText(images[position].imgBefore1);
            } else {

                beforeText1.setVisibility(View.GONE);
            }
        } else {
            beforeText1.setVisibility(View.GONE);
        }
        if(images[position].imgBefore2 != null) {
            if(!images[position].imgBefore2.equalsIgnoreCase("")) {

                beforeText2.setText(images[position].imgBefore2);
            } else {

                beforeText2.setVisibility(View.GONE);
            }
        } else {
            beforeText2.setVisibility(View.GONE);
        }
        if(images[position].imgAfter != null) {
            if(!images[position].imgAfter.equalsIgnoreCase("")) {

                afterText.setText(images[position].imgAfter);
            } else {

                afterText.setVisibility(View.GONE);
            }
        } else {
            afterText.setVisibility(View.GONE);
        }

//        beforeText1.setText("Order in which tables are populated, keeping the foregin key relationships in mind.");
//        beforeText2.setText("Users(UserID primary key, FirstName varchar, LastName varchar, Email varchar, Password varchar, Reputation int, Reported varchar,  CreationDate datetime, PhoneNo varchar, CountryID foregin key Countries(CountriesId))");
//        afterText.setText("Question(QuestionID primary key, Title varchar, Description varchar, AttachmentURL varchar, Votes int, Reported varchar, AuthorID foregin key Users(UserID), SubjectID foregin key Subjects(SubjectID), CreationDate datetime)");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.placeholder(R.mipmap.loading);

        Glide.with(ctx).load(src)
                .apply(requestOptions)
                .error(Glide.with(ctx).load(R.drawable.no_internet))
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                allGood = false;
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                allGood = true;


                return false;
            }
        }).into(imageView);



        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    private void delImage() {
        images[viewPager.getCurrentItem()].imgApproved = "false";

        mRootRef.child(images[viewPager.getCurrentItem()].parentPost.jsonFileName).child(images[viewPager.getCurrentItem()].parentPost.postKey).child("images/" + images[viewPager.getCurrentItem()].imageIndex).child("img_approved").setValue("false");

        this.notifyDataSetChanged();
        viewPager.setCurrentItem(0);
    }
    public void delPost() {
        images[viewPager.getCurrentItem()].parentPost.postApproved = "false";

        mRootRef.child(images[viewPager.getCurrentItem()].parentPost.jsonFileName).child(images[viewPager.getCurrentItem()].parentPost.postKey).child("post_approved").setValue("false");

        this.notifyDataSetChanged();
        viewPager.setCurrentItem(0);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

}
