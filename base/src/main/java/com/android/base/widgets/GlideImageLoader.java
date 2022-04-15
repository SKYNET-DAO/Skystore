//package com.android.base.widgets;
//
//import android.app.Activity;
//import android.net.Uri;
//import android.widget.ImageView;
//import com.android.base.glide.GlideApp;
//import com.lzy.imagepicker.loader.ImageLoader;
//
//import java.io.File;
//
//public class GlideImageLoader implements ImageLoader {
//    @Override
//    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
//
//        GlideApp.with(activity)
//                .load(Uri.fromFile(new File(path)))
////                .placeholder(R.mipmap.default_image)
////                .error(R.mipmap.default_image)
//                .centerCrop()
//                .into(imageView);
//
//
//
//    }
//
//    @Override
//    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
//        GlideApp.with(activity)
//                .load(Uri.fromFile(new File(path)))
////                .placeholder(R.mipmap.default_image)
////                .error(R.mipmap.default_image)
//                .centerCrop()
//                .into(imageView);
//    }
//
//    @Override
//    public void clearMemoryCache() {
//
//    }
//}
