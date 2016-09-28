package com.nufdev.firelink.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nufdev.firelink.R;

import butterknife.ButterKnife;

/**
 * Created by Nuf on 04/09/2016.
 */
public class createFirelink extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firelink);
        ButterKnife.bind(this);
    }


}
