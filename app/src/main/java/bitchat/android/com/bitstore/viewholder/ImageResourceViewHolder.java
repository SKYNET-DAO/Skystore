package bitchat.android.com.bitstore.viewholder;

import android.view.View;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.view.CornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.zhpan.bannerview.holder.ViewHolder;


public class ImageResourceViewHolder implements ViewHolder<String> {

    private int roundCorner;

    public ImageResourceViewHolder(int roundCorner) {
        this.roundCorner = roundCorner;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_slide_mode;
    }

    @Override
    public void onBind(View itemView, String data, int position, int size) {
        CornerImageView imageView = itemView.findViewById(R.id.banner_image);
//        imageView.setImageResource(data);
        imageView.setRoundCorner(roundCorner);
        Glide.with(itemView.getContext()).load(data).into(imageView);
    }

}
