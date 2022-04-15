package bitchat.android.com.bitstore.fragments


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.activitys.JiaoUploadManagerActivity
import bitchat.android.com.bitstore.activitys.ShareActivity
import bitchat.android.com.bitstore.activitys.UploadManagerActivity
import bitchat.android.com.bitstore.utils.AndroidVersionUtil
import bitchat.android.com.bitstore.viewmodel.AppInfoViewModel
import bitchat.android.com.bitstore.wallet.BitUtil
import bitchat.android.com.bitstore.wallet.viewmodel.WalletViewModel
import cn.wildfire.imshat.wallet.activity.TrustWalletActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.android.base.utils.ManifestUtil
import com.android.wallet.constants.Constants
import com.lqr.optionitemview.OptionItemView
import com.orhanobut.logger.Logger

class MineFragment : Fragment() {

    var sharedPreferences: SharedPreferences?=null
    var token:String?=null
    var uid:String?=null
    var walletViewModel:WalletViewModel?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view=layoutInflater.inflate(R.layout.fragment_my,container,false)
        initView(view)
        return view
    }

    fun initView(view: View){
        walletViewModel=ViewModelProviders.of(this).get(WalletViewModel::class.java)
        walletViewModel?.walletLoadLiveData?.observe(this, Observer {
            val root = BitUtil.getMasterAddress1(it!!, Constants.NETWORK_PARAMETERS)



        })

        view.findViewById<View>(R.id.share).setOnClickListener {
            startActivity(Intent(activity,ShareActivity::class.java))
        }

        view.findViewById<View>(R.id.applist).setOnClickListener {
            startActivity(Intent(activity,UploadManagerActivity::class.java))
        }

        view.findViewById<View>(R.id.plugaction).setOnClickListener {
            startActivity(Intent(activity, JiaoUploadManagerActivity::class.java))
        }

        view.findViewById<View>(R.id.wallet).setOnClickListener {
            startActivity(Intent(activity, TrustWalletActivity::class.java))
        }


//        view.findViewById<TextView>(R.id.version).text=getString(R.string.str_version)+" "+AndroidVersionUtil.getVersionName()

        view.findViewById<OptionItemView>(R.id.version1).setRightText(AndroidVersionUtil.getVersionName())
        initData()



    }

    fun initData(){

        walletViewModel?.walletLoadLiveData?.loadWallet()
    }





}
