package com.nufdev.firelink.activities;

import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nufdev.firelink.MainActivity;
import com.nufdev.firelink.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nuf on 04/09/2016.
 */
public class createFirelink extends AppCompatActivity {
    @BindView(R.id.myLink)
    EditText myLink;
    @BindView(R.id.pasteBtn)
    ImageButton pasteBtn;
    ClipboardManager clipboard;
    String cpValue;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cpValue = clipboard.getText().toString();
        if (URLUtil.isValidUrl(cpValue)) {
            myLink.setText(cpValue);
        }


    }

    @OnClick(R.id.pasteBtn)
    public void submit(View view) {
        cpValue = clipboard.getText().toString();
        myLink.setText(cpValue);

    }

    @OnClick(R.id.fireBtn)
    public void fireLink(View view) {
        String url = myLink.getText().toString();
        if (URLUtil.isValidUrl(url)) {
            mDatabase.child("users").child(MainActivity.firelinkUser.getUid()).child("link").child("url").setValue(url);
        }
    }
}
