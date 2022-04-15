package com.android.base.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.android.base.R;

public class IntroPagerAdapter extends PagerAdapter {


    public static View mCurrentView;
    private View.OnClickListener itemClick;


    public void setItemClick(View.OnClickListener itemClick){

        this.itemClick=itemClick;
    }

    private int[] titles = new int[] {
            R.string.intro_title_first_page,
            R.string.welcome_erc20_label_title,
            R.string.intro_title_second_page,
            R.string.intro_title_third_page,
    };
    private int[] messages = new int[] {
            R.string.intro_message_first_page,
            R.string.welcome_erc20_label_description,
            R.string.intro_message_second_page,
            R.string.intro_message_third_page,
    };
    private int[] images = new int[] {
            R.mipmap.app_start_1,
            R.mipmap.app_start_2,
            R.mipmap.app_start_3,
    };

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.layout_page_intro1, container, false);
//        ((TextView) view.findViewById(R.id.title)).setText(titles[position]);
//        ((TextView) view.findViewById(R.id.message)).setText(messages[position]);
        ((ImageView) view.findViewById(R.id.img)).setImageResource(images[position]);
        if(position==images.length-1){
            Button btn=view.findViewById(R.id.start);
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(this.itemClick);

        }

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View)object;
    }

    public View getPrimaryItem() {
        return mCurrentView;
    }


    public void onItemClick(View v){};

}
