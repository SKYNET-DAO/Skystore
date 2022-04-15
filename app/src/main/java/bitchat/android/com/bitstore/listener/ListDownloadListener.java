package bitchat.android.com.bitstore.listener;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Toast;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.utils.AppUtils;
import bitchat.android.com.bitstore.utils.SnackbarUtil;
import bitchat.android.com.bitstore.widget.ProgressButton;
import com.google.android.material.snackbar.Snackbar;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadListener;
import com.orhanobut.logger.Logger;
import com.vondear.rxtool.RxAppTool;

import java.io.File;

public class ListDownloadListener  extends DownloadListener {


    private View holder;
    private static Context context;
    private boolean autoInstall;

    public ListDownloadListener(Object tag, View holder) {
        super(tag);
        this.holder = holder;
        context=holder.getContext();


    }


    public ListDownloadListener(Object tag, View holder,boolean autoInstall) {
        super(tag);
        this.holder = holder;
        context=holder.getContext();
        this.autoInstall=autoInstall;

    }

    @Override
    public void onStart(Progress progress) {
    }

    @Override
    public void onProgress(Progress progress) {
        Logger.e("-----onProgress-->"+tag.toString()+" "+holder.getTag()+"  " +progress.filePath);
        if (tag == holder.getTag()) {
            refresh(holder,progress);
        }
    }

    @Override
    public void onError(Progress progress) {
        Throwable throwable = progress.exception;
        if (throwable != null) throwable.printStackTrace();
    }

    @Override
    public void onFinish(File file, Progress progress) {
        Toast.makeText(context, "Downloaded" + progress.filePath, Toast.LENGTH_SHORT).show();
//            updateData(type);
        if(autoInstall){
            AppUtils.installApk(context,progress.filePath);
        }

    }

    @Override
    public void onRemove(Progress progress) {
    }

    public static void refresh(View holder,Progress progress) {
        String currentSize = Formatter.formatFileSize(context, progress.currentSize);
        String totalSize = Formatter.formatFileSize(context, progress.totalSize);
        ProgressButton download;
        if(holder instanceof ProgressButton){
            download=(ProgressButton) holder;
        }else{
            download =holder.findViewById(R.id.download);
        }

        switch (progress.status) {
            case Progress.NONE:
                download.setText("Download");
                download.setProgress((int) (progress.fraction * 100));
                Logger.e("----Download---->"+(int) (progress.fraction * 100));
                break;
            case Progress.PAUSE:

                download.setText("Continue");
                download.setProgress((int) (progress.fraction * 100));
                Logger.e("----Continue---->"+(int) (progress.fraction * 100));
                break;
            case Progress.ERROR:

                download.setText("Error");
                Logger.e("----Error---->");
                break;
            case Progress.WAITING:
                download.setText("Wait");
                download.setProgress((int) (progress.fraction * 100));
                Logger.e("----Wait---->"+(int) (progress.fraction * 100));

                break;
            case Progress.FINISH:
                download.setText("Finished");
                Logger.e("----Finished---->");

                break;
            case Progress.LOADING:
                download.setText("Pause");
                download.setProgress((int) (progress.fraction * 100));
                Logger.e("----Pause---->"+(int) (progress.fraction * 100));


                break;
        }

    }

}
