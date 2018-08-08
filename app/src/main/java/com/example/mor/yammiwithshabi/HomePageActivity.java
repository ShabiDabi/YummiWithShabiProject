package com.example.mor.yammiwithshabi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class HomePageActivity extends AppCompatActivity {


    final int REQUEST_WRITE_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SetupDrawer(toolbar);

        if (savedInstanceState == null) {
            FeedFragment fragment = new FeedFragment();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.add(R.id.main_container, fragment);
            tran.addToBackStack("");
            tran.commit();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            }
        }

        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.nameView);
        //textView.setText("Welcome " + user.getEmail());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    private void SetupDrawer(Toolbar toolbar) {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_profile);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_logOut);

        final Intent intent = new Intent(this, MainActivity.class);
        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        //new DividerDrawerItem(),
                        item2,
                        //new SecondaryDrawerItem().withName(R.string.drawer_item_settings),
                        item3
                        //new SecondaryDrawerItem().withName(R.string.drawer_item_logOut)
                )



                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        long id = drawerItem.getIdentifier();

                        if (id == 1) {
                            FeedFragment fragment = new FeedFragment();
                            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                            tran.replace(R.id.main_container, fragment);
                            tran.addToBackStack("");
                            tran.commit();

                        }else if (id == 2) {
                            NewFeedItemFragment fragment = new NewFeedItemFragment();
                            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                            tran.replace(R.id.main_container, fragment);
                            tran.addToBackStack("");
                            tran.commit();

                        }else if (id == 3){

                            //startActivity(intent);
                            finish();
                            return true;
                        }

                        return false;
                    }
                })

                .build();
    }
}
