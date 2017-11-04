package com.ss.android.allepyfish.activities_new;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.LoginActivity;
import com.ss.android.allepyfish.activities.office_boy.OfficeBoyLandingScreen;
import com.ss.android.allepyfish.fragments.MyProfile;
import com.ss.android.allepyfish.fragments.office_boys.MyGoodsSuppliedFragment;
import com.ss.android.allepyfish.fragments.office_boys.ContactManagers;
import com.ss.android.allepyfish.fragments.office_boys.LatestOrdersFragment;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.utils.SessionManager;

public class FisherManScreen extends AppCompatActivity {

    private TextView mTextMessage;

    Fragment landingTabsFragment;

    SQLiteHandler db;
    SessionManager session;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                // Handle the camera action
                landingTabsFragment = new LatestOrdersFragment();

            } else if (id == R.id.navigation_dashboard) {
                landingTabsFragment = new MyGoodsSuppliedFragment();

            } else if (id == R.id.navigation_notifications) {
                landingTabsFragment = new ContactManagers();

            }

            if (landingTabsFragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, landingTabsFragment);
                ft.commit();
            }


//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisher_man_screen);

        landingTabsFragment = new LatestOrdersFragment();

        db = new SQLiteHandler(this);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }


        if (landingTabsFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, landingTabsFragment);
            ft.commit();
        }

//        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.office_boy_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout_ob) {
            logoutUser();
            return true;
        }
        if (id == R.id.my_profile) {
//            logoutUser();
            startActivity(new Intent(FisherManScreen.this, MyProfileDetails.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(FisherManScreen.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
