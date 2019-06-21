package com.example.mohamed.anticheating;

import android.graphics.Bitmap;

import org.opencv.core.Rect;

import java.util.ArrayList;

/**
 * Created by Mohamed on 01/03/2017.
 */
public class IntialImage {
    public int surveillanceId;
    public Bitmap bitmap;
    public ArrayList<Rect> rects = new ArrayList<Rect>() ;
    /*public IntialImage(){

        ArrayList<Rect> rects = new ArrayList<Rect>();
    }*/
}
