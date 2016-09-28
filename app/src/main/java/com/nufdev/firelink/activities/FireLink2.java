package com.nufdev.firelink.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nufdev.firelink.R;

import butterknife.ButterKnife;

/**
 * Created by Nuf on 04/09/2016.
 */
public class FireLink2 extends AppCompatActivity {
    //Butterknife declarations:

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firelink);
        ButterKnife.bind(this);
    }

    public void ButtonClick() {

    }

    public void pushLink(String url, String comment) {
        mDatabase.child("users").child(SignIn.signA.firelinkUser.getUid()).child("link").child("url").setValue(url);
        mDatabase.child("users").child(SignIn.signA.firelinkUser.getUid()).child("link").child("comment").setValue(comment);

    }

}
