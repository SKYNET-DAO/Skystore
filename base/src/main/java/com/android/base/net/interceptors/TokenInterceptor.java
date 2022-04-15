package com.android.base.net.interceptors;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.orhanobut.logger.Logger;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class TokenInterceptor implements Interceptor {

    private Application application;
    private SharedPreferences mSharedPreferences;
    private  LocalBroadcastManager broadcastManager;


    public TokenInterceptor(Application application) {
        this.application = application;
        if(mSharedPreferences==null){
            mSharedPreferences=application.getSharedPreferences("config", Context.MODE_PRIVATE);
            broadcastManager=LocalBroadcastManager.getInstance(application);
        }


    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        
        String token= mSharedPreferences.getString("token","");
        Logger.e("-------intercept-token----->"+token);
        Request mRequest;
        if(!TextUtils.isEmpty(token)){
             mRequest = chain.request().newBuilder()
                    .addHeader("Authorization","Bearer "+ token)
                    .build();
        }else{
            mRequest = chain.request().newBuilder()
                      .build();
        }

        try {
            JSONObject jsonObject= null;
            
            Response mResponse=chain.proceed(mRequest);
            MediaType mediaType = mResponse.body().contentType();
            String content= mResponse.body().string();
            jsonObject = new JSONObject(content);
            int code=jsonObject.getInt("code");
            if (code==20001){
                broadcastManager.sendBroadcast(new Intent("android.action.token.fail"));
                jsonObject.put("data",null);//{}->null else the ui crash
                content=jsonObject.toString();
            }else{
                if(jsonObject.getString("data").equals("{}")){
                    jsonObject.put("data",null);//{}->null else the ui crash
                }
                content=jsonObject.toString();
            }

            return mResponse.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
}
