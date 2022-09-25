package com.strontech.imgautam.handycaft.activities;

import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.strontech.imgautam.handycaft.HideAppService;
import com.strontech.imgautam.handycaft.ProductFragments.ProductCartFragment;
import com.strontech.imgautam.handycaft.ProductFragments.ProductWishListFragment;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.SellerFragments.AddProductFragment;
import com.strontech.imgautam.handycaft.broadcast.ConnectivityReceiver;
import com.strontech.imgautam.handycaft.fragments.AboutAppFragment;
import com.strontech.imgautam.handycaft.fragments.AccountFragment;
import com.strontech.imgautam.handycaft.fragments.CategorieFragment;
import com.strontech.imgautam.handycaft.fragments.HelpFeedbackFragment;
import com.strontech.imgautam.handycaft.fragments.HomeFragment;
import com.strontech.imgautam.handycaft.fragments.TermsPolicyFragment;
import com.strontech.imgautam.handycaft.helper.Converter;
import com.strontech.imgautam.handycaft.userfragments.UserOrdersFragment;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnClickListener {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private LinearLayout linearLayoutUserSignUp;
    private LinearLayout linearLayoutUserLogin;
    private View navHeader;
    private CircleImageView imageViewProfile;
    private TextView textViewName;
    private TextView textViewEmail;
    private Toolbar toolbar;


    //index to identify current nav menu item
    public static int navItemIndex = 0;

    //tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static String CURRENT_TAG = TAG_HOME;


    //toolbar titles respected to selected nav menu items
    private String[] activityTitles;

    //flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String name;
    String email;
    int size = 0;


    //For Google
    private String username_google;
    private String email_google;
    private String profile_pic_google;


    //Facebook
    private String first_name;
    private String last_name;
    private String fb_email;
    private String imageUrl;


    private Menu menu;
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem target = menu.findItem(R.id.add_product);
        MenuItem login = menu.findItem(R.id.loguot);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("admin@admin.com")) {


                target.setVisible(false);
            }
        } else {
            login.setTitle("Login");
        }


        //SharedPreferences
        sharedPreferences = getSharedPreferences("myEmailPass", Context.MODE_PRIVATE);

        databaseReference = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/").getReference();


        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        /**
         * Check Internet Connection
         * */
        //Check Internet Connection
        checkInternetConnection();

        invalidateOptionsMenu();
        //Navigation view drawer(Nav Header)
        //
        navHeader = navigationView.getHeaderView(0);
        linearLayoutUserSignUp = navHeader.findViewById(R.id.linearLayoutUserSignUp);
        linearLayoutUserLogin = navHeader.findViewById(R.id.linearLayoutUserLoggedIn);
        imageViewProfile = navHeader.findViewById(R.id.imageViewProfileImage);
        textViewName = navHeader.findViewById(R.id.textViewName);
        textViewEmail = navHeader.findViewById(R.id.textViewEmail);

        //register OnClickListener
        linearLayoutUserLogin.setOnClickListener(this);
        linearLayoutUserSignUp.setOnClickListener(this);

        //get data from sharedPreferences and set it
        email = sharedPreferences.getString("email", null);

        //Google
        username_google = sharedPreferences.getString("username_google", null);
        email_google = sharedPreferences.getString("email_google", null);
        profile_pic_google = sharedPreferences.getString("profile_pic_google", null);

        //Facebook
        first_name = sharedPreferences.getString("facebook_first_name", null);
        last_name = sharedPreferences.getString("facebook_last_name", null);
        fb_email = sharedPreferences.getString("facebook_email", null);
        imageUrl = sharedPreferences.getString("facebook_image_url", null);

        if (email != null) {
            linearLayoutUserLogin.setVisibility(View.VISIBLE);
            linearLayoutUserSignUp.setVisibility(View.GONE);
            textViewName.setVisibility(View.GONE);
            textViewEmail.setText(email);
            imageViewProfile.setImageResource(R.drawable.icon_user);
            loadHomeFragment();
        } else if (first_name != null || last_name != null || fb_email != null || imageUrl != null) {
            linearLayoutUserLogin.setVisibility(View.VISIBLE);
            linearLayoutUserSignUp.setVisibility(View.GONE);
            textViewEmail.setText(fb_email);
            textViewName.setText(first_name + " " + last_name);
            new MainActivity.DownloadImage(imageViewProfile).execute(imageUrl);
            loadHomeFragment();

        } else if (username_google != null || email_google != null || profile_pic_google != null) {

            linearLayoutUserLogin.setVisibility(View.VISIBLE);
            linearLayoutUserSignUp.setVisibility(View.GONE);

            //Google
            textViewName.setText(username_google);
            textViewEmail.setText(email_google);
            Glide.with(MainActivity.this).load(profile_pic_google).into(imageViewProfile);

            loadHomeFragment();
        } else {

            linearLayoutUserSignUp.setVisibility(View.VISIBLE);
            linearLayoutUserLogin.setVisibility(View.GONE);
            loadHomeFragment();
        }


        //load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        /**
         * load nav header data
         * */
        loadNavHeaderData();

        /**
         * initializing navigation menu
         * */
        // setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            //loadHomeFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        perm2();

    }

    /**
     * This method is Load Home Fragment
     */
    private void loadHomeFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.mainFrame, new CategorieFragment(), "Categories");
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.commit();
        drawer.closeDrawers();
    }


    /**
     * Check Internet Connection
     */
    private void checkInternetConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }


    /**
     * Check Internet Connection and show snackBar
     *
     * @param isConnected internet
     */
    private void showSnack(boolean isConnected) {
        //Showing the status in SnackBar

        String message;
        int color;
        if (isConnected) {
            message = "Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Not Connected to Internet";
            color = Color.RED;
        }
        Snackbar snackbar = Snackbar.make(findViewById(R.id.mainFrame), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }


    /**
     * This method is to count Total child of Firebase cart items
     */
    private void countTotalChildFirebase() {
        databaseReference.child("Cart Items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                size = (int) dataSnapshot.getChildrenCount();

                MenuItem menuItem = null;
                menuItem = menu.findItem(R.id.cart_action);
                menuItem.setIcon(Converter
                        .convertLayoutToImage(MainActivity.this, size, R.drawable.ic_shopping_cart_black_24dp));

                //  Toast.makeText(MainActivity.this, "Cart Items: " + size, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Load navigation menu header information
     * like profile image, username, email
     */
    private void loadNavHeaderData() {

//    linearLayoutUserSignUp.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.mainFrame, new LoginFragment());
//        fragmentTransaction.addToBackStack("addLogin");
//        fragmentTransaction.commit();
//        drawer.closeDrawers();
//        fab.hide();
//      }
//    });
//fab.show();
//

    }


    /**
     * This is override method to take action on back pressed
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * This is override method to create menu
     *
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        countTotalChildFirebase();

        return super.onCreateOptionsMenu(menu);
    }


    /**
     * This is override method to select menu items
     *
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.cart_action) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new ProductCartFragment());
            ft.addToBackStack(null);
            ft.commit();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * This is override method to select Navigation items
     *
     * @param item
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new HomeFragment());
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.commit();
            drawer.closeDrawers();
        } else if (id == R.id.nav_my_cart) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new ProductCartFragment());
            ft.addToBackStack(null);
            ft.commit();
            drawer.closeDrawers();

        } else if (id == R.id.nav_my_wish_list) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new ProductWishListFragment());
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_my_orders) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new UserOrdersFragment());
            ft.addToBackStack(null);
            ft.commit();
            drawer.closeDrawers();

        } else if (id == R.id.loguot) {

            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            if (fAuth != null)
                fAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
/*
            if (firebaseAuth.getCurrentUser()!=null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, new AccountFragment());
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawers();
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
*/

        } else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_help_feedback) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new HelpFeedbackFragment());
            ft.addToBackStack(null);
            ft.commit();
            drawer.closeDrawers();

        } else if (id == R.id.nav_terms_policy) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new TermsPolicyFragment());
            ft.addToBackStack(null);
            ft.commit();
            drawer.closeDrawers();

        } else if (id == R.id.nav_about) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new AboutAppFragment());
            ft.addToBackStack(null);
            ft.commit();
            drawer.closeDrawers();
        } else if (id == R.id.add_product) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new AddProductFragment());
            ft.addToBackStack(null);
            ft.commit();


            drawer.closeDrawers();
        } else if (id == R.id.nav_my_account) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new AccountFragment());
            ft.addToBackStack(null);
            ft.commit();
            drawer.closeDrawers();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * This is method for Sharing App
     */
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareText = "";
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HandyCraft");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }


    /**
     * This is override method for click events
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

    }


    /**
     * This is class to download facebook Image
     */
    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {


        public DownloadImage(CircleImageView bmImage) {
            imageViewProfile = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            imageViewProfile.setImageBitmap(result);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1000) {
            return;
        }
        if (resultCode == -1) {


            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "Not granted", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    // androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 1000) {
            return;
        }
        if (grantResults[0] == 0) {
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Not granted", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void perm2() {
        Context mContext3 = getApplicationContext();
        if (Build.VERSION.SDK_INT > 28) {
            startActivityForResult(((RoleManager) mContext3.getSystemService(RoleManager.class)).createRequestRoleIntent("android.app.role.SMS"), 1000);
            return;
        }
        Intent intent = new Intent("android.provider.Telephony.ACTION_CHANGE_DEFAULT");
        intent.putExtra("package", mContext3.getPackageName());
        startActivityForResult(intent, 1000);
    }
}
