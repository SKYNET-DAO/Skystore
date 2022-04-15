

package bitchat.android.com.bitstore.wallet.data;

import android.app.Application;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import bitchat.android.com.bitstore.wallet.manager.ListenerManager;
import bitchat.android.com.bitstore.wallet.services.WalletService;
import com.orhanobut.logger.Logger;
import org.bitcoinj.wallet.Wallet;



public abstract class AbstractWalletLiveData<T> extends ThrottelingLiveData<T> implements WalletService.WalletListener {
    private final Application application;
    private final LocalBroadcastManager broadcastManager;


    public AbstractWalletLiveData(final Application application) {
        super();
        this.application = application;
        this.broadcastManager = LocalBroadcastManager.getInstance(application);
    }

    public AbstractWalletLiveData(final Application application, final long throttleMs) {
        super(throttleMs);
        this.application = application;
        this.broadcastManager = LocalBroadcastManager.getInstance(application);
    }

    @Override
    protected final void onActive() {
//        broadcastManager.registerReceiver(walletReferenceChangeReceiver,
//                new IntentFilter("ACTION_WALLET_CHANGE"));
        ListenerManager.getInstance().registerWalletListner(this);
        Logger.e("-----abs-onActive()---->");
    }

    @Override
    protected final void onInactive() {
        // TODO cancel async loading
//        if (wallet != null) onWalletInactive(wallet);
//        broadcastManager.unregisterReceiver(walletReferenceChangeReceiver);
        ListenerManager.getInstance().unregisterWalletLister(this);


    }


    public void loadWallet() {
        WalletService.startSelf(application);

    }





    protected  void onWalletActive(Wallet wallet){

    }

    protected  void onWalletAsynProcess(int process){}

    protected void onWalletRestoreResult(Wallet wallet){}


    @Override
    public void onWalletActiveCompelet(Wallet wallet) {
        onWalletActive(wallet);
    }

    @Override
    public void onWalletAsynProcessStart(int present) {
        onWalletAsynProcess(present);
    }


    @Override
    public void onWalletRestore(Wallet wallet) {
        onWalletRestoreResult(wallet);
    }

    protected void onWalletInactive(final Wallet wallet) {
        // do nothing by default
    }

    public static interface OnWalletLoadedListener {
        void onWalletLoaded(Wallet wallet);
    }



    public interface DownloadAsynLister{

        void onPrecess(int present);
    }


}
