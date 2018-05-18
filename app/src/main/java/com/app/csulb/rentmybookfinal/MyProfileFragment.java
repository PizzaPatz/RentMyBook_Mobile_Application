package com.app.csulb.rentmybookfinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.app.csulb.rentmybookfinal.login.AuthenticationManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.User;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.provider.CalendarContract.CalendarCache.URI;

/*
    View your profile class
 */
public class MyProfileFragment extends Fragment implements View.OnClickListener{

    View view;
    TextView info, email, point;
    ImageView profile_pic;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebase;
    DatabaseReference database;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Profile");
        // Firebase initialization
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Set profile picture
        profile_pic = view.findViewById(R.id.profile_picture);
        profile_pic.setImageBitmap(Application.User.getProfilePicture());
        // Set name
        info = view.findViewById(R.id.user_name);
        info.setText(Application.User.getDisplayName());

        //Making a user in Firebase
        firebase = FirebaseDatabase.getInstance();
        database = firebase.getReference("Users");
        database.child(Application.User.getDisplayName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> temp = (HashMap<String, String>)dataSnapshot.getValue();
                String email_val = temp.get("Email");
                String point_val = temp.get("Point");
                setData(email_val, point_val);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Set field data
    private void setData(String email_val, String point_val){
        point = view.findViewById(R.id.user_point);
        point.setText("Points: "+point_val);
        email = view.findViewById(R.id.user_email);
        email.setText(email_val);
    }

    @Override
    public void onClick(View view) {

    }
}