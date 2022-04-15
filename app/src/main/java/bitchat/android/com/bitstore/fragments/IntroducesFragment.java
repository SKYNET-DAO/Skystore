package bitchat.android.com.bitstore.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.bean.AppDetailBean;
import bitchat.android.com.bitstore.utils.AppLanguageUtils;
import bitchat.android.com.bitstore.utils.GlideUtil;
import bitchat.android.com.bitstore.viewmodel.AppInfoViewModel;
import com.android.base.net.AppConst;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.recyclerview.LQRRecyclerView;
import com.orhanobut.logger.Logger;

import java.util.*;


public class IntroducesFragment extends Fragment {


    private List<String> uris=new ArrayList<>();
    private LQRRecyclerView introduces;
    private LQRAdapterForRecyclerView<String> adapter;
    private TextView introduce_txt;
    private String appid;
    private AppInfoViewModel appInfoViewModel;


    public IntroducesFragment(String appid){
        this.appid=appid;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getActivity()).inflate( R.layout.fragment_introduce,container,false);
        initView(view);
        return view;
    }


    protected void initView(View view) {
        introduces=view.findViewById(R.id.introduces);
        introduce_txt=view.findViewById(R.id.simple_introduce);
        appInfoViewModel= ViewModelProviders.of(this).get(AppInfoViewModel.class);

        adapter=new LQRAdapterForRecyclerView<String>(getActivity()
                ,new ArrayList<>(), R.layout.item_introduce_list) {
            @Override
            public void convert(LQRViewHolderForRecyclerView helper, String item, int position) {

                ImageView imv=helper.getView(R.id.imv);
                GlideUtil.loadImageView(getActivity(),item,imv );

            }
        };
        introduces.setAdapter(adapter);
        appInfoViewModel.onAppDetail().observe(this, o -> {
            Logger.e("-----Fetch detail data---->"+o.toString());
            initData(o);

        });


        Map<String,Object> map=new HashMap<>();
        map.put("appid",appid);
        map.put("source","0");
        map.put("lang", AppLanguageUtils.getCurrentLang());
        appInfoViewModel.getAppDetail(map);
    }




    public void initData(AppDetailBean bean){

        if(bean!=null&&!TextUtils.isEmpty(bean.getIntroduce())){
            String[] splits=bean.getIntroduce().split(",");
            for(int i=0;i<splits.length;i++){
                splits[i]= AppConst.BASE_URL +splits[i];

            }
            adapter.addNewData(Arrays.asList(splits));
            introduce_txt.setText(bean.getDescribe());
        }



    }




}
