package bitchat.android.com.bitstore.wallet.subscribe;


public  abstract class RxSubscriber<T> extends ErrorSubscriber<T> {


    @Override
    public void onComplete() {
        
    }

    @Override
    protected void onStart() {
        super.onStart();
        

    }


    @Override
    public void onNext(T t) {

          onSuccess(t);
    }
    public abstract  void onSuccess(T t);

}