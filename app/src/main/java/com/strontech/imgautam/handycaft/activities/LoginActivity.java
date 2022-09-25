package com.strontech.imgautam.handycaft.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.helper.InputValidation;
import com.strontech.imgautam.handycaft.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private Toolbar toolbarLogin;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTexPassword;

    private Button buttonLogin;
    private TextView textViewLinkRegister;


    private FirebaseAuth auth;
    private ProgressBar progressBar;


    ///Google Login
    private Button buttonGoogleLogin;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient googleApiClient;
    private static final int REQUEST_CODE_GOOGLE_LOGIN = 101;


    //For Facebook
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private LoginManager loginManager;
    private Button buttonFacebookLogin;
    private URL urlProfilePicure;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String birthday;
    private String gender;


    private InputValidation inputValidation;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String FILE = "myEmailPass";


    //user data save on FirebaseDatabase
    DatabaseReference databaseReference;
    String emailIdExisted;
    String username_google, email_google, profile_pic_google;

    List<String> stringList;
    List<UserInfo> userInfos;
    List<String> userInfosEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FacebookSdk.sdkInitialize(FacebookSdk.getApplicationContext());
        initViews();
        initListeners();
        initObjects();
    }


    /**
     * this method is to initialize views
     */
    private void initViews() {
        toolbarLogin = findViewById(R.id.toolbarLogin);

        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTexPassword = findViewById(R.id.textInputEditTexPassword);

        buttonLogin = findViewById(R.id.buttonLogin);

        textViewLinkRegister = findViewById(R.id.textViewLinkRegister);

        buttonFacebookLogin = findViewById(R.id.buttonFacebookLogin);
        buttonGoogleLogin = findViewById(R.id.buttonGoogleLogin);

        progressBar = findViewById(R.id.progressBar);

    }


    /**
     * this method is to initialize listeners
     */
    private void initListeners() {

        setupFacebook();
        buttonLogin.setOnClickListener(this);
        buttonFacebookLogin.setOnClickListener(this);
        buttonGoogleLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);

    }


    /**
     * this method to initialize the Objects
     */
    private void initObjects() {

        setupToolbar();
        inputValidation = new InputValidation(this);

        userInfos = new ArrayList<>();
        userInfosEmail = new ArrayList<>();

        sharedPreferences = this.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        databaseReference = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/").getReference("UserInfo");

        //Get firebase auth instance
        auth = FirebaseAuth.getInstance();
        checkUser();

    }

    /**
     * This method shows toolbar
     */
    private void setupToolbar() {
        toolbarLogin.setTitle("Sign in");
        toolbarLogin.setTitleTextColor(Color.WHITE);
        toolbarLogin.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbarLogin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    /**
     * This method checks user logged in or not
     */
    private void checkUser() {

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    /**
     * this implemented method is to listen the click on view
     *
     * @param v to get id of view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonLogin:
                EmailAndPasswordSignIn();
                break;
            case R.id.textViewLinkRegister:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.buttonFacebookLogin:
                //   logInWithFacebook();
                break;

            case R.id.buttonGoogleLogin:
                // logInWithGoogle();
                break;
        }
    }


    /**
     * This method For Login Using Email and Password
     */
    private void EmailAndPasswordSignIn() {

        if (!inputValidation
                .isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, "Enter Email")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTexPassword, textInputLayoutPassword,
                "Enter Password")) {
            return;
        }

        final String email, password;
        email = textInputEditTextEmail.getText().toString().trim();
        password = textInputEditTexPassword.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        //authenticate the user
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            //there is an error
                            if (password.length() < 6) {
                                textInputLayoutPassword.setError("Please enter 6 digit password");
                            } else {
                                Toast.makeText(LoginActivity.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                });
    }


    /**
     * This method for restarting the activity for take changes
     */
    public void restartActivity() {
        Intent intent = this.getIntent();
        //intent.addFlags(Intent.FLAG_ACTIVITY_);
        this.finish();
        startActivity(intent);
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    /**
     * This method Login With Facebook From Button
     */
    private void logInWithFacebook() {
        accessTokenTracker.startTracking();
        loginManager.logInWithReadPermissions(this, Arrays
                .asList("public_profile", "email", "user_birthday"));
    }


    /**
     * This method setup Facebook
     */
    private void setupFacebook() {
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {

            }
        };

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (loginResult.getRecentlyGrantedPermissions().contains("email")) {
                            requestObjectUser(loginResult.getAccessToken());
                        } else {
                            LoginManager.getInstance().logOut();
                            Toast.makeText(LoginActivity.this, "Error permissions", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("ERROR", error.toString());
                    }
                });

        if (AccessToken.getCurrentAccessToken() != null) {
            requestObjectUser(AccessToken.getCurrentAccessToken());
        }
    }


    /**
     * This method gets user data from facebook account
     *
     * @param accessToken
     */
    private void requestObjectUser(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            userId = object.getString("id");
                            urlProfilePicure = new URL("https://graph.facebook.com/" + userId + "/picture?width=96&height=96");
                            if (object.has("first_name"))
                                firstName = object.getString("first_name");
                            if (object.has("last_name"))
                                lastName = object.getString("last_name");
                            if (object.has("email"))
                                email = object.getString("email");
                            if (object.has("birthday"))
                                birthday = object.getString("birthday");
                            if (object.has("gender"))
                                gender = object.getString("gender");


                            //Save user data on database
                            if (databaseReference != null) {
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (userInfosEmail != null) {
                                            userInfosEmail.clear();
                                        }

                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                UserInfo userInfo = postSnapshot.getValue(UserInfo.class);
                                                userInfos.add(userInfo);
                                                userInfosEmail.add(userInfo.getUser_email());

                                            }

                                            if (userInfosEmail.contains(email)) {

                                                saveFbDataOpenMain();
                                            } else {
                                                String userId = databaseReference.push().getKey();
                                                UserInfo userInfo1 = new UserInfo();
                                                userInfo1.setUser_id(userId);
                                                userInfo1.setUser_name(username_google);
                                                userInfo1.setUser_email(email_google);
                                                userInfo1.setUser_profile_pic(profile_pic_google);

                                                databaseReference.child(userId).setValue(userInfo1);
                                                saveFbDataOpenMain();
                                            }
                                            //method
                                            saveFbDataOpenMain();
                                        } else {
                                            String userId = databaseReference.push().getKey();
                                            UserInfo userInfo1 = new UserInfo();
                                            userInfo1.setUser_id(userId);
                                            userInfo1.setUser_name(firstName + " " + lastName);
                                            userInfo1.setUser_email(email);
                                            userInfo1.setUser_profile_pic(urlProfilePicure.toString());
                                            databaseReference.child(userId).setValue(userInfo1);

                                            saveFbDataOpenMain();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, birthday, gender , location");
        request.setParameters(parameters);
        request.executeAsync();
    }


    /**
     * This method For Login Using Google
     */
    private void logInWithGoogle() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions
                .DEFAULT_SIGN_IN).requestEmail().requestProfile().build();
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,
                        googleSignInOptions).build();

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQUEST_CODE_GOOGLE_LOGIN);
    }


    /**
     * This is override method (get data from google account, fb)
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //This is For Facebook Login
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //This is For Google Sign IN
        if (requestCode == REQUEST_CODE_GOOGLE_LOGIN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()) {
                GoogleSignInAccount account = googleSignInResult.getSignInAccount();
                try {
                    username_google = account.getDisplayName();
                    email_google = account.getEmail();
                    if (account.getPhotoUrl() != null) {
                        profile_pic_google = account.getPhotoUrl().toString();

                    }

                    //Save User information on database
                    if (databaseReference != null) {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (userInfosEmail != null) {
                                    userInfosEmail.clear();
                                }

                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                        UserInfo userInfo = postSnapshot.getValue(UserInfo.class);
                                        userInfos.add(userInfo);
                                        userInfosEmail.add(userInfo.getUser_email());

                                    }

                                    if (userInfosEmail.contains(email_google)) {

                                        saveDataOpenMain();
                                    } else {
                                        String userId = databaseReference.push().getKey();

                                        UserInfo userInfo1 = new UserInfo();

                                        userInfo1.setUser_id(userId);
                                        userInfo1.setUser_name(username_google);
                                        userInfo1.setUser_email(email_google);
                                        userInfo1.setUser_profile_pic(profile_pic_google);

                                        databaseReference.child(userId).setValue(userInfo1);
                                        saveDataOpenMain();
                                    }
                                    //method
                                    saveDataOpenMain();

                                } else {

                                    String userId = databaseReference.push().getKey();
                                    UserInfo userInfo1 = new UserInfo();
                                    userInfo1.setUser_id(userId);
                                    userInfo1.setUser_name(username_google);
                                    userInfo1.setUser_email(email_google);
                                    userInfo1.setUser_profile_pic(profile_pic_google);

                                    databaseReference.child(userId).setValue(userInfo1);
                                    saveDataOpenMain();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Data save Google user info on SharedPreferences and open MainActivity
     */
    private void saveDataOpenMain() {
        editor.putString("username_google", username_google);
        editor.putString("email_google", email_google);
        editor.putString("profile_pic_google", profile_pic_google);
        editor.commit();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * Data save facebook user info on SharedPreferences and open MainActivity
     */
    private void saveFbDataOpenMain() {
        editor.putString("facebook_first_name", firstName);
        editor.putString("facebook_last_name", lastName);
        editor.putString("facebook_email", email);
        editor.putString("facebook_image_url", urlProfilePicure.toString());
        editor.commit();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
