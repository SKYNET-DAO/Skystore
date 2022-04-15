package bitchat.android.com.bitstore.wallet.activity

import android.content.Intent
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.wallet.viewmodel.WalletViewModel
import bitchat.android.com.bitstore.wallet.BitUtil
import com.android.base.activitys.WfcBaseActivity
import kotlinx.android.synthetic.main.activity_mnemonic.*

class BackupMnemonicActivity : WfcBaseActivity() {
    override fun isAcceptAppManager()=true

    var walletViewModel: WalletViewModel?=null

    override fun contentLayout(): Int= R.layout.activity_mnemonic

    override fun menu(): Int {
        return R.menu.mnemonic_next
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.next ->{

             startActivity(Intent(this@BackupMnemonicActivity, BackupMnemonicVertifyActivity::class.java).apply {
                 putExtra("words",words.text)
             })
                finish()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun afterViews() {
        walletViewModel=ViewModelProviders.of(this).get(WalletViewModel::class.java)

        walletViewModel?.walletLoadLiveData?.observe(this, Observer {
            BitUtil.getSeedWordsFromWallet(it).toString()?.apply {
                com.orhanobut.logger.Logger.e("----getSeedWordsFromWallet----->$this")
                words.text=  substring(1,this.length-1)
            }
        })
        walletViewModel?.walletLoadLiveData?.loadWallet()
        title=getString(R.string.str_backup_words)

    }



}
