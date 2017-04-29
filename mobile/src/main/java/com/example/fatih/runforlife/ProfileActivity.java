package com.example.fatih.runforlife;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Fatih on 19.04.2017.
 */

public class ProfileActivity extends AppCompatActivity {



    ListView listViewA;

    ArrayList<Activities> listem=new ArrayList<>();
    ArrayList<String> startList=new ArrayList<String>();
    ArrayList<String> finishList=new ArrayList<String>();



    FirebaseDatabase mFireDB;
    DatabaseReference mDBRef;

    DatabaseReference mDBref2;

    Button arkadas;
    Button msgBtn;
    ImageView imgView;
    TextView displayTxt;
    TextView emailTxt;

    String uid;
    String photoUrl;
    String email;
    String displayname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        arkadas=(Button)findViewById(R.id.arkadasEkleBtn);
        msgBtn=(Button)findViewById(R.id.messageBtn);
        listViewA=(ListView)findViewById(R.id.listActivite) ;

        imgView=(ImageView)findViewById(R.id.imageView);
        displayTxt=(TextView)findViewById(R.id.displayNameText);
        emailTxt=(TextView)findViewById(R.id.emailText);

        mFireDB=FirebaseDatabase.getInstance();
        mDBRef=mFireDB.getReference("Users");
        mDBref2=mFireDB.getReference("Activities");

        Intent inte=getIntent();
        uid=inte.getStringExtra("ID");

        mDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String id= dataSnapshot.child("uId").getValue().toString();
                if(id.equals(uid))
                {
                    displayname=dataSnapshot.child("displayName").getValue().toString();
                    photoUrl=dataSnapshot.child("photoUrl").getValue().toString();
                    email= dataSnapshot.child("email").getValue().toString();


                    Glide.with(getApplicationContext()).load(photoUrl).into(imgView);
                    emailTxt.setText(email);
                    displayTxt.setText(displayname);


                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messages=new Intent(getApplicationContext(),MessagesActivity.class);
                startActivity(messages);
            }
        });
        mDBref2.addChildEventListener(new ChildEventListener() {
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
                UserActivitiesAdapter adapter=new UserActivitiesAdapter(ProfileActivity.this,listem);
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
        });listViewA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent git=new Intent(getApplicationContext(),ShowUserActActivity.class);
                git.putExtra("Lat",startList.get(position));
                git.putExtra("Long",finishList.get(position));
                startActivity(git);
            }
        });
    }
    public void arkadasEkle(View view)
    {
        DatabaseReference friends=FirebaseDatabase.getInstance().getReference("FriendShip");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user.getUid().equals(uid))
        {

        }else
            {
                FriendShip friendShip=new FriendShip(user.getUid(),uid);
                friends.push().setValue(friendShip);

            }
    }
}
