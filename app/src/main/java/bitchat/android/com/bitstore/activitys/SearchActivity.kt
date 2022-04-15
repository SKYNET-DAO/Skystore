package bitchat.android.com.bitstore.activitys

import android.annotation.SuppressLint
import android.text.TextUtils
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.adapter.SearchListAdapter
import bitchat.android.com.bitstore.bean.HistoryBean
import bitchat.android.com.bitstore.bean.SearchBean
import bitchat.android.com.bitstore.bean.SearchHistory
import bitchat.android.com.bitstore.bean.SearchHot
import bitchat.android.com.bitstore.utils.AppLanguageUtils
import bitchat.android.com.bitstore.viewmodel.SearchViewModel
import com.android.base.activitys.WfcBaseActivity
import com.android.base.utils.JsonUtil
import com.android.base.utils.ToastUtils
import com.jakewharton.rxbinding2.widget.RxTextView
import com.lqr.recyclerview.LQRRecyclerView
import com.lzy.okgo.model.Response
import com.orhanobut.logger.Logger
import com.vondear.rxtool.RxDeviceTool
import com.vondear.rxtool.RxTextTool
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import org.litepal.LitePal
import org.w3c.dom.Text
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class SearchActivity:WfcBaseActivity(){
    private var adapter: SearchListAdapter? = null
    private var search: EditText? = null
    private var result:LQRRecyclerView?=null
    var searchViewModel:SearchViewModel?=null


    override fun isAcceptAppManager(): Boolean {
        return true
    }

    override fun contentLayout()= R.layout.activity_search


    override fun afterViews() {
        searchViewModel=ViewModelProviders.of(this).get(SearchViewModel::class.java)
        search=findViewById(R.id.search)
        result=findViewById(R.id.result)
        adapter = SearchListAdapter(this, search, mutableListOf<Any>())
        result?.adapter=adapter
        initEvent()
        initData()

    }


    @SuppressLint("CheckResult")
    fun initEvent(){

        searchViewModel?.onHot()?.observe(this, Observer {
            val searchHot = SearchHot("3",it)
            adapter?.addFirstItem(searchHot)
        })


        searchViewModel?.onSearch()?.observe(this, Observer {
            adapter?.data = it
        })

        searchViewModel?.error()?.observe(this, Observer {
            adapter?.clearData()
        })



        RxTextView.textChanges(search!!)
            .debounce(200, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())// 
            //
            //.subscribeOn(Schedulers.io()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { charSequence ->kotlin.run {
                Logger.e("------charSequence--->$charSequence")

                    if(TextUtils.isEmpty(charSequence))return@subscribe

                    var map_search= mutableMapOf<String,Any>(
                        "appname" to charSequence.toString(),
                        "lang" to  AppLanguageUtils.getCurrentLang())
                    searchViewModel?.getSearch(map_search)



            }

            }


    }

    fun initData(){




        var map= mutableMapOf<String,Any>(
            "lang" to AppLanguageUtils.getCurrentLang(),
            "page" to 1,
            "columnId" to 7,
            "pageNum" to 20
            )
        searchViewModel?.getHot(map)




        
        //  List<SearchHistory>  list=LitePal.where("address=? and lang=?",wallet.getAddress(), AppLanguageUtils.getCurrentLang()).find(SearchHistory.class);
        val list = LitePal.where("address=? ",RxDeviceTool.getAndroidId(this)).find(
            SearchHistory::class.java
        )
        if (list == null || list.size == 0) return
        val beans = list as List<SearchHistory>
        val historyBean = HistoryBean("0", beans)
        historyBean.datas = beans
        adapter?.addLastItem(historyBean)

    }

    override fun onDestroy() {
        super.onDestroy()
         adapter?.clearData()
    }

}