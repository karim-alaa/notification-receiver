package com.example.mohamed.anticheating;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import Classes.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AutoNotiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AutoNotiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AutoNotiFragment extends Fragment {
    View rootView;
    FirebaseDatabase database;
    User user;
    String key;
    AutomaticNotificationAdapter autoNotificationAdapter;
    ArrayList<ManualNotification> autoNotificationArrayList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   /* private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
*/
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     //* @param param1 Parameter 1.
     //* @param param2 Parameter 2.
     * @return A new instance of fragment AutoNotiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AutoNotiFragment newInstance() {
        AutoNotiFragment fragment = new AutoNotiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AutoNotiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_auto_noti, container, false);
        /*autoNotificationArrayList= new ArrayList<ManualNotification>();
        for (int i =0;i<10;i++){
            ManualNotification mn = new ManualNotification();
            mn.header = "header";
            mn.content = "content";
            autoNotificationArrayList.add(mn);

        }*/


        Intent myIntent = getActivity().getIntent();
        user = (User)myIntent.getSerializableExtra("userObj");



        database = FirebaseDatabase.getInstance();
        final DatabaseReference manual_notification = database.getReference("notifications").child("admin_"+user.getAdmin()).child("school_"+user.getSchool()).child("committee_"+user.getCommittee()).child("automatic").child(user.getDate());

        manual_notification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                autoNotificationArrayList = new ArrayList<ManualNotification>();

                for (DataSnapshot d: dataSnapshot.getChildren()) {

                    ManualNotification manualNotification = new ManualNotification();

                    manualNotification.setKey(d.getKey());
                    manualNotification.setIs_automatic(true);
                    manualNotification.setHeader(d.child("header").getValue().toString());
                    manualNotification.setContent(d.child("content").getValue().toString());
                    manualNotification.setVideo_url(d.child("url").getValue().toString());
                    String seen = d.child("seen").getValue().toString();
                    if (seen.equals("true")) {
                        manualNotification.setSeen(true);
                    } else {
                        manualNotification.setSeen(false);
                    }
                    autoNotificationArrayList.add(manualNotification);
                }





                autoNotificationAdapter = new AutomaticNotificationAdapter(
                        getActivity(),
                        autoNotificationArrayList
                );

                GridView gridView = (GridView) rootView.findViewById(
                        R.id.grdVwAutoNoti);

                gridView.setAdapter(autoNotificationAdapter);


                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {

                        ManualNotification mn = (ManualNotification) adapterView.getItemAtPosition(postion);
                        //make it seen
                        if(!mn.isSeen())
                        makeItSeen(user,mn,"automatic");
                        Intent intent;
                        intent = new Intent(getActivity(), streamingActivity.class)
                                .putExtra("mn", (Serializable) mn).putExtra("user",user);
                        //.putExtra("surveillanceId", ((ManualNotificationActivity) getActivity()).surveillanceId);
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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
        Toast.makeText(getActivity(),mn.getKey(),Toast.LENGTH_LONG).show();
    }

}
