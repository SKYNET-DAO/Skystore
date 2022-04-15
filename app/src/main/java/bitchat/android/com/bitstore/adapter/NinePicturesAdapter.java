package bitchat.android.com.bitstore.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.utils.GlideUtil;
import bitchat.android.com.bitstore.utils.ViewHolderUtil;
import com.bumptech.glide.Glide;

import java.util.List;


public class NinePicturesAdapter extends BaseAblistViewAdapter<String> {
    private boolean showAdd = true;
    private int picturnNum = 0;
    private boolean isDelete = false;
    private OnClickAddListener onClickAddListener;
    private boolean isAdd=true;



    private RemoveListener removeListener;

    public void setOnRemoveListener(RemoveListener removeListener){

        this.removeListener=removeListener;
    }
    public interface RemoveListener{
        void onRemove(int position);
    }


    public NinePicturesAdapter(Context context, int picturnNum, OnClickAddListener onClickAddListener) {
        super(context);
        this.picturnNum = picturnNum;
        this.onClickAddListener = onClickAddListener;
        showAdd();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_photo, parent, false);
        }
        final ImageView imageView = ViewHolderUtil.get(convertView, R.id.img_photo);
        ImageView imgDelete = ViewHolderUtil.get(convertView, R.id.img_delete);
        final String url = getData().get(position);
        
        if (TextUtils.isEmpty(url) && showAdd) {
//            ImageLoaderUtils.display(mContext, imageView, R.drawable.addphoto);
            GlideUtil.loadImageView(mContext,R.drawable.addphoto,imageView);
            imgDelete.setVisibility(View.GONE);
        } else {
            imgDelete.setVisibility(View.VISIBLE);
            GlideUtil.loadImageView(mContext,url,imageView);

        }

        autoHideShowAdd();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if (TextUtils.isEmpty(url)) {
                    if (onClickAddListener != null) {
                        onClickAddListener.onClickAdd(position);
                    }
                } else {
                    
                   // BigImagePagerActivity.startImagePagerActivity((Activity) mContext, getData(), position);
                }
            }
        });
        
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(position);
                if(removeListener!=null)
                    removeListener.onRemove(position);
                if (!isDelete && getCount() < 1) {
                    add("");
                    isDelete = true;

                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public void setData(List<String> d) {
        boolean hasAdd=false;
        for (int i = 0; i < d.size(); i++) {
            if(TextUtils.isEmpty(d.get(i))){
                hasAdd=true;
                break;
            }
        }
        super.setData(d);
        if(!hasAdd){
            showAdd();
        }
    }

    @Override
    public void addAll(List<String> d) {
        if(isAdd){
            HideAdd();
        }
        super.addAll(d);
        showAdd();
    }


    public void autoHideShowAdd(){
        int lastPosition=getData().size()-1;
            if(lastPosition==picturnNum&&getData().get(lastPosition)!=null&& TextUtils.isEmpty(getData().get(lastPosition))){
                getData().remove(lastPosition);
                isAdd=false;
                notifyDataSetChanged();
            }else if(!isAdd){
                showAdd();
            }
    }

    public void HideAdd(){
        int lastPosition=getData().size()-1;
        if(getData().get(lastPosition)!=null&& TextUtils.isEmpty(getData().get(lastPosition))){
            getData().remove(lastPosition);
            isAdd=false;
            notifyDataSetChanged();
        }
    }

    public void showAdd(){
        if(getData().size()<picturnNum){
            addAt(getData().size(),"");
            isAdd=true;
            notifyDataSetChanged();
        }
    }


   public int getPhotoCount(){
       return isAdd==true?getCount()-1:getCount();
   }


    public interface OnClickAddListener {
        void onClickAdd(int positin);
    }

}