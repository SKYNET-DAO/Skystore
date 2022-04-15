package bitchat.android.com.bitstore.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import bitchat.android.com.bitstore.bean.CategaryBean;
import bitchat.android.com.bitstore.bean.CategaryListBean;
import bitchat.android.com.bitstore.bean.HomeDataBean;
import com.android.base.mvvm.viewmodel.BaseViewModel;
import com.android.base.net.helper.LzyResponse;
import com.android.mine_android.api.Api;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class CategaryViewModel extends BaseViewModel {


    private MutableLiveData<List<CategaryBean>> onCategaryLiveData;
    private MutableLiveData<List<HomeDataBean.DatasBean.AppsBean>> onCategaryListLiveData;

    public CategaryViewModel(@NotNull Application application) {
        super(application);
    }

    public MutableLiveData<List<CategaryBean>> onCategary(){  //VerifyCodeBean
        if(onCategaryLiveData==null){
            onCategaryLiveData=new MutableLiveData<>();
        }
        return onCategaryLiveData;
    }


    public MutableLiveData<List<HomeDataBean.DatasBean.AppsBean>> onCategaryList(){  //VerifyCodeBean
        if(onCategaryListLiveData==null){
            onCategaryListLiveData=new MutableLiveData<>();
        }
        return onCategaryListLiveData;
    }

    @SuppressLint("CheckResult")
    public void getCategary(Map<String,Object> Param){
        setDisposable(Api.Companion.getCategary(Param).subscribe(this::onCategarySuccess,this::onReqFail));
    }

    @SuppressLint("CheckResult")
    public void getCategaryList(Map<String,Object> Param){
        getProgress().postValue(true);
        setDisposable(Api.Companion.getCategaryList(Param).subscribe(this::onCategaryListSuccess,this::onReqFail));
    }



    private void onCategarySuccess(LzyResponse<List<CategaryBean>> bean){
        if(bean!=null&&bean.getResult()!=null){
            onCategary().postValue(bean.getResult());}
    }

    private void onCategaryListSuccess(LzyResponse<List<HomeDataBean.DatasBean.AppsBean>> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.getResult()!=null){
            onCategaryList().postValue(bean.getResult());}
    }

    private void onReqFail(Throwable e){
        onError(e);
    }




}
