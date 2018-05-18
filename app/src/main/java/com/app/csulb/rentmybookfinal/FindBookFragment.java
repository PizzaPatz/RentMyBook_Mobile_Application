package com.app.csulb.rentmybookfinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.provider.CalendarContract.CalendarCache.URI;


public class FindBookFragment extends Fragment implements View.OnClickListener{

    View view;
    private Uri filePath;


    private EditText search;
    private ImageButton icon;
    private RecyclerView result;
    private static Context mContext;
    FirebaseDatabase data;
    DatabaseReference mBookData;
    Query firebaseSearchQuery;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find_book, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Find Book");
        mContext = getContext();
        data = FirebaseDatabase.getInstance();
        mBookData = data.getReference("Books");
        search = (EditText)view.findViewById(R.id.search_field);
        icon = (ImageButton)view.findViewById(R.id.search_button);
        result =(RecyclerView)view.findViewById(R.id.result_list);
        result.setHasFixedSize(true);
        result.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle arguments = getArguments();
        String desired_string = arguments.getString("search_key");
        if(!desired_string.isEmpty()){
            firebaseBookSearch(desired_string);
        }

        // If icon of the reward is clicked, go to the Firebase book search method
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = search.getText().toString();
                firebaseBookSearch(searchText);
            }
        });

        // Go to Firebase search when pressed Enter
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH ||
                        i == EditorInfo.IME_ACTION_DONE ||
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                                keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    String searchText = search.getText().toString();
                    firebaseBookSearch(searchText);
                    return true;
                }
                return false;
            }
        });
    }

    // Search the book in the Firebase
    private void firebaseBookSearch(String text) {

        if (text.isEmpty()) {
            Toast.makeText(getActivity(), "Enter a value to search. ", Toast.LENGTH_LONG).show();
        }
        else {
            StringBuilder searchText = new StringBuilder();
            String[] token = text.split("\\s+");
            for (int i = 0; i < token.length; i++) {
                String toUpperCase = token[i].substring(0, 1).toUpperCase();
                searchText.append(toUpperCase + token[i].substring(1).toLowerCase());
                if (i < (token.length - 1)) {
                    searchText.append(" ");
                }
            }
            final String find = searchText.toString();

           final String [] array = {"title", "author", "isbn"};
           for (int i = 0; i < array.length; i++){
               final int count = i;

               firebaseSearchQuery = mBookData.orderByChild(array[count]).startAt(find.toString()).endAt(find.toString() + "\uf8ff");
               firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       if (dataSnapshot.exists()){
                           Query searchQuery = mBookData.orderByChild(array[count]).startAt(find.toString()).endAt(find.toString() + "\uf8ff");
                           FirebaseRecyclerAdapter<Books, BookViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Books, BookViewHolder>(
                                   Books.class,
                                   R.layout.book_list,
                                   BookViewHolder.class,
                                   searchQuery
                           ) {
                           @Override
                           protected void populateViewHolder(BookViewHolder viewHolder, Books model, int position){
                                viewHolder.setDetails(getContext(), model.getTitle(), model.getAuthor(), model.getImage(), model.getIsbn());
                           }
                           };

                           result.setAdapter(firebaseRecyclerAdapter);
                           return;
                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });
           }
        }

    }


    @Override
    public void onClick(View view) {

    }
    //View Holder class
    public static class BookViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public BookViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDetails(Context context, String titleResult, String authorResult, String imageResult, String isbnResult ){
            TextView showTitle = (TextView)mView.findViewById(R.id.book_title);
            TextView showAuthor = (TextView)mView.findViewById(R.id.book_author);
            TextView showISBN = (TextView)mView.findViewById(R.id.book_isbn);
            ImageView showImage = (ImageView)mView.findViewById(R.id.result_image);


            showTitle.setText(titleResult);
            showAuthor.setText(authorResult);
            showISBN.setText(isbnResult);
            Glide.with(context).load(imageResult).into(showImage);

            showTitle.setTextColor(Color.BLUE);
            showTitle.setPaintFlags(showTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


            final String clickTitle = showTitle.getText().toString();
            showTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBookSearch(clickTitle);

                }
            });
        }

        public void openBookSearch(String bookTitle) {
            Intent findBookDetail = new Intent(mContext, BookDetails.class);
            findBookDetail.putExtra("value", bookTitle);
            mContext.startActivity(findBookDetail);
        }
    }
}