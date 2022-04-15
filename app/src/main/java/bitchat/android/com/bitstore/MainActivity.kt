package bitchat.android.com.bitstore

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.bean.AppVersionBean
import bitchat.android.com.bitstore.fragments.CategaryFragment
import bitchat.android.com.bitstore.fragments.HomeFragment
import bitchat.android.com.bitstore.fragments.MineFragment
import bitchat.android.com.bitstore.utils.AppUtils
import bitchat.android.com.bitstore.utils.UpdateAppUtils
import bitchat.android.com.bitstore.utils.UploadUtils
import bitchat.android.com.bitstore.viewmodel.AppInfoViewModel
import bitchat.android.com.bitstore.viewmodel.HomeViewModel
import bitchat.android.com.bitstore.wallet.services.WalletService
import bitchat.android.com.bitstore.wallet.utils.RxTimerUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.android.base.activitys.WfcBaseActivity
import com.android.base.adapter.BaseFragmentPagerAdapter
import com.android.base.net.AppConst
import com.android.base.utils.JsonUtil
import com.android.base.utils.ManifestUtil
import com.android.base.utils.ToastUtils
import com.android.mine_android.api.Api
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.orhanobut.logger.Logger
import com.vondear.rxtool.RxAppTool
import com.vondear.rxtool.RxTimeTool
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_receiver_cmc.*
import java.io.File

class MainActivity : WfcBaseActivity() {
    lateinit var fragmentAdapter: BaseFragmentPagerAdapter
    var fragments: MutableList<Fragment>? = null
    var appInfoViewModel:AppInfoViewModel?=null
    var homeViewModel: HomeViewModel? = null
    var mAm:ActivityManager?=null

    override fun isAcceptAppManager() = true

    override fun contentLayout() = R.layout.activity_main

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                viewpager.currentItem=0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_categary -> {
                viewpager.currentItem=1
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_mine -> {
                viewpager.currentItem=2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun afterViews() {
         mAm =getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        homeViewModel=ViewModelProviders.of(this).get(HomeViewModel::class.java)
        appInfoViewModel=ViewModelProviders.of(this).get(AppInfoViewModel::class.java)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        fragments= mutableListOf(HomeFragment(), CategaryFragment(), MineFragment())
        fragmentAdapter = BaseFragmentPagerAdapter(supportFragmentManager, fragments)
        appInfoViewModel?.onUpdateApp()?.observe(this, Observer {
        })
        viewpager.adapter=fragmentAdapter
        viewpager.offscreenPageLimit=3
        viewpager.setScroll(false)

    }

    fun  getAllChildViews():List<View> {
        var view = this.getWindow().getDecorView();
        return getAllChildViews(view)
    }

    fun  getAllChildViews(view:View ):List<View> {

        var allchildren = ArrayList<View>()

        if (view is ViewGroup) {

            var vp = view as ViewGroup

            for (i in 0..vp.childCount) {

                var viewchild = vp.getChildAt(i)
                Logger.e("--------viewchild----->$viewchild ${viewchild.id}")
                allchildren.add(viewchild)

                allchildren.addAll(getAllChildViews(viewchild))
            }
        }

        return allchildren

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            moveTaskToBack(true)

            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
        Logger.e("--------onBackPressed--->")
    }

    override fun onDestroy() {
        super.onDestroy()
        WalletService.stopSelf(this)
    }

}
