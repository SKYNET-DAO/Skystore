

package com.android.wallet.send;

import android.os.Handler;
import android.os.Looper;

import com.android.wallet.constants.Constants;
import com.orhanobut.logger.Logger;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.crypto.KeyCrypterException;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.Wallet.CompletionException;
import org.bitcoinj.wallet.Wallet.CouldNotAdjustDownwards;



public abstract class SendCoinsOfflineTask {
    private final Wallet wallet;
    private final Handler backgroundHandler;
    private final Handler callbackHandler;

//    private static final Logger log = LoggerFactory.getLogger(SendCoinsOfflineTask.class);

    public SendCoinsOfflineTask(final Wallet wallet, final Handler backgroundHandler) {
        this.wallet = wallet;
        this.backgroundHandler = backgroundHandler;
        this.callbackHandler = new Handler(Looper.myLooper());
    }

    public final void sendCoinsOffline(final SendRequest sendRequest) {
        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                org.bitcoinj.core.Context.propagate(Constants.CONTEXT);

                try {
                    Logger.i("sending: {}", sendRequest);
                    final Transaction transaction = wallet.sendCoinsOffline(sendRequest); // can take long
                    Logger.i("send successful, transaction committed: {}", transaction.getTxId());

                    callbackHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess(transaction);
                        }
                    });
                } catch (final InsufficientMoneyException x) {
                    final Coin missing = x.missing;
                    if (missing != null)
                        Logger.i("send failed, {} missing", missing.toFriendlyString());
                    else
                        Logger.i("send failed, insufficient coins");

                    callbackHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onInsufficientMoney(x.missing);
                        }
                    });
                } catch (final ECKey.KeyIsEncryptedException x) {
                    Logger.i("send failed, key is encrypted: {}", x.getMessage());

                    callbackHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onFailure(x);
                        }
                    });
                } catch (final KeyCrypterException x) {
                    Logger.i("send failed, key crypter exception: {}", x.getMessage());

                    final boolean isEncrypted = wallet.isEncrypted();
                    callbackHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isEncrypted)
                                onInvalidEncryptionKey();
                            else
                                onFailure(x);
                        }
                    });
                } catch (final CouldNotAdjustDownwards x) {
                    Logger.i("send failed, could not adjust downwards: {}", x.getMessage());

                    callbackHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onEmptyWalletFailed();
                        }
                    });
                } catch (final CompletionException x) {
                    Logger.i("send failed, cannot complete: {}", x.getMessage());

                    callbackHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onFailure(x);
                        }
                    });
                }
            }
        });
    }

    protected abstract void onSuccess(Transaction transaction);

    protected abstract void onInsufficientMoney(Coin missing);

    protected abstract void onInvalidEncryptionKey();

    protected void onEmptyWalletFailed() {
        onFailure(new CouldNotAdjustDownwards());
    }

    protected abstract void onFailure(Exception exception);
}
