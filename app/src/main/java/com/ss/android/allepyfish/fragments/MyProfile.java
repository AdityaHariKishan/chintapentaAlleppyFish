package com.ss.android.allepyfish.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.model.ContactInfo;

import java.util.List;

/**
 * Created by dell on 4/30/2017.
 */

public class MyProfile extends Fragment {


    String userName;
    String userEmail;
    String profile_pic;

    SQLiteHandler db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        ImageView profilePicFMIV = (ImageView)rootView.findViewById(R.id.profilePicFMIV);
        db = new SQLiteHandler(getContext());

        TextView myProfileFManTV = (TextView)rootView.findViewById(R.id.myProfileFManTV);
        TextView myProfileuserEmailTV = (TextView)rootView.findViewById(R.id.myProfileuserEmailTV);
        TextView myProfileuserPhoneTV = (TextView)rootView.findViewById(R.id.myProfileuserPhoneTV);


        String userPhoneNo = null;
        List<ContactInfo> contacts = db.getAllContacts();

        for (ContactInfo cn : contacts) {
            userName = cn.getName();
            userEmail = cn.getEmail();
            userPhoneNo = cn.getPhone_no();
            profile_pic = cn.getprofile_pic_url();
            // Writing Contacts to log
            Log.d("Name: userName :: ", userName+" "+userEmail+" ProfilePic URL "+profile_pic);
        }

        myProfileFManTV.setText(userName);
        myProfileuserEmailTV.setText(userEmail);
        myProfileuserPhoneTV.setText(userPhoneNo);
        String s1 = profile_pic;
//        Picasso.with(getContext()).load("http://"+s1).into(profilePicFMIV);
//        Picasso.with(getContext()).load("http://192.168.1.5/alleppyfish/uploads/aditya_pp3.jpg").into(profilePicFMIV);
        Picasso.with(getContext()).load(profile_pic).into(profilePicFMIV);
        return rootView;
    }


}
