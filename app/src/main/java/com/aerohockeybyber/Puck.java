package com.aerohockeybyber;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.util.Log;

final class Puck {
    private static final int RIVAL_SCORE = 2;
    private static final int NO_SCORE = 0;
    private static final int PLAYER_SCORE = 1;
    static int maxGate;
    static int minGate;
    private Bitmap imgBitmap;
    private int height;
    private int heightlb;
    private int widthlb;
    private float gap;
    int perem = 2;
    int intRadi;
    private boolean clicked;
    private VCTR vectorVelocity;
    private int width;
    private VCTR cursorVector;
    VCTR vectorPosition;


    static void setGate(int width, int gate_si) {
        minGate = (width / RIVAL_SCORE)  - (gate_si / RIVAL_SCORE);
        maxGate = (width / RIVAL_SCORE) + (gate_si / RIVAL_SCORE);


    }

    Puck(int r, VCTR plrM) {
        this.vectorPosition = new VCTR(0.0f, 0.0f);
        this.intRadi = NO_SCORE;
        this.vectorVelocity = new VCTR(0.0f, 0.0f);
        this.gap = 0.1f;
        this.clicked = false;
        this.intRadi = r;
        this.cursorVector = plrM;
    }
    void initPuck(float positionX, float positionY, float velocityX, float velocityY) {
        this.vectorVelocity.floatX = velocityX;
        this.vectorVelocity.floatY = velocityY;
        this.vectorPosition.floatX = positionX;
        this.vectorPosition.floatY = positionY;

    }
    void initPuck(VCTR position, VCTR velocity) {
        this.vectorPosition.floatX = position.floatX;
        this.vectorPosition.floatY = position.floatY;
        this.vectorVelocity.floatX = velocity.floatX;
        this.vectorVelocity.floatY = velocity.floatY;
    }
    void setBorder(int width, int height, int width1, int height1) {
        this.widthlb = width;
        this.heightlb = height;
        this.width = width1;
        this.height = height1;
    }

    void updPos() {
        this.vectorPosition.add(this.vectorVelocity);
    }

    void plrStVel() {
        //velocity
        if (!this.clicked) {
            this.vectorVelocity = this.vectorPosition.getSub(this.cursorVector);
            this.vectorVelocity.multiplie(-0.5f);
        }
    }

    void checkBorder() {
        VCTR vctr;
        if (this.vectorPosition.floatY < ((float) (this.heightlb + this.intRadi))) {
            this.vectorPosition.floatY = (float) (this.heightlb + this.intRadi);
            this.cursorVector.floatY = this.vectorPosition.floatY;
            vctr = this.vectorVelocity;
            this.vectorVelocity.floatY = 0.0f;
            vctr.floatX = 0.0f;
        } else if (this.vectorPosition.floatY >= ((float) ((this.heightlb + this.height) - this.intRadi))) {
            this.vectorPosition.floatY = (float) (((this.heightlb + this.height) - this.intRadi) - 1);
            this.cursorVector.floatY = this.vectorPosition.floatY;
            vctr = this.vectorVelocity;
            this.vectorVelocity.floatY = 0.0f;
            vctr.floatX = 0.0f;
        }
    }

    void goTo(int x, int y, int difficulty) {
        this.cursorVector.floatX = (float) x;
        this.cursorVector.floatY = (float) y;
        if (this.cursorVector.floatX < ((float) (this.widthlb + this.intRadi))) {
            this.cursorVector.floatX = (float) (this.widthlb + this.intRadi);
        } else if (this.cursorVector.floatX >= ((float) ((this.widthlb + this.width) - this.intRadi))) {
            this.cursorVector.floatX = (float) (((this.widthlb + this.width) - this.intRadi) - 1);
        }
        if (this.cursorVector.floatY < ((float) (this.heightlb + this.intRadi))) {
            this.cursorVector.floatY = (float) (this.heightlb + this.intRadi);
        } else if (this.cursorVector.floatY >= ((float) ((this.heightlb + this.height) - this.intRadi))) {
            this.cursorVector.floatY = (float) (((this.heightlb + this.height) - this.intRadi) - 1);
        }
        if (!this.clicked) {
            this.vectorVelocity = this.vectorPosition.getSub(this.cursorVector);
            this.vectorVelocity.multiplie(-0.025f * ((float) Math.pow(difficulty, 0.8d)));
        }
    }
    void chekcPuckCollision(Puck puck) {
        VCTR dspl_vec = new VCTR(puck.vectorPosition.getSub(this.vectorPosition));
        float dis = dspl_vec.value() - ((float) (puck.intRadi + this.intRadi));
        if (dis > 0.0f) {
            this.clicked = false;
        } else if (this.clicked) {
            this.vectorVelocity = dspl_vec.getElement().getMultiplie(dis);
            updPos();
            puck.vectorVelocity = dspl_vec.getElement().getMultiplie((-dis) * 0.5f).getAdd(puck.vectorVelocity);
        } else {
            this.clicked = true;

            VCTR vn1 = new VCTR(dspl_vec);
            vn1.setElement();
            VCTR vt1 = vn1.getStandart();
            float v1n = this.vectorVelocity.doProd(vn1);
            float v1t = this.vectorVelocity.doProd(vn1);
            float v2n = puck.vectorVelocity.doProd(vn1);
            float v2t = puck.vectorVelocity.doProd(vt1);
            float v1n_a = ((((float) (this.intRadi - puck.intRadi)) * v1n) + (((float) (puck.intRadi * RIVAL_SCORE)) * v2n)) / ((float) (this.intRadi + puck.intRadi));
            VCTR vn2 = vn1.getMultiplie(((((float) (puck.intRadi - this.intRadi)) * v2n) + (((float) (this.intRadi * RIVAL_SCORE)) * v1n)) / ((float) (this.intRadi + puck.intRadi)));
            VCTR vt2 = vt1.getMultiplie(v2t);
            vn1.multiplie(v1n_a);
            vt1.multiplie(v1t);
            puck.vectorVelocity = vn2.getAdd(vt2);
            puck.vectorVelocity.multiplie(1.2f);
            puck.superSPEED(20);
            puck.updPos();
        }
    }
    int wallCheckCol() {
        VCTR vector2D;
        if (this.vectorPosition.floatX - ((float) this.intRadi) < ((float) this.widthlb)) {
            vector2D = this.vectorVelocity;
            vector2D.floatX *= -(1.0f - this.gap);
            this.vectorPosition.floatX = (float) (this.widthlb + this.intRadi);

        } else if (this.vectorPosition.floatX + ((float) this.intRadi) > ((float) (this.widthlb + this.width))) {
            vector2D = this.vectorVelocity;
            vector2D.floatX *= -(1.0f - this.gap);
            this.vectorPosition.floatX = (float) ((this.widthlb + this.width) - this.intRadi);

        }
        if (this.vectorPosition.floatY - ((float) this.intRadi) < ((float) this.heightlb)) {
            if (this.vectorPosition.floatX < ((float) minGate) || this.vectorPosition.floatX >= ((float) maxGate)) {
                vector2D = this.vectorVelocity;
                vector2D.floatY *= -(1.0f - this.gap);
                this.vectorPosition.floatY = (float) (this.heightlb + this.intRadi);

                return NO_SCORE;
            } else if (this.vectorPosition.floatY <= ((float) (this.heightlb - (this.intRadi / RIVAL_SCORE)))) {
                return PLAYER_SCORE;
            } else {
                return NO_SCORE;
            }
        } else if (this.vectorPosition.floatY + ((float) this.intRadi) <= ((float) (this.heightlb + this.height))) {
            return NO_SCORE;
        } else {
            if (this.vectorPosition.floatX < ((float) minGate) || this.vectorPosition.floatX >= ((float) maxGate)) {
                vector2D = this.vectorVelocity;
                vector2D.floatY *= -(1.0f - this.gap);
                this.vectorPosition.floatY = (float) ((this.heightlb + this.height) - this.intRadi);

                return NO_SCORE;
            } else if (this.vectorPosition.floatY >= ((float) ((this.heightlb + this.height) + (this.intRadi / RIVAL_SCORE)))) {
                return RIVAL_SCORE;
            } else {
                return NO_SCORE;
            }
        }
    }



    void superSPEED(int velocity) {
        if (this.vectorVelocity.value() > velocity) {
            this.vectorVelocity.setElement();
            this.vectorVelocity.multiplie(velocity);
        }
    }

    public void setImgBitmap(Bitmap image) {
        int imSize = this.intRadi * RIVAL_SCORE;
        Bitmap scaledSrc = Bitmap.createScaledBitmap(image, imSize, imSize, false);
        this.imgBitmap = Bitmap.createBitmap(imSize, imSize, Config.ARGB_8888);
        Paint paint = new Paint(PLAYER_SCORE);
        paint.setShader(new BitmapShader(scaledSrc, TileMode.CLAMP, TileMode.CLAMP));
        Canvas canvas = new Canvas(this.imgBitmap);

        imSize /= RIVAL_SCORE;
        canvas.drawColor(NO_SCORE);
        canvas.drawCircle((float) imSize, (float) imSize, (float) imSize, paint);
        int k = 0;
        Log.d("This is log of K", String.valueOf(k));
    }

    void draw(Canvas c) {
        c.drawBitmap(this.imgBitmap, this.vectorPosition.floatX - ((float) this.intRadi), this.vectorPosition.floatY - ((float) this.intRadi), null);
    }

}
