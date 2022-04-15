package bitchat.android.com.bitstore.activitys

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.bean.SelfUploadApp
import bitchat.android.com.bitstore.bean.UploadListBean
import bitchat.android.com.bitstore.constants.StoreConfig.MINCMC
import bitchat.android.com.bitstore.constants.StoreConfig.isPay
import bitchat.android.com.bitstore.utils.AppLanguageUtils
import bitchat.android.com.bitstore.utils.FileUtils
import bitchat.android.com.bitstore.utils.GlideUtil
import bitchat.android.com.bitstore.utils.MoneyFormatUtil.Companion.formatFWalletAmount
import bitchat.android.com.bitstore.viewmodel.AppInfoViewModel
import bitchat.android.com.bitstore.wallet.BitUtil
import bitchat.android.com.bitstore.wallet.viewmodel.WalletViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.android.base.activitys.WfcBaseActivity
import com.android.base.net.AppConst
import com.android.base.utils.FileSizeUtil
import com.android.base.utils.JsonUtil
import com.android.base.utils.ToastUtils
import com.android.wallet.constants.Configuration
import com.android.wallet.constants.Constants
import com.android.wallet.widgets.CurrencyTextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lqr.adapter.LQRAdapterForRecyclerView
import com.lqr.adapter.LQRViewHolderForRecyclerView
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.activity_categary.*
import kotlinx.android.synthetic.main.activity_upload_manager.*
import org.bitcoinj.core.Coin
import org.bitcoinj.wallet.Wallet
import java.util.ArrayList

class UploadManagerActivity:WfcBaseActivity() {


    var adapter: LQRAdapterForRecyclerView<UploadListBean>? = null
    var appInfoViewModel:AppInfoViewModel?=null
    var walletViewModel:WalletViewModel?=null
    var wallet:Wallet?=null

    override fun isAcceptAppManager()=true

    override fun contentLayout()= R.layout.activity_upload_manager

    override fun afterViews() {
        walletViewModel=ViewModelProviders.of(this).get(WalletViewModel::class.java)
        appInfoViewModel=ViewModelProviders.of(this).get(AppInfoViewModel::class.java)

        walletViewModel?.walletLoadLiveData?.observe(this, Observer {
            wallet=it
            getUploadAppList()
        })

        appInfoViewModel?.onuploadApplist()?.observe(this, Observer {

            it.forEach {
                Logger.e("--------selfapp---->${JsonUtil.toJson(it)}")
            }
            adapter?.data=it
            findViewById<SmartRefreshLayout>(R.id.smartrefreshlayout).finishRefresh()

        })

        appInfoViewModel?.progress()?.observe(this, Observer {
            showLoading(it)
        })

        appInfoViewModel?.onDrawdownApp()?.observe(this, Observer {
            Logger.e("-----onDrawdownApp----->${JsonUtil.toJson(it)}")
            if(!TextUtils.isEmpty(it.hash))ToastUtils.show(getString(R.string.str_drawdown_delay),6000)

        })

        floatbutton.setOnClickListener {


            var balancecoin=wallet?.getBalance( Wallet.BalanceType.AVAILABLE)
            var balance=formatFWalletAmount(this,balancecoin!!)
            Logger.e("-----balance----->$balance")
            if(isPay){
                if(balance.trim().toDouble()>=MINCMC){
                    startActivity(Intent(this,LocalAppActivity::class.java))
                }else{
                    ToastUtils.show(getString(R.string.str_bigequel_100))
                }
            }else{
                startActivity(Intent(this,LocalAppActivity::class.java))
            }


        }

        adapter = object :
            LQRAdapterForRecyclerView<UploadListBean>(this, ArrayList(), R.layout.item_manager_list) {
            override fun convert(helper: LQRViewHolderForRecyclerView, item: UploadListBean, position: Int) {

                val icon = helper.getView<ImageView>(R.id.icon)
                val title = helper.getView<TextView>(R.id.title)
                val subtitle2 = helper.getView<TextView>(R.id.subtitle2)
                val subtitle3 = helper.getView<TextView>(R.id.subtitle3)
                val draw_down = helper.getView<Button>(R.id.draw_down)
                val draw_down1 = helper.getView<Button>(R.id.draw_down1)

                GlideUtil.loadImageView(this@UploadManagerActivity, AppConst.BASE_URL+item.getAppicon(), icon)
                title.text = item.getAppname()

                subtitle3.text=getString(R.string.str_version)+item.version
                subtitle2.visibility = View.GONE

                draw_down1.setOnClickListener { showDrawAppDialog(item) }


            }

        }

        applist.adapter=adapter

        findViewById<SmartRefreshLayout>(R.id.smartrefreshlayout).setOnRefreshListener {
            getUploadAppList()
        }

        initData()

    }


    fun initData(){
        walletViewModel?.walletLoadLiveData?.loadWallet()

    }

    fun showDrawAppDialog(item: UploadListBean){
        var dialog=MaterialDialog.Builder(this)
            .title(R.string.str_app_drawdown)
            .content(getString(R.string.str_sure_drawdown)+" "+item.appname+"?")
            .positiveText(R.string.str_app_drawdown)
            .positiveColor(getColor(R.color.colorPrimary))
            .onPositive { dialog, which ->kotlin.run {

                appInfoViewModel?.drawdownApp(mapOf("hash" to item.id))
            }  }
            .negativeText(R.string.str_cancel)
            .build()
        dialog.show()



    }

    fun getUploadAppList(){

        val root = BitUtil.getMasterAddress1(this.wallet!!, Constants.NETWORK_PARAMETERS)
        var mapper= mapOf("account" to root,"lang" to AppLanguageUtils.getCurrentLang())
        appInfoViewModel?.getApplist(mapper)

    }






}