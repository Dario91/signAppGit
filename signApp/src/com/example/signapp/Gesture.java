package com.example.signapp;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class Gesture extends GestureDetector.SimpleOnGestureListener {
 
    public static final String TAG = "GestureListener";
    private SignAppMain signAppMain;

    public Gesture(){}
    public Gesture(SignAppMain s)
    {
        signAppMain = s;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // Up motion completing a single tap occurred.
        signAppMain.signPicture((int)e.getX(),(int)e.getY());
        Log.i(TAG, String.valueOf(e.getX()));
        Log.i(TAG, String.valueOf(e.getY()));
        Log.i(TAG, "Single Tap Up");
        return false;
    }
 
    @Override
    public void onLongPress(MotionEvent e) {
        // Touch has been long enough to indicate a long press.
        // Does not indicate motion is complete yet (no up event necessarily)
        Log.i(TAG, "Long Press");
    }
 
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
    float distanceY) {
        // User attempted to scroll
        Log.i(TAG, "Scroll");
        return false;
    }
 
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
    float velocityY) {
        // Fling event occurred.  Notification of this one happens after an "up" event.
        Log.i(TAG, "Fling");
        return false;
    }
 
    @Override
    public void onShowPress(MotionEvent e) {
        // User performed a down event, and hasn't moved yet.
        Log.i(TAG, "Show Press");
    }
 
    @Override
    public boolean onDown(MotionEvent e) {
        // "Down" event - User touched the screen.
        Log.i(TAG, "Down");
        return false;
    }
 
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        // User tapped the screen twice.
        Log.i(TAG, "Double tap");
        return false;
    }
 
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        // Since double-tap is actually several events which are considered one aggregate
        // gesture, there's a separate callback for an individual event within the doubletap
        // occurring.  This occurs for down, up, and move.
        Log.i(TAG, "Event within double tap");
        return false;
    }
 
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        // A confirmed single-tap event has occurred.  Only called when the detector has
        // determined that the first tap stands alone, and is not part of a double tap.
        Log.i(TAG, "Single tap confirmed");
        return false;
    }
}