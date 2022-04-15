package com.android.base.net;


import com.android.base.BuildConfig;
import com.android.base.app.BaseApp;

public class AppConst {

    public static  String BASE_URL ="http://boot.trustpro.io:8888";    //A default static bootstrap node when it fails to resolve the ipfs bootstrap.
    public static String TESTADDRESS="";
    public static String walletdir=BaseApp.getContext().getFilesDir().getPath()+"/"+"cmcwallet";

}
