package bitchat.android.com.bitstore.wallet.bean;

import android.content.Context;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.format.DateUtils;
import androidx.annotation.Nullable;
import bitchat.android.com.bitstore.R;
import com.android.wallet.constants.Constants;
import com.android.wallet.utils.Formats;
import com.android.wallet.utils.StringUtil;
import com.android.wallet.utils.WalletUtils;
import com.orhanobut.logger.Logger;
import org.bitcoinj.core.*;
import org.bitcoinj.utils.ExchangeRate;
import org.bitcoinj.utils.Fiat;
import org.bitcoinj.utils.MonetaryFormat;
import org.bitcoinj.wallet.Wallet;

import java.util.Date;

public  class TransactionItem {

    public final Sha256Hash transactionHash;

    public final CharSequence time;

    public  long comPtime;
    @Nullable
    public final Spanned address;
    @Nullable
    public final Coin fee;

    public  MonetaryFormat feeFormat=null;

    @Nullable
    public final Coin value;

    public  MonetaryFormat valueFormat=null;
    @Nullable
    public final Fiat fiat;

    @Nullable
    public final MonetaryFormat fiatFormat;

    @Nullable
    public final String state;

    private Context context;

    private MonetaryFormat format;

    public TransactionItem(final Context context, final Transaction tx, final @Nullable Wallet wallet
    ,final MonetaryFormat format) {

        this.context=context;
        this.format=format;
        this.transactionHash = tx.getTxId();

        final Coin value = tx.getValue(wallet);
        final boolean sent = value.signum() < 0;
        final boolean self = WalletUtils.isEntirelySelf(tx, wallet);
        final TransactionConfidence confidence = tx.getConfidence();
        final TransactionConfidence.ConfidenceType confidenceType = confidence.getConfidenceType();
        final boolean isOwn = confidence.getSource().equals(TransactionConfidence.Source.SELF);
        final Transaction.Purpose purpose = tx.getPurpose();
        final String[] memo = Formats.sanitizeMemo(tx.getMemo());

        Logger.e("------confidence----->"+confidence.toString());

        // confidence
        if (confidenceType == TransactionConfidence.ConfidenceType.PENDING) {

            Logger.e("-------process---->"+"receive|send wait to confirm");
            state=context.getString(R.string.str_wait_confirm);

        } else if (confidenceType == TransactionConfidence.ConfidenceType.IN_CONFLICT) {

            Logger.e("-------process---->"+"receive|send wait to confirm");
            state=context.getString(R.string.str_wait_confirm);
        } else if (confidenceType == TransactionConfidence.ConfidenceType.BUILDING) {

            Logger.e("-------process---->"+"receive|send success");
            state=sent?context.getString(R.string.str_already_sent):context.getString(R.string.str_already_receriver);

        } else if (confidenceType == TransactionConfidence.ConfidenceType.DEAD) {
            Logger.e("-------process---->"+"receive|send undo");
            state=context.getString(R.string.str_revert);
        } else {
            Logger.e("-------process---->"+"receive|send unkonw");
            state=context.getString(R.string.str_unknow);
        }

        // time
        final Date time = tx.getUpdateTime();
        comPtime=time.getTime();
        Logger.e("------transaction-time----->"+comPtime);
            this.time = true
                    ? DateUtils.formatDateTime(context, time.getTime(),
                    DateUtils.FORMAT_SHOW_YEAR|DateUtils.FORMAT_SHOW_DATE)
                    : DateUtils.getRelativeTimeSpanString(context, time.getTime());


        // address
        final Address address = sent ? WalletUtils.getToAddressOfSent(tx, wallet)
                : WalletUtils.getWalletAddressOfReceived(tx, wallet);

        if (tx.isCoinBase()) {
            this.address = SpannedString
                    .valueOf("Minned");
        } else if (purpose == Transaction.Purpose.RAISE_FEE) {
            this.address = null;
        } else if (purpose == Transaction.Purpose.KEY_ROTATION || self) {
            this.address = SpannedString.valueOf(context.getString(R.string.symbol_internal) + " "
                    + context.getString(R.string.wallet_transactions_fragment_internal));
        } else if (memo != null && memo.length >= 2) {
            this.address = SpannedString.valueOf(memo[1]);
        } else if (address != null) {
           Spanned address1 = WalletUtils.formatAddress(address,address.toString().length(),
                    Constants.ADDRESS_FORMAT_LINE_SIZE);
            this.address= StringUtil.Companion.hidmiddleStr(address1,13);

        } else {
            this.address = SpannedString.valueOf("?");
        }

        // fee
        final Coin fee = tx.getFee();
        final boolean showFee = sent && fee != null && !fee.isZero();
        this.feeFormat = format;
        this.fee =showFee ? fee.negate() : null;

        // value
        this.valueFormat = format;
        if (purpose == Transaction.Purpose.RAISE_FEE) {
            this.value = fee.negate();
        } else if (value.isZero()) {
            this.value = null;
        } else {
            this.value = showFee ? value.add(fee) : value;
        }
        // fiat value
        final ExchangeRate exchangeRate = tx.getExchangeRate();
        if (exchangeRate != null && !value.isZero()) {
            this.fiat = exchangeRate.coinToFiat(value);
            this.fiatFormat = Constants.LOCAL_FORMAT.code(0,
                    Constants.PREFIX_ALMOST_EQUAL_TO + exchangeRate.fiat.getCurrencyCode());
        } else {
            this.fiat = null;
            this.fiatFormat = null;

        }

    }


}