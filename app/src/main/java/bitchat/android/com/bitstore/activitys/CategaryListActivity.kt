package bitchat.android.com.bitstore.activitys

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.adapter.VDownloadListAdapter
import bitchat.android.com.bitstore.utils.AppLanguageUtils
import bitchat.android.com.bitstore.viewmodel.CategaryViewModel
import com.android.base.activitys.WfcBaseActivity
import com.lzy.okgo.model.HttpParams
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_categary.*

class CategaryListActivity:WfcBaseActivity() {

    private var categaryid: Int = 0
    private var currentpage = 1
    private var vDownloadListAdapter:VDownloadListAdapter?=null
    var categaryViewModel:CategaryViewModel?=null
    override fun isAcceptAppManager()=true

    override fun contentLayout()= R.layout.activity_categary

    override fun afterViews() {

        categaryViewModel=ViewModelProviders.of(this).get(CategaryViewModel::class.java)
        if (intent != null && intent.extras != null) { categaryid = intent!!.extras!!.get("categaryid") as Int }

        vDownloadListAdapter=VDownloadListAdapter(this, mutableListOf())
        categary_list.adapter=vDownloadListAdapter
        categaryViewModel?.onCategaryList()?.observe(this, Observer {
            vDownloadListAdapter?.addMoreData(it)
            smartrefreshlayout.finishLoadMore()
        })


        initEvent()
        initData(1)


    }

    fun initData(currentpage:Int){
        var map= mutableMapOf<String,Any>(
            "categoryid" to categaryid,
            "lang" to  AppLanguageUtils.getCurrentLang(),
            "page" to currentpage,
            "pageNum" to 20

        )
        categaryViewModel?.getCategaryList(map)


    }

    fun initEvent(){

        smartrefreshlayout.setOnLoadMoreListener(object :OnRefreshLoadMoreListener{
            override fun onLoadMore(refreshLayout: RefreshLayout) {

                currentpage++
                initData( currentpage)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {

                currentpage = 1
                initData( currentpage)
            }


        })


    }





}