package com.example.fatih.runforlife;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    FirebaseDatabase mFireDB;
    DatabaseReference mDBRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase Auth örneği alındı.
        mAuth = FirebaseAuth.getInstance();

        mFireDB=FirebaseDatabase.getInstance();
        mDBRef=mFireDB.getReference("Users");

        //CallBack Manager üretildi
        mCallbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("ÇIKTI", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.e("ÇIKTI", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("ÇIKTI", "facebook:onError", error);
                // ...
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                    Log.e("ÇIKTI", "onAuthStateChanged:signed_in:" + user.getDisplayName());

                    try {
                        Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                       // ii.putExtra("ID", user.getUid());
                        startActivity(ii);
                    }catch (Exception e){
                        System.out.println("asdasd"+e.getMessage());
                    }
                } else {
                    // User is signed out
                    Log.e("ÇIKTI", "onAuthStateChanged:signed_out");
                }

                // ...
            }
        };

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);


    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.e("ÇIKTI", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("ÇIKTI", "signInWithCredential:onComplete:" + task.isSuccessful());

                        FirebaseUser user=mAuth.getCurrentUser();

                        Users userYaz=new Users(user.getUid(),user.getDisplayName(),user.getEmail(),user.getPhotoUrl().toString());

                        mDBRef.child(user.getUid()).setValue(userYaz);

                        Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                        // ii.putExtra("ID", user.getUid());
                        startActivity(ii);


                        if (!task.isSuccessful()) {

                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }




    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
