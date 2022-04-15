

package com.android.wallet.send;

import android.os.Handler;
import android.os.Looper;

import com.android.wallet.constants.Constants;
import com.orhanobut.logger.Logger;

import org.bitcoinj.crypto.KeyCrypter;
import org.bitcoinj.crypto.KeyCrypterException;
import org.bitcoinj.crypto.KeyCrypterScrypt;
import org.bitcoinj.wallet.Wallet;
import org.bouncycastle.crypto.params.KeyParameter;

import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.core.util.Preconditions.checkState;


public abstract class DeriveKeyTask {
    private final Handler backgroundHandler;
    private final Handler callbackHandler;
    private final int scryptIterationsTarget;

//    private static final Logger log = LoggerFactory.getLogger(DeriveKeyTask.class);

    public DeriveKeyTask(final Handler backgroundHandler, final int scryptIterationsTarget) {
        this.backgroundHandler = backgroundHandler;
        this.callbackHandler = new Handler(Looper.myLooper());
        this.scryptIterationsTarget = scryptIterationsTarget;
    }

    public final void deriveKey(final Wallet wallet, final String password) {
        checkState(wallet.isEncrypted());
        final KeyCrypter keyCrypter = checkNotNull(wallet.getKeyCrypter());

        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                org.bitcoinj.core.Context.propagate(Constants.CONTEXT);

                // Key derivation takes time.
                KeyParameter key = keyCrypter.deriveKey(password);
                boolean wasChanged = false;

                // If the key isn't derived using the desired parameters, derive a new key.
                if (keyCrypter instanceof KeyCrypterScrypt) {
                    final long scryptIterations = ((KeyCrypterScrypt) keyCrypter).getScryptParameters().getN();

                    if (scryptIterations != scryptIterationsTarget) {
                        Logger.i("upgrading scrypt iterations from {} to {}; re-encrypting wallet", scryptIterations,
                                scryptIterationsTarget);

                        final KeyCrypterScrypt newKeyCrypter = new KeyCrypterScrypt(scryptIterationsTarget);
                        final KeyParameter newKey = newKeyCrypter.deriveKey(password);

                        // Re-encrypt wallet with new key.
                        try {
                            wallet.changeEncryptionKey(newKeyCrypter, key, newKey);
                            key = newKey;
                            wasChanged = true;
                            Logger.i("scrypt upgrade succeeded");
                        } catch (final KeyCrypterException x) {
                            Logger.i("scrypt upgrade failed: {}", x.getMessage());
                        }
                    }
                }

                // Hand back the (possibly changed) encryption key.
                final KeyParameter keyToReturn = key;
                final boolean keyToReturnWasChanged = wasChanged;
                callbackHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(keyToReturn, keyToReturnWasChanged);
                    }
                });
            }
        });
    }

    protected abstract void onSuccess(KeyParameter encryptionKey, boolean changed);
}
