package bitchat.android.com.bitstore.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.MainActivity
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.activitys.SearchActivity
import bitchat.android.com.bitstore.adapter.HomeListAdapter
import bitchat.android.com.bitstore.adapter.VDownloadListAdapter
import bitchat.android.com.bitstore.bean.BannerBean
import bitchat.android.com.bitstore.bean.HomeDataBean
import bitchat.android.com.bitstore.utils.AppLanguageUtils
import bitchat.android.com.bitstore.utils.AppUtils
import bitchat.android.com.bitstore.utils.UploadUtils
import bitchat.android.com.bitstore.viewholder.ImageResourceViewHolder
import bitchat.android.com.bitstore.viewmodel.AppInfoViewModel
import bitchat.android.com.bitstore.viewmodel.HomeViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.android.base.mvvm.viewmodel.ErrorEnvelope
import com.android.base.net.AppConst
import com.android.base.net.AppConst.BASE_URL
import com.android.base.utils.CompareVersion
import com.android.base.utils.JsonUtil
import com.android.base.utils.ManifestUtil
import com.android.base.utils.ToastUtils
import com.android.mine_android.api.Api
import com.google.gson.Gson
import com.lqr.recyclerview.LQRRecyclerView
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.vondear.rxtool.RxImageTool
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.annotation.APageStyle
import com.zhpan.bannerview.constants.IndicatorSlideMode
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.bannerview.utils.BannerUtils
import kotlinx.android.synthetic.main.activity_categary.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.io.File
import java.util.ArrayList

class HomeFragment : Fragment() {

    var sharedPreferences: SharedPreferences? = null
    var token: String? = null
    var uid: String? = null
    var mViewPager: BannerViewPager<String, ImageResourceViewHolder>? = null
    var bottomRecycleview: LQRRecyclerView? = null
    var vDownloadListAdapter: VDownloadListAdapter? = null
    var mDrawableList: MutableList<Int> = ArrayList()
    var homeViewModel: HomeViewModel? = null
    var appinfoViewModel:AppInfoViewModel?=null
    var homeListAdapter: HomeListAdapter? = null
    var currentPage = 1
    var smartrefreshlayout:SmartRefreshLayout?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = layoutInflater.inflate(R.layout.fragment_home, container, false)
        initView(view)
        return view
    }

    fun initView(view: View) {
        smartrefreshlayout=view?.findViewById(R.id.smartrefreshlayout)
        appinfoViewModel=ViewModelProviders.of(this).get(AppInfoViewModel::class.java)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        var top = layoutInflater.inflate(R.layout.item_home_top, null, true)
        var bottom = layoutInflater.inflate(R.layout.item_home_bottom, null, true)
        mViewPager = top?.findViewById(R.id.banner_view)
        bottomRecycleview = bottom.findViewById(R.id.loadmorelist)
        view.findViewById<TextView>(R.id.search).setOnClickListener {
             startActivity(Intent(activity,SearchActivity::class.java))
        }

        homeViewModel?.randomIP?.observe(this, Observer {
            Logger.e("---------randomIP----->${JsonUtil.toJson(it)}")
            BASE_URL = "http://${it.ip}"

            var map = mapOf("lang" to AppLanguageUtils.getCurrentLang())
            homeViewModel?.getBanner(map)

        })

        initGetRadomIp()

        homeViewModel?.banner?.observe(this, Observer { it ->
            Logger.e("----Results of fetch----->${it}")
            var urlarray = mutableListOf<String>()
            it.banners.forEach { urlarray.add(it.img) }
            setNetEaseMusicStyle(urlarray)

            var datas = mutableListOf<HomeDataBean.DatasBean>()
            it.datas.forEach {
                if (it.getApps().size !== 0) {
                    datas.add(it)
                }
            }
            homeListAdapter = HomeListAdapter(activity, it.datas)

            vDownloadListAdapter = VDownloadListAdapter(activity, mutableListOf())

            homeListAdapter?.addHeaderView(top)
            homeListAdapter?.addFooterView(bottom)

            bottomRecycleview?.adapter = vDownloadListAdapter

            applist.adapter = homeListAdapter?.headerAndFooterAdapter

            //init banner
            mViewPager!!
                .setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
                .setHolderCreator { ImageResourceViewHolder(resources.getDimensionPixelOffset(R.dimen.dp5)) }
                .setIndicatorColor(
                    activity!!.getColor(R.color.red_normal_color),
                    activity!!.getColor(R.color.red_checked_color)
                )
                .setOnPageClickListener { position -> ToastUtils.show("position is$position") }
                .setInterval(5000)

            //loadMoreData()
        //checkAppversion()

        })

        appinfoViewModel?.onUpdateApp()?.observe(this, Observer {
            Logger.e("----onUpdateApp----->${JsonUtil.toJson(it)}")


            val currentversion = ManifestUtil.getVersionName(activity)
            val remoteversion = it.version

            if (it != null && CompareVersion.compareVersion(remoteversion,currentversion) >=1) {
                showUpdateDialog(AppConst.BASE_URL+it.downloadurl)
            }
        })

        checkAppversion()

        homeViewModel?.homeLoadmoreMutableLiveData()?.observe(this, Observer {
            Logger.e("-----homeLoadmoreMutableLiveData--->" + it.size)
            vDownloadListAdapter?.addMoreData(it)
            smartrefreshlayout?.finishLoadMore()

        })

        homeViewModel?.progress()?.observe(this, Observer<Boolean> {
            Logger.e("----progress--->")
            (activity as MainActivity).showLoading(it)
        }
        )

        homeViewModel?.error()?.observe(this, Observer<ErrorEnvelope> {
            Logger.e("----error--->")
            (activity as MainActivity).showError(it)
        }
        )
        //initEvent()
    }

    fun initEvent() {
        smartrefreshlayout?.setOnLoadMoreListener {
            currentPage+=1
            loadMoreData()
        }

    }

    protected fun initGetRadomIp() {
        homeViewModel?.getRandomIp()
    }

    fun loadMoreData(){
        val moreparam = mapOf(
            "lang" to AppLanguageUtils.getCurrentLang(),
            "page" to currentPage, "pageNum" to 250
        )
        homeViewModel?.getMoreData(moreparam)

    }

    fun checkAppversion(){
        var sign="A5:11:3A:83:0F:0E:07:02:92:4D:85:61:80:8C:1B:9D:F4:6E:F3:77"
        var version= ManifestUtil.getVersionName(activity)
        Logger.e("----version---->$sign   $version")
        var mapper= mapOf("sign" to sign,"version" to version)
        appinfoViewModel?.checkAppVersion(mapper)

    }

    private fun setNetEaseMusicStyle(urls: List<String>) {
        mViewPager!!
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp15))
            .setRevealWidth(BannerUtils.dp2px(0f))
            .setPageStyle(PageStyle.MULTI_PAGE_SCALE)
            .setHolderCreator { ImageResourceViewHolder(resources.getDimensionPixelOffset(R.dimen.dp5)) }
            .setIndicatorColor(
                activity!!.getColor(R.color.red_normal_color),
                activity!!.getColor(R.color.red_checked_color)
            )
            .setOnPageClickListener { position -> ToastUtils.show("position:$position") }
            .setInterval(5000)
            .create(urls)
    }

    override fun onPause() {
        super.onPause()
        if (mViewPager != null) {
            mViewPager!!.stopLoop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mViewPager != null) {
            mViewPager!!.startLoop()
        }
    }


    fun showUpdateDialog(url:String?){
        var dialog= MaterialDialog.Builder(activity!!)
            .title(getString(R.string.str_new_version))
            .titleColorRes(R.color.colorPrimary)
            .content(getString(R.string.str_modify_content))
            .positiveText(R.string.str_update_now)
            .positiveColor(activity!!.getColor(R.color.colorPrimary))
            .negativeText(getString(R.string.str_wait_moment))
            .cancelable(false)
            .onPositive { dialog, which ->run{
                startDownload(url)
            }  }

        dialog.show()
    }

    fun startDownload(url:String?){
        Logger.e("----startDownload---->$url")
        var progressdialog=MaterialDialog.Builder(activity!!)
            .title(R.string.str_update_progress)
            .progress(false, 100,true)
            .cancelable(false)
        var dialog=progressdialog.show()

        UploadUtils.DownLoadFile(activity, url, object : UploadUtils.OkGoFileCallBack {
            override fun onSuccess(response: Response<File>?) {
                dialog.dismiss()
                try {
                    val filePath = response?.body()?.path //Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/SanjuScanApp_Android.apk";
                    AppUtils.installApk(activity, filePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onError(response: Response<File>?) {
                ToastUtils.show(R.string.str_net_error)
                dialog.dismiss()
            }

            override fun onloadProgress(progress: Progress?) {
                var value = (100 * progress!!.currentSize / progress!!.totalSize).toInt()
                Logger.e("------onloadProgress-->$value")
                dialog.setProgress(value)
            }
        })
    }
}

