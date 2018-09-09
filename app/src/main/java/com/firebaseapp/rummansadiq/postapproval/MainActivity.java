package com.firebaseapp.rummansadiq.postapproval;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    CustomSwipeAdaptor customSwipeAdaptor;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LoadImages loadImages = new LoadImages();
        loadImages.execute();

//        viewPager = (ViewPager) findViewById(R.id.view_pager);
//
//        customSwipeAdaptor = new CustomSwipeAdaptor(MainActivity.this, new Post[4]);
//        viewPager.setAdapter(customSwipeAdaptor);






    }

    public void disapproveImage(View view) {

    }

    private class LoadImages extends AsyncTask<Void, Integer, Void> {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        String jsonFileName;

        @Override
        protected Void doInBackground(Void... voids) {

            jsonFileName = "";

            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    ArrayList<DataSnapshot> jsonFiles= new ArrayList<>();
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                        if(d.getKey().equalsIgnoreCase(""))
                        jsonFiles.add(d);
                    }


                    Collections.reverse(jsonFiles);

                    ArrayList<Image> imageArrayList = new ArrayList<>();

                    int totalImages = 0;


                    Iterator<DataSnapshot> iterator = jsonFiles.iterator();
                    while (iterator.hasNext() ) {
                        DataSnapshot jsonFile = iterator.next();

                        if (totalImages > 300) {
                            break;
                        }

                        jsonFileName = jsonFile.getKey();

                        for (DataSnapshot ds : jsonFile.getChildren()) {
                            String postKey = ds.getKey();

                            // Post Approved
                            String postApprove = ds.child("post_approved").getValue(String.class);

                            if (postApprove.equalsIgnoreCase("True")) {


                                // Post Url
                                String url = ds.child("post_url").getValue(String.class);

                                // Post Titles
                                String[] titles = new String[3];
                                DataSnapshot dsTitles = ds.child("titles");

                                for (DataSnapshot dsTitle : dsTitles.getChildren()) {

                                    titles[0] = dsTitle.getValue(String.class);
                                }

                                titles[1] = "";
                                titles[2] = "";

                                // Post Tags
                                DataSnapshot dsTags = ds.child("tags");

                                int j = 0;

                                String[] tags = new String[(int) dsTags.getChildrenCount()];

                                for (DataSnapshot dsTag : dsTags.getChildren()) {
                                    tags[j] = dsTag.getValue(String.class);
                                    j++;
                                }

                                //Creating a post object

                                Post post = new Post(jsonFileName, postKey, postApprove, url, titles, tags);

                                // Images
                                DataSnapshot dsImages = ds.child("images");

                                int index = 0;
                                for (DataSnapshot singleImg : dsImages.getChildren()) {

                                    // Image Approved
                                    String imgApprove = singleImg.child("img_approved").getValue(String.class);

                                    if (imgApprove.equalsIgnoreCase("true")) {

                                        // Image Before
                                        String imgBefore1 = singleImg.child("img_before1").getValue(String.class);
                                        String imgBefore2 = singleImg.child("img_before2").getValue(String.class);

                                        // Image After
                                        String imgAfter = singleImg.child("img_after").getValue(String.class);

                                        // Image Source
                                        String imgSource = singleImg.child("img_src").getValue(String.class);

                                        imageArrayList.add(new Image(post, index, imgBefore1, imgBefore2, imgAfter, imgApprove, imgSource));

                                        totalImages++;
                                    }
                                    index++;
                                }

                            }
                            if (totalImages > 300) {
                                break;
                            }

                        }
                    }
                    final Image[] arr = imageArrayList.toArray( new Image[imageArrayList.size()]);
                    viewPager = (ViewPager) findViewById(R.id.view_pager);

                    customSwipeAdaptor = new CustomSwipeAdaptor(MainActivity.this, arr, viewPager);
                    viewPager.setAdapter(customSwipeAdaptor);


                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int i, float v, int i1) {

                        }

                        @Override
                        public void onPageSelected(int i) {
//
                            if(i > 0) {
//                                Toast.makeText(customSwipeAdaptor.ctx, "Item Approved : " + (viewPager.getCurrentItem() - 1), Toast.LENGTH_SHORT).show();
                                customSwipeAdaptor.images[i - 1].imgApproved = "done";
                                mRootRef.child(customSwipeAdaptor.images[i - 1].parentPost.jsonFileName).child(customSwipeAdaptor.images[i - 1].parentPost.postKey).child("images/" + customSwipeAdaptor.images[i - 1].imageIndex).child("img_approved").setValue("done");

                                customSwipeAdaptor.notifyDataSetChanged();

                                viewPager.setCurrentItem(0);
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(int i) {

                        }
                    });

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
        }


    }

}
