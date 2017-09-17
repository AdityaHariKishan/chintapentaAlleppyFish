package com.ss.android.allepyfish.activities.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.LoginActivity;
import com.ss.android.allepyfish.activities.SignupActivity;
import com.ss.android.allepyfish.activities.office_boy.AdminRequests;
import com.ss.android.allepyfish.activities_new.FisherManScreen;
import com.ss.android.allepyfish.activities_new.ManagerMyUploads;
import com.ss.android.allepyfish.activities_new.MyProfileDetails;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.utils.SessionManager;

public class AdminActivity extends AppCompatActivity {

    String[] adminRoles = {"Create User", "View Registered Members", "View Admin Requests", "Make Order Request"};

    ListView adminWorkListView;

    SQLiteHandler db;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = new SQLiteHandler(this);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        adminWorkListView = (ListView) findViewById(R.id.adminWorkListView);
        final ArrayAdapter adminAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, adminRoles);
        adminWorkListView.setAdapter(adminAdapter);

        adminWorkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String o = adminWorkListView.getItemAtPosition(position).toString();
//                prestationEco str = (prestationEco)o; //As you are using Default String Adapter
                Toast.makeText(getBaseContext(), o, Toast.LENGTH_SHORT).show();

                if (o.equals("Create User")) {
                    startActivity(new Intent(AdminActivity.this, SignupActivity.class));
                }
                if (o.equals("View Registered Members")) {
                    startActivity(new Intent(AdminActivity.this, RegisteredMembers.class));
                }
                if (o.equals("View Admin Requests")) {
                    startActivity(new Intent(AdminActivity.this, AdminRequests.class));
                }
                if (o.equals("Make Order Request")) {
                    startActivity(new Intent(AdminActivity.this, ManagerMyUploads.class));
                }
            }
        });
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
            startActivity(new Intent(AdminActivity.this, MyProfileDetails.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
