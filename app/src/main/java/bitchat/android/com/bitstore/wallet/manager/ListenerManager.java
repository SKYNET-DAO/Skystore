package bitchat.android.com.bitstore.wallet.manager;


import bitchat.android.com.bitstore.wallet.services.WalletService;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {
    private static  ListenerManager instance;
    private List<WalletService.WalletListener> listeners;


    public ListenerManager(){

        listeners=new ArrayList<>();
    }



    public static ListenerManager getInstance(){

        synchronized (ListenerManager.class){
            if(instance==null){

                instance=new ListenerManager();
            }

        }
        return instance;
    }

    public void registerWalletListner(WalletService.WalletListener listener){
        if(listener==null)return;
        listeners.add(listener);

    }

    public void unregisterWalletLister(WalletService.WalletListener listener){
        if(listener==null)return;
        listeners.remove(listener);
    }



    public List<WalletService.WalletListener> getListeners(){

        return listeners;
    }


}
