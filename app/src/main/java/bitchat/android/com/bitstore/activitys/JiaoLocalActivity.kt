package bitchat.android.com.bitstore.activitys

import android.content.Intent
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.bean.FileBean
import bitchat.android.com.bitstore.bean.JiaoshuiBean
import bitchat.android.com.bitstore.viewmodel.AppInfoViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.android.base.activitys.WfcBaseActivity
import com.android.base.utils.FileSizeUtil
import com.android.base.utils.JsonUtil
import com.lqr.adapter.LQRAdapterForRecyclerView
import com.lqr.adapter.LQRViewHolderForRecyclerView
import com.lqr.recyclerview.LQRRecyclerView
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_app_select.*
import java.util.ArrayList

class JiaoLocalActivity:WfcBaseActivity() {


    var lqrRecyclerView:LQRRecyclerView?=null
     var adapter: LQRAdapterForRecyclerView<FileBean>?=null
    var appInfoViewModel:AppInfoViewModel?=null
      var map = HashMap<Int, Boolean>()
    private  var onBind:Boolean = false
    private var checkedPosition = -1
    var jiaoshuibean:JiaoshuiBean?=null


    override fun isAcceptAppManager()=true

    override fun contentLayout()= R.layout.activity_app_select

    override fun afterViews() {
        intent?.extras?.get("jiaoshuibean").apply {
            if(this!=null){
                jiaoshuibean=this as JiaoshuiBean
            }
        }
        lqrRecyclerView=findViewById(R.id.localapps)
        appInfoViewModel=ViewModelProviders.of(this).get(AppInfoViewModel::class.java)
        appInfoViewModel?.onLocalGz()?.observe(this, Observer {
            Logger.e("----onLocalGz--->${it.size}")
            adapter?.addNewData(it)
        })

        appInfoViewModel?.onUploadIcon()?.observe(this, Observer {

            Logger.e("-------onUploadIcon---->${JsonUtil.toJson(it)}")
            if(it!=null&&!it.result.url.isNullOrEmpty()){
               var intent= Intent(this,UploadActivity::class.java).apply {
                   putExtra("iconurl",it.result.url)
                   putExtra("size",it.result.size)
               }
                startActivity(intent)
            }
        })

        appInfoViewModel?.progress()?.observe(this, Observer {
            showLoading(it)
        })



        adapter= object : LQRAdapterForRecyclerView<FileBean>(this@JiaoLocalActivity, ArrayList(), R.layout.item_app_select){
            override fun convert(helper: LQRViewHolderForRecyclerView?, item: FileBean?, position: Int) {
                helper?.getView<ImageView>(R.id.icon)?.setImageResource(item?.iconId!!)
                helper?.getView<TextView>(R.id.title)?.text=item?.title
                helper?.getView<TextView>(R.id.subtitle2)?.text=FileSizeUtil.FormetFileSize(item?.size!!)
                helper?.getView<Button>(R.id.btn)?.setOnClickListener {
                    confirmUpload(item)
                }
            }
        }
        localapps.adapter=adapter
        initData()
    }




    fun initData(){

        appInfoViewModel?.getLocalGzs(this)
    }


    fun confirmUpload(fileBean: FileBean){
        val dialog = MaterialDialog.Builder(this)
            .title(getString(R.string.str_confirm_upload_jiao))
            .content(fileBean.path)
            .positiveText(R.string.str_comfirm)
            .negativeText(R.string.str_cancel)
            .positiveColor(getColor(R.color.colorPrimary))
            .cancelable(false)
            .onPositive { d, w ->kotlin.run {
                var intent= Intent(this@JiaoLocalActivity,JiaoUploadActivity::class.java).apply {
                    putExtra("name",fileBean.title)
                    putExtra("path",fileBean.path)
                    putExtra("size",fileBean.size)
                    if(jiaoshuibean!=null)putExtra("jiaoshuibean",jiaoshuibean)


                }
                startActivity(intent)
            }}.build()
        dialog.show()
    }




}