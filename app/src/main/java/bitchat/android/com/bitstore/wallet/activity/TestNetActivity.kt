package bitchat.android.com.bitstore.wallet.activity

import android.content.SharedPreferences
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.wallet.viewmodel.WalletViewModel
import com.android.base.activitys.WfcBaseActivity
import kotlinx.android.synthetic.main.activity_net_test.*

class TestNetActivity : WfcBaseActivity() {

    override fun isAcceptAppManager()=true

    var sharedPreferences: SharedPreferences? = null
    lateinit var walletViewModel: WalletViewModel

    override fun contentLayout(): Int = R.layout.activity_net_test


    override fun afterViews() {
        walletViewModel=ViewModelProviders.of(this).get(WalletViewModel::class.java)
        walletViewModel.walletLoadLiveData.observe(this, Observer {
            netpeer.text=it.networkParameters.id
            lastblockheight.text=it.lastBlockSeenHeight.toString()
            lasthash.text=it.lastBlockSeenHash.toString()
        })

        walletViewModel.walletLoadLiveData.loadWallet()


    }




}



