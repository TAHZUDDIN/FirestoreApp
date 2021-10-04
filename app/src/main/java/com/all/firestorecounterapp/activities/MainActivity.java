package com.all.firestorecounterapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.all.firestorecounterapp.R;
import com.all.firestorecounterapp.models.CounterModel;
import com.all.firestorecounterapp.models.SingleEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public String TAG = "MainActivity";
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    private DatabaseReference db;

    TextView nameOne;
    TextView nameTwo;
    TextView nameThree;

    TextView likesOne;
    TextView likesTwo;
    TextView likesThree;

    ImageView likedOne;
    ImageView notlikedOne;

    ImageView likedTwo;
    ImageView notlikedTwo;

    ImageView likedThree;
    ImageView notlikedThree;

    public String documentIdOne = "one";
    public String documentIdTwo = "two";
    public String documentIdThree = "three";

    CounterModel cm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = this.getPreferences(this.MODE_PRIVATE);
        spe = sp.edit();

        db = FirebaseDatabase.getInstance().getReference("LikeCounter");


        nameOne = findViewById(R.id.nameOne);
        nameTwo = findViewById(R.id.nameTwo);
        nameThree = findViewById(R.id.nameThree);

        likesOne = findViewById(R.id.likesOne);
        likesTwo = findViewById(R.id.likesTwo);
        likesThree = findViewById(R.id.likesThree);

        likedOne = findViewById(R.id.likedOne);
        notlikedOne = findViewById(R.id.notLikedOne);

        likedTwo = findViewById(R.id.likedTwo);
        notlikedTwo = findViewById(R.id.notLikedTwo);

        likedThree = findViewById(R.id.likedThree);
        notlikedThree = findViewById(R.id.notLikedThree);


        likedOne.setOnClickListener(this);
        notlikedOne.setOnClickListener(this);

        likedTwo.setOnClickListener(this);
        notlikedTwo.setOnClickListener(this);

        likedThree.setOnClickListener(this);
        notlikedThree.setOnClickListener(this);


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cm = dataSnapshot.getValue(CounterModel.class);
                populate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        db.addValueEventListener(postListener);
    }


    public void populate(){
        likesOne.setText(cm.getOne().likes+"");
        likesTwo.setText(cm.getTwo().likes+"");
        likesThree.setText(cm.getThree().likes+"");

        nameOne.setText(cm.getOne().name);
        nameTwo.setText(cm.getTwo().name);
        nameThree.setText(cm.getThree().name);

        boolean oneIsLiked =  sp.getBoolean(documentIdOne,false);
        boolean twoIsLiked =  sp.getBoolean(documentIdTwo,false);
        boolean threeIsLiked =  sp.getBoolean(documentIdThree,false);


        if(oneIsLiked){
            likedOne.setVisibility(View.VISIBLE);
            notlikedOne.setVisibility(View.GONE);

        }else{
            likedOne.setVisibility(View.GONE);
            notlikedOne.setVisibility(View.VISIBLE);
        }

        if(twoIsLiked){
            likedTwo.setVisibility(View.VISIBLE);
            notlikedTwo.setVisibility(View.GONE);

        }else{
            likedTwo.setVisibility(View.GONE);
            notlikedTwo.setVisibility(View.VISIBLE);
        }


        if(threeIsLiked){
            likedThree.setVisibility(View.VISIBLE);
            notlikedThree.setVisibility(View.GONE);

        }else{
            likedThree.setVisibility(View.GONE);
            notlikedThree.setVisibility(View.VISIBLE);
        }

    }




    public void update(SingleEntry singleEntry, String documentId, boolean liked) {
        long likes = singleEntry.likes;
        if (liked) {
            spe.putBoolean(documentId, true);
            likes++;
        } else {
            spe.putBoolean(documentId, false);
            likes--;
        }
        spe.commit();

        db.child(documentId).child("likes").setValue(likes);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.likedOne:
                update(cm.getOne(), documentIdOne, false);
                break;
            case R.id.notLikedOne:
                update(cm.getOne(), documentIdOne, true);
                break;
            case R.id.likedTwo:
                update(cm.getTwo(), documentIdTwo, false);
                break;
            case R.id.notLikedTwo:
                update(cm.getTwo(), documentIdTwo, true);
                break;
            case R.id.likedThree:
                update(cm.getThree(), documentIdThree, false);
                break;
            case R.id.notLikedThree:
                update(cm.getThree(), documentIdThree, true);
                break;
            default:
                break;
        }

    }


}

