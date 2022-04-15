package bitchat.android.com.bitstore.wallet.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import bitchat.android.com.bitstore.wallet.bean.TransactionItem;
import bitchat.android.com.bitstore.wallet.data.TransactionsLiveData;
import bitchat.android.com.bitstore.wallet.data.WalletBalanceLiveData;
import bitchat.android.com.bitstore.wallet.data.WalletLiveData;
import com.orhanobut.logger.Logger;
import org.bitcoinj.core.Address;

import java.util.List;

public class WalletViewModel extends AndroidViewModel implements WalletLiveData.DownloadProcessListener {
    private MutableLiveData<Integer> walletPrograss;
    private WalletLiveData walletLoadLiveData;
    private MutableLiveData<Address> currentReceiverAddress;
    private WalletBalanceLiveData balanceLiveData;
    private TransactionsLiveData liveData;
    private Application application;

    public final MediatorLiveData<List<TransactionItem>> list = new MediatorLiveData<>();


    public WalletViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
        this.walletLoadLiveData=new WalletLiveData(application,this);
        this.balanceLiveData = new WalletBalanceLiveData(application);
        this.liveData=new TransactionsLiveData(application);
        Logger.e("-----constructer-WalletViewModel------>");

    }

    public MutableLiveData<Integer> walletProgressLiveData() {
        if (walletPrograss == null) {
            walletPrograss = new MutableLiveData<>();
        }
        return walletPrograss;
    }


    public TransactionsLiveData transactionsLiveData() {
        return liveData;
    }

    public MutableLiveData<Address> currentReceiverAddress() {
        if (currentReceiverAddress == null) {
            currentReceiverAddress = new MutableLiveData<>();
        }
        return currentReceiverAddress;
    }

    public WalletLiveData getWalletLoadLiveData(){

        return walletLoadLiveData; }

    @Override
    public void onProcess(int present) {
        walletProgressLiveData().postValue(present);
    }


    
    public void getTransforms(){
        this.liveData.setWallet(walletLoadLiveData.getValue());
    }



    
    public void  getCurrentReceiverAddress(){
        Address address= walletLoadLiveData.getValue().currentReceiveAddress();
        currentReceiverAddress().postValue(address);
    }

    
    public WalletBalanceLiveData balanceLiveData() {
        return balanceLiveData;
    }


    public void getBalance() {
        if (balanceLiveData != null){
            balanceLiveData.setWallet(walletLoadLiveData.getValue());
        }
    }


}
