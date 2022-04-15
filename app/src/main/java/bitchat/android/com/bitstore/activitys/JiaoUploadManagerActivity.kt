package bitchat.android.com.bitstore.activitys

import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.bean.JiaoshuiBean
import bitchat.android.com.bitstore.bean.UploadListBean
import bitchat.android.com.bitstore.constants.StoreConfig.MINCMC
import bitchat.android.com.bitstore.constants.StoreConfig.isPay
import bitchat.android.com.bitstore.utils.AppLanguageUtils
import bitchat.android.com.bitstore.utils.GlideUtil
import bitchat.android.com.bitstore.utils.MoneyFormatUtil.Companion.formatFWalletAmount
import bitchat.android.com.bitstore.viewmodel.AppInfoViewModel
import bitchat.android.com.bitstore.viewmodel.JiaoshuiInfoViewModel
import bitchat.android.com.bitstore.wallet.BitUtil
import bitchat.android.com.bitstore.wallet.viewmodel.WalletViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.android.base.activitys.WfcBaseActivity
import com.android.base.net.AppConst
import com.android.base.utils.JsonUtil
import com.android.base.utils.ToastUtils
import com.android.wallet.constants.Constants
import com.lqr.adapter.LQRAdapterForRecyclerView
import com.lqr.adapter.LQRViewHolderForRecyclerView
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.activity_upload_manager.*
import org.bitcoinj.wallet.Wallet
import java.util.ArrayList

class JiaoUploadManagerActivity:WfcBaseActivity() {


    var adapter: LQRAdapterForRecyclerView<JiaoshuiBean>? = null
    var jsInfoViewModel:JiaoshuiInfoViewModel?=null
    var walletViewModel:WalletViewModel?=null
    var wallet:Wallet?=null

    override fun isAcceptAppManager()=true

    override fun contentLayout()= R.layout.activity_upload_manage_js

    override fun afterViews() {
        walletViewModel=ViewModelProviders.of(this).get(WalletViewModel::class.java)
        jsInfoViewModel=ViewModelProviders.of(this).get(JiaoshuiInfoViewModel::class.java)

        walletViewModel?.walletLoadLiveData?.observe(this, Observer {
            wallet=it
            getJiaoshuiList(it)
        })
        jsInfoViewModel?.onJiaoshuiList()?.observe(this, Observer {

            Logger.e("--------selfapp---->${JsonUtil.toJson(it)}")
            adapter?.data=it
            findViewById<SmartRefreshLayout>(R.id.smartrefreshlayout).finishRefresh()

        })

        jsInfoViewModel?.error()?.observe(this, Observer {
            Logger.e("----error---->${it.message}   ${it.code}")
        })

        jsInfoViewModel?.progress()?.observe(this, Observer {
            showLoading(it)
        })

        jsInfoViewModel?.onDelJiaoshui()?.observe(this, Observer {
            ToastUtils.show(getString(R.string.str_del_jiaoshui_tip),6000)
            Logger.e("-----onDelJiaoshui----->${JsonUtil.toJson(it)}")

        })

        findViewById<Button>(R.id.tv_publish).setOnClickListener {

            var balancecoin=wallet?.getBalance( Wallet.BalanceType.AVAILABLE)
            var balance=formatFWalletAmount(this,balancecoin!!)
            Logger.e("-----balance----->$balance")
            if(isPay){
                if(balance.trim().toDouble()>=MINCMC){
                    startActivity(Intent(this,JiaoLocalActivity::class.java))
                }else{
                    ToastUtils.show(getString(R.string.str_bigequel_100))
                }
            }else{
                startActivity(Intent(this,JiaoLocalActivity::class.java))
            }



        }

        adapter = object :
            LQRAdapterForRecyclerView<JiaoshuiBean>(this, ArrayList(), R.layout.item_manager_jiaoshui_list) {
            override fun convert(helper: LQRViewHolderForRecyclerView, item: JiaoshuiBean, position: Int) {

                val icon = helper.getView<ImageView>(R.id.icon)
                val title = helper.getView<TextView>(R.id.title)
                val subtitle2 = helper.getView<TextView>(R.id.subtitle2)
                val subtitle3 = helper.getView<TextView>(R.id.subtitle3)
//                val draw_down = helper.getView<Button>(R.id.draw_down)
                val del = helper.getView<Button>(R.id.del)
                val updatejiaoshui = helper.getView<Button>(R.id.updatejiaoshui)

                GlideUtil.loadImageView(this@JiaoUploadManagerActivity, AppConst.BASE_URL+item.getAppicon(), icon)
                title.text = item.getAppname()
                subtitle3.text=getString(R.string.str_version)+item.version
                subtitle2.visibility = View.GONE

                del.setOnClickListener { showDrawJiaoshuiDialog(item) }
                updatejiaoshui.setOnClickListener { showUpdateJiaoshuiDialog(item) }


            }

        }

        applist.adapter=adapter

        findViewById<SmartRefreshLayout>(R.id.smartrefreshlayout).setOnRefreshListener {
            getJiaoshuiList(wallet)
        }

        initData()

    }


    fun initData(){
        walletViewModel?.walletLoadLiveData?.loadWallet()

    }

    fun showDrawJiaoshuiDialog(item: JiaoshuiBean){
        var dialog=MaterialDialog.Builder(this)
            .title(R.string.str_del)
            .content(getString(R.string.str_del)+" "+item.appname+"?")
            .positiveText(R.string.str_del)
            .positiveColor(getColor(R.color.colorPrimary))
            .onPositive { dialog, which ->kotlin.run {

                var mapper= mapOf("hash" to item.id)
                jsInfoViewModel?.delJiaoshui(mapper)
            }  }
            .negativeText(R.string.str_cancel)
            .build()
        dialog.show()



    }

    fun showUpdateJiaoshuiDialog(item: JiaoshuiBean){
        var dialog=MaterialDialog.Builder(this)
            .title(R.string.str_update)
            .content(getString(R.string.str_update)+" "+item.appname+"?")
            .positiveText(R.string.str_update)
            .positiveColor(getColor(R.color.colorPrimary))
            .onPositive { dialog, which ->kotlin.run {
                var intent=Intent(this,JiaoLocalActivity::class.java)
                intent.putExtra("jiaoshuibean",item)
                startActivity(intent)
            }  }
            .negativeText(R.string.str_cancel)
            .build()
        dialog.show()

    }


    fun getJiaoshuiList(wallet: Wallet?){
        val account = BitUtil.getMasterAddress1(wallet!!, Constants.NETWORK_PARAMETERS)
        var mapper= mapOf("type" to "simple","page" to 1,"pageNum" to 100,"account" to account)
        jsInfoViewModel?.getJiaoshuiList(mapper)

    }

}