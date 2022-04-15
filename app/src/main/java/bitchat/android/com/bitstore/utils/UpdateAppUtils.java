package bitchat.android.com.bitstore.utils;

import android.app.Activity;
import com.android.base.utils.ManifestUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;
import com.vondear.rxtool.RxAppTool;

import java.io.File;

public class UpdateAppUtils {

    public static void getAppVersionInfo(Activity activity, String url, OkGoCommonResult commonResult){
        HttpParams httpParams=new HttpParams();
        String signal ="A5:11:3A:83:0F:0E:07:02:92:4D:85:61:80:8C:1B:9D:F4:6E:F3:77";
        Logger.d("-------Requested signature:----->"+signal);
        httpParams.put("sign",signal);
        httpParams.put("version", ManifestUtil.getVersionName(activity));
        OkGo.<String>post(url)
                .tag(activity)
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {


                        if(commonResult!=null)commonResult.onSuccess(response);
                    }

                    @Override
                    public void onError(Response<String> response) {

                        if(commonResult!=null)commonResult.onError(response);
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {

                    }
                });


    }

    public interface OkGoCommonResult{

        void onSuccess(Response<String> response);
        void onError(Response<String> response);


    }






}
