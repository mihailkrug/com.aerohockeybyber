package com.aerohockeybyber;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import android.view.Window;

public final class MainActivity extends AppCompatActivity {

    static boolean bckPress;

    private boolean created;
    private SurfV surfaceView;
    static MainActivity cntxt;
    public double doble;

    public MainActivity() {
        this.created = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cntxt = this;


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        if (!this.created) {
            this.created = true;
            this.surfaceView = new SurfV(this);
        }
        if (this.surfaceView == null) {
            //surface gone
        }
        setContentView(this.surfaceView);
        bckPress = false;
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        getDoble();
        finish();
    }
    public double getDoble(){
        return doble++;
    }
}
