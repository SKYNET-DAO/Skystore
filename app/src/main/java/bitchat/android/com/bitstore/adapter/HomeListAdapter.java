package bitchat.android.com.bitstore.adapter;

import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.bean.HomeDataBean;
import bitchat.android.com.bitstore.utils.AppLanguageUtils;
import bitchat.android.com.bitstore.viewmodel.HomeViewModel;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.recyclerview.LQRRecyclerView;
import com.orhanobut.logger.Logger;
import xiao.free.horizontalrefreshlayout.HorizontalRefreshLayout;
import xiao.free.horizontalrefreshlayout.RefreshCallBack;
import xiao.free.horizontalrefreshlayout.refreshhead.LoadingRefreshHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeListAdapter extends LQRAdapterForRecyclerView {

    private int layout1= R.layout.item_home_list;
    private int layout2= R.layout.item_home_list1;
    private HomeViewModel homeViewModel;
    private int currentPage=1;
//    private int layout3= R.layout.item_home_bottom;
    private FragmentActivity mContext;
    private int type;
    private LQRRecyclerView currentrecycleview;
    private HorizontalRefreshLayout currenthorizontalRefreshLayout;
    private Map<Integer,Integer> pageindexMap=new HashMap();

    public HomeListAdapter(FragmentActivity activity, List data) {
        super(activity, data);
        this.mContext=activity;
        homeViewModel= ViewModelProviders.of(mContext).get(HomeViewModel.class);
        homeViewModel.homeLoadmoreMutableLiveData().observe(mContext, appsBeans -> {
            currenthorizontalRefreshLayout.onRefreshComplete();
            ((HDownloadListAdapter)currentrecycleview.getAdapter()).addMoreData(appsBeans);

        });



    }

    public HomeListAdapter(FragmentActivity context, List data, int defaultLayoutId) {
        super(context, data, defaultLayoutId);
        this.mContext=context; }




    @Override
    public int getItemViewType(int position) {


        if(getData()!=null){
            if(getData().get(position) instanceof HomeDataBean.DatasBean){
                HomeDataBean.DatasBean bean=  (HomeDataBean.DatasBean)getData().get(position);
                if(bean.getShowtype()==0)
                    return layout1;
                else if(bean.getShowtype()==1){
                    return layout2;
                }


            }
            }


        return super.getItemViewType(position);
    }




    @Override
    public void convert(LQRViewHolderForRecyclerView helper, Object item, int position) {

        if(item instanceof HomeDataBean.DatasBean){
            if(((HomeDataBean.DatasBean) item).getShowtype()==1)return;

            HomeDataBean.DatasBean bean=  (HomeDataBean.DatasBean)item;

            
            TextView tag=(TextView) helper.getView(R.id.tag);
            tag.setText(bean.getLabel());

            if(bean.getShowtype()==0){
                LQRRecyclerView  applists=helper.getView(R.id.applists);

                if(applists!=null)applists.setAdapter(new HDownloadListAdapter(mContext,bean.getApps()));
                HorizontalRefreshLayout  horizontalRefreshLayout= helper.getView(R.id.refresh);
                horizontalRefreshLayout.setRefreshCallback(new RefreshCallBack() {
                    @Override
                    public void onLeftRefreshing() {

                    }

                    @Override
                    public void onRightRefreshing() {
                        int currentpage=pageindexMap.get(bean.getColumnId());
                        pageindexMap.put(bean.getColumnId(),currentpage+1);
                        loadMoreData(bean.getColumnId(),applists,horizontalRefreshLayout);
                    }
                });
                horizontalRefreshLayout.setRefreshHeader(new LoadingRefreshHeader(mContext), HorizontalRefreshLayout.RIGHT);


            }

            pageindexMap.put(bean.getColumnId(),currentPage);

        }





    }




    private void loadMoreData(int id,LQRRecyclerView lqrRecyclerViewv,HorizontalRefreshLayout horizontalRefreshLayout){

        Logger.e("-----loadMoreData-id--->"+id+" "+pageindexMap.get(id));
        currentrecycleview=lqrRecyclerViewv;
       currenthorizontalRefreshLayout=horizontalRefreshLayout;
        Map morparam=new HashMap();
        morparam.put("lang", AppLanguageUtils.getCurrentLang());
        morparam.put("page",pageindexMap.get(id));
        morparam.put("pageNum",50);
        morparam.put("columnId",id);
        homeViewModel.getMoreData(morparam);




    }

}

