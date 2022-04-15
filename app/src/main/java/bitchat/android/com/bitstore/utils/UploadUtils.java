package bitchat.android.com.bitstore.utils;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import bitchat.android.com.bitstore.bean.UploadBean;
import com.android.base.net.helper.LzyResponse;
import com.android.base.utils.JsonUtil;
import com.android.mine_android.api.Api;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;

public class UploadUtils {

    public static void upLoadFiles(AppCompatActivity activity, String url, List<File> datas, OkGoCallBack callBack){

        Logger.e("----upLoadFiles----->"+AppLanguageUtils.getCurrentLang());
        HttpParams httpParams=new HttpParams();
        httpParams.put("lang", AppLanguageUtils.getCurrentLang());
        OkGo.<String>post(url)
                .tag(activity)
                .params(httpParams)
                .isMultipart(true)
                .addFileParams("file",datas)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<String> response) {

                        Logger.d("-----Uploaded---->"+response.body());
                        UploadBean bean= JsonUtil.toBean(response.body(),UploadBean.class);
                        if(callBack!=null)callBack.onSuccess(bean);
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                        Logger.d("-----Progress of upload---->"+progress.currentSize);
                        if(callBack!=null)callBack.onloadProgress(progress);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        //   activity.dismissDialog();
                        Logger.d("-----Failed to upload---->"+response.body());
                        if(callBack!=null)callBack.onError(response);
                    }
                });

    }



    public interface OkGoCallBack{

        void onSuccess(UploadBean response);
        void onError(Response<String> response);
        void onloadProgress(Progress progress);



    }

    public interface OkGoFileCallBack{

        void onSuccess(Response<File> response);
        void onError(Response<File> response);
        void onloadProgress(Progress progress);



    }


    public static void DownLoadFile(Activity activity, String url, OkGoFileCallBack callBack){
        OkGo.<File>get(url)
                .tag(activity)
                .execute(new FileCallback() {
                    @Override
                    public void onSuccess(Response<File> response) {

                        Logger.d("------File downloaded------>"+response.body());
                        callBack.onSuccess(response);

                    }

                    @Override
                    public void onError(Response<File> response) {

                        Logger.d("------Failed to download file------>"+response.body());

                        callBack.onError(response);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                        Logger.d("------downloading------>"+progress.currentSize+" "+ progress.totalSize*100);
                        callBack.onloadProgress(progress);

                    }

                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        Logger.d("------starting download------>");

                    }
                });

    }




}
