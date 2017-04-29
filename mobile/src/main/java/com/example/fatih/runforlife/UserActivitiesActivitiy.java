package com.example.fatih.runforlife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserActivitiesActivitiy extends AppCompatActivity {

    String uid;
    FirebaseDatabase mFireDB;
    DatabaseReference mDBRef;
    ListView listViewA;

    ArrayList<Activities> listem=new ArrayList<>();
    ArrayList<String> startList=new ArrayList<String>();
    ArrayList<String> finishList=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_activities_activitiy);

        Intent inte=getIntent();
        uid=inte.getStringExtra("ID");

        mFireDB=FirebaseDatabase.getInstance();
        mDBRef=mFireDB.getReference("Activities");
        listViewA=(ListView)findViewById(R.id.listViewactivite) ;


        mDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Activities data=dataSnapshot.getValue(Activities.class);
                if(data.getUserID().equals(uid))
                {
                    dataSnapshot.getKey();
                    startList.add(data.getStartLat()+","+data.getStartLong());
                    finishList.add(data.getFinishLat()+","+data.getFinishLong());

                    listem.add(data);
                    //listView den kaçıncısına tıkladığını ogrenip onu string olarak gonder
                  }
                  UserActivitiesAdapter adapter=new UserActivitiesAdapter(UserActivitiesActivitiy.this,listem);
                listViewA.setAdapter(adapter);
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

        listViewA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent git=new Intent(getApplicationContext(),ShowUserActActivity.class);
                git.putExtra("Lat",startList.get(position));
                git.putExtra("Long",finishList.get(position));
                startActivity(git);
            }
        });


    }

    }

