package com.nufdev.firelink.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.nufdev.firelink.MainActivity;
import com.nufdev.firelink.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {

    public String strSnap;

    //[BUTTONS]:
    @BindView(R.id.openBtn)
    Button openBtn;
    @BindView(R.id.copyBtn)
    Button copyBtn;

    //[TEXTVIEWS]:
    @BindView(R.id.firelinkView)
    HTextView firelinkView;
    @BindView(R.id.progressLayout)

    //[LAYOUTS]:
            RelativeLayout progressLayout;
    @BindView(R.id.firelinkLayout)
    RelativeLayout firelinkLayout;

    public static View Mainview;
    private int timeout = 1;
    private OnFragmentInteractionListener mListener;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public String snap(String txt) {
        if(txt.length() > 22) {
            return txt.substring(0, 22) + "...";
        }
        else {
            return txt;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Mainview = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, Mainview);
        progressLayout.setVisibility(View.VISIBLE);
        firelinkLayout.setVisibility(View.GONE);
        if (MainActivity.firelinkUser != null) {

            mDatabase.child("users").child(MainActivity.firelinkUser.getUid()).child("link").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snap) {
                            // Get user value

                            strSnap = String.valueOf(snap.child("url").getValue().toString());
                            Log.v("Link:", strSnap);
                            //firelinkView.setText(strSnap);
                            firelinkView.setAnimateType(HTextViewType.SCALE);
                            if(strSnap != null) {
                                firelinkView.animateText(snap(strSnap)); // animate
                                progressLayout.setVisibility(View.GONE);
                                firelinkLayout.setVisibility(View.VISIBLE);
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

            mDatabase.child("users").child(MainActivity.firelinkUser.getUid()).child("link").addChildEventListener(
                    new ChildEventListener() {

                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            try {

                                strSnap = String.valueOf(dataSnapshot.getValue(String.class));
                                Log.v("Link:", strSnap);
                                if(strSnap != null) {
                                    firelinkView.animateText(snap(strSnap)); // animate
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            strSnap = String.valueOf(dataSnapshot.getValue(String.class));
                            Log.v("Link:", strSnap);
                            if(strSnap != null) {
                                firelinkView.animateText(snap(strSnap)); // animate
                            }
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            strSnap = String.valueOf(dataSnapshot.getValue(String.class));
                            Log.v("Link:", strSnap);
                            if(strSnap != null) {
                                firelinkView.animateText(snap(strSnap)); // animate
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.v("Error::", databaseError.getMessage().toString());

                        }
                    });

        }
        firelinkView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast = Toast.makeText(getActivity(), "Firelink: " + strSnap, Toast.LENGTH_LONG);
                toast.show();
                return true;
            }
        });
        return Mainview;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }


    @OnClick(R.id.firelinkView)
    public void openlinkFromView(View view) {
        openLink(view);
    }

    @OnClick(R.id.openBtn)
    public void openLink(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strSnap));
        startActivity(browserIntent);
    }

    @OnClick(R.id.copyBtn)
    public void copyLink(View view) {

        if (timeout == 1) {
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(strSnap);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Linkl", strSnap);
                clipboard.setPrimaryClip(clip);
            }

            Snackbar snackbar = Snackbar
                    .make(view, "Firelink copied!", Snackbar.LENGTH_SHORT);

            snackbar.show();
            timeout = 0;
            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {
                    Log.v("seconds remaining: ", String.valueOf(millisUntilFinished / 1000));
                }
                public void onFinish() {
                    timeout = 1;
                }
            }.start();
        }

    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }



}
