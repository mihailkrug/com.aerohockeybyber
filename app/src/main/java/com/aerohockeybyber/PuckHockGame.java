package com.aerohockeybyber;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.core.view.MotionEventCompat;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.SurfaceHolder;

public final class PuckHockGame implements Runnable {

    private static int height = 0;
    private static int lvl = 0;
    private static final int COMP_W = 1;
    private static final int N_W = 3;
    private static final int PL_W = 2;
    private static BtnMy btnMy1 = null;
    private static BtnMy btnMy2 = null;
    private static BtnMy btnMy3 = null;
    private static BtnMy btnMy4 = null;
    private static BtnMy btnMy5 = null;
    private static final int skorostKadra = 61;

int myvalue = 0;
    private static Paint paintGoal1 = null;
    private static Paint paintGoal2 = null;
    private static long start = 0;
    private static final int target = 16;
    private static Paint pntTxt, pntTxt1, pntTxt2;
    private static int STATE = 1;
    private static final int STATE_ESC = 4;
    private static boolean protivRival;
    private static long sleep;
    private final SurfaceHolder surfHolder;
    private Bitmap tmpr;
    private int tm_goal;
    private final SurfV surfWin;
    private static int wdth;
    private Bitmap imgMenu;
    private VCTR mPlr1;
    private VCTR mPlr2;
    private Bitmap background;
    private Puck puck;
    private Puck rival;
    private int sc_curr;
    private int rival_score;
    private int plr_score;
    private int peremoga;
    private Paint blckPnt;
    private Puck plyr;
    private int decision;


    PuckHockGame(SurfV surfaceView, SurfaceHolder surfHolder, int w, int h) {
        this.mPlr1 = new VCTR(0.0f, 0.0f);
        this.mPlr2 = new VCTR(0.0f, 0.0f);
        this.peremoga = 9;
        STATE = COMP_W;
        this.surfWin = surfaceView;
        this.surfHolder = surfHolder;
        wdth = w;
        height = h;
        setBrd();
        this.tmpr = BitmapFactory.decodeResource(this.surfWin.getResources(), R.drawable.back);
        this.imgMenu = Bitmap.createScaledBitmap(this.tmpr, wdth, height, false);
        int size = wdth / 5;
        int pos = (height * PL_W) / N_W;
        btnMy1 = new BtnMy(wdth / STATE_ESC, pos, size);
        btnMy1.btmpsSET(BitmapFactory.decodeResource(this.surfWin.getResources(), R.drawable.play_computer));
        btnMy2 = new BtnMy((wdth * N_W) / STATE_ESC, pos, size);
        btnMy2.btmpsSET(BitmapFactory.decodeResource(this.surfWin.getResources(), R.drawable.player2));
        btnMy3 = new BtnMy(wdth / PL_W, (height * 5) / 6, size);
        btnMy3.btmpsSET(BitmapFactory.decodeResource(this.surfWin.getResources(), R.drawable.exit));
        btnMy4 = new BtnMy(wdth / STATE_ESC, pos, wdth / STATE_ESC);
        btnMy4.btmpsSET(BitmapFactory.decodeResource(this.surfWin.getResources(), R.drawable.resume));
        btnMy5 = new BtnMy((wdth * N_W) / STATE_ESC, pos, wdth / STATE_ESC);
        btnMy5.btmpsSET(BitmapFactory.decodeResource(this.surfWin.getResources(), R.drawable.exit));
        this.blckPnt = new Paint();
        this.blckPnt.setColor(Color.argb(150, 0, 0, 0));

        this.puck = new Puck(height / 30, null);
        this.puck.setImgBitmap(BitmapFactory.decodeResource(this.surfWin.getResources(), R.drawable.ball));
        this.puck.setBorder(0, 0, wdth, height);
        this.puck.initPuck(100.0f, 100.0f, 5.0f, 2.0f);
        this.rival = new Puck(height / target, this.mPlr1);
        this.rival.setImgBitmap(BitmapFactory.decodeResource(this.surfWin.getResources(), R.drawable.player_1));
        this.rival.setBorder(0, 0, wdth, (height / PL_W) - (this.puck.intRadi / PL_W));
        this.rival.initPuck((float) (wdth / PL_W), (float) (height / N_W), 0.0f, 0.0f);
        this.plyr = new Puck(height / target, this.mPlr2);
        this.plyr.setImgBitmap(BitmapFactory.decodeResource(this.surfWin.getResources(), R.drawable.player_2));
        this.plyr.setBorder(wdth / PL_W, (height / PL_W) + (this.puck.intRadi / PL_W), wdth, height);
        this.plyr.initPuck((float) (wdth / PL_W), (float) ((height * PL_W) / N_W), 0.0f, 0.0f);
        Puck.setGate(wdth, (int) (wdth/ 1.9d));
        pntTxt = new Paint(COMP_W);
        pntTxt.setColor( Color.rgb(143, 32, 132) );
        pntTxt.setTextAlign(Align.CENTER);
        pntTxt.setTypeface(Typeface.MONOSPACE);
        pntTxt1 = new Paint(COMP_W);
        pntTxt1.setTextSize((float) (wdth / 7));
        pntTxt1.setColor( Color.rgb(143, 32, 132) );
        pntTxt1.setTextAlign(Align.RIGHT);

        pntTxt2 = new Paint(COMP_W);
        pntTxt2.setTextSize((float) (wdth / 7));
        pntTxt2.setColor( Color.rgb(0, 183, 214) );
        pntTxt2.setTextAlign(Align.LEFT);

        paintGoal1 = new Paint();
        paintGoal1.setStyle(Style.STROKE);
        paintGoal1.setStrokeWidth((float) (wdth / 30));
        paintGoal2 = new Paint(paintGoal1);
        paintGoal1.setColor( Color.rgb(143, 32, 132) );
        paintGoal2.setColor( Color.rgb(0, 183, 214) );

        rstIgra();
    }

    private void rstIgra() {
        lvl = COMP_W;
        this.rival_score = 0;
        this.plr_score = 0;
        myvalue = lvl;
        restartPuck();
    }

    private void rstrtScore() {
        lvl += COMP_W;
        this.rival_score = 0;
        this.plr_score = 0;
        myvalue = lvl;
    }

    private void setBrd() {
        int id;
        id = R.drawable.backgr;
        this.tmpr = BitmapFactory.decodeResource(this.surfWin.getResources(), id);
        this.background = Bitmap.createScaledBitmap(this.tmpr, wdth, height, false);
    }

    public void run() {
       //Thread starting
        while (!SurfV.pauseThr) {
            //state
            switch (STATE) {
                case 1 :
                    mMenu();
                    rstIgra();
                    dissolve(1);
                    break;
                case 2 :
                    mPause();
                    if (STATE != 1) {
                        break;
                    }
                    fadeOutPause(1);
                    break;
                case 3 :
                    if (protivRival) {
                        dissolveString("Level " + lvl, PL_W, wdth / 6, wdth / STATE_ESC);
                    }
                    setBrd();
                    this.decision = resume();
                    if (protivRival) {
                        if (this.decision != 1) {
                            if (this.decision != PL_W) {
                                STATE = PL_W;
                                break;
                            }
                            msgShow("WINNER", 2, wdth / 5);
                            rstrtScore();
                            break;
                        }
                        msgShow("GAME OVER", 2, wdth / 7);
                        STATE = PL_W;
                        break;
                    }
                    STATE = PL_W;
                    break;
                case 4 :
                    MainActivity.cntxt.finish();
                    SurfV.pauseThr = true;
                    break;
                default:
                    break;
            }
        }
    }

    private void restartPuck() {
        if (this.sc_curr == PL_W) {
            this.puck.initPuck(new VCTR((float) (wdth / PL_W), (float) (((double) height) * 0.6d)), new VCTR(0.0f, 0.0f));
        } else if (this.sc_curr == COMP_W) {
            this.puck.initPuck(new VCTR((float) (wdth / PL_W), (float) (((double) height) * 0.4d)), new VCTR(0.0f, 0.0f));
        } else {
            this.puck.initPuck(new VCTR((float) (wdth / PL_W), -1.0f + ((float) (((double) height) * 0.5d))), new VCTR(0.0f, 0.0f));
            myvalue = 1;
        }
        this.rival.initPuck(new VCTR((float) (wdth / PL_W), (float) (((double) height) * 0.2d)), new VCTR(0.0f, 0.0f));
        this.plyr.initPuck(new VCTR((float) (wdth / PL_W), (float) (((double) height) * 0.8d)), new VCTR(0.0f, 0.0f));
        this.surfWin.floatX1 = this.rival.vectorPosition.floatX;
        this.surfWin.floatY1 = this.rival.vectorPosition.floatY;
        this.surfWin.floatX2 = this.plyr.vectorPosition.floatX;
        this.surfWin.floatY2 = this.plyr.vectorPosition.floatY;
        this.tm_goal = 0;
        myvalue = 9;
    }

    private int resume() {
        Log.d("Resume: ", "resumed");
        this.surfWin.floatX1 = this.rival.vectorPosition.floatX;
        this.surfWin.floatY1 = this.rival.vectorPosition.floatY;
        this.surfWin.floatX2 = this.plyr.vectorPosition.floatX;
        this.surfWin.floatY2 = this.plyr.vectorPosition.floatY;
        myvalue = 0;
        start = System.currentTimeMillis();
        while (!SurfV.pauseThr) {
            try {
                if (MainActivity.bckPress) {
                    MainActivity.bckPress = false;
                    return N_W;
                }
                if (protivRival) {
                    rivalBrain();
                } else {
                    this.mPlr1.set(this.surfWin.floatX1, this.surfWin.floatY1);
                    this.rival.plrStVel();
                }
                this.mPlr2.set(this.surfWin.floatX2, this.surfWin.floatY2);
                this.plyr.plrStVel();
                this.puck.updPos();
                this.plyr.updPos();
                this.rival.updPos();
                if (!protivRival) {
                    this.rival.checkBorder();
                }
                this.plyr.checkBorder();
                this.plyr.chekcPuckCollision(this.puck);
                this.rival.chekcPuckCollision(this.puck);
                if (this.tm_goal == 1) {
                    restartPuck();
                }
                if (this.tm_goal == 0) {
                    this.sc_curr = this.puck.wallCheckCol();
                    switch (this.sc_curr) {
                        case 1 :
                            this.tm_goal = 180;
                            this.plr_score++;

                            break;
                        case 2:
                            this.tm_goal = 180;
                            this.rival_score++;

                            break;
                    }
                }
                if (this.tm_goal > 0) {
                    this.tm_goal--;
                }
                if (this.rival_score == this.peremoga && this.tm_goal < skorostKadra) {
                    return 1;
                }
                if (this.plr_score == this.peremoga && this.tm_goal < skorostKadra) {
                    return 2;
                }
                Canvas canvas = this.surfHolder.lockCanvas();
                gmDrawCust(canvas);

                this.surfHolder.unlockCanvasAndPost(canvas);
                wtNxtTmSlce();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return N_W;
    }

    private void gmDrawCust(Canvas canvas) throws Exception {
        canvas.drawBitmap(this.background, 0.0f, 0.0f, null);
        canvas.drawLine((float) Puck.minGate, 0.0f, (float) Puck.maxGate, 0.0f, paintGoal1);
        canvas.drawLine((float) Puck.minGate, (float) height, (float) Puck.maxGate, (float) height, paintGoal2);
        canvas.drawText(String.valueOf(this.rival_score), (float) wdth, (float) (height / PL_W), pntTxt1);
        canvas.drawText(String.valueOf(this.plr_score), 0.0f, (float) (height / PL_W), pntTxt2);
        this.rival.draw(canvas);
        this.puck.draw(canvas);
        this.plyr.draw(canvas);
    }

    private void mPause() {
        boolean clicked = false;
        start = System.currentTimeMillis();
        this.surfWin.top = false;

        Log.d("Pause: ", this.decision + " pause");
        while (!SurfV.pauseThr && !clicked) {
            try {
                boolean backPressed = MainActivity.bckPress;
                MainActivity.bckPress = false;
                if (!backPressed) {
                    if (this.decision == N_W && btnMy4.clickTo(this.surfWin.floatX2, this.surfWin.floatY2, this.surfWin.floor, this.surfWin.top)) {
                        STATE = N_W;
                        clicked = true;
                    } else if (btnMy5.clickTo(this.surfWin.floatX2, this.surfWin.floatY2, this.surfWin.floor, this.surfWin.top)) {
                        STATE = COMP_W;
                        rstIgra();
                        clicked = true;
                    }
                    this.surfWin.top = false;
                    Canvas c = this.surfHolder.lockCanvas();
                    gmDrawCust(c);
                    c.drawPaint(this.blckPnt);
                    if (this.decision == N_W) {
                        btnMy4.draw(c);
                    }
                    btnMy5.draw(c);
                    if (c != null) {
                        this.surfHolder.unlockCanvasAndPost(c);
                    }
                    wtNxtTmSlce();
                } else if (this.decision == N_W) {
                    STATE = N_W;
                    return;
                } else {
                    STATE = COMP_W;
                    myvalue = STATE;
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fadeOutPause(int time) {
        time *= skorostKadra;
        float t = 255.0f;
        float m = 255.0f / ((float) time);
        start = System.currentTimeMillis();
        Log.d("Fade: ", this.decision + " fade");
        while (!SurfV.pauseThr && time > 0) {
            t -= m;
            time--;
            if (t < 0.0f) {
                t = 0.0f;
            }
            try {
                Canvas canvas = this.surfHolder.lockCanvas();
                gmDrawCust(canvas);
                canvas.drawPaint(this.blckPnt);
                if (this.decision == N_W) {
                    btnMy4.draw(canvas);
                }
                btnMy5.draw(canvas);
                canvas.drawColor(Color.argb((int) (255.0f - t), 0, 0, 0));
                this.surfHolder.unlockCanvasAndPost(canvas);
                sleep = 16 - (System.currentTimeMillis() - start);
                if (sleep > 0) {
                    Thread.sleep(sleep);
                }
                start = System.currentTimeMillis();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void mMenu() {
        boolean clicked = false;
        start = System.currentTimeMillis();
        this.surfWin.top = false;
        MainActivity.bckPress = false;

        while (!SurfV.pauseThr && !clicked) {
            try {
                if (btnMy1.clickTo(this.surfWin.floatX2, this.surfWin.floatY2, this.surfWin.floor, this.surfWin.top)) {
                    STATE = N_W;
                    protivRival = true;
                    clicked = true;
                } else if (btnMy2.clickTo(this.surfWin.floatX2, this.surfWin.floatY2, this.surfWin.floor, this.surfWin.top)) {
                    STATE = N_W;
                    protivRival = false;
                    clicked = true;
                } else if (MainActivity.bckPress || btnMy3.clickTo(this.surfWin.floatX2, this.surfWin.floatY2, this.surfWin.floor, this.surfWin.top)) {
                    STATE = STATE_ESC;
                    clicked = true;
                }
                this.surfWin.top = false;
                Canvas c = this.surfHolder.lockCanvas();
                c.drawBitmap(this.imgMenu, 0.0f, 0.0f, null);
                btnMy1.draw(c);
                btnMy2.draw(c);
                btnMy3.draw(c);
                if (c != null) {
                    this.surfHolder.unlockCanvasAndPost(c);
                }
                wtNxtTmSlce();
            } catch (Exception e) {
            }
        }
    }
    private static void wtNxtTmSlce() throws InterruptedException {
        sleep = 16 - (System.currentTimeMillis() - start);
        if (sleep > 0) {
            Thread.sleep(sleep);
        }
        start = System.currentTimeMillis();
    }
    private void dissolve(int time) {
        time *= skorostKadra;
        float t = 255.0f;
        float millis = 255.0f / ((float) time);
        start = System.currentTimeMillis();
        while (!SurfV.pauseThr && time > 0) {
            t -= millis;
            time--;
            if (t < 0.0f) {
                t = 0.0f;
            }
            try {
                Canvas c = this.surfHolder.lockCanvas();
                c.drawBitmap(this.imgMenu, 0.0f, 0.0f, null);
                btnMy1.draw(c);
                btnMy2.draw(c);
                btnMy3.draw(c);
                c.drawColor(Color.argb((int) (255.0f - t), 0, 0, 0));
                this.surfHolder.unlockCanvasAndPost(c);
                sleep = 16 - (System.currentTimeMillis() - start);
                if (sleep > 0) {
                    Thread.sleep(sleep);
                }
                start = System.currentTimeMillis();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void rivalBrain() {
        int ballRad = this.rival.intRadi;
        int diskRad = this.puck.intRadi;

        if (this.puck.vectorPosition.floatY > ((float) (diskRad * PL_W))) {
            if (this.puck.vectorPosition.floatY < ((float) ((height / PL_W) + ballRad))) {
                this.rival.goTo((int) this.puck.vectorPosition.floatX, (int) this.puck.vectorPosition.floatY, lvl);
            } else {
                this.rival.goTo((int) this.puck.vectorPosition.floatX, ballRad * PL_W, lvl);
            }
        }
        if (this.rival.vectorPosition.floatX < ((float) diskRad) || this.rival.vectorPosition.floatX > ((float) (wdth - (diskRad - ballRad))) || this.rival.vectorPosition.floatY < ((float) diskRad) || this.rival.vectorPosition.floatY > ((float) ((height / PL_W) - (diskRad - ballRad)))) {
            this.rival.goTo(wdth / PL_W, height / 35, lvl);
        }
        if (this.puck.vectorPosition.floatY < 0.0f || this.puck.vectorPosition.floatY > ((float) height)) {
            this.rival.goTo(wdth / PL_W, height / 35, lvl);
        }
    }
    private void msgShow(String msg, int time, int size) {
        time *= 2000;
        try {

            pntTxt.setTextSize((float) size);
            pntTxt.setColor(-16711681);
            Canvas c = this.surfHolder.lockCanvas(null);
            gmDrawCust(c);
            c.drawText(msg, (float) (wdth / PL_W), (float) ((height + size) / PL_W), pntTxt);

            this.surfHolder.unlockCanvasAndPost(c);

            Thread.sleep( time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void dissolveString(String msg, int time, int size1, int size2) {
        time *= skorostKadra;
        float size = (float) size1;
        float fs = (2.0f * ((float) (size2 - size1))) / ((float) (time * time));
        float ft = 510.0f / ((float) (time * time));
        int t = 0;

        start = System.currentTimeMillis();
        while (!SurfV.pauseThr && time > 0) {
            int td = t * t;
            try {
                pntTxt.setTextSize(size);
                pntTxt.setARGB(255 - ((int) ((((float) td) * ft) / 2.0f)), 20, 220, MotionEventCompat.ACTION_MASK);
                time--;
                t += COMP_W;
                size = ((float) size1) + ((((float) td) * fs) / 2.0f);
                Canvas c = this.surfHolder.lockCanvas();
                gmDrawCust(c);
                c.drawText(msg, (float) (wdth / PL_W), (((float) height) + size) / 2.0f, pntTxt);

                    this.surfHolder.unlockCanvasAndPost(c);

                sleep = 16 - (System.currentTimeMillis() - start);
                if (sleep > 0) {
                    Thread.sleep(sleep);
                }
                start = System.currentTimeMillis();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

