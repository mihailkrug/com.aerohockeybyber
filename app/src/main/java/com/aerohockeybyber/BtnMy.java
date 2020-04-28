package com.aerohockeybyber;
import android.graphics.Canvas;
import android.graphics.Bitmap;

final class BtnMy {
    private Bitmap btmpCur;
    private Bitmap standartIMG;
    private int rad;
    private int floatX;
    private int floatY;

    BtnMy(int coordinateX, int coordinateY, int rad) {
        this.rad = rad;
        this.floatX = coordinateX;
        this.floatY = coordinateY;
    }

    private Bitmap btmpSET(Bitmap img) {
        int diam = this.rad * 2;
        return Bitmap.createScaledBitmap(img, diam, diam, false);
    }
    boolean clickTo(float X, float Y, boolean floor, boolean top) {
        if (new VCTR(X - ((float) this.floatX), Y - ((float) this.floatY)).value() <= ((float) this.rad)) {
            if (floor) {
                return false;
            } else if (top) {
                return true;
            }
        }
        this.btmpCur = this.standartIMG;
        return false;
    }
    void btmpsSET(Bitmap normalImg) {
        this.standartIMG = btmpSET(normalImg);
       // this.hvrIMG = btmpSET(hoverImg);
        //this.clickImg = btmpSET(clickImg);
        this.btmpCur = this.standartIMG;
    }



    void draw(Canvas c) {
        c.drawBitmap(this.btmpCur, (float) (this.floatX - this.rad), (float) (this.floatY - this.rad), null);
    }
}

