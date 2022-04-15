

package bitchat.android.com.bitstore.wallet.data;

import android.os.Handler;
import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;


public abstract class ThrottelingLiveData<T> extends LiveData<T> {
    private final long throttleMs;
    private final Handler handler = new Handler();
    private long lastMessageMs;
    private static final long DEFAULT_THROTTLE_MS = 500;

    public ThrottelingLiveData() {
        this(DEFAULT_THROTTLE_MS);
    }

    public ThrottelingLiveData(final long throttleMs) {
        this.throttleMs = throttleMs;
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        handler.removeCallbacksAndMessages(null);
    }

    @MainThread
    protected void triggerLoad() {
        handler.removeCallbacksAndMessages(null);
        final Runnable runnable = () -> {
            lastMessageMs = System.currentTimeMillis();
            load();
        };
        final long lastMessageAgoMs = System.currentTimeMillis() - lastMessageMs;
        if (lastMessageAgoMs < throttleMs)
            handler.postDelayed(runnable, throttleMs - lastMessageAgoMs);
        else
            runnable.run(); // immediately
    }

    @MainThread
    protected void load() {
        // do nothing by default
    }
}
