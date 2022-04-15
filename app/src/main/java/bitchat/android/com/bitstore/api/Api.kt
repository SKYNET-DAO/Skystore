package com.android.mine_android.api

import bitchat.android.com.bitstore.bean.*
import com.android.base.net.AppConst
import com.android.base.net.api.ImRetrofitService
import com.android.base.net.helper.JsonConvert
import com.android.base.net.helper.LzyResponse
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okrx2.adapter.ObservableBody
import io.reactivex.Observable
import java.util.*
import bitchat.android.com.bitstore.bean.HomeDataBean.DatasBean.AppsBean
import bitchat.android.com.bitstore.constants.StoreConfig.remote
import bitchat.android.com.bitstore.utils.AppLanguageUtils
import com.android.base.utils.ManifestUtil
import com.lzy.okgo.model.HttpParams
import com.orhanobut.logger.Logger
import com.vondear.rxtool.RxAppTool
import java.io.File

class Api : ImRetrofitService() {
    companion object {

        var randomApi = "$remote/api/getGatewayIP"

        val home = "/api/getIndexInfo" 

        val moredata = "/api/getAppList"
        val detail = "/api/getAppDetails"
        val info ="/ipfs/appVersionUpgrade"
        val comment = "/api/getCommentList"

        val categary =  "/api/getCategory"
        val hot = "/api/getIndexColumnlist"

        val search = "/api/searchAppList"

        val categarylist = "/api/searchCategoryList"

        val commitComment = "/api/commitComment"
        val selfapplist="/api/getSelfInfo"

        val upload="/ipfs/uploads"

        val commitapp="/api/commitinfo"

        val drawdownapp="/api/delNewApp"

        val version="/ipfs/appInfo"

        val jiaoshuilist="/api/plugin/getPluginList"
        val deljiaoshui="/api/plugin/delNewPlugin"
        val addjiaoshui="/api/plugin/addPlugin"


        fun addJiaoshui(json:String): Observable<LzyResponse<DrawAppBean>>? {
            return OkGo.post<LzyResponse<DrawAppBean>>(AppConst.BASE_URL +addjiaoshui)
                .upJson(json)
                .converter(object : JsonConvert<LzyResponse<DrawAppBean>>() {

                }).adapt(ObservableBody()) }


        fun getJiaoshuiList(parameter: Map<String, Any>): Observable<LzyResponse<List<JiaoshuiBean>>>? {
            return OkGo.post<LzyResponse<List<JiaoshuiBean>>>(AppConst.BASE_URL +jiaoshuilist)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<List<JiaoshuiBean>>>() {

                }).adapt(ObservableBody())

        }



        fun delJiaoshui(parameter: Map<String, Any>): Observable<LzyResponse<DrawAppBean>>? {
            return OkGo.post<LzyResponse<DrawAppBean>>(AppConst.BASE_URL +deljiaoshui)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<DrawAppBean>>() {

                }).adapt(ObservableBody())

        }


        fun updateApp(parameter: Map<String, Any>): Observable<LzyResponse<AppInfoBean>>? {
            return OkGo.post<LzyResponse<AppInfoBean>>(AppConst.BASE_URL +version)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<AppInfoBean>>() {

                }).adapt(ObservableBody())

        }


        fun drawdownApp(parameter: Map<String, Any>): Observable<LzyResponse<DrawAppBean>>? {
            return OkGo.post<LzyResponse<DrawAppBean>>(AppConst.BASE_URL +drawdownapp)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<DrawAppBean>>() {

                }).adapt(ObservableBody())

        }



        fun uploadAppFile(parameter: Map<String, Any>,files: List<File>): Observable<LzyResponse<UploadBean>>? {

           return OkGo.post<LzyResponse<UploadBean>>(AppConst.BASE_URL + upload)
                .upJson(Gson().toJson(parameter))
                .isMultipart(true)
                .addFileParams("file",files)
                .converter(object : JsonConvert<LzyResponse<UploadBean>>() {

                }).adapt(ObservableBody())

        }


        fun commitApp2Store(json:String): Observable<LzyResponse<Any>>? {
            return OkGo.post<LzyResponse<Any>>(AppConst.BASE_URL + commitapp)
                .upJson(json)
                .params("lang", AppLanguageUtils.getCurrentLang())
                .converter(object : JsonConvert<LzyResponse<Any>>() {

                }).adapt(ObservableBody())

        }

        fun getSelfApplist(parameter: Map<String, Any>): Observable<LzyResponse<List<UploadListBean>>>? {
            //defined httpparams
            return OkGo.post<LzyResponse<List<UploadListBean>>>(AppConst.BASE_URL + selfapplist)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<List<UploadListBean>>>() {

                }).adapt(ObservableBody())
        }


        fun getBanner(parameter: Map<String, String>): Observable<LzyResponse<HomeDataBean>>? {
            //defined httpparams
            Logger.e("---------randomIP-getBanner--->${AppConst.BASE_URL}")
            return OkGo.post<LzyResponse<HomeDataBean>>(AppConst.BASE_URL + home)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<HomeDataBean>>() {

                }).adapt(ObservableBody())
        }



        fun getHotSearch(parameter: Map<String, Any>): Observable<LzyResponse<List<HomeDataBean.DatasBean.AppsBean>>>? {
            //defined httpparams
            return OkGo.post<LzyResponse<List<HomeDataBean.DatasBean.AppsBean>>>(AppConst.BASE_URL + hot)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<List<HomeDataBean.DatasBean.AppsBean>>>() {

                }).adapt(ObservableBody())
        }


        fun getCategaryList(parameter: Map<String, Any>): Observable<LzyResponse<List<HomeDataBean.DatasBean.AppsBean>>>? {
            //defined httpparams
            return OkGo.post<LzyResponse<List<HomeDataBean.DatasBean.AppsBean>>>(AppConst.BASE_URL + categarylist)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<List<HomeDataBean.DatasBean.AppsBean>>>() {

                }).adapt(ObservableBody())
        }





        fun getSearch(parameter: Map<String, Any>): Observable<LzyResponse<List<SearchBean>>>? {
            //defined httpparams
            return OkGo.post<LzyResponse<List<SearchBean>>>(AppConst.BASE_URL + search)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<List<SearchBean>>>() {

                }).adapt(ObservableBody())
        }



        fun getAllComment(parameter: Map<String, Any>): Observable<LzyResponse<List<CommentBean>>>? {
            //defined httpparams
            return OkGo.post<LzyResponse<List<CommentBean>>>(AppConst.BASE_URL + comment)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<List<CommentBean>>>() {

                }).adapt(ObservableBody())
        }


        fun getCategary(parameter: Map<String, Any>): Observable<LzyResponse<List<CategaryBean>>>? {
            //defined httpparams
            Logger.e("--------AppConst.BASE_URL + categary--->"+AppConst.BASE_URL)
            return OkGo.post<LzyResponse<List<CategaryBean>>>(AppConst.BASE_URL + categary)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<List<CategaryBean>>>() {

                }).adapt(ObservableBody())
        }



        fun getAppDetail(parameter: Map<String, Any>): Observable<LzyResponse<AppDetailBean>>? {
            //defined httpparams
            return OkGo.post<LzyResponse<AppDetailBean>>(AppConst.BASE_URL + detail)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<AppDetailBean>>() {

                }).adapt(ObservableBody())
        }


        fun getAppInfo(parameter: Map<String, Any>): Observable<LzyResponse<AppInfoBean>>? {

            //defined httpparams
            Logger.e("-------getappinfo--->"+AppConst.BASE_URL + version)
            return OkGo.post<LzyResponse<AppInfoBean>>(AppConst.BASE_URL + version)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<AppInfoBean>>() {

                }).adapt(ObservableBody())
        }

        fun getRandomIp(): Observable<LzyResponse<RandomIp>>? {
            Logger.e("-------getRandomIp111----->$randomApi")
            return OkGo.get<LzyResponse<RandomIp>>(randomApi)
                .converter(object : JsonConvert<LzyResponse<RandomIp>>() {
                }).adapt(ObservableBody())
        }

        fun getRandomIp(reseturl:String): Observable<LzyResponse<RandomIp>>? {

            var randomApi = "http://$reseturl/api/getGatewayIP"
            Logger.e("-------randomApi---->$randomApi")
            //defined httpparams
            return OkGo.get<LzyResponse<RandomIp>>(randomApi)
                .converter(object : JsonConvert<LzyResponse<RandomIp>>() {

                }).adapt(ObservableBody())
        }



        fun getMoreData(parameter: Map<String, Any>): Observable<LzyResponse<List<AppsBean>>>? {
            //defined httpparams
            return OkGo.post<LzyResponse<List<AppsBean>>>(AppConst.BASE_URL + moredata)
                .upJson(Gson().toJson(parameter))
                .converter(object : JsonConvert<LzyResponse<List<AppsBean>>>() {})
                .adapt(ObservableBody())
        }


    }


}
