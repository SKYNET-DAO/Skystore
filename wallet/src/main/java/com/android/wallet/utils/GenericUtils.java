

package com.android.wallet.utils;

import java.util.Currency;


public class GenericUtils {
    public static boolean startsWithIgnoreCase(final String string, final String prefix) {
        return string.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public static String currencySymbol(final String currencyCode) {
        try {
            final Currency currency = Currency.getInstance(currencyCode);
            return currency.getSymbol();
        } catch (final IllegalArgumentException x) {
            return currencyCode;
        }
    }
}
