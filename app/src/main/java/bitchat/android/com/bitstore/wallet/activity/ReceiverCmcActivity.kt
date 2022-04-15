package bitchat.android.com.bitstore.wallet.activity

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.utils.UIUtils
import bitchat.android.com.bitstore.wallet.utils.RxTimerUtil
import bitchat.android.com.bitstore.wallet.viewmodel.WalletViewModel
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.android.base.activitys.WfcBaseActivity
import com.android.base.utils.ImageUtils
import com.vondear.rxtool.RxClipboardTool
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_receiver_cmc.*

class ReceiverCmcActivity: WfcBaseActivity() {

    override fun isAcceptAppManager()=true

    var clipboardManager:ClipboardManager?=null
    var walletViewModel: WalletViewModel?=null
    var bitmap:Bitmap?=null
    override fun contentLayout(): Int= R.layout.activity_receiver_cmc


    override fun afterViews() {
        clipboardManager= getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        title=getString(R.string.str_receiver_cmc)
        onInitView()
    }

     fun onInitView() {

         walletViewModel=ViewModelProviders.of(this).get(WalletViewModel::class.java)
         walletViewModel?.walletLoadLiveData?.observe(this, Observer {
             address_tv.text=it.currentReceiveAddress().toString()
             makeQRCode(address_tv.text.toString())
         })
         walletViewModel?.walletLoadLiveData?.loadWallet()

         copy.setOnClickListener {
             RxClipboardTool.copyText(this,address_tv.text)
             Toast.makeText(this,R.string.str_copy_address,Toast.LENGTH_SHORT).show()
         }

         save.setOnClickListener {
             ImageUtils.saveImageToGallery(this, bitmap)
             Toast.makeText(this,R.string.str_save_success,Toast.LENGTH_SHORT).show()
         }


         RxTimerUtil.interval(3000) {
             var newaddress= walletViewModel?.walletLoadLiveData?.value?.freshReceiveAddress().toString()
             makeQRCode(newaddress)
             address_tv.text=newaddress
         }

     }


    @SuppressLint("CheckResult")
    fun makeQRCode(content:String){
        Flowable.just(content)
                .observeOn(Schedulers.io())
                .map { QRCodeEncoder.syncEncodeQRCode(it, UIUtils.dip2Px(200)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    bitmap=it
                    qrcode.setImageBitmap(it)
                }


    }

    override fun onDestroy() {
        super.onDestroy()
        RxTimerUtil.cancel()
    }


}