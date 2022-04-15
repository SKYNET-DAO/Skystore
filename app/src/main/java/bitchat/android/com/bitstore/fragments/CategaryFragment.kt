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
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.activitys.CategaryListActivity
import bitchat.android.com.bitstore.bean.CategaryBean
import bitchat.android.com.bitstore.utils.AppLanguageUtils
import bitchat.android.com.bitstore.utils.GlideUtil
import bitchat.android.com.bitstore.viewmodel.CategaryViewModel
import com.google.gson.Gson
import com.lqr.adapter.LQRAdapterForRecyclerView
import com.lqr.adapter.LQRViewHolderForRecyclerView
import com.lqr.recyclerview.LQRRecyclerView
import com.orhanobut.logger.Logger
import com.vondear.rxtool.RxImageTool
import org.w3c.dom.Text
import java.util.ArrayList

class CategaryFragment : Fragment() {

    var sharedPreferences: SharedPreferences?=null
    var token:String?=null
    var uid:String?=null
    var lqrRecyclerView:LQRRecyclerView?=null
    var categaryViewModel:CategaryViewModel?=null
     var categaryadapter: LQRAdapterForRecyclerView<CategaryBean>?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view=layoutInflater.inflate(R.layout.fragment_categary,container,false)
        initView(view)
        return view
    }

    fun initView(view: View){
        categaryViewModel=ViewModelProviders.of(this).get(CategaryViewModel::class.java)
        lqrRecyclerView=view.findViewById(R.id.categarylist)
        categaryViewModel?.onCategary()?.observe(this, Observer {
            Logger.d("---------onCategary------>" + it.size)
            categaryadapter?.data=it
        })
        categaryadapter = object :
            LQRAdapterForRecyclerView<CategaryBean>(context, ArrayList(), R.layout.item_categary) {
            override fun convert(helper: LQRViewHolderForRecyclerView, item: CategaryBean, position: Int) {
                val icon = helper.getView<ImageView>(R.id.icon)
                val title = helper.getView<TextView>(R.id.title)
                Logger.d("---------item.getCategoryIcon()------>" + item.getCategoryIcon())
                GlideUtil.loadImageView(context, item.categoryIcon, icon)
                //     icon.setImageDrawable(getResourcesId(item.getCategoryId()));
                title.text = item.categoryName


                helper.convertView.setOnClickListener { v ->

                    
                    val intent = Intent(activity, CategaryListActivity::class.java)

                    intent.putExtra("categaryid", item.categoryId)
                    startActivity(intent)


                }


            }


        }

        lqrRecyclerView?.adapter=categaryadapter

       initData()


    }

    fun initData(){
        var map= mutableMapOf<String,Any>("lang" to  AppLanguageUtils.getCurrentLang())
        categaryViewModel?.getCategary(map)

    }


}
