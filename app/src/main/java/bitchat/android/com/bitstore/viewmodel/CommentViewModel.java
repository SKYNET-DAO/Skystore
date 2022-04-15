package bitchat.android.com.bitstore.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import bitchat.android.com.bitstore.bean.AppDetailBean;
import bitchat.android.com.bitstore.bean.CommentBean;
import com.android.base.mvvm.viewmodel.BaseViewModel;
import com.android.base.net.helper.LzyResponse;
import com.android.mine_android.api.Api;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class CommentViewModel extends BaseViewModel {


    private MutableLiveData<List<CommentBean>> allCommentLiveData;

    public CommentViewModel(@NotNull Application application) {
        super(application);
    }

    public MutableLiveData<List<CommentBean>> onAppComment(){  //VerifyCodeBean
        if(allCommentLiveData==null){
            allCommentLiveData=new MutableLiveData<>();
        }
        return allCommentLiveData;
    }



    @SuppressLint("CheckResult")
    public void getAllComment(Map<String,Object> Param){
        setDisposable(Api.Companion.getAllComment(Param).subscribe(this::onCommentSuccess,this::onReqFail));
    }





    private void onCommentSuccess(LzyResponse<List<CommentBean>> bean){
        if(bean!=null&&bean.getData()!=null){
            onAppComment().postValue(bean.getData());}
    }

    private void onReqFail(Throwable e){
        onError(e);
    }




}
