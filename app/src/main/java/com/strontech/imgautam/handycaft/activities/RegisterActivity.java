package com.strontech.imgautam.handycaft.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.helper.InputValidation;

public class RegisterActivity extends Activity implements View.OnClickListener {


    private Toolbar toolbarSignUp;
    private TextInputLayout textInputLayoutUserName;
    private TextInputLayout textInputLayoutUserEmail;
    private TextInputLayout textInputLayoutUserMobNumber;
    private TextInputLayout textInputLayoutUserPassword;
    private TextInputLayout textInputLayoutUserConfirmPassword;

    private TextInputEditText textInputEditTextUserName;
    private TextInputEditText textInputEditTextUserEmail;
    private TextInputEditText textInputEditTextUserMobNumber;
    private TextInputEditText textInputEditTextUserPassword;
    private TextInputEditText textInputEditTextUserConfirmPassword;

    private Button buttonRegisterUser;
    private TextView textViewLoginLink;


    private InputValidation inputValidation;

    private FirebaseAuth firebaseAuth;
    //private FirebaseAuth.AuthStateListener authStateListener;
    //private DatabaseReference databaseReference;

    // private Context context;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        toolbarSignUp = findViewById(R.id.toolbarSignUp);
        progress = findViewById(R.id.progress);
        textInputLayoutUserName = findViewById(R.id.textInputLayoutUserName);
        textInputLayoutUserEmail = findViewById(R.id.textInputLayoutUserEmail);
        textInputLayoutUserMobNumber = findViewById(R.id.textInputLayoutUserMobNumber);
        textInputLayoutUserPassword = findViewById(R.id.textInputLayoutUserPassword);
        textInputLayoutUserConfirmPassword = findViewById(R.id.textInputLayoutUserConfirmPassword);

        textInputEditTextUserName = findViewById(R.id.textInputEditTextUserName);
        textInputEditTextUserEmail = findViewById(R.id.textInputEditTextUserEmail);
        textInputEditTextUserMobNumber = findViewById(R.id.textInputEditTextUserMobNumber);
        textInputEditTextUserPassword = findViewById(R.id.textInputEditTextUserPassword);
        textInputEditTextUserConfirmPassword = findViewById(R.id.textInputEditTextUserConfirmPassword);

        buttonRegisterUser = findViewById(R.id.buttonRegisterUser);
        textViewLoginLink = findViewById(R.id.textViewLoginLink);


    }


    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        buttonRegisterUser.setOnClickListener(this);
        textViewLoginLink.setOnClickListener(this);
    }


    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {

        setUpToolbar();

        inputValidation = new InputValidation(this);
        firebaseAuth = FirebaseAuth.getInstance();


    }


    /**
     * This method shows toolbar
     */
    private void setUpToolbar() {
        toolbarSignUp.setTitle("Sign up");
        toolbarSignUp.setTitleTextColor(Color.WHITE);
        toolbarSignUp.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbarSignUp.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    /**
     * this implemented method is to listen the click on view
     *
     * @param v to get View id
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegisterUser:
                postDataToFirebaseDb();
                break;
            case R.id.textViewLoginLink:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }

    }

    /**
     * This method is to validate the input text fields and post data to Firebase database
     */
    private void postDataToFirebaseDb() {

        if (!inputValidation.isInputEditTextFilled(textInputEditTextUserName, textInputLayoutUserName,
                "Username")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUserEmail, textInputLayoutUserEmail,
                "Enter Username")) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextUserEmail, textInputLayoutUserEmail,
                "Enter valid Email")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUserMobNumber, textInputLayoutUserMobNumber,
                "Enter valid Mobile Number")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUserPassword, textInputLayoutUserPassword,
                "Enter Password")) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextUserPassword, textInputEditTextUserConfirmPassword, textInputLayoutUserConfirmPassword,
                "Password Does Not Match")) {
            return;
        }
        setRefreshMode(true);
        final String username, email, mob_number, password;

        username = textInputEditTextUserName.getText().toString().trim();
        email = textInputEditTextUserEmail.getText().toString().trim();
        mob_number = textInputEditTextUserMobNumber.getText().toString().trim();
        password = textInputEditTextUserConfirmPassword.getText().toString().trim();


        //create User
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener

                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                    setRefreshMode(false);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setRefreshMode(false);
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onStart() {
        super.onStart();

        //  firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        //  firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void setRefreshMode(boolean r) {
        if (r) {
            progress.setVisibility(View.VISIBLE);
            buttonRegisterUser.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.GONE);
            buttonRegisterUser.setVisibility(View.VISIBLE);
        }
    }
}

