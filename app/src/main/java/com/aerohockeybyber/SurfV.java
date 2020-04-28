package com.aerohockeybyber;

import android.annotation.SuppressLint;


import android.view.View;
import android.view.MotionEvent;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public final class SurfV extends SurfaceView implements Callback {


    private SurfaceHolder surfHolder;
    private Thread thread3;
    public boolean top;
    private int amount;
    boolean initiated;
    public boolean floor;
    PuckHockGame hockG;
    public String message;
    int i;
    private int heightDisplay;

    public float floatX1;
    public float floatX2;
    public float floatY1;
    public float floatY2;


    static boolean pauseThr;


    class OnTouched implements OnTouchListener {
        OnTouched() {
        }

        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouch(View v, MotionEvent event) {
            int d = SurfV.this.heightDisplay / 2;
            SurfV.this.amount = event.getPointerCount();
            if (SurfV.this.amount > 0) {
                if (event.getY(0) < d) {
                    SurfV.this.floatX1 = event.getX(0);
                    SurfV.this.floatY1 = event.getY(0);
                } else if (event.getY(0) > d) {
                    SurfV.this.floatX2 = event.getX(0);
                    SurfV.this.floatY2 = event.getY(0);
                }
            }
            if (SurfV.this.amount > 1) {
                if (event.getY(1) < d) {
                    SurfV.this.floatX1 = event.getX(1);
                    SurfV.this.floatY1 = event.getY(1);
                } else {
                    SurfV.this.floatX2 = event.getX(1);
                    SurfV.this.floatY2 = event.getY(1);
                }
            }
            if (event.getAction() == 1) {
                SurfV.this.floor = false;
                SurfV.this.top = true;
            }
            if (event.getAction() == 0) {
                SurfV.this.floor = true;
                SurfV.this.top = false;
            }
            return true;
        }
    }

    public SurfV(MainActivity context) {
        super(context);

        this.message = "no";
        this.amount = 0;
        this.surfHolder = getHolder();
        this.surfHolder.addCallback(this);
        this.initiated = false;
        this.top = false;
        this.floor = false;
        setOnTouchListener(new OnTouched());
    }

    public void surfaceCreated(SurfaceHolder holder) {
        //surface creating
        if (!this.initiated) {
            int screenWidth = getWidth();
            this.heightDisplay = getHeight();
            this.initiated = true;
           // surface initiated 1st time
            this.hockG = new PuckHockGame(this, this.surfHolder, screenWidth, this.heightDisplay);
        }
        pauseThr = false;
        this.thread3 = new Thread(this.hockG);
        this.thread3.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        //surface changing
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        //surface destroying
        pauseThr = true;
        boolean retry = true;
        while (retry) {
            try {
                this.thread3.join();
                retry = false;
            } catch (Exception e) {
                //join thread
            }
        }
    }
}
