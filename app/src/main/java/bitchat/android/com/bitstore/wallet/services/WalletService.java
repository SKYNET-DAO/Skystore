package bitchat.android.com.bitstore.wallet.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;

import bitchat.android.com.bitstore.wallet.Kit;
import bitchat.android.com.bitstore.wallet.manager.ListenerManager;
import bitchat.android.com.bitstore.wallet.utils.WalletUtil;
import com.orhanobut.logger.Logger;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import org.bitcoinj.core.PeerAddress;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.wallet.Wallet;

import java.io.Serializable;
import java.util.List;


public class WalletService extends LifecycleService {

    private Wallet wallet;
    private MutableLiveData<Integer> walletPrograss;
    private WalletAppKit walletAppKit;
    private static WalletKitListener kitListener;
    private static final String WALLETCHANNEL="walletchannel";

    private static final String ACTION_BROADCAST_TRANSACTION="action_broadcast_transaction";
    private static final String ACTION_BROADCAST_TRANSACTION_HASH="action_broadcast_transaction_hash";
    private static final String ACTION_STOP_SELF="action_stop_self";
    private static final String ACTION_START_SELF="action_start_self";
    private static final String ACTION_RESTORE_WALLET="action_restore_wallet";
    private static final String ACTION_GET_WALLETAPPKIT="action_get_walletappkit";
    private static PeerAddress[] peerAddresses;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            initChannel();

        }
        walletPrograss = new MutableLiveData<>();
        walletPrograss.observe(this, progress -> {
            Logger.e("-------walletPrograss-->"+progress);
            if(progress!=null) notifyAllAsynprocess(progress);

        });

        loadWallet();
    }

    private void initChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager mn= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mn.createNotificationChannel(new NotificationChannel(WALLETCHANNEL,"wallet_channel",NotificationManager.IMPORTANCE_LOW));

            NotificationCompat.Builder builder=new NotificationCompat.Builder(this,WALLETCHANNEL);
            
            startForeground(1, builder.build());

        }

    }

    @SuppressLint("CheckResult")
    public void loadWallet() {

        Flowable.just("")
                .observeOn(Schedulers.io())
                .map(s -> Kit.getWalletKit(s, present -> {
                    walletPrograss.postValue(present);
                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(walletAppKit -> {
                    this.walletAppKit=walletAppKit;
                    wallet=walletAppKit.wallet();
                    notifyAllWalletActive(wallet);
                });

    }

    @SuppressLint("CheckResult")
    public void restoreWallet(String seed,PeerAddress... peerAddress){
        //stop sync
        if(walletAppKit!=null){ walletAppKit.stopAsync().awaitTerminated();}


        Flowable.just(seed)
                .observeOn(Schedulers.io())
                .map(s -> WalletUtil.restoreWallet(seed,peerAddress))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wallet -> {
                    this.wallet=wallet;
                    notifyAllWalletRestore(wallet);
                });

    }

    @SuppressLint("CheckResult")
    public void restoreWallet1(String seed){
        //stop sync
        Flowable.just(seed)
                .observeOn(Schedulers.io())
                .map(s -> {
                    if(walletAppKit!=null){ walletAppKit.stopAsync().awaitTerminated();}
                    return Kit.getWalletKit(s,null);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(walletAppKit -> {
                    this.walletAppKit=walletAppKit;
                    this.wallet=walletAppKit.wallet();
                    notifyAllWalletRestore(wallet);
                });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            final String action = intent.getAction();
            if (WalletService.ACTION_BROADCAST_TRANSACTION.equals(action)) {
                final Sha256Hash hash = Sha256Hash
                        .wrap(intent.getByteArrayExtra(WalletService.ACTION_BROADCAST_TRANSACTION_HASH));
                final Transaction tx = wallet.getTransaction(hash);

                if (walletAppKit != null) {
                    Logger.i("broadcasting transaction {}", tx.getTxId());
                    walletAppKit.peerGroup().broadcastTransaction(tx);
                } else {
                    Logger.i("peergroup not available, not broadcasting transaction {}", tx.getTxId());
                }
            } else if (WalletService.ACTION_STOP_SELF.equals(action)) {
                stopSelf();
            } else if (WalletService.ACTION_RESTORE_WALLET.equals(action)) {
                String seed = intent.getExtras().getString(WalletService.ACTION_RESTORE_WALLET);
                if (!TextUtils.isEmpty(seed)) {

                    restoreWallet1(seed);


                }

            } else if (WalletService.ACTION_START_SELF.equals(action)) {//need to return action wallet
                if (wallet != null){
                    notifyAllWalletActive(wallet);
                    
                    walletPrograss.postValue(walletPrograss.getValue());

                }
            }else if(WalletService.ACTION_GET_WALLETAPPKIT.equals(action)){

                if(kitListener!=null){
                    kitListener.onWalletKit(this.walletAppKit);
                }

            }
        }

        return START_NOT_STICKY;
    }

    public interface  WalletListener  {
          void onWalletActiveCompelet(Wallet wallet);
          void onWalletAsynProcessStart(int present);
          void onWalletRestore(Wallet wallet);
    }

    public interface  WalletKitListener{
        void onWalletKit(WalletAppKit walletAppKit);
    }

    public static void broadcastTransaction(final Context context, final Transaction tx) {
        final Intent intent = new Intent(WalletService.ACTION_BROADCAST_TRANSACTION, null, context,
                WalletService.class);
        intent.putExtra(WalletService.ACTION_BROADCAST_TRANSACTION_HASH, tx.getTxId().getBytes());
        context.startService(intent);
    }

    public static void broadcastGetWalletAppKit(final Context context,WalletKitListener walletKitListener) {
        final Intent intent = new Intent(WalletService.ACTION_GET_WALLETAPPKIT, null, context,
                WalletService.class);
        kitListener=walletKitListener;
        context.startService(intent);

    }

    public static void broadcastRestoreWallet(final Context context,String seed) {
        final Intent intent = new Intent(WalletService.ACTION_RESTORE_WALLET, null, context,
                WalletService.class);
        intent.putExtra(WalletService.ACTION_RESTORE_WALLET,seed);
        context.startService(intent);
    }

    public static void stopSelf(final Context context) {
        final Intent intent = new Intent(WalletService.ACTION_STOP_SELF, null, context,
                WalletService.class);
        context.stopService(intent);
    }

    public static void startSelf(final Context context) {
        final Intent intent = new Intent(WalletService.ACTION_START_SELF, null, context,
                WalletService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            context.startForegroundService(intent);

        } else {

            context.startService(intent);

        }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(walletAppKit!=null){
            walletAppKit.stopAsync().awaitTerminated();
        }
    }

    private List<WalletListener> getWalletListener(){

       return ListenerManager.getInstance().getListeners();

    }

    //notify all listener of wallet asyn process
    private void notifyAllAsynprocess(int present){
        Logger.e("------notifyAllAsynprocess--->"+getWalletListener().size());
        for(int i=0;i<getWalletListener().size();i++){
            WalletListener walletListener= getWalletListener().get(i);
            if(walletListener!=null)walletListener.onWalletAsynProcessStart(present);
        }
    }

    //notify all listener of wallet load complete
    private void notifyAllWalletActive(Wallet wallet){
        for(int i=0;i<getWalletListener().size();i++){
            WalletListener walletListener= getWalletListener().get(i);
            if(walletListener!=null)walletListener.onWalletActiveCompelet(wallet);
        }
    }

    private void notifyAllWalletRestore(Wallet wallet){
        for(int i=0;i<getWalletListener().size();i++){
            WalletListener walletListener= getWalletListener().get(i);
            if(walletListener!=null)walletListener.onWalletRestore(wallet);
        }
    }

}
