package bitchat.android.com.bitstore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.bean.CommentBean;
import bitchat.android.com.bitstore.viewmodel.CommentViewModel;
import com.android.base.utils.JsonUtil;
import com.android.base.utils.TimeUtils;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.recyclerview.LQRRecyclerView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentFragment extends Fragment {

    private String appid;
    private LQRRecyclerView listcomment;
    private LQRAdapterForRecyclerView<CommentBean> adapter;

    private CommentViewModel commentViewModel;
    public CommentFragment(String appid) {
        this.appid = appid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getActivity()).inflate( R.layout.fragment_coment,container,false);
        initView(view);
        return view;
    }
    private void initView(View view){
        commentViewModel= ViewModelProviders.of(this).get(CommentViewModel.class);
        listcomment=  view.findViewById(R.id.listcomment);

        listcomment.setAdapter(adapter=new LQRAdapterForRecyclerView<CommentBean>(getActivity(),new ArrayList(),R.layout.item_coment) {
            @Override
            public void convert(LQRViewHolderForRecyclerView helper, CommentBean item, int position) {

                TextView account= helper.getView(R.id.nickname);
                TextView source=helper.getView(R.id.phone_type);
                TextView time=helper.getView(R.id.time);
                TextView content=helper.getView(R.id.content);
                TextView zan=helper.getView(R.id.zan);
                TextView commentcount=helper.getView(R.id.commentcount);
                account.setText(item.getAccount());
                source.setText(item.getModel());
                time.setText(TimeUtils.getFormatTimeyyyyMMddHHmm(item.getUtime()));
                content.setText(item.getCommenttext());
                zan.setText(item.getPraisecount()+"");
                zan.setOnClickListener(v->{

                });

            }


        });
        
        commentViewModel.onAppComment().observe(this,o->{

            Logger.e("----Comments---->"+ JsonUtil.toJson(o));
            adapter.setData(o);
        });

        Map<String,Object> map=new HashMap<>();
        map.put("appid",appid);
        commentViewModel.getAllComment(map);

    }
}
