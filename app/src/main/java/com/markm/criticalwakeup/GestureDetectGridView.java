package com.markm.criticalwakeup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

public class GestureDetectGridView extends GridView {

    private GestureDetector gDetector;
    private boolean mFlingConfirm = false;
    private float mTouchX;
    private float mTouchY;

    private static final int swipe_min_distance = 100;
    private static final int swipe_max_off_path = 100;
    private static final int swipe_threshold_velocity = 100;

    public GestureDetectGridView(Context context){
        super(context);
        init(context);
    }

    public GestureDetectGridView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) //API 21
    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
        init(context);
    }

    private void init(final Context context){
        gDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onDown(MotionEvent event){
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
                final int position = GestureDetectGridView.this.pointToPosition(Math.round(e1.getX()), Math.round(e1.getY()));

                if(Math.abs(e1.getY() - e2.getY()) > swipe_max_off_path){
                    if(Math.abs(e1.getX() - e2.getX()) > swipe_max_off_path || Math.abs(velocityY) < swipe_threshold_velocity){
                        return false;
                    }
                    if(e1.getY() - e2.getY() > swipe_min_distance){
                        MainActivity.moveTiles(context, MainActivity.UP, position);
                    }
                    else if(e2.getY() - e1.getY() > swipe_min_distance){
                        MainActivity.moveTiles(context, MainActivity.DOWN, position);
                    }
                }
                else{
                    if(Math.abs(velocityX) < swipe_threshold_velocity){
                        return false;
                    }
                    if(e1.getX() - e2.getX() > swipe_min_distance){
                        MainActivity.moveTiles(context, MainActivity.LEFT, position);
                    }
                    else if(e2.getX() - e1.getX() > swipe_min_distance){
                        MainActivity.moveTiles(context, MainActivity.RIGHT, position);
                    }
                }

                return super.onFling(e1,e2,velocityX,velocityY);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        int action = ev.getActionMasked();
        gDetector.onTouchEvent(ev);

        if(action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP){
            mFlingConfirm = false;
        }
        else if(action == MotionEvent.ACTION_DOWN){
            mTouchX = ev.getX();
            mTouchY = ev.getY();
        }
        else{
            if(mFlingConfirm){
                return true;
            }

            float dX = (Math.abs(ev.getX()) - mTouchX);
            float dY = (Math.abs(ev.getY()) - mTouchY);
            if((dX > swipe_min_distance) || (dY > swipe_min_distance)){
                mFlingConfirm = true;
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return gDetector.onTouchEvent(ev);
    }
}
