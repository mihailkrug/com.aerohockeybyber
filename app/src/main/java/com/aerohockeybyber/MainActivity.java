package com.aerohockeybyber;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import android.util.Log;
import android.view.Window;

import com.onesignal.OneSignal;


public final class MainActivity extends AppCompatActivity {

    static MainActivity cntxt;

    final String SAVED_TEXT1 = "saved_text";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cntxt = this;

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);

        openLink();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {

        finish();
    }





    void openLink(){


        String url = "trck.applsy.";
        url = url + "xyz/click?pid=59&offer_id=307";

        url = "http://" + url;


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = 24;
        options.outHeight = 24;
        options.inScaled = true; //already default, just for illustration - ie scale to screen density (dp)

        final Bitmap backButton = BitmapFactory.decodeResource(getResources(), R.drawable.round_done_black_24dp, options);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.enableUrlBarHiding();
        builder.setToolbarColor(Color.BLACK);
        builder.setShowTitle(false);
        builder.addDefaultShareMenuItem();
        builder.setCloseButtonIcon(backButton);
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }



        CustomTabsIntent customTabsIntent = builder.build();
        String packageName = "com.android.chrome";
        // if we cant find a package name, it means there's no browser that supports
        // Custom Tabs installed. So, we fallback to a view intent


        try {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(MainActivity.this, Uri.parse(url));
            finish();
            // Your startActivity code wich throws exception
        } catch (ActivityNotFoundException activityNotFound) {
            MainActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            // Now, You can catch the exception here and do what you want
            finish();
        }

    }


}
