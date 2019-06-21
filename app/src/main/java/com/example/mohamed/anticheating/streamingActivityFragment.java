package com.example.mohamed.anticheating;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.FirebaseDatabase;

import Classes.User;

/**
 * A placeholder fragment containing a simple view.
 */
public class streamingActivityFragment extends Fragment {

    //VideoView vidView = (VideoView)findViewById(R.id.myVideo);

    public streamingActivityFragment() {
    }
    ManualNotification mn;
    VideoView vidView;
    User user;
    int surveillanceId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_streaming, container, false);
        Button btnConfirm = (Button)view.findViewById(R.id.btnConfirm);
        Button btnIgnore = (Button)view.findViewById(R.id.btnIgnore);

        btnConfirm.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                updateStatus(mn,user,"confirmed");
                // TODO Auto-generated method stub
                /*Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);*/
                //ConfirmTask confirmTask =new ConfirmTask();

                //   Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();

                //confirmTask.execute(s);

            }});


        btnIgnore.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                updateStatus(mn,user,"ignored");
                // TODO Auto-generated method stub
                /*Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);*/
    //            ConfirmTask confirmTask =new ConfirmTask();

                //   Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();

               // confirmTask.execute(s);

            }});

        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra("mn") || mn != null) {
            if (null == mn)
                mn = (ManualNotification) intent.getSerializableExtra("mn");
                user = (User) intent.getSerializableExtra("user");
                //this.surveillanceId = intent.getIntExtra("surveillanceId",0);
            vidView = (VideoView)view.findViewById(R.id.myVideo);
            Toast.makeText(getActivity(), mn.getVideo_url(), Toast.LENGTH_SHORT).show();
            if(!mn.getVideo_url().isEmpty()) {
                String vidAddress = mn.getVideo_url();
            /*            Uri vidUri = Uri.parse(vidAddress);
                        vidView.setVideoURI(vidUri);
                        //vidView.setVideoPath(vidAddress);
                        MediaController vidControl = new MediaController(getActivity());
                        vidControl.setAnchorView(vidView);
                        vidView.setMediaController(vidControl);
                        vidView.start();*/


                Uri video = Uri.parse(vidAddress);
                vidView.setVideoURI(video);
                vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                        vidView.start();
                    }
                });
            }
        }
      /*  vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });*/

        /*
        VideoView vidView = (VideoView)view.findViewById(R.id.myVideo);
        String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);
        MediaController vidControl = new MediaController(getActivity());
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        vidView.start();
        */return view;


    }

    public void updateStatus(ManualNotification mn, User user, String status){
        try {
            String type = "manual";
            if(mn.is_automatic())
                type = "automatic";
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference()
                    .child("notifications")
                    .child("admin_"+user.getAdmin())
                    .child("school_"+user.getSchool())
                    .child("committee_"+user.getCommittee())
                    .child(type)
                    .child(user.getDate())
                    .child(mn.getKey())
                    .child("status")
                    .setValue(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
