

package com.android.wallet.constants;

import android.os.Build;
import android.os.Environment;
import android.text.format.DateUtils;

import com.google.common.io.BaseEncoding;
import com.orhanobut.logger.Logger;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.utils.MonetaryFormat;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public final class Constants {


    public static final boolean TEST = false;
    public static final NetworkParameters NETWORK_PARAMETERS = TEST? TestNet3Params.get() : MainNetParams.get();


    public static final Context CONTEXT = new Context(NETWORK_PARAMETERS);


    public static final Script.ScriptType DEFAULT_OUTPUT_SCRIPT_TYPE = Script.ScriptType.P2PKH;


    public static final Script.ScriptType UPGRADE_OUTPUT_SCRIPT_TYPE = Script.ScriptType.P2PKH;


    public static final boolean ENABLE_BLOCKCHAIN_SYNC = true;

    public static final boolean ENABLE_EXCHANGE_RATES = false;

    public static final boolean ENABLE_SWEEP_WALLET = true;

    public static final boolean ENABLE_BROWSE = true;

    public final static class Files {
        private static final String FILENAME_NETWORK_SUFFIX = NETWORK_PARAMETERS.getId()
                .equals(NetworkParameters.ID_MAINNET) ? "" : "-testnet";


        public static final String WALLET_FILENAME_PROTOBUF = "wallet-protobuf" + FILENAME_NETWORK_SUFFIX;


        public static final long WALLET_AUTOSAVE_DELAY_MS = 3 * DateUtils.SECOND_IN_MILLIS;


        public static final String WALLET_KEY_BACKUP_BASE58 = "key-backup-base58" + FILENAME_NETWORK_SUFFIX;


        public static final String WALLET_KEY_BACKUP_PROTOBUF = "key-backup-protobuf" + FILENAME_NETWORK_SUFFIX;


        public static final File EXTERNAL_STORAGE_DIR = Environment.getExternalStorageDirectory();


        public static final File EXTERNAL_WALLET_BACKUP_DIR = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        public static final String EXTERNAL_WALLET_BACKUP = "bitcoin-wallet-backup" + FILENAME_NETWORK_SUFFIX;


        public static final String BLOCKCHAIN_FILENAME = "blockchain" + FILENAME_NETWORK_SUFFIX;


        public static final int BLOCKCHAIN_STORE_CAPACITY = SPVBlockStore.DEFAULT_CAPACITY * 2;


        public static final String CHECKPOINTS_ASSET = "checkpoints" + FILENAME_NETWORK_SUFFIX + ".txt";


        public static final String FEES_ASSET = "fees" + FILENAME_NETWORK_SUFFIX + ".txt";


        public static final String ELECTRUM_SERVERS_ASSET = "electrum-servers.txt";
    }

//
//    public static final HttpUrl VERSION_URL = HttpUrl.parse("https://wallet.schildbach.de/version"
//            + (NETWORK_PARAMETERS.getId().equals(NetworkParameters.ID_MAINNET) ? "" : "-test"));
//
    public static final HttpUrl DYNAMIC_FEES_URL = HttpUrl.parse("https://wallet.schildbach.de/fees");


    public static final String MIMETYPE_TRANSACTION = "application/x-btctx";


    public static final String MIMETYPE_WALLET_BACKUP = "application/x-bitcoin-wallet-backup";


    public static final int MAX_NUM_CONFIRMATIONS = 7;


    public static final String USER_AGENT = "Bitcoin Wallet";


    public static final String DEFAULT_EXCHANGE_CURRENCY = "USD";


    public static final String DONATION_ADDRESS = NETWORK_PARAMETERS.getId().equals(NetworkParameters.ID_MAINNET)
            ? "bc1qzug4shzgksqfqxuphgxluhnayqq3rmmh5v0dql" : null;


    public static final String REPORT_EMAIL = "bitcoin.wallet.developers@gmail.com";


    public static final String REPORT_SUBJECT_ISSUE = "Reported issue";


    public static final String REPORT_SUBJECT_CRASH = "Crash report";

    public static final char CHAR_HAIR_SPACE = '\u200a';
    public static final char CHAR_THIN_SPACE = '\u2009';
    public static final char CHAR_BITCOIN = '\u20ac';//'\u20bf'
    public static final char CHAR_ALMOST_EQUAL_TO = '\u2248';
    public static final char CHAR_CHECKMARK = '\u2713';
    public static final char CURRENCY_PLUS_SIGN = '\uff0b';
    public static final char CURRENCY_MINUS_SIGN = '\uff0d';
    public static final String PREFIX_ALMOST_EQUAL_TO = Character.toString(CHAR_ALMOST_EQUAL_TO) + CHAR_THIN_SPACE;
    public static final int ADDRESS_FORMAT_GROUP_SIZE = 4;
    public static final int ADDRESS_FORMAT_LINE_SIZE = 12;

    public static final MonetaryFormat LOCAL_FORMAT = new MonetaryFormat().noCode().minDecimals(2).optionalDecimals();

    public static final BaseEncoding HEX = BaseEncoding.base16().lowerCase();

    public static final String SOURCE_URL = "https://github.com/bitcoin-wallet/bitcoin-wallet";
    public static final String BINARY_URL = "https://wallet.schildbach.de/";

    public static final int PEER_DISCOVERY_TIMEOUT_MS = 10 * (int) DateUtils.SECOND_IN_MILLIS;
    public static final int PEER_TIMEOUT_MS = 15 * (int) DateUtils.SECOND_IN_MILLIS;

    public static final long LAST_USAGE_THRESHOLD_JUST_MS = DateUtils.HOUR_IN_MILLIS;
    public static final long LAST_USAGE_THRESHOLD_RECENTLY_MS = 2 * DateUtils.DAY_IN_MILLIS;
    public static final long LAST_USAGE_THRESHOLD_INACTIVE_MS = 4 * DateUtils.WEEK_IN_MILLIS;

    public static final long DELAYED_TRANSACTION_THRESHOLD_MS = 2 * DateUtils.HOUR_IN_MILLIS;


    public static final Coin TOO_MUCH_BALANCE_THRESHOLD = Coin.COIN.divide(8);

    public static final Coin SOME_BALANCE_THRESHOLD = Coin.COIN.divide(400);

    public static final int SDK_DEPRECATED_BELOW = Build.VERSION_CODES.LOLLIPOP;
    public static final String SECURITY_PATCH_INSECURE_BELOW = "2018-01-01";

    public static final int NOTIFICATION_ID_CONNECTED = 1;
    public static final int NOTIFICATION_ID_COINS_RECEIVED = 2;
    public static final int NOTIFICATION_ID_MAINTENANCE = 3;
    public static final int NOTIFICATION_ID_INACTIVITY = 4;
    public static final String NOTIFICATION_GROUP_KEY_RECEIVED = "group-received";
    public static final String NOTIFICATION_CHANNEL_ID_RECEIVED = "received";
    public static final String NOTIFICATION_CHANNEL_ID_ONGOING = "ongoing";
    public static final String NOTIFICATION_CHANNEL_ID_IMPORTANT = "important";


    public static final int SCRYPT_ITERATIONS_TARGET = 65536;
    public static final int SCRYPT_ITERATIONS_TARGET_LOWRAM = 32768;


    public static final int ELECTRUM_SERVER_DEFAULT_PORT_TCP = NETWORK_PARAMETERS.getId()
            .equals(NetworkParameters.ID_MAINNET) ? 50001 : 51001;
    public static final int ELECTRUM_SERVER_DEFAULT_PORT_TLS = NETWORK_PARAMETERS.getId()
            .equals(NetworkParameters.ID_MAINNET) ? 50002 : 51002;


    public static OkHttpClient HTTP_CLIENT;
    static {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(final String message) {
                        Logger.d(message);
                    }
                });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.followRedirects(false);
        httpClientBuilder.followSslRedirects(true);
        httpClientBuilder.connectTimeout(15, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(15, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(15, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(loggingInterceptor);
        HTTP_CLIENT = httpClientBuilder.build();
    }

}
