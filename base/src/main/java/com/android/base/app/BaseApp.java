package com.android.base.app;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDexApplication;
import com.android.base.BuildConfig;
import com.android.base.R;
import com.android.base.net.interceptors.TokenInterceptor;
//import com.android.base.widgets.GlideImageLoader;
//import com.didichuxing.doraemonkit.DoraemonKit;
//import com.lzy.imagepicker.ImagePicker;
//import com.lzy.imagepicker.view.CropImageView;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.vondear.rxtool.RxTool;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.OkHttpClient;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class BaseApp extends MultiDexApplication {

    public static boolean isBackground = false;
    private static Handler mHandler;
    private static long mMainThreadId;
    private static Context mContext;

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        mHandler = new Handler();
        mContext = getApplicationContext();
        mMainThreadId = android.os.Process.myTid();
        initPrettyFormatStrategy();
        initOkgo();
        RxTool.init(this);
//        DoraemonKit.install(this);
        initLifeCycle();
        initImagePicker();

    }


    private void initImagePicker(){


    }

    private void initLifeCycle(){

        ProcessLifecycleOwner.get().getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            public void onForeground() {
                isBackground = false;

            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            public void onBackground() {
                isBackground = true;
                // viewModelStore.clear();
            }
        });

    }




    private void initOkgo(){


        //----------------------------------------------------------------------------------------//

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Logger.e("-----network-error--->"+throwable.getMessage());
               // Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        
        loggingInterceptor.setColorLevel(Level.INFO);                               
        builder.addInterceptor(loggingInterceptor);
      
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   

        
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              
        
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        
        OkGo.getInstance().init(this)                           
                .setOkHttpClient(builder.build())               
                .setCacheMode(CacheMode.NO_CACHE)               
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   
                .setRetryCount(3);                           

    }



    private void initPrettyFormatStrategy() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("TW99")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return true;
            }
        });
    }
    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }


    public boolean getBackground(){
        return isBackground;
    }
}
