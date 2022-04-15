package bitchat.android.com.bitstore.wallet;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import bitchat.android.com.bitstore.wallet.data.AbstractWalletLiveData;
import com.android.base.app.BaseApp;
import com.android.base.net.AppConst;
import com.android.wallet.constants.Constants;
import com.android.wallet.manager.IPManager;
import com.orhanobut.logger.Logger;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.PeerAddress;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Kit {


    
    public static WalletAppKit getWalletKit(String seedcode, AbstractWalletLiveData.DownloadAsynLister downloadAsynLister) {


        Logger.e("------path---->"+ AppConst.walletdir);
        WalletAppKit walletAppKit = new WalletAppKit(Constants.NETWORK_PARAMETERS, new File(AppConst.walletdir), "CMC_WALLET") {
            @SuppressLint("CheckResult")
            @Override
            protected void onSetupCompleted() {

                Logger.e("-----wallet().getImportedKeys()---->"+wallet().getImportedKeys().size());
                if (wallet().getImportedKeys().size() < 1) {
                    wallet().importKey(new ECKey());
                }
//                wallet().allowSpendingUnconfirmedTransactions();


                BitUtil.INSTANCE.setupWalletListeners(wallet());
                ECKey ecKey = wallet().getImportedKeys().get(0);

               String pubkey= BitUtil.INSTANCE.getPubKeyFrom(ecKey);
                Logger.e("------ecKey--->"+pubkey);


                List<String> seedWordsFromWallet = BitUtil.INSTANCE.getSeedWordsFromWallet(wallet());
                Logger.e("-------seedWordsFromWallet---->" + JsonUtil.toJson(seedWordsFromWallet));

            }
        };
        walletAppKit.setAutoSave(true);
        walletAppKit.setBlockingStartup(false);

//        try {
//            if(Constants.NETWORK_PARAMETERS instanceof TestNet3Params){
//
//                walletAppKit.setCheckpoints(BaseApp.getContext().getAssets().open("checkpoints-testnet.txt"));
//
//            }else{
//                walletAppKit.setCheckpoints(BaseApp.getContext().getAssets().open("checkpoints.txt"));
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }




        if (downloadAsynLister != null)
            BitUtil.INSTANCE.setDownListener(walletAppKit, downloadAsynLister);

        if (!TextUtils.isEmpty(seedcode)) {
            try {
                Logger.e("-------seedcode111--->" + seedcode);


                DeterministicSeed seed = new DeterministicSeed(seedcode, null, "",1559320399);
                Logger.e("-------seed--->" + seed.toString());
                walletAppKit.restoreWalletFromSeed(seed);
            } catch (UnreadableWalletException e) {
                e.printStackTrace();
            }
        }

        List<PeerAddress>  peerAddressList=IPManager.initIp(BaseApp.getContext());
        walletAppKit.setPeerNodes(peerAddressList.toArray(new PeerAddress[peerAddressList.size()]));

        walletAppKit.startAsync().awaitRunning();
        Logger.e("-------walletAppKit.state()--->" + walletAppKit.state());

        return walletAppKit;
    }


    public static int maxConnectedPeers() {
        ActivityManager activityManager = (ActivityManager) BaseApp.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getMemoryClass() <= 128 ? 4 : 6;
    }

    private static void initPeerGroup(WalletAppKit walletAppKit) {


        walletAppKit.setPeerNodes(new PeerAddress(Constants.NETWORK_PARAMETERS, Constants.NETWORK_PARAMETERS.getId(), Constants.NETWORK_PARAMETERS.getPort()));


    }
}
