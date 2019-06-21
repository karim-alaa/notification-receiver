package com.example.mohamed.anticheating;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import Classes.User;

/**
 * A placeholder fragment containing a simple view.
 */
public class ManualNotiFragment extends Fragment {
    ArrayList<ManualNotification> manualNotifications;
    NotiListener notiListener;
    FirebaseDatabase database;
    User user;
    String key;
    View rootView;

    ManualNotificationAdapter notificationAdapter;
    public ManualNotiFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         rootView = inflater.inflate(R.layout.fragment_manual_notification, container, false);


        Intent myIntent = getActivity().getIntent();
        user = (User)myIntent.getSerializableExtra("userObj");



        database = FirebaseDatabase.getInstance();
        final DatabaseReference manual_notification = database.getReference("notifications").child("admin_"+user.getAdmin()).child("school_"+user.getSchool()).child("committee_"+user.getCommittee()).child("manual").child(user.getDate());

        manual_notification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ManualNotification> notifications = new ArrayList<ManualNotification>();

                for (DataSnapshot d: dataSnapshot.getChildren()) {

                    ManualNotification manualNotification = new ManualNotification();
                    manualNotification.setKey(d.getKey());
                    manualNotification.setIs_automatic(false);
                    manualNotification.setHeader(d.child("header").getValue().toString());
                    manualNotification.setContent(d.child("content").getValue().toString());
                    manualNotification.setVideo_url(d.child("url").getValue().toString());
                    String seen = d.child("seen").getValue().toString();
                    if (seen.equals("true")) {
                        manualNotification.setSeen(true);
                    } else {
                        manualNotification.setSeen(false);
                    }
                    notifications.add(manualNotification);
                }

                ListView listView = (ListView) rootView.findViewById(R.id.lstVwManNoti);





                notificationAdapter = new ManualNotificationAdapter(getActivity(), notifications);

                listView.setAdapter(notificationAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                        ManualNotification mn = (ManualNotification) adapterView.getItemAtPosition(postion);
                        //make it seen
                        if(!mn.isSeen())
                        makeItSeen(user,mn,"manual");
                            Intent myIntent = new Intent(getActivity(), streamingActivity.class);
                            myIntent.putExtra("mn", (Serializable) mn).putExtra("user",user);
                            startActivity(myIntent);

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







        return rootView;
    }

    public void setmListener(NotiListener notiListener){
        this.notiListener = notiListener;
    }

    public void makeItSeen(User user,ManualNotification mn,String type){
        try {
            database.getReference()
                    .child("notifications")
                    .child("admin_"+user.getAdmin())
                    .child("school_"+user.getSchool())
                    .child("committee_"+user.getCommittee())
                    .child(type)
                    .child(user.getDate())
                    .child(mn.getKey())
                    .child("seen")
                    .setValue("true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
