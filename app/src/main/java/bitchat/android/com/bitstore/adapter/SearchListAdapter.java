package bitchat.android.com.bitstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.activitys.AppDetailActivity;
import bitchat.android.com.bitstore.bean.*;
import bitchat.android.com.bitstore.utils.AppLanguageUtils;
import bitchat.android.com.bitstore.utils.FileUtils;
import bitchat.android.com.bitstore.utils.GlideUtil;
import com.android.base.net.AppConst;
import com.android.base.utils.JsonUtil;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.recyclerview.LQRRecyclerView;
import com.orhanobut.logger.Logger;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxTool;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import org.litepal.crud.callback.SaveCallback;

import java.util.List;

public class SearchListAdapter extends LQRAdapterForRecyclerView {

    private int layout1= R.layout.item_search_list;
    private int layout2= R.layout.item_search_list1;
    private int layout3= R.layout.item_search_hot;
    private Context mContext;
    private EditText mSearch;


    public SearchListAdapter(Context context, List data) {
        super(context, data);
        this.mContext=context;
    }

    public SearchListAdapter(Context context, EditText search, List data) {
        super(context, data);
        this.mContext=context;
        this.mSearch=search;
    }

    public SearchListAdapter(Context context, List data, int defaultLayoutId) {
        super(context, data, defaultLayoutId);
        this.mContext=context;
    }



    @Override
    public int getItemViewType(int position) {



        if(getData()!=null&&getData().size()>0){

            if(getData().get(position) instanceof  HistoryBean)
                return layout2;
                else if(getData().get(position) instanceof SearchHot)
                return layout3;
                else
                return layout1;

        }

        return super.getItemViewType(position);
    }

    @Override
    public void convert(LQRViewHolderForRecyclerView helper, Object item, int position) {


        if(item instanceof HistoryBean){

            
            TextView tag=(TextView) helper.getView(R.id.tag);
            TagFlowLayout flowlayout=(TagFlowLayout)helper.getView(R.id.flowlayout);

            

            HistoryBean historyBean=(HistoryBean)item;

            if(historyBean.getShowtype().equals("0")){
                tag.setText(mContext.getString(R.string.str_history_search));
                setDrawableLocation(tag, R.drawable.history);


            }

            else if(historyBean.getShowtype().equals("1")){
                tag.setText(mContext.getString(R.string.str_hot_search));
                setDrawableLocation(tag, R.drawable.hot);
            }




            if(flowlayout!=null) {
                flowlayout.setAdapter(new TagAdapter<SearchHistory>(historyBean.getDatas()) {
                    @Override
                    public View getView(FlowLayout parent, int position, SearchHistory history) {

                        TextView tv = (TextView) View.inflate(mContext, R.layout.item_search_tab, null);
                        tv.setText(history.getHistory());
                        return tv;

                    }
                });


                flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                       String selstr= historyBean.getDatas().get(position).getHistory();
                       if(!TextUtils.isEmpty(selstr)){
                           mSearch.setText(selstr);
                           mSearch.setSelection(selstr.length());

                       }
                        return false;
                    }
                });


            }



        }else if(item instanceof SearchBean){

            SearchBean appInfo=(SearchBean)item;
            TextView title=helper.getView(R.id.title);
            TextView subtitle=helper.getView(R.id.subtitle2);
            TextView download=helper.getView(R.id.download);
            GlideUtil.loadImageView(mContext, AppConst.BASE_URL+appInfo.getAppicon(),helper.getView(R.id.icon));

//            if(AppLanguageUtils.getCurrentLang().equals("en")){//current lang is en
//                title.setText(appInfo.getAppname_en());
//            }else if(AppLanguageUtils.getCurrentLang().equals("zh")){//current lang is zh
//
//                title.setText(appInfo.getAppname_zh());
//            }else{
//                title.setText(appInfo.getAppname());
//            }

            title.setText(appInfo.getAppname());

//            if(TextUtils.isEmpty(title.getText().toString())){
//
//                if(!TextUtils.isEmpty(appInfo.getAppname_en())){
//                    title.setText(appInfo.getAppname_en());
//                }else{
//                    title.setText(appInfo.getAppname_zh());
//                }
//
//            }


            subtitle.setText(Formatter.formatFileSize(mContext, Long.parseLong(appInfo.getSize())));

            download.setOnClickListener(v->{

                
                SearchHistory searchHistory=new SearchHistory(RxDeviceTool.getAndroidId(v.getContext()),"0",title.getText().toString(), AppLanguageUtils.getCurrentLang());
                searchHistory.saveOrUpdateAsync("history=?",appInfo.getAppname()).listen(new SaveCallback() {
                    @Override
                    public void onFinish(boolean b) {
                        com.orhanobut.logger.Logger.d("----save results----->"+b);
                    }
                });
                
                Intent intent=new Intent(mContext, AppDetailActivity.class);
                HomeDataBean.DatasBean.AppsBean appsBean=new HomeDataBean.DatasBean.AppsBean();
                appsBean.setAppicon(appInfo.getAppicon());
                appsBean.setAppid(appInfo.getApp_id());
                appsBean.setAppname(title.getText().toString());
                appsBean.setAppsize(appInfo.getSize());
                appsBean.setDowncount(appInfo.getDowncount());
                appsBean.setVersion(appInfo.getVersion());
                appsBean.setDownloadurl(appInfo.getDownloadurl());
                intent.putExtra("appsbean",appsBean);
                intent.putExtra("source","1");
                mContext.startActivity(intent);


            });


        }
        else if(item instanceof SearchHot){

            SearchHot hot=(SearchHot)item;
            TextView tag=helper.getView(R.id.tag);
            LQRRecyclerView applists=helper.getView(R.id.applists);
            tag.setText(mContext.getString(R.string.str_hot_search));
            applists.setAdapter(new HDownloadListAdapter(mContext,hot.getData()));


        }

    }


    private void setDrawableLocation(TextView target, int resid){

        Drawable drawable = mContext.getResources().getDrawable(resid);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        target.setCompoundDrawables(drawable, null, null, null);

    }


}
