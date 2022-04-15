package bitchat.android.com.bitstore.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.bean.FileBean;
import bitchat.android.com.bitstore.utils.AppUtils;
import com.android.base.utils.FileSizeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.ViewHolder> {

    private List<FileBean> mList;
    private Context mContext;

    private Map<Integer, Boolean> map = new HashMap<>();
    private boolean onBind;
    private int checkedPosition = -1;

    public CheckBoxAdapter(Context context, List<FileBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_app_select, parent, false);
        return new ViewHolder(view);
    }

    
    public int getCheckedPosition() {
        return checkedPosition;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {



        //icon
        AppUtils.AppInfo info=mList.get(position).appInfo;
        if(info!=null){
            holder.icon.setImageDrawable(info.getIcon());
        }else{
            return; }


        //title
        holder.title.setText(mList.get(position).title);
        //subtitle
        holder.subtitle2.setText(FileSizeUtil.FormetFileSize(mList.get(position).size));
        //checkbox
        holder.mCheckBox.setChecked(mList.get(position).ischeck);
        holder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public int getSelectSize(){
        return map.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView subtitle2;
        CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            icon=itemView.findViewById(R.id.icon);
            mCheckBox =  itemView.findViewById(R.id.checkbox);
            title = itemView.findViewById(R.id.title);
            subtitle2 = itemView.findViewById(R.id.subtitle2);
        }
    }

}
