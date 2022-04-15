package bitchat.android.com.bitstore.wallet.subscribe;

import com.orhanobut.logger.Logger;
import io.reactivex.subscribers.ResourceSubscriber;




public abstract class ErrorSubscriber<T> extends ResourceSubscriber<T> {
    @Override
    public void onError(Throwable e) {
        Logger.e("error msgs:"+e.getMessage());
        if(e instanceof ResponseThrowable){
            onError((ResponseThrowable)e);
        }else{
            onError(new ResponseThrowable(e,"1000"));
        }
    }


    protected abstract void onError(ResponseThrowable ex);
}

