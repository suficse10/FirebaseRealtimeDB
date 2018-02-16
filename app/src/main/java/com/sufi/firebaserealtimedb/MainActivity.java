package com.sufi.firebaserealtimedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sufi.firebaserealtimedb.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText eTusername, eTemail;
    ListView userListview;
    ArrayList<User> arrayListUser;
    ArrayAdapter<User> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eTusername = findViewById(R.id.editTextUsername);
        eTemail = findViewById(R.id.editTextEmail);
        userListview = findViewById(R.id.listViewUser);

        arrayListUser = new ArrayList<>();
        adapter = new ArrayAdapter<>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, arrayListUser);
        userListview.setAdapter(adapter);

        // Write a message to the database
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Username");
        DatabaseReference myRef2 = database.getReference("Email");

        myRef.setValue("ABC");
        myRef2.setValue("abc@gmail.com");*/

        // Read from the database
/*
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.i("Data", "Username: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("Error", "Failed to read value.", error.toException());
            }
        });
*/      getdataFromFirebase();
    }

    public void saveToFirebaseDB(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users");
        String userID = userRef.push().getKey();

        User user = new User(eTusername.getText().toString(), eTemail.getText().toString());
        userRef.child(userID).setValue(user);

    }

    public void getdataFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListUser.clear();
                for(DataSnapshot usersnap : dataSnapshot.getChildren()) {
                    String name = usersnap.child("name").getValue().toString();
                    String email = usersnap.child("email").getValue().toString();
                    User user = new User(name, email);
                    arrayListUser.add(user);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("Error", "Failed to read value.", error.toException());
            }
        });
    }
}
