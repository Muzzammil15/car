package com.example.muzza.carbon1;

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

public class Registration extends AppCompatActivity {

    EditText editTextEmail, editTextPasswd;

    Button buttonRegister;

    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPasswd = (EditText) findViewById(R.id.editTextPasswd);

        mAuth = FirebaseAuth.getInstance();


    }

    public void registerMe(View view){
        String email = editTextEmail.getText().toString();
        String password = editTextPasswd.getText().toString();

        // TODO: Create the user account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Registration.this, "User created", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(Registration.this, "Account creation failed", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }
}
