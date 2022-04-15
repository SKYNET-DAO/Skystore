package bitchat.android.com.bitstore.activitys

import android.content.Intent
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bitchat.android.com.bitstore.R
import bitchat.android.com.bitstore.adapter.NinePicturesAdapter
import bitchat.android.com.bitstore.viewmodel.AppInfoViewModel
import com.android.base.activitys.WfcBaseActivity
import com.google.android.material.appbar.AppBarLayout
import com.lqr.adapter.LQRAdapterForRecyclerView
import com.lqr.adapter.LQRViewHolderForRecyclerView
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_upload.*
import android.R.attr.data
import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import bitchat.android.com.bitstore.bean.*
import bitchat.android.com.bitstore.utils.*
import bitchat.android.com.bitstore.utils.FileUtils
import bitchat.android.com.bitstore.viewmodel.CategaryViewModel
import bitchat.android.com.bitstore.viewmodel.JiaoshuiInfoViewModel
import bitchat.android.com.bitstore.wallet.BitUtil
import bitchat.android.com.bitstore.wallet.viewmodel.WalletViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.android.base.net.AppConst
import com.android.base.utils.*
import com.android.mine_android.api.Api
import com.android.wallet.constants.Constants
import com.lqr.imagepicker.ImagePicker
import com.lqr.imagepicker.bean.ImageItem
//import com.lzy.imagepicker.ImagePicker
//import com.lzy.imagepicker.bean.ImageItem
//import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.vondear.rxtool.RxAppTool
import org.bitcoinj.wallet.Wallet
import java.io.File

class JiaoUploadActivity:WfcBaseActivity() {

    var appInfoViewModel: JiaoshuiInfoViewModel?=null
    var categaryViewModel: CategaryViewModel?=null
    var walletViewModel: WalletViewModel?=null

    var title:TextView?=null
    var et_version:EditText?=null
    var fileBean:FileBean?=null
    var path:String?=null
    var size:Long?=null
    var name:String?=null
    var nineAdapter:NinePicturesAdapter?=null
    val IMAGE_PICKER = 120
    var categarys:MutableList<String>?=null
    var categaryIds= mutableListOf<Int>()
    var currentIndex=-1
    var uploadIconUrls= mutableListOf<String>()
    var apphash:String?=null
    var iconhash:String?=null
    var jiaoshuibean:JiaoshuiBean?=null
    var wallet: Wallet?=null

    override fun isAcceptAppManager()=true

    override fun contentLayout()= R.layout.activity_upload_jiaoshui

    override fun afterViews() {
        title=findViewById(R.id.title)
        et_version=findViewById(R.id.et_version)
        path=intent?.extras?.getString("path")
        size=intent?.extras?.getLong("size")
        name=intent?.extras?.getString("name")
        intent?.extras?.get("jiaoshuibean")?.apply{
            if(this!=null){
                jiaoshuibean=this as JiaoshuiBean
            }
        }

        Logger.e("-----sign----->${JsonUtil.toJson(jiaoshuibean)}")

        fileBean = FileBean(this, path, name,size!!, FileUtils.getFileIconByPath(path))
        nineAdapter= NinePicturesAdapter(this,1,NinePicturesAdapter.OnClickAddListener {

            Logger.e("-----NinePicturesAdapter----->$it")
            val intent = ImagePicker.picker().showCamera(false).buildPickIntent(this)
            startActivityForResult(intent, IMAGE_PICKER)
        })


        appInfoViewModel= ViewModelProviders.of(this).get(JiaoshuiInfoViewModel::class.java)
        categaryViewModel=ViewModelProviders.of(this).get(CategaryViewModel::class.java)
        walletViewModel=ViewModelProviders.of(this).get(WalletViewModel::class.java)
        categaryViewModel?.onCategary()?.observe(this, Observer {
            categarys= mutableListOf()
            it.forEach {
                categarys?.add(it.categoryName)
                categaryIds?.add(it.categoryId)
            }

        })

        walletViewModel?.walletLoadLiveData?.observe(this, Observer {
            wallet=it
        })

        appInfoViewModel?.onAddJiaoshui()?.observe(this, Observer {
            Logger.e("------onAddJiaoshui---->$it   ${jiaoshuibean?.id}")
            if(!TextUtils.isEmpty(it.hash)){
                ToastUtils.show(R.string.str_publish_success)
                finish()
            }

        })


        appInfoViewModel?.onCommitAppInfo()?.observe(this, Observer {
            Logger.e("------onCommitAppInfo---->$it")
            ToastUtils.show(getString(R.string.str_publish_success))
            finish()
        })

        appInfoViewModel?.progress()?.observe(this, Observer {
            showLoading(true)
        })



        appcategary.setOnClickListener {
            showCategaryListDialog(categarys!!)
        }
        findViewById<Button>(R.id.tv_commit).setOnClickListener {

            
            if(path?.isNullOrEmpty()!!){
                ToastUtils.show(getString(R.string.str_info_infect_base))
                return@setOnClickListener
            }
            
            if(nineAdapter?.data?.size==0){
                ToastUtils.show(getString(R.string.str_upload_jiaoshui_icon))
                return@setOnClickListener
            }
            

            if(uploadIconUrls.size==0){
                ToastUtils.show(getString(R.string.str_upload_jiaoshui_icon))
                return@setOnClickListener
            }
            
            if(et_content.text.isNullOrEmpty()){
                ToastUtils.show(getString(R.string.str_add_intruduce_info))
                return@setOnClickListener
            }
            if(et_version?.text.isNullOrEmpty()){
                ToastUtils.show(getString(R.string.str_jiaoshui_version_info))
                return@setOnClickListener
            }

            if(et_content.text.length<5){
                ToastUtils.show(getString(R.string.str_5_limit_intruduce))
                return@setOnClickListener
            }

            
            if(currentIndex==-1){
                ToastUtils.show(getString(R.string.str_sel_categary))
                return@setOnClickListener
            }

            var files= mutableListOf<File>()
            files.add(File(path))
            appInfoViewModel?.uploadAppIcon(this, AppConst.BASE_URL+ Api.upload,files,object : UploadUtils.OkGoCallBack{
                    override fun onSuccess(uploadBean: UploadBean?) {
                        showLoading(false)
                        Logger.e("-------uploadBean---->${JsonUtil.toJson(uploadBean)}")
                        if(!uploadBean?.result?.url.isNullOrEmpty()){
                            apphash=uploadBean?.result?.hash
                            ShowCommitAppDialog(uploadBean?.result?.url)
                        }
                    }
                    override fun onError(response: Response<String>?) {
                        showLoading(false)
                    }

                    override fun onloadProgress(progress: Progress?) {
                        dialog.setContent(getString(R.string.str_uploading)+ (100 * progress?.currentSize!!/progress?.totalSize).toInt()+"%")
                        dialog.setCancelable(false)
                    }

                })

        }

        nineAdapter?.setOnRemoveListener {
            Logger.e("-------setOnRemoveListener---->$it")
            uploadIconUrls.removeAt(it)
        }

        gridview.adapter = nineAdapter

        initData()
    }


    fun initData(){


        var map= mutableMapOf<String,Any>("lang" to  AppLanguageUtils.getCurrentLang())
        categaryViewModel?.getCategary(map)


        icon.setImageResource(fileBean?.iconId!!)
        title?.text=fileBean?.title
        subtitle2?.text=FileSizeUtil.FormetFileSize(size!!)
        walletViewModel?.walletLoadLiveData?.loadWallet()


    }


     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         super.onActivityResult(requestCode, resultCode, data)
         if (requestCode == IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
             if (data != null) {
                 val images = data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>
                 var paths= mutableListOf<String>()
                 images.forEach { paths.add(it.path) }
                 nineAdapter?.addAll(paths)
                 startUploadIcoin(paths)
             } else {
                 Toast.makeText(this, getString(R.string.str_no_data), Toast.LENGTH_SHORT).show()
             }
         }
    }

    fun showCategaryListDialog(mutableList: MutableList<String>){

        var dialog = MaterialDialog.Builder(this)
            .title(R.string.str_select_categary)
            .items(mutableList)
            .itemsCallback { dialog, itemView, position, text ->
                run {
                    appcategary.setRightText(text.toString())
                    currentIndex=position
                }
            }
            .positiveText(R.string.str_ok)
            .positiveColor(getColor(R.color.colorPrimary))
            .negativeText(R.string.str_cancel)
                .build()
        dialog.show()


    }


    fun startUploadIcoin(paths:List<String>){
        var files= mutableListOf<File>()
        paths.forEach { files.add(File(it)) }
        appInfoViewModel?.uploadAppIcon(this,
            AppConst.BASE_URL+ Api.upload,files,object : UploadUtils.OkGoCallBack{
                override fun onSuccess(uploadBean: UploadBean?) {
                    showLoading(false)
                    Logger.e("-------uploadBean---->${JsonUtil.toJson(uploadBean)}")
                    if(uploadBean!=null&&!uploadBean.result.url.isNullOrEmpty()){
                        uploadIconUrls.add(uploadBean?.result.url)
                        iconhash=uploadBean?.result.hash
                        Logger.e("-------uploadIconUrls---->${uploadIconUrls.size}")
                    }
                }
                override fun onError(response: Response<String>?) {
                    showLoading(false)
                }

                override fun onloadProgress(progress: Progress?) {
                }

            })

    }

    
    fun ShowCommitAppDialog(appuploadurl:String?){

        if(apphash.isNullOrEmpty()){
            ToastUtils.show(R.string.str_upload_jiaoshui_before)
            return
        }
        if(iconhash.isNullOrEmpty()){
            ToastUtils.show(R.string.str_upload_jiaoshui_icon)
            return
        }


        var commitAppBean = CommitAppBean()
        val root = BitUtil.getMasterAddress1(this.wallet!!, Constants.NETWORK_PARAMETERS)

        commitAppBean.account =root
        commitAppBean.appname = title?.text.toString() 
        commitAppBean.version = et_version?.text.toString() 
        commitAppBean.categary = categaryIds?.get(currentIndex)!! 
        commitAppBean.appicon = uploadIconUrls[0] 
        commitAppBean.size = size.toString()  
        commitAppBean.downloadurl = appuploadurl 
        commitAppBean.defaultlang = AppLanguageUtils.getCurrentLang()
        commitAppBean.update = et_content.text.toString()
        commitAppBean.apphash=apphash
        commitAppBean.iconhash=iconhash
        commitAppBean.status=0
//        commitAppBean.packagename =appinfo?.packagename

        //set sign
        if(jiaoshuibean!=null){
            commitAppBean.sign=jiaoshuibean?.sign
        }else{
            commitAppBean.sign=MD5Util.getMD5String(commitAppBean.appname)
        }
        Logger.e("-------sign-->${commitAppBean.sign}")

        //set intruduce img
        setIntruduce(commitAppBean)
        setLang(commitAppBean)


        var dialog = MaterialDialog.Builder(this)
            .title(R.string.str_js_app_info_commit)
            .content(R.string.str_js_app_info_commit_true)
            .positiveText(R.string.str_jiaoshui_upload)
            .positiveColor(getColor(R.color.colorPrimary))
            .onPositive { dialog, which ->
                run {
                    
                    appInfoViewModel?.addJiaoshui(JsonUtil.toJson(commitAppBean))
                }
            }
            .negativeText(R.string.str_cancel)

            .build()
        dialog.show()


    }

    fun setIntruduce(commitAppBean: CommitAppBean){
        val stringBuilder = StringBuilder()
        for (item in uploadIconUrls) {
            if (uploadIconUrls.indexOf(item) == uploadIconUrls.size - 1)
                stringBuilder.append(item)
            else
                stringBuilder.append(item + ",")
        }

        commitAppBean.introduce =stringBuilder.toString()
        Logger.e("-------setIntruduce-->${stringBuilder.toString()}")

    }

    fun setLang(commitAppBean: CommitAppBean){
       // commitAppBean.appname_zh=AppLanguageUtils.getCurrentLang()
        commitAppBean.lang = AppLanguageUtils.getCurrentLang()
    }

}