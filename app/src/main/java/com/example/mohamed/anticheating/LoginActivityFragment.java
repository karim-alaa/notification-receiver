package com.example.mohamed.anticheating;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import Classes.User;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String email;
    private String password;
    private String id;
    User user = new User();
    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button btnLogin = (Button)view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                /*Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);*/
                LoginTask loginTask =new LoginTask();

                email = ((TextView)view.findViewById(R.id.edtTxtEmail)).getText().toString();
                password = ((TextView)view.findViewById(R.id.edtTxtPassword)).getText().toString();



                mAuth = FirebaseAuth.getInstance();

                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // User is signed in
                            //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        } else {
                            // User is signed out
                            //Log.d(TAG, "onAuthStateChanged:signed_out");
                        }
                        // ...
                    }
                };
                //Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                 // Toast.makeText(getActivity(),"login successful",Toast.LENGTH_LONG).show();

                                FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
                                id = fbUser.getUid();

                                // Toast.makeText(getActivity(),id,Toast.LENGTH_SHORT).show();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("users").child(id);

                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // This method is called once with the initial value and again
                                        // whenever data at this location is updated.
                                        // List<User>  users = new ArrayList<User>();


                                        user.setEmail(email);
                                        user.setId(id);
                                        user.setName((String) dataSnapshot.child("name").getValue());

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference user_date_data_ref = database.getReference("users").child(id).child("date_data");
                                        //DataSnapshot user_date_data =  (DataSnapshot) dataSnapshot.child("date_data").getValue();
if(dataSnapshot.child("role").getValue().equals("AS_Admin")) {
    user_date_data_ref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            for (DataSnapshot obj : dataSnapshot.getChildren()) {

                String user_data = obj.getValue().toString();
                //Toast.makeText(getActivity(),user_data,Toast.LENGTH_LONG).show();
                String[] user_date_data = user_data.split("##");
                try {
                    String date = user_date_data[0].replaceAll("\\s", "");
                    String admin_num = user_date_data[1].replaceAll("\\s", "");
                    String school_num = user_date_data[2].replaceAll("\\s", "");
                    String committee_num = user_date_data[3].replaceAll("\\s", "");


                    if (date.contains(getTodayDate())) {

                        user.setDate(date);
                        user.setAdmin(admin_num);
                        user.setSchool(school_num);
                        user.setCommittee(committee_num);

                    }
                } catch (Exception ex) {

                }

            }

            //if(!user.getDate().equals("")){
            Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_LONG).show();
            if (user.getDate() != null) {
                // selectNestedObjects(user);
                Intent myIntent = new Intent(getActivity(), ManualNotificationActivity.class);
                myIntent.putExtra("userObj", user);
                startActivity(myIntent);
            } else {
                // Toast.makeText(getActivity(),getTodayDate(),Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "You are not allowed to be here !!", Toast.LENGTH_LONG).show();
            }

            // Toast.makeText(getActivity(),user.getDate()+"/"+user.getAdmin_num()+"/"+user.getCommittee_num()+"/"+user.getSchool_num(),Toast.LENGTH_LONG).show();
                                               /* }else {
                                                    Toast.makeText(getActivity(),"You are not allow to be here !!",Toast.LENGTH_LONG).show();
                                                }*/


        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
} else {
                                        // Toast.makeText(getActivity(),getTodayDate(),Toast.LENGTH_LONG).show();
                                        Toast.makeText(getActivity(), "You are not allowed to be here !!", Toast.LENGTH_LONG).show();
                                    }


                                        Toast.makeText(getActivity(),user.getName(),Toast.LENGTH_SHORT).show();

                                      /*  for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                            User user = new User();
                                            String user_id = (String) messageSnapshot.child("user_id").getValue();
                                            String name = (String) messageSnapshot.child("name").getValue();
                                            //DataSnapshot user_date_data = (DataSnapshot) messageSnapshot.child("date_data").getValue();
                                            user.setName(name);
                                            user.setId(user_id);
                                            user.setEmail(email);

                                            users.add(user);
                                        }*/

                                        //Toast.makeText(getActivity(),FirebaseAuth.getInstance().getCurrentUser().getUid(),Toast.LENGTH_LONG).show();
                                       /* for (User user:users) {

                                            if(user.getId().equals(id)){
                                                Toast.makeText(getActivity(),user.getName(),Toast.LENGTH_LONG).show();
                                            }else {
                                               // Toast.makeText(getActivity(),"Sorry You Are Not Allow To Be Here !!",Toast.LENGTH_SHORT).show();
                                            }
                                        }*/

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Failed to read value
                                        Log.w(TAG, "Failed to read value.", error.toException());
                                    }
                                });


                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    //  Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(getActivity(), "There is an invalid email or password", Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });











                //   Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();

                //  loginTask.execute(s);

/*                Intent myIntent = new Intent(getActivity(),
                        ManualNotificationActivity.class);
                startActivity(myIntent);
  */          }});
        return view;
    }



    public class LoginTask extends AsyncTask<String, Void, String > {

        private final String LOG_TAG = LoginTask.class.getSimpleName();



        @Override
        protected String doInBackground(String... params){
            if(params.length == 0){
                return  null;
            }
            // We add the content that we want to pass with the POST request to as name-value pairs
            //Now we put those sending details to an ArrayList with type safe of NameValuePair
            //
            //
            // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                String urlParameters  = "username="+params[0]+"&password="+params[1];
                byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
                int    postDataLength = postData.length;

                StringBuffer buffer = new StringBuffer();


                String FORECAST_BASE_URL = "http://192.168.137.1:80/ch/login.php" ;

//                Toast.makeText(getActivity(),builtUri.toString() , Toast.LENGTH_SHORT).show();


                URL url = new URL(FORECAST_BASE_URL);
                // Create the request to OpenWeatherMap, and open the connection

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                try( DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream())) {
                    wr.write( postData );
                }
                try {
                    urlConnection.connect();

                    Log.e(LOG_TAG, "connected");
                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                        //forecastJsonStr = null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;

                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                }
                catch (Exception ex){
                    return null;
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                    //forecastJsonStr = null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // to parse it
                //
                return null;
                //forecastJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return movieJsonStr;
            }
            catch (Exception e){
                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();
            }

            return "0";
        }


        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity(),result, Toast.LENGTH_SHORT).show();

            //if(result != null ) {
            Intent myIntent = new Intent(getActivity(),
                    MainActivity.class);
            Bundle extras= new Bundle();








//            extras.putInt("surveillanceId",Integer.valueOf(result.replaceAll("\\s+","")));
            //extras.putInt("surveillanceId",1);

            //myIntent.putExtras(extras);

            //startActivity(myIntent);
            /*}else {
                Toast.makeText(getActivity(), "data isn't correct", Toast.LENGTH_SHORT).show();
            }*/

        }
    }

    private String getTodayDate(){
        Date date = new Date();
        //String nowDate = String.format("%d/%m/%y", date );
        SimpleDateFormat ft = new SimpleDateFormat ("dd-MM-yyyy");

        //System.out.println("Current Date: " + ft.format(date));
        String NDate = ft.format(date);
        String[] listDate = NDate.split("-");

        String day="",month="",year="";

        if (listDate[0].length() == 2)
            day = listDate[0];
        else
            day = "0" + listDate[0];
        if (listDate[1].length() == 2)
            month = listDate[1];
        else
            month = "0" + listDate[1];
        if (listDate[2].length() == 4)
            year = listDate[2];


        year = listDate[2];


        NDate = day + "-" + month + "-" + year;
        return NDate;
    }

/* void selectNestedObjects(User user){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference manual_notification = database.getReference("notifications").child("admin_"+user.getAdmin()).child("school_"+user.getSchool()).child("committee_"+user.getCommittee()).child("manual").child(user.getDate());

        manual_notification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String header = "";
                ArrayList<ManualNotification> notifications = new ArrayList<ManualNotification>();

                for (DataSnapshot d: dataSnapshot.getChildren()) {
                    ManualNotification manualNotification = new ManualNotification();
                    header += d.child("content").getValue();
                       manualNotification.setHeader(d.child("header").getValue().toString());
                        manualNotification.setContent(d.child("content").getValue().toString());
                        manualNotification.setVideo_url(d.child("url").getValue().toString());
                        String seen = d.child("seen").getValue().toString();
                        if(seen.equals("true")){
                            manualNotification.setSeen(true);
                        }else{
                            manualNotification.setSeen(false);
                        }
                    notifications.add(manualNotification);
                }
                Toast.makeText(getActivity(),notifications.get(1).getVideo_url(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }*/



}
