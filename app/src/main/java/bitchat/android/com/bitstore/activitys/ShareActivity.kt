package bitchat.android.com.bitstore.activitys

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.utils.AndroidVersionUtil
import bitchat.android.com.bitstore.utils.ShareUtil
import bitchat.android.com.bitstore.utils.UIUtils
import bitchat.android.com.bitstore.viewmodel.AppInfoViewModel
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.android.base.activitys.WfcBaseActivity
import com.android.base.net.AppConst
import com.android.base.utils.JsonUtil
import com.android.base.utils.ManifestUtil
import com.android.base.utils.ToastUtils
import com.google.android.material.appbar.AppBarLayout
import com.orhanobut.logger.Logger
import com.vondear.rxtool.RxClipboardTool
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity:WfcBaseActivity() {

    var appInfoViewModel: AppInfoViewModel?=null
    var title:TextView?=null
    var copyStr:String?=null


    override fun isAcceptAppManager()=true

    override fun contentLayout()= R.layout.activity_share

    override fun afterViews() {
        appInfoViewModel= ViewModelProviders.of(this).get(AppInfoViewModel::class.java)
        appInfoViewModel?.onAppInfo()?.observe(this, Observer {
            Logger.e("------appinfo---->${JsonUtil.toJson(it)}")
            copyStr=AppConst.BASE_URL+it.downloadurl
            saveImg2Gallery(copyStr)


        })
        appInfoViewModel?.progress()?.observe(this, Observer {
            showLoading(it)
        })

        share.setOnClickListener {
            RxClipboardTool.copyText(this,copyStr)
            ShareUtil.share(this,getString(R.string.str_share_tip)+copyStr)
        }

        initData()
    }


    fun initData(){
        var version= AndroidVersionUtil.getVersionName()
        var sign="A5:11:3A:83:0F:0E:07:02:92:4D:85:61:80:8C:1B:9D:F4:6E:F3:77"
//        val sign = RxAppTool.getAppSignatureSHA1(activity)

        Logger.e("----version---->$version")
        appInfoViewModel?.getAppInfo(mapOf("version" to version,"sign" to sign))
    }


    @SuppressLint("CheckResult")
    private fun saveImg2Gallery(address: String?) {
        Flowable.just(address)
            .observeOn(Schedulers.io())
            .map {
                QRCodeEncoder.syncEncodeQRCode(it, UIUtils.dp2px(200f).toInt())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                qrcode.setImageBitmap(it)
            }



    }

}