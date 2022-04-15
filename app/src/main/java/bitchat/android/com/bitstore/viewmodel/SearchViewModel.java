package bitchat.android.com.bitstore.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import bitchat.android.com.bitstore.bean.AppTabGroups;
import bitchat.android.com.bitstore.bean.HomeDataBean;
import bitchat.android.com.bitstore.bean.SearchBean;
import com.android.base.mvvm.viewmodel.BaseViewModel;
import com.android.base.net.helper.LzyResponse;
import com.android.mine_android.api.Api;
import org.jetbrains.annotations.NotNull;


import java.util.List;
import java.util.Map;

public class SearchViewModel extends BaseViewModel {


    private MutableLiveData<List<HomeDataBean.DatasBean.AppsBean>> hotLiveData;
    private MutableLiveData<List<SearchBean>> searchLiveData;


    public SearchViewModel(@NotNull Application application) {
        super(application);
    }

    public MutableLiveData<List<HomeDataBean.DatasBean.AppsBean>> onHot(){  //VerifyCodeBean
        if(hotLiveData==null){
            hotLiveData=new MutableLiveData<>();
        }
        return hotLiveData;
    }

    public MutableLiveData<List<SearchBean>> onSearch(){  //VerifyCodeBean
        if(searchLiveData==null){
            searchLiveData=new MutableLiveData<>();
        }
        return searchLiveData;
    }



    @SuppressLint("CheckResult")
    public void getHot(Map<String,Object> Param){
        setDisposable(Api.Companion.getHotSearch(Param).subscribe(this::onHotSuccess,this::onReqFail));
    }

    @SuppressLint("CheckResult")
    public void getSearch(Map<String,Object> Param){
        setDisposable(Api.Companion.getSearch(Param).subscribe(this::onSearchSuccess,this::onReqFail));
    }





    private void onHotSuccess(LzyResponse<List<HomeDataBean.DatasBean.AppsBean>> bean){
        if(bean!=null&&bean.getResult()!=null){
            onHot().postValue(bean.getResult());}
    }


    private void onSearchSuccess(LzyResponse<List<SearchBean>> bean){

            onSearch().postValue(bean.getResult());
    }

    private void onReqFail(Throwable e){
        onError(e);
    }




}
