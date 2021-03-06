package com.android.wallet;

import com.android.base.BaseApp;
import com.android.wallet.constants.Constants;
import com.orhanobut.logger.Logger;

import org.bitcoinj.core.*;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;


public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }



    final private NetworkParameters netParams = TestNet3Params.get();


    private String privateKeyAsWiF = "cVYU7EDwyHdzhtz26v5REiLmq3zHpveb83LyWSpSYhf7BoaKY6mp";

    @Test
    public void genKey() throws Exception {
        ECKey ecKey = new ECKey();
        Address address = LegacyAddress.fromKey(netParams, ecKey);
        String privateKeyAsWiF = ecKey.getPrivateKeyAsWiF(netParams);
        System.out.println(privateKeyAsWiF);
    }

    @Test
    public void restoreKey() throws Exception {
        ECKey ecKey = DumpedPrivateKey.fromBase58(netParams, privateKeyAsWiF).getKey();
        Address address = LegacyAddress.fromKey(netParams, ecKey);
        System.out.println(address);
    }


    @Test
    public void SyncBasicWallet0151() throws Exception {
        //PrintStream fileOut = new PrintStream("./newWaller-log.txt");
        //System.setOut(fileOut);

//        Context.propagate(new Context(netParams));
//
//        File chainFileSeed = new File("seed.spvchain");
//        File chainFile = new File(System.currentTimeMillis() + ".spvchain");
//        byte[] allBytes = Files.readAllBytes(chainFileSeed.toPath());
//        Files.write(chainFile.toPath(), allBytes);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, -1);
//        Date time = calendar.getTime();
//
//        ECKey ecKey = DumpedPrivateKey.fromBase58(netParams, privateKeyAsWiF).getKey();
//        ecKey.setCreationTimeSeconds(time.getTime() / 1000);
//        Wallet wallet = new Wallet(netParams);
//        wallet.importKey(ecKey);
//
//        BlockStore blockStore = new SPVBlockStore(netParams, chainFile);
//        BlockChain chain = new BlockChain(netParams, blockStore);
//        final PeerGroup peerGroup = new PeerGroup(netParams, chain);
//
//        //peerGroup.setBloomFilteringEnabled(false);
//
//        peerGroup.addPeerDiscovery(new DnsDiscovery(netParams));
//        peerGroup.setFastCatchupTimeSecs(time.getTime());
//
//        peerGroup.addWallet(wallet);
//        chain.addWallet(wallet);
//
//        peerGroup.start();
//        peerGroup.downloadBlockChain();
//        peerGroup.stop();
//
//        chainFile.delete();
//
//        System.out.println(wallet);
//        wallet.cleanup();
    }


    @Test
    public void restoreWallet(){

        NetworkParameters params =Constants.NETWORK_PARAMETERS;
        // Bitcoinj supports hierarchical deterministic wallets (or "HD Wallets"): https://github.com/bitcoin/bips/blob/master/bip-0032.mediawiki
        // HD wallets allow you to restore your wallet simply from a root seed. This seed can be represented using a short mnemonic sentence as described in BIP 39: https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki

        // Here we restore our wallet from a seed with no passphrase. Also have a look at the BackupToMnemonicSeed.java example that shows how to backup a wallet by creating a mnemonic sentence.
//        String seedCode = "yard impulse luxury drive today throw farm pepper survey wreck glass federal";
//        String passphrase = "";
//        Long creationtime = 1409478661L;

//        String seedCode = "bicycle accident unlock accident beauty crack enforce easily mule this current unable";
        String seedCode="behave pink ill noble unit blade real shove stuff result sad badge";

        String passphrase = "";
        Long creationtime = Utils.currentTimeSeconds();

        DeterministicSeed seed = null;
        try {
            seed = new DeterministicSeed(seedCode, null, passphrase, creationtime);
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }

        // The wallet class provides a easy fromSeed() function that loads a new wallet from a given seed.
//        Wallet wallet = Wallet.fromSeed(params, seed, Script.ScriptType.P2PKH);
        final Wallet wallet = Wallet.fromSeed(params, seed, Script.ScriptType.P2PKH);

        // Because we are importing an existing wallet which might already have transactions we must re-download the blockchain to make the wallet picks up these transactions
        // You can find some information about this in the guides: https://bitcoinj.github.io/working-with-the-wallet#setup
        // To do this we clear the transactions of the wallet and delete a possible existing blockchain file before we download the blockchain again further down.
//        System.out.println(wallet.toString());
//        System.out.println(getMasterAddress1(wallet,params));
        Logger.e("------wallet-info---->"+wallet.toString());
        Logger.e("-----getMasterAddress1---->"+getMasterAddress1(wallet,params));
        wallet.clearTransactions(0);
//        File chainFile = new File("restore-from-seed.spvchain");
        File chainFile = new File(BaseApp.getContext().getFilesDir().getPath(), "CMC_WALLET" + ".spvchain");
        if (chainFile.exists()) {
            chainFile.delete();
        }

        // Setting up the BlochChain, the BlocksStore and connecting to the network.
        SPVBlockStore chainStore = null;
        BlockChain chain =null;
        try {
            chainStore = new SPVBlockStore(params, chainFile);
            chain = new BlockChain(params, chainStore);

        } catch (BlockStoreException e) {
            e.printStackTrace();
        }
        PeerGroup peerGroup = new PeerGroup(params, chain);
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));

        // Now we need to hook the wallet up to the blockchain and the peers. This registers event listeners that notify our wallet about new transactions.
        chain.addWallet(wallet);
        peerGroup.addWallet(wallet);

        DownloadProgressTracker bListener = new DownloadProgressTracker() {

            @Override
            protected void progress(double pct, int blocksSoFar, Date date) {
                super.progress(pct, blocksSoFar, date);
                Logger.e("-----progress----->"+pct);
            }

            @Override
            public void doneDownload() {
                Logger.e("-----doneDownload----->");

            }
        };

        // Now we re-download the blockchain. This replays the chain into the wallet. Once this is completed our wallet should know of all its transactions and print the correct balance.
        peerGroup.start();
        peerGroup.startBlockChainDownload(bListener);

        try {
            bListener.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Print a debug message with the details about the wallet. The correct balance should now be displayed.
        System.out.println(wallet.toString());

        // shutting down again
        peerGroup.stop();

    }


    private static String getMasterAddress1(Wallet wallet,NetworkParameters networkParameters) {
        DeterministicKey deterministicKey = wallet.getWatchingKey().getParent().dropPrivateBytes();
        deterministicKey = HDKeyDerivation.createMasterPubKeyFromBytes(deterministicKey.getPubKey(), deterministicKey.getChainCode());


        String  xPublicKey = deterministicKey.serializePubB58(networkParameters);
        String privateKey = wallet.getKeyByPath(DeterministicKeyChain.ACCOUNT_ZERO_PATH).getParent().getPrivateKeyAsWiF(networkParameters);

        StringBuilder builder = new StringBuilder();
        deterministicKey.formatKeyWithAddress(false, null, builder,networkParameters, Script.ScriptType.P2PKH, "root");
        String  master=Address.fromKey(networkParameters, deterministicKey, Script.ScriptType.P2PKH).toString();

        Logger.d("-----xPublicKey----->"+xPublicKey);
        Logger.d("-----privateKey----->"+privateKey);
        Logger.d("-----master----->"+master);

        return master;
    }




}




