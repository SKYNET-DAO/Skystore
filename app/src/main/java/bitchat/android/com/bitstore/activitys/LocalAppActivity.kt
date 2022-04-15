package bitchat.android.com.bitstore.activitys

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.adapter.CheckBoxAdapter
import bitchat.android.com.bitstore.bean.CategaryBean
import bitchat.android.com.bitstore.bean.FileBean
import bitchat.android.com.bitstore.bean.UploadBean
import bitchat.android.com.bitstore.utils.*
import bitchat.android.com.bitstore.viewmodel.AppInfoViewModel
import bitchat.android.com.bitstore.viewmodel.AppinstallViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.android.base.activitys.AppManager
import com.android.base.activitys.WfcBaseActivity
import com.android.base.app.BaseApp
import com.android.base.glide.GlideApp
import com.android.base.net.AppConst
import com.android.base.utils.FileSizeUtil
import com.android.base.utils.JsonUtil
import com.android.base.utils.ManifestUtil
import com.android.mine_android.api.Api
import com.google.android.material.appbar.AppBarLayout
import com.lqr.adapter.LQRAdapterForRecyclerView
import com.lqr.adapter.LQRViewHolderForRecyclerView
import com.lqr.recyclerview.LQRRecyclerView
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.orhanobut.logger.Logger
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_app_select.*
import java.io.File
import java.util.ArrayList

class LocalAppActivity:WfcBaseActivity() {


    var lqrRecyclerView:LQRRecyclerView?=null
     var adapter: LQRAdapterForRecyclerView<FileBean>?=null
    var appInfoViewModel:AppInfoViewModel?=null
      var map = HashMap<Int, Boolean>()
    private  var onBind:Boolean = false
    private var checkedPosition = -1


    override fun isAcceptAppManager()=true

    override fun contentLayout()= R.layout.activity_app_select

    override fun afterViews() {

        lqrRecyclerView=findViewById(R.id.localapps)
        appInfoViewModel=ViewModelProviders.of(this).get(AppInfoViewModel::class.java)
        appInfoViewModel?.onLocalapp()?.observe(this, Observer {
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



        adapter= object : LQRAdapterForRecyclerView<FileBean>(this@LocalAppActivity, ArrayList(), R.layout.item_app_select){
            override fun convert(helper: LQRViewHolderForRecyclerView?, item: FileBean?, position: Int) {
                helper?.getView<ImageView>(R.id.icon)?.setImageDrawable(item?.appInfo?.icon)
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
//        for(i in 1..20){
//            adapter?.addLastItem(i)
//
//        }
        appInfoViewModel?.getLocalApps(this)
    }


    fun confirmUpload(fileBean: FileBean){
        val dialog = MaterialDialog.Builder(this)
            .title(getString(R.string.str_confirm_upload))
            .content(fileBean.path)
            .positiveText(R.string.str_comfirm)
            .negativeText(R.string.str_cancel)
            .negativeColor(getColor(R.color.colorPrimary))
            .cancelable(false)
            .onPositive { d, w ->kotlin.run {
                commitIcon(fileBean)
            }}.build()
        dialog.show()
    }

    fun commitIcon(fileBean: FileBean){
        var mapper= mapOf("lang" to AppLanguageUtils.getCurrentLang())
        val bd = fileBean.appInfo.icon as BitmapDrawable
        val iconpath = SaveImgUtil.saveImageToPath(this, "appicon", bd.bitmap).path
        Logger.e("--------111--->${AppConst.BASE_URL+Api.upload}")
        appInfoViewModel?.uploadAppIcon(this,AppConst.BASE_URL+Api.upload,arrayListOf(File(iconpath)),object :UploadUtils.OkGoCallBack{
            override fun onSuccess(uploadBean: UploadBean?) {
                showLoading(false)
                if(uploadBean!=null&&!uploadBean.result.url.isNullOrEmpty()){
                    var intent= Intent(this@LocalAppActivity,UploadActivity::class.java).apply {
                        putExtra("iconurl",uploadBean.result.url)
                        putExtra("path",fileBean.path)
                        putExtra("size",fileBean.size)

                    }
                    startActivity(intent)
                }
            }
            override fun onError(response: Response<String>?) {
                showLoading(false)
            }

            override fun onloadProgress(progress: Progress?) {
            }

        })

     //  appInfoViewModel?.uploadAppIcon(mapOf("lang" to AppLanguageUtils.getCurrentLang()),arrayListOf(File(iconpath)))
    }


}