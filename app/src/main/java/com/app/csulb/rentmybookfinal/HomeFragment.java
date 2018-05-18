package com.app.csulb.rentmybookfinal;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.app.csulb.rentmybookfinal.login.AuthenticationManager;
import com.bumptech.glide.Glide;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.provider.CalendarContract.CalendarCache.URI;


public class HomeFragment extends Fragment implements View.OnClickListener{

    View view;
    Fragment fragment;
    private Uri filePath;
    ImageView news1,news2,news3,news4,news5,news6,news7,news8;
    FirebaseStorage storage;
    StorageReference storageReference;

    private EditText search;
    private ImageButton icon;

    FirebaseDatabase data;
    DatabaseReference mBookData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
        data = FirebaseDatabase.getInstance();
        mBookData = data.getReference("Books");
        search = (EditText)view.findViewById(R.id.search_field);
        icon = (ImageButton)view.findViewById(R.id.search_button);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = search.getText().toString();
                fragment = new FindBookFragment();
                Bundle arguments = new Bundle();
                arguments.putString( "search_key" , searchText);
                fragment.setArguments(arguments);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();

            }
        });

        news1 = (ImageView)view.findViewById(R.id.imageView);
        news2 = (ImageView)view.findViewById(R.id.imageView2);
        news3 = (ImageView)view.findViewById(R.id.imageView3);
        news4 = (ImageView)view.findViewById(R.id.imageView4);
        news5 = (ImageView)view.findViewById(R.id.imageView5);
        news6 = (ImageView)view.findViewById(R.id.imageView6);
        news7 = (ImageView)view.findViewById(R.id.imageView7);
        news8 = (ImageView)view.findViewById(R.id.imageView8);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        // 1st news
        StorageReference imageRef = storage.getReferenceFromUrl("gs://rentmybook-30c9e.appspot.com/News/1.jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri.toString()).fit().into(news1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        news1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://web.csulb.edu/sites/newsatthebeach/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // 2nd news
        StorageReference imageRef2 = storage.getReferenceFromUrl("gs://rentmybook-30c9e.appspot.com/News/2.jpg");
        imageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri.toString()).fit().into(news2);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        news2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://web.csulb.edu/sites/newsatthebeach/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        // 3rd news
        StorageReference imageRef3 = storage.getReferenceFromUrl("gs://rentmybook-30c9e.appspot.com/News/3.jpg");
        imageRef3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri.toString()).fit().into(news3);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        news3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://web.csulb.edu/sites/newsatthebeach/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // 4th news
        StorageReference imageRef4 = storage.getReferenceFromUrl("gs://rentmybook-30c9e.appspot.com/News/4.jpg");
        imageRef4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri.toString()).fit().into(news4);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        news4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://web.csulb.edu/sites/newsatthebeach/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // 5th news
        StorageReference imageRef5 = storage.getReferenceFromUrl("gs://rentmybook-30c9e.appspot.com/News/5.png");
        imageRef5.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri.toString()).fit().into(news5);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        news5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://web.csulb.edu/sites/newsatthebeach/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // 6th news
        StorageReference imageRef6 = storage.getReferenceFromUrl("gs://rentmybook-30c9e.appspot.com/News/ad6.jpg");
        imageRef6.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri.toString()).fit().into(news6);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        // 6th news
        StorageReference imageRef7 = storage.getReferenceFromUrl("gs://rentmybook-30c9e.appspot.com/News/ad7.jpg");
        imageRef7.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri.toString()).fit().into(news7);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        news6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://web.csulb.edu/sites/newsatthebeach/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // 6th news
        StorageReference imageRef8 = storage.getReferenceFromUrl("gs://rentmybook-30c9e.appspot.com/News/ad8.jpg");
        imageRef8.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri.toString()).fit().into(news8);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        news7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://web.csulb.edu/sites/newsatthebeach/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 71);
    }



    @Override
    public void onClick(View view) {

    }
}