package com.ss.android.allepyfish.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.fragments.MyPaymentDetails;
import com.ss.android.allepyfish.fragments.MyProductsUploadsFragment;
import com.ss.android.allepyfish.fragments.MyProfile;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.model.ContactInfo;
import com.ss.android.allepyfish.utils.SessionManager;

import net.hockeyapp.android.UpdateManager;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;
    String title = null;
    String userEmail = null;
    String userPP = null;

    String userName = null;
    SQLiteHandler db;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForUpdates();

        db = new SQLiteHandler(this);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragment = new MyProductsUploadsFragment();

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }


        List<ContactInfo> contacts = db.getAllContacts();

        for (ContactInfo cn : contacts) {
            userName = cn.getName();
            userEmail = cn.getEmail();
            userPP = cn.getprofile_pic_url();
            // Writing Contacts to log
            Log.d("Name: userName :: ", userName+" "+userEmail+" userPP "+userPP);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                startActivity(new Intent(MainActivity.this, UploadNewFish.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawer.bringToFront();
        drawer.requestLayout();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        View header=navigationView.getHeaderView(0);
        TextView drawerNameTV = (TextView)header.findViewById(R.id.drawerNameTV);
        TextView userEmailTV = (TextView)header.findViewById(R.id.userEmailTV);

        ImageView imageViewProfilePic = (ImageView)header.findViewById(R.id.imageViewProfilePic);
//        Picasso.with(this).load("http://"+userPP).into(imageViewProfilePic);
        Picasso.with(this).load("http://alleppeyfish.com/mobile_test/alleppyfish/uploads/"+userName+".png").into(imageViewProfilePic);

        drawerNameTV.setText(userName);
        userEmailTV.setText(userEmail);
//        Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/roboto.light-italic.ttf");
//        userEmailTV.setTypeface(typeFace);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_profile_info) {
            // Handle the camera action
            fragment = new MyProfile();
            title = "My Profile";
        } else if (id == R.id.nav_my_products) {
            fragment = new MyProductsUploadsFragment();
            title = "Track My Products";
        } else if (id == R.id.nav_payment_details) {

            fragment = new MyPaymentDetails();
            title = "Payment Details";

        } else if (id == R.id.nav_contact_us) {

        }
    /*    else if (id == R.id.nav_share) {

//            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//            sharingIntent.setType("text/plain");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Try our Allepy Fish App http://www.alleppeyfish.com/");
//            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } */
        else if (id == R.id.nav_sign_out) {

            logoutUser();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            getSupportActionBar().setTitle(title);
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }


    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

}