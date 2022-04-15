package bitchat.android.com.bitstore.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import bitchat.android.com.bitstore.bean.AppDetailBean;
import com.android.base.mvvm.viewmodel.BaseViewModel;
import com.android.base.net.helper.LzyResponse;
import com.android.mine_android.api.Api;
import com.orhanobut.logger.Logger;
import com.vondear.rxtool.RxAppTool;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AppinstallViewModel extends BaseViewModel {


    private MutableLiveData<Integer> appinstall;
    private MutableLiveData<String> apppkgname=new MutableLiveData<>();
    Application application;

    public AppinstallViewModel(@NotNull Application application) {
        super(application);
        this.application=application;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);

        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);

        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);

        intentFilter.addDataScheme("package");


        LocalBroadcastManager.getInstance(application).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent != null && TextUtils.equals(Intent.ACTION_PACKAGE_ADDED, intent.getAction())) {

                    if (intent.getData() != null) {

                        String packageName = intent.getData().getSchemeSpecificPart();

                        Logger.e("The name of installed app is-------->" + packageName);
                        onAppInstall().postValue(2);
                        apppkgname.postValue(packageName);
                    }

                }
            }
        }, intentFilter);
    }



    public MutableLiveData<Integer> onAppInstall() {  //VerifyCodeBean
        if (appinstall == null) {
            appinstall = new MutableLiveData<>();
        }
        return appinstall;
    }


    @SuppressLint("CheckResult")
    public void installApp(String filepath) {
        RxAppTool.installAppSilent(application,filepath);
        onAppInstall().postValue(1);//installing
    }


    private void onBannerSuccess(Integer status) {

    }

    private void onReqFail(Throwable e) {
        getProgress().postValue(false);
        onError(e);
    }
}
