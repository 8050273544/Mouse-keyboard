package com.example.akash.shareit;



import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;


public class GestureLestener extends GestureDetector.SimpleOnGestureListener {

    static  float xx,yy,nn;
    public static final String TAG = "GestureListener";


    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        nn= 880;
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // User attempted to scroll
        String s ="x-axis" + distanceX+"y-axis"+ distanceY;


        xx=distanceX;
        yy=distanceY;



        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        nn=882;
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {

        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {

        nn= 882;
        return false;
    }



}