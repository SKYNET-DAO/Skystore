package bitchat.android.com.bitstore.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.android.wallet.constants.Configuration
import com.android.wallet.widgets.CurrencyTextView
import com.orhanobut.logger.Logger
import org.bitcoinj.core.Coin

class MoneyFormatUtil {

    companion object {
        fun formatFWalletAmount(context: Context, coin: Coin):String{
            var configuration= Configuration(PreferenceManager.getDefaultSharedPreferences(context),context.resources)
            var balancetxtview= CurrencyTextView(context)
            balancetxtview.setFormat(configuration.format)
            balancetxtview.setAmount(coin)
            Logger.e("----balancetxtview---->${ balancetxtview.text}")
            return balancetxtview.text.toString()
        }
    }

}