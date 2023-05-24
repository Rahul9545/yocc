package com.bigv.app.yocc.utils;

import android.app.Application;

import com.mithsoft.lib.utils.TypefaceUtil;

import quickutils.core.QuickUtils;

/**
 * Created by mithun on 12/8/17.
 */

public class MyApplicationConstants extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        init QuickUtils lib
        QuickUtils.init(getApplicationContext());

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "MYRIADPRO-REGULAR.OTF"); // font from assets: "assets/fonts/Roboto-Regular.ttf
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
}
