package com.example.muzza.carbon1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPasswd;
    CheckBox checkBoxRemember;
    Button buttonLogin;
    Button buttonRegister;

    SharedPreferences rememberMePrefs;
    SharedPreferences.Editor rememberMePrefsEditor;

    boolean rememberMe;

    String email, password;

    String user_email;

    ///
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPasswd = (EditText) findViewById(R.id.editTextPasswd);
        checkBoxRemember = (CheckBox) findViewById(R.id.chkRememberMe);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);


        // TODO: Get a reference to the Firebase auth object
        mAuth = FirebaseAuth.getInstance();

        // TODO: Attach a new AuthListener to detect sign in and out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in

                    Toast.makeText(MainActivity.this, "Signed in: " + user.getUid(), Toast.LENGTH_SHORT).show();
                    // Log.d(TAG, "Signed in: " + user.getUid());
                } else {
                    // User is signed out
                    Toast.makeText(MainActivity.this, "Currently signed out", Toast.LENGTH_SHORT).show();
                    //Log.d(TAG, "Currently signed out");
                }
            }
        };


        rememberMePrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        rememberMePrefsEditor = rememberMePrefs.edit();


        rememberMe = rememberMePrefs.getBoolean("rememberMe", false);
        if (rememberMe == true) {
            editTextEmail.setText(rememberMePrefs.getString("username", ""));
            editTextPasswd.setText(rememberMePrefs.getString("password", ""));
            checkBoxRemember.setChecked(true);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        // TODO: add the AuthListener
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // TODO: Remove the AuthListener
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void register(View view){
        String email = editTextEmail.getText().toString();
        String password = editTextPasswd.getText().toString();

        // TODO: Create the user account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "User created", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(MainActivity.this, "Account creation failed", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }

    public void login(View view){
        email = editTextEmail.getText().toString();
        password = editTextPasswd.getText().toString();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ///
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {

                                 user_email = user.getEmail();

                                // The user's ID, unique to the Firebase project. Do NOT use this value to
                                // authenticate with your backend server, if you have one. Use
                                // FirebaseUser.getToken() instead.

                                Toast.makeText(MainActivity.this, "signed in" + user_email , Toast.LENGTH_SHORT).show();


                                ///moving data to homePg
                                Intent i = new Intent(MainActivity.this, HomePage.class);

                                i.putExtra("userEMail", user_email);
                                startActivity(i);




                            }
                        } else {
                            Toast.makeText(MainActivity.this, "signed in failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        /////

        if(checkBoxRemember.isChecked()){
            rememberMePrefsEditor.putBoolean("rememberMe",true);
            rememberMePrefsEditor.putString("username", email);
            rememberMePrefsEditor.putString("password", password);
            rememberMePrefsEditor.commit();

        }
        else{
            rememberMePrefsEditor.clear();
            rememberMePrefsEditor.commit();
        }

    }


    public void registerAct(View view){
        Intent k = new Intent(MainActivity.this, Registration.class);
        startActivity(k);


    }

    public void logOut(View view){
        mAuth.signOut();

    }
}
