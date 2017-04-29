package com.example.fatih.runforlife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MessagesActivity extends AppCompatActivity {

    String DisplayName;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String mUsername;
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;

    String id, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mFirebaseAnalytics=FirebaseAnalytics.getInstance(this);
        database=FirebaseDatabase.getInstance();
        mFirebaseRef=database.getReference("Message");

        Bundle bundle=new Bundle();

        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);
        setupUsername();
        setTitle("Chat; "+mUsername);
        EditText inputText=(EditText)findViewById(R.id.messageInput);
        try {
            inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        sendMessage();
                    }
                    return true;
                }
            });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        }); }catch (Exception e){
            System.out.println("Mehmet"+e.getMessage());
        }}




        /*Button button = (Button) findViewById(R.id.mesajButon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Message_activity.class);
                startActivity(intent);

            }
        });}*/
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = (ListView) findViewById(R.id.list);
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limitToFirst(50), this, R.layout.chat_message, mUsername);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(MessagesActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MessagesActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                // No-op
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }
    private void setupUsername() {
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        mUsername = prefs.getString("username", null);
        if (mUsername != null) {
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            String DisplayName=user.getDisplayName();
            mUsername=DisplayName;
            //Random r = new Random();
            // Assign a random user name if we don't have one saved.
           // mUsername = "JavaUser" + r.nextInt(100000);
            prefs.edit().putString("username", mUsername).commit();
        }
    }
    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);

        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
            String userNamee=user.getDisplayName();
            Chat chat = new Chat(input, userNamee);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            inputText.setText("");
        }
    }}
