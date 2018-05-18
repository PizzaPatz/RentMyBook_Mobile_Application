package com.app.csulb.rentmybookfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

// Show details of the book with ISBN, Author, Title, and Book Image
public class BookDetails extends AppCompatActivity {

    FirebaseDatabase data;
    ArrayList<OwnerList> list;
    OwnerAdapter adapter;
    String user, value, image, isbnVal;
    ListView myList;

    TextView title, author, isbn;
    ImageView bookImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Intent intent = getIntent();
        String getValue = intent.getExtras().getString("value");
        String book_title = getValue;

        data = FirebaseDatabase.getInstance();
        DatabaseReference book = data.getReference("Books");
        DatabaseReference getBook = book.child(book_title);
        DatabaseReference getAuthor = getBook.child("author");
        DatabaseReference getImage = getBook.child("image");
        DatabaseReference getISBN = getBook.child("isbn");
        DatabaseReference getOwners = getBook.child("owners");


        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);
        bookImage = (ImageView) findViewById(R.id.book_image);
        isbn = (TextView) findViewById(R.id.isbn);
        title.setText(book_title);

        // Get author value from Firebase and display on listview
        getAuthor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value = dataSnapshot.getValue(String.class);
                author.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Get ISBN value from Firebase and display on listview
        getISBN.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isbnVal = dataSnapshot.getValue(String.class);
                isbn.setText("ISBN: " + isbnVal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Get image value from Firebase and display on listview
        getImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                image = dataSnapshot.getValue(String.class);
                Glide.with(getApplicationContext()).load(image).into(bookImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Set list adapter to show all of the relevant information of the book
        list = new ArrayList<>();
        user = new String();
        myList = (ListView) findViewById(R.id.owner_list);

        adapter = new OwnerAdapter(this, list);
        myList.setAdapter(adapter);

        // Get owner value from Firebase and display on listview
        getOwners.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> value = (Map<String, String>) dataSnapshot.getValue();
                String name = value.get("name");
                String condition = value.get("condition");
                String price = value.get("price");
                String avail = value.get("available");

                OwnerList addValue = new OwnerList(name, price, condition, avail);

                list.add(addValue);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}