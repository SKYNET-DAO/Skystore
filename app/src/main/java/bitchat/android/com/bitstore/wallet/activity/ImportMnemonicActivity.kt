package bitchat.android.com.bitstore.wallet.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.wallet.services.WalletService
import bitchat.android.com.bitstore.wallet.viewmodel.WalletViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.android.base.activitys.AppManager
import com.android.base.activitys.WfcBaseActivity
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_mnemonic_import.*
import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.wallet.UnreadableWalletException

class ImportMnemonicActivity : WfcBaseActivity() {

    override fun isAcceptAppManager()=true

    var sharedPreferences: SharedPreferences? = null
    lateinit var walletViewModel: WalletViewModel

    override fun contentLayout(): Int = R.layout.activity_mnemonic_import

    override fun menu(): Int {
        return R.menu.mnemonic_ok
    }

    override fun afterViews() {
        walletViewModel=ViewModelProviders.of(this).get(WalletViewModel::class.java)
        walletViewModel.walletLoadLiveData.observe(this, Observer {
            Logger.e("-----restore wallet success----->$it")
            showLoading(false)
            showRestartAppDialog()
        })
        title=getString(R.string.str_import_words)



    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ok -> {

//
//                val dialog = MaterialDialog.Builder(this)
//                        .content(getString(R.string.str_restore_wallet))
//                        .progress(true, 100)
//                        .cancelable(false)
//                        .build()
//                dialog.show()
                showLoading(true)
                var words = wordsstr.text.toString().trim()

                
//                BitUtil.getSeedWordsFromWallet(walletViewModel.walletLoadLiveData.value!!).toString().apply {
//
//                    if(words==substring(1,length-1).replace(",","")){
//                        dialog.dismiss()
//                        return@apply
//                    }
//                }
                try {
                    
                    MnemonicCode.INSTANCE.check(words.split(" "))
                    val sp = getSharedPreferences("config", Context.MODE_PRIVATE)
                    sp.edit().clear().commit()

                    
                    try {


                        WalletService.broadcastRestoreWallet(this,words)

                    } catch (e: UnreadableWalletException) {
                        e.printStackTrace()
                     //   dialog.dismiss()
                        Toast.makeText(this,getString(R.string.str_reset_error),Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                  //  dialog.dismiss()
                    Logger.e("------seeds incorrect----->${e.message}")
                    Toast.makeText(this, getString(R.string.str_mne_error), Toast.LENGTH_SHORT).show()
                }finally {

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }



    fun showRestartAppDialog(){
        val dialog = MaterialDialog.Builder(this)
                .content(R.string.str_restart_app)
                .positiveText(R.string.str_restart_app_now)
                .cancelable(false)
                .onPositive { dialog1, which ->kotlin.run {
                    AppManager.getInstance().finishAllActivity()
                     System.exit(0)
                }}.build()
        dialog.show()

    }

}



