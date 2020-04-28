package com.aerohockeybyber;


public class VCTR {
    float floatX;
    float floatY;

    VCTR(float x, float y) {
        this.floatX = x;
        this.floatY = y;
    }

    VCTR(VCTR vector2D) {
        this.floatX = vector2D.floatX;
        this.floatY = vector2D.floatY;
    }

    void set(float x, float y) {
        this.floatX = x;
        this.floatY = y;
    }

    float value() {
        return (float) Math.sqrt(((this.floatX * this.floatX) + (this.floatY * this.floatY)));
    }

    void add(VCTR vctr) {
        this.floatX += vctr.floatX;
        this.floatY += vctr.floatY;
    }

    VCTR getAdd(VCTR vctr) {
        return new VCTR(this.floatX + vctr.floatX, this.floatY + vctr.floatY);
    }



    VCTR getSub(VCTR vctr) {
        return new VCTR(this.floatX - vctr.floatX, this.floatY - vctr.floatY);
    }

    void multiplie(float multiplier) {
        this.floatX *= multiplier;
        this.floatY *= multiplier;
    }

    VCTR getMultiplie(float multiplier) {
        return new VCTR(this.floatX * multiplier, this.floatY * multiplier);
    }

    VCTR getElement() {
        float element = value();
        float X = this.floatX / element;
        float Y = this.floatY / element;
        return new VCTR(X, Y);
    }

    void setElement() {
        float element = value();
        this.floatX /= element;
        this.floatY /= element;
    }

    float doProd(VCTR v) {
        return (this.floatX * v.floatX) + (this.floatY * v.floatY);
    }

    VCTR getStandart() {
        return new VCTR(this.floatY, -this.floatX);
    }
}

