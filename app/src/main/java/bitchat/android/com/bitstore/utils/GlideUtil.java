package bitchat.android.com.bitstore.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import bitchat.android.com.bitstore.R;
import com.android.base.glide.GlideApp;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import jp.wasabeef.glide.transformations.BitmapTransformation;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


@GlideModule
public final class GlideUtil extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "glide_cache", 100 * 1024 * 1024));
    }




    public static void loadImageView(Context mContext, String path, ImageView mImageView) {
        GlideApp.with(mContext).load(path).transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }


    public static void loadImageViewWH(Context mContext, String path, ImageView mImageView, int w, int h) {
        GlideApp.with(mContext).load(path)
                .override(w,h)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
    }


    public static void loadImageView(Context mContext, @DrawableRes int res, ImageView mImageView) {
        GlideApp.with(mContext).load(res).into(mImageView);
    }

    public static void loadImageView(Context mContext, String path, ImageView mImageView, DiskCacheStrategy strategy) {
        GlideApp.with(mContext).load(path).diskCacheStrategy(strategy).into(mImageView);
    }

    public static void loadImageViewCenterCrop(Context mContext, String path, ImageView mImageView) {
        GlideApp.with(mContext).load(path).centerCrop().into(mImageView);
    }


    public static void loadImageViewWithTransform(Context mContext, String path, BitmapTransformation transformation, ImageView mImageView) {
        GlideApp.with(mContext).load(path).transforms(transformation).into(mImageView);
    }


    public static void loadImageViewWithTransformForPlaceholder(Context mContext, String path, BitmapTransformation transformation, int placeholderid, ImageView mImageView) {
        GlideApp.with(mContext).load(path).placeholder(placeholderid).transforms(transformation).into(mImageView);
    }





    public static void loadImageViewWithTransform(Context mContext, Integer resid, RoundedCornersTransformation transformation, ImageView mImageView) {
        GlideApp.with(mContext).load(resid).centerCrop().transforms(transformation).into(mImageView);
    }


    public static void loadImageViewWithTransform(Context mContext, String path, RoundedCornersTransformation transformation, ImageView mImageView) {
        GlideApp.with(mContext).load(path).centerCrop().transforms(transformation).into(mImageView);
    }


    public static void loadImageViewWithTransform(Context mContext, int path, BlurTransformation transformation, ImageView mImageView) {
        GlideApp.with(mContext).load(path).transforms(transformation).into(mImageView);
    }



    public static void loadCircleImage(Context context, String url, ImageView imageview) {
        GlideApp.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .dontAnimate()
                .placeholder(R.mipmap.ic_mine_default_avatar)
                .transforms(new CircleCrop())
                .into(imageview);
    }


    public static void loadImageViewSize(Context mContext, String path, int width, int height, ImageView mImageView) {
        GlideApp.with(mContext).load(path).override(width, height).into(mImageView);
    }


    public static void loadImageViewLoding(Context mContext, String path, ImageView mImageView, int lodingImage, int errorImageView) {
        GlideApp.with(mContext).load(path).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }



    public static void loadImageViewLoding(Context mContext, Bitmap bm, ImageView mImageView, int lodingImage, int errorImageView) {
        GlideApp.with(mContext).load(bm).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }



    public static void loadImageViewLoding(Context mContext, byte[] bytearray, ImageView mImageView, int lodingImage, int errorImageView) {
        GlideApp.with(mContext).load(bytearray).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }


    public static void loadImageViewLodingSize(Context mContext, String path, int width, int height, ImageView mImageView, int lodingImage, int errorImageView) {
        GlideApp.with(mContext).load(path).override(width, height).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }


    public static void loadImageViewCache(Context mContext, String path, ImageView mImageView) {
        GlideApp.with(mContext).load(path).skipMemoryCache(true).into(mImageView);
    }


    public static void loadImageViewPriority(Context mContext, String path, ImageView mImageView) {
        GlideApp.with(mContext).load(path).priority(Priority.NORMAL).into(mImageView);
    }




    public static void loadImageViewDiskCache(Context mContext, String path, ImageView mImageView) {
        GlideApp.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }




    public static void loadImageViewAnim(Context mContext, String path, int anim, ImageView mImageView) {
//        GlideApp.with(mContext).load(path).animate(anim).into(mImageView);
    }






    public static void loadImageViewThumbnail(Context mContext, String path, ImageView mImageView) {
        GlideApp.with(mContext).load(path).thumbnail(0.1f).into(mImageView);
    }



    public static void loadImageViewCrop(Context mContext, String path, ImageView mImageView) {
        GlideApp.with(mContext).load(path).centerCrop().into(mImageView);
    }

    public static void loadImageViewDynamicGif(Context mContext, String path, ImageView mImageView) {
        GlideApp.with(mContext).asGif().load(path).into(mImageView);
    }


    public static void loadImageViewStaticGif(Context mContext, String path, ImageView mImageView) {
        GlideApp.with(mContext).asBitmap().load(path).into(mImageView);
    }


    public static void loadImageViewListener(Context mContext, String path, ImageView mImageView, RequestListener<Drawable> requstlistener) {
        GlideApp.with(mContext).load(path).listener(requstlistener).into(mImageView);
    }


    public static void loadImageViewContent(Context mContext, String path, SimpleTarget<Drawable> simpleTarget) {
        GlideApp.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(simpleTarget);
    }

    public static void loadImageViewContentWithSizeFixRatio(Context mContext, String path, float ratio, int width, int height, ImageView imageView) {
        GlideApp.with(mContext).load(path).override(width, height).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean
                    isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        int intrinsicWidth = resource.getIntrinsicWidth();
                        int intrinsicHeight = resource.getIntrinsicHeight();
                        if(intrinsicWidth/intrinsicHeight>ratio){
                            GlideApp.with(mContext).load(path).override(width, (int) (height*.6+.5f)).centerCrop().into(imageView);
                        }else if(intrinsicHeight/intrinsicWidth>ratio){
                            GlideApp.with(mContext).load(path).override((int) (width*.6+.5f), (int) (height*1.2+.5f)).centerCrop().into(imageView);
                        }else {
                            GlideUtil.loadImageViewSize(mContext, path, width, height, imageView);
                        }
                    }
                });
                return true;
            }
        }).into(imageView);

    }


    public static void GuideClearDiskCache(Context mContext) {

        Glide.get(mContext).clearDiskCache();
    }


    public static void GuideClearMemory(Context mContext) {

        Glide.get(mContext).clearMemory();

    }


//    public static void clearView(View view) {
//        Glide.clear(view);
//    }


}
