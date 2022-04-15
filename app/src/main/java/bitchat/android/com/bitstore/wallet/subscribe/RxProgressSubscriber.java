package bitchat.android.com.bitstore.wallet.subscribe;


import bitchat.android.com.bitstore.wallet.utils.NetworkUtil;
import com.android.base.app.BaseApp;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.net.ConnectException;



public abstract class RxProgressSubscriber<T> extends ErrorSubscriber<T> {

    private WeakReference<BaseProgressView> mWeakReferenceProgress;
    private boolean mIsShowLoading;
    private String msg;

    public RxProgressSubscriber(BaseProgressView progressView) {
        mWeakReferenceProgress = new WeakReference<>(progressView);
        mIsShowLoading = true;
    }

    public RxProgressSubscriber(BaseProgressView progressView, boolean isShowDialog) {
        mWeakReferenceProgress = new WeakReference<>(progressView);
        mIsShowLoading = isShowDialog;
    }



    public RxProgressSubscriber(BaseProgressView progressView,String msg ,boolean isShowDialog) {
        mWeakReferenceProgress = new WeakReference<>(progressView);
        this.msg=msg;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!NetworkUtil.isNetworkAvailable(BaseApp.getContext())) {
            onError(new ResponseThrowable(new ConnectException(), ExceptionHandle.ERROR.NETWORD_ERROR));
            //onComplete();
            Logger.e("--------------->", "no connection");
            return;
        }
        //LogUtil.e("mIsShowLoading   ---- > " + mIsShowLoading);
        if (mIsShowLoading) {
            if (mWeakReferenceProgress != null) {
                BaseProgressView progressView = mWeakReferenceProgress.get();
                if (null != progressView) {
                    progressView.showDialog(msg);
                }
            }
        }
    }

    private void dismissDialog() {
        if (mWeakReferenceProgress != null && mWeakReferenceProgress.get() != null) {
            mWeakReferenceProgress.get().dismissDialog();
            mWeakReferenceProgress.clear();
        }
    }

    @Override
    public void onNext(T t) {
        dismissDialog();
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        dismissDialog();
        super.onError(e);
    }

    @Override
    protected void onError(ResponseThrowable ex) {
        dismissDialog();
    }

    @Override
    public void onComplete() {
        dismissDialog();
    }

    public abstract void onSuccess(T data);
}
