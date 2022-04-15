package bitchat.android.com.bitstore.activitys;

import android.annotation.SuppressLint;
import android.text.format.Formatter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.bean.HomeDataBean;
import bitchat.android.com.bitstore.fragments.CommentFragment;
import bitchat.android.com.bitstore.fragments.IntroducesFragment;
import bitchat.android.com.bitstore.fragments.RecommendFrament;
import bitchat.android.com.bitstore.listener.ListDownloadListener;
import bitchat.android.com.bitstore.utils.AppUtils;
import bitchat.android.com.bitstore.utils.GlideUtil;
import bitchat.android.com.bitstore.utils.ShareUtil;
import bitchat.android.com.bitstore.widget.ProgressButton;
import com.android.base.activitys.WfcBaseActivity;
import com.android.base.net.AppConst;
import com.flyco.tablayout.SegmentTabLayout;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;
import com.orhanobut.logger.Logger;
import com.vondear.rxtool.RxAppTool;
import com.vondear.rxtool.RxFileTool;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SearchActivity1 extends WfcBaseActivity {



    private EditText search;


    @Override
    protected void afterViews() {
        search=findViewById(R.id.search);

    }

    @SuppressLint("CheckResult")
    private void initData(){

        RxTextView.textChanges(search)
                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())// 
                //
                .filter((Predicate<? super CharSequence>) charSequence -> charSequence.toString().trim().length() > 0)
                //.subscribeOn(Schedulers.io()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {

                    Logger.e("-------charSequence-->"+charSequence);
                });

    }



    @Override
    public boolean isAcceptAppManager() {
        return false;
    }

    @Override
    protected int contentLayout() {
        return R.layout.activity_search;
    }
}
