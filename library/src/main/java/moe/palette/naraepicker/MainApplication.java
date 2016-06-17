package moe.palette.naraepicker;

import android.app.Application;

import com.adobe.creativesdk.aviary.IAviaryClientCredentials;
import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.mikepenz.iconics.Iconics;
import com.mikepenz.palette_dream_ui_typeface_library.PaletteDREAMUI;

public class MainApplication extends Application implements IAviaryClientCredentials {

    /* Be sure to fill in the two strings below. */
    private static final String CREATIVE_SDK_CLIENT_ID = "7d3bc188fb4e464f845bc9b14eb43e84";
    private static final String CREATIVE_SDK_CLIENT_SECRET = "3e4b5b4a-f72e-41a8-8b79-bf5a3722dfa6";

    @Override
    public void onCreate() {
        super.onCreate();
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
        Iconics.init(getApplicationContext());
        Iconics.registerFont(new PaletteDREAMUI());
    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

    /* 2) Add the getBillingKey() method */
    @Override
    public String getBillingKey() {
        return ""; // Leave this blank
    }
}