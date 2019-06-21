package com.example.mohamed.anticheating;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ManualNotificationActivity extends AppCompatActivity
implements AutoNotiFragment.OnFragmentInteractionListener,NotiListener{


    Context context;
    boolean is_manual;
    int surveillanceId;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        is_manual=true;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_notification);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_hand);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_auto);




        //findViewById(R.id.AutoNotiFragment).setVisibility(View.GONE);


       // Button btnSwap = (Button) findViewById(R.id.swap);
        /*btnSwap.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(is_manual){
                    is_manual=false;
         */           // Create new fragment and transaction


           //         findViewById(R.id.manNotiFragment).setVisibility(View.GONE);

             //       findViewById(R.id.AutoNotiFragment).setVisibility(View.VISIBLE);
                   /* AutoNotiFragment newFragment = new AutoNotiFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack if needed
                    transaction.replace(R.id.notiFragment, newFragment);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();*/
               /* }
                else{
                    is_manual=false;
                    findViewById(R.id.manNotiFragment).setVisibility(View.VISIBLE);
                    findViewById(R.id.AutoNotiFragment).setVisibility(View.GONE);
                 */   // Create new fragment and transaction

                /*    findViewById(R.id.layer1front).setVisibility(View.GONE);

                    View root = findViewById(R.id.root);
                    root.removeView(yourViewToRemove);
              */
                   /* ManualNotiFragment newFragment = new ManualNotiFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.notiFragment, newFragment).commit();
*/
                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack if needed
                  /*  transaction.replace(R.id.notiFragment, newFragment);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();
*/
                //}
                // TODO Auto-generated method stub
                /*Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);*/

/*                Intent myIntent = new Intent(getActivity(),
                        ManualNotificationActivity.class);
                startActivity(myIntent);
  */
            //}
        //});

        //((ManualNotiFragment) getSupportFragmentManager().findFragmentById(R.id.manNotiFragment)).setmListener(this);



        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra("surveillanceId")){
            Bundle extras =intent.getExtras();
            this.surveillanceId= extras.getInt("surveillanceId");
      //      GetNotificationsTask getNotificationsTask =new GetNotificationsTask();
            String[]s=new String[1];
            s[0]= Integer.toString(this.surveillanceId);
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();

    //        getNotificationsTask.execute(s);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manual_notification, menu);
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


    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    public void setSelectedNoti(ManualNotification mn) {

        Intent intent;
        intent = new Intent(this, streamingActivity.class)
                        .putExtra("mn", (Serializable) mn);
        startActivity(intent);
    }



        /**
         * Prepare the weather high/lows for presentation.
         */


    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment frag = null;
            if(position == 0)
            {
                frag = new ManualNotiFragment();
            }
            if(position == 1)
            {
                frag = new AutoNotiFragment();
            }

            return frag;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            ActionBar acb = getSupportActionBar();
            acb.setIcon(R.drawable.ic_action_logo);
            switch (position) {
                case 0:
                    return "Manual";
                case 1:
                    return "Auto";
            }
            return null;
        }
    }

}
