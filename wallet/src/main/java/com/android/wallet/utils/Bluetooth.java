

package com.android.wallet.utils;

import android.bluetooth.BluetoothAdapter;

import androidx.annotation.Nullable;


import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;


public class Bluetooth {

    public static final UUID PAYMENT_REQUESTS_UUID = UUID.fromString("3357A7BB-762D-464A-8D9A-DCA592D57D59");

    public static final UUID BIP70_PAYMENT_PROTOCOL_UUID = UUID.fromString("3357A7BB-762D-464A-8D9A-DCA592D57D5A");
    public static final String BIP70_PAYMENT_PROTOCOL_NAME = "Bitcoin BIP70 payment protocol";

    public static final UUID CLASSIC_PAYMENT_PROTOCOL_UUID = UUID.fromString("3357A7BB-762D-464A-8D9A-DCA592D57D5B");
    public static final String CLASSIC_PAYMENT_PROTOCOL_NAME = "Bitcoin classic payment protocol (deprecated)";

    public static final String MAC_URI_PARAM = "bt";

    private static final String MARSHMELLOW_FAKE_MAC = "02:00:00:00:00:00";

//    private static final Logger log = LoggerFactory.getLogger(Bluetooth.class);

    public static @Nullable String getAddress(final BluetoothAdapter adapter) {
        if (adapter == null)
            return null;

        final String address = adapter.getAddress();
        if (!MARSHMELLOW_FAKE_MAC.equals(address))
            return address;

        // Horrible reflection hack needed to get the Bluetooth MAC for Marshmellow and above.
        try {
            final Field mServiceField = BluetoothAdapter.class.getDeclaredField("mService");
            mServiceField.setAccessible(true);
            final Object mService = mServiceField.get(adapter);
            if (mService == null)
                return null;
            return (String) mService.getClass().getMethod("getAddress").invoke(mService);
        } catch (final InvocationTargetException x) {
            Logger.i("Problem determining Bluetooth MAC via reflection", x);
            return null;
        } catch (final Exception x) {
            throw new RuntimeException(x);
        }
    }

    public static String compressMac(final String mac) {
        return mac.replaceAll(":", "");
    }

    public static String decompressMac(final String compressedMac) {
        final StringBuilder mac = new StringBuilder();
        for (int i = 0; i < compressedMac.length(); i += 2)
            mac.append(compressedMac.substring(i, i + 2)).append(':');
        mac.setLength(mac.length() - 1);

        return mac.toString();
    }

    public static boolean isBluetoothUrl(final String url) {
        return url != null && GenericUtils.startsWithIgnoreCase(url, "bt:");
    }

    public static String getBluetoothMac(final String url) {
        if (!isBluetoothUrl(url))
            throw new IllegalArgumentException(url);

        final int queryIndex = url.indexOf('/');
        if (queryIndex != -1)
            return url.substring(3, queryIndex);
        else
            return url.substring(3);
    }

    public static String getBluetoothQuery(final String url) {
        if (!isBluetoothUrl(url))
            throw new IllegalArgumentException(url);

        final int queryIndex = url.indexOf('/');
        if (queryIndex != -1)
            return url.substring(queryIndex);
        else
            return "/";
    }
}
