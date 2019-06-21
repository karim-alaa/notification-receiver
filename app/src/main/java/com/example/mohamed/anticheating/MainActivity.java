package com.example.mohamed.anticheating;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    public ImageView targetImage;
    Bitmap originalImg;
//    ArrayList<Rect> rects = new ArrayList<Rect>();
    Point touchedPoint;
    int selectedRectId;
    int touchX;
    int touchY;
    Mat imgMat;
    Context context;
    IntialImage intialImage;
    int surveillanceId;
//    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;

/*
        rects.add(new Rect(new Point(300, 50), new Point(500, 350)));
        rects.add(new Rect(new Point(650, 50), new Point(900, 350)));
        rects.add(new Rect(new Point(300, 400), new Point(500, 650)));
        rects.add(new Rect(new Point(650, 400),new Point(900,650)));*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        targetImage = (ImageView)findViewById(R.id.cheatingImage);



        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra("surveillanceId")){
            Bundle extras =intent.getExtras();
            this.intialImage=new IntialImage();
             this.intialImage.surveillanceId= extras.getInt("surveillanceId");
            this.surveillanceId = this.intialImage.surveillanceId;
    //        FetchIntialPositionTask intialPositionTask =new FetchIntialPositionTask((MainActivity)context);
            String[]s=new String[1];
            s[0]= Integer.toString(this.intialImage.surveillanceId);
            // Toast.makeText(c, "clicked", Toast.LENGTH_SHORT).show();

      //      intialPositionTask.execute(s);
        }


        /*FetchIntialPositionTask intialPositionTask =new FetchIntialPositionTask();
        String[]s=new String[1];
        s[0]= "1";
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();

        intialPositionTask.execute(s);
*/
        // Button buttonLoadImage = (Button)findViewById(R.id.loadimage);
        Button buttonConfirm = (Button)findViewById(R.id.btnConfirm);


        buttonConfirm.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {

        //        ConfirmPositionTask confirmPositionTask =new ConfirmPositionTask((MainActivity)context);
                Object[]s=new Object[1];
                s[0]= intialImage;
               // Toast.makeText(c, "clicked", Toast.LENGTH_SHORT).show();

          //      confirmPositionTask.execute(s);


            }});
        //textTargetUri = (TextView)findViewById(R.id.targeturi);

        /*buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //Intent intent = new Intent(Intent.ACTION_PICK,
                  //      android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(intent, 0);

                Intent myIntent = new Intent(MainActivity.this,
                        streamingActivity.class);
                startActivity(myIntent);
            }});
*/
        targetImage.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                    float x1=0, x2, y1=0 , y2, dx, dy;
                    String direction;
                    switch(event.getAction()) {
                        case(MotionEvent.ACTION_DOWN):
                            Toast.makeText(context, " fingre DOWN ", Toast.LENGTH_SHORT).show();
                            int width = targetImage.getWidth();
                            int height=targetImage.getHeight();
                             touchX = (int)(((double) event.getX())/ (double)width * (double)originalImg.getWidth());
                             touchY = (int)(((double) event.getY())/(double)height * (double)originalImg.getHeight());
                            x1 = touchX;
                            y1 = touchY;
                            Toast.makeText(context,"x1 = "+ x1 +" y1 = " + y1, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context, "x loc = " + width + " y loc = " + height, Toast.LENGTH_SHORT).show();
                           // Toast.makeText(context,"x touch = "+ touchX +" y touch = " + touchY, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context,"x imageX = "+ imageX +" y imageY = " + imageY, Toast.LENGTH_SHORT).show();
                            touchedPoint = new Point(touchX,touchY);
                            for (int i=0;i<intialImage.rects.size();i++){
                                if( intialImage.rects.get(i).contains(touchedPoint)) {
                                    selectedRectId = i;
                                    Toast.makeText(context, "selected = " + selectedRectId, Toast.LENGTH_SHORT).show();
                                }
                            }

                            break;

                        case(MotionEvent.ACTION_MOVE): {

                            x2 = (int)(((double) event.getX())/ (double)targetImage.getWidth() * (double)originalImg.getWidth());
                            y2 = (int)(((double) event.getY())/(double)targetImage.getHeight() * (double)originalImg.getHeight());
                            dx = x2-touchX;
                            dy = y2-touchY;
                            touchX=(int)x2;touchY=(int)y2;
                            intialImage.rects.get(selectedRectId).x+=dx;
                            intialImage.rects.get(selectedRectId).y+=dy;
                            // Use dx and dy to determine the direction
                            //if(Math.abs(dx) > Math.abs(dy)) {
                                /*if(dx>0) {
                                    direction = "right";
                                    Toast.makeText(context, Float.toString(x1)+ " " +direction + " " +Float.toString(x2), Toast.LENGTH_SHORT).show();

                                    rects.get(selectedRectId).x+=5;
                                }
                                else {
                                    direction = "left";

                                    Toast.makeText(context, Float.toString(x1)+ " " +direction + " " +Float.toString(x2), Toast.LENGTH_SHORT).show();

                                    rects.get(selectedRectId).x-=5;
                                }
                            //}
                            //else {
                                if(dy>0) {
                                    direction = "down";


                                    Toast.makeText(context, Float.toString(y1)+ " " + direction + " " +Float.toString(y2), Toast.LENGTH_SHORT).show();

                                    rects.get(selectedRectId).y+=5;
                                }
                                else {
                                    direction = "up";

                                    Toast.makeText(context, Float.toString(y1)+ " "+direction + " " +Float.toString(y2), Toast.LENGTH_SHORT).show();

                                    rects.get(selectedRectId).y-=5;
                                }*/
                            //}

                            Utils.bitmapToMat(originalImg, imgMat);

                            Mat imgSource=imgMat.clone();

                            for( int i=0;i<intialImage.rects.size();i++) {
                                Imgproc.rectangle(imgSource, new Point(intialImage.rects.get(i).x, intialImage.rects.get(i).y), new Point(intialImage.rects.get(i).x + intialImage.rects.get(i).width, intialImage.rects.get(i).y+intialImage.rects.get(i).height), new Scalar(255, 255, 255, 255), 6);
                            }


                            Bitmap analyzed = Bitmap.createBitmap(imgSource.cols(),imgSource.rows(),Bitmap.Config.ARGB_8888);
                            Utils.matToBitmap(imgSource, analyzed);
                            targetImage.setImageBitmap(analyzed);
                        }

                            break;
                        default: break;
                    }


                return  true;

            }});
    }



    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    imgMat=new Mat();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public  void detect(View view){
        Utils.bitmapToMat(originalImg, imgMat);

        Mat imgSource=imgMat.clone();

        for(int i=0;i<intialImage.rects.size();i++) {
            Imgproc.rectangle(imgSource, new Point(intialImage.rects.get(i).x, intialImage.rects.get(i).y), new Point(intialImage.rects.get(i).x + intialImage.rects.get(i).width, intialImage.rects.get(i).y+intialImage.rects.get(i).height), new Scalar(255, 0, 0, 255), 3);
        }


        Bitmap analyzed = Bitmap.createBitmap(imgSource.cols(),imgSource.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imgSource, analyzed);
        targetImage.setImageBitmap(analyzed);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
          //  textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                originalImg = bitmap;
                targetImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onTaskCompleted(IntialImage values) {
        this.intialImage=values;
        this.intialImage.surveillanceId=this.surveillanceId;
    }







}
