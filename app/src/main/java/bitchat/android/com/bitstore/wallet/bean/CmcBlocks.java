package bitchat.android.com.bitstore.wallet.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CmcBlocks {




    private String account;
    private String address;
    private String category;
    private double amount;
    private int vout;
    private int confirmations;
    private boolean generated;
    private String blockhash;
    private int blockindex;
    private double blocktime;
    private String txid;
    private double time;
    private double timereceived;
    @SerializedName("bip125-replaceable")
    private String bip125replaceable;
    private List<?> walletconflicts;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getVout() {
        return vout;
    }

    public void setVout(int vout) {
        this.vout = vout;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public String getBlockhash() {
        return blockhash;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public int getBlockindex() {
        return blockindex;
    }

    public void setBlockindex(int blockindex) {
        this.blockindex = blockindex;
    }

    public double getBlocktime() {
        return blocktime;
    }

    public void setBlocktime(double blocktime) {
        this.blocktime = blocktime;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getTimereceived() {
        return timereceived;
    }

    public void setTimereceived(double timereceived) {
        this.timereceived = timereceived;
    }

    public String getBip125replaceable() {
        return bip125replaceable;
    }

    public void setBip125replaceable(String bip125replaceable) {
        this.bip125replaceable = bip125replaceable;
    }

    public List<?> getWalletconflicts() {
        return walletconflicts;
    }

    public void setWalletconflicts(List<?> walletconflicts) {
        this.walletconflicts = walletconflicts;
    }
}
