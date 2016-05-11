package rong.im.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import rong.im.demo.R;

public class AppUtil {

    public static final String SHARED_INFO = "RongDemo";
    public static final String LOGIN_USER_ID = "Login_UserId";
    public static final String LOGIN_TOKEN = "Login_Token";

    private static SharedPreferences gsp;
    private static Context gContext;
    private static DisplayImageOptions imageOptions;

    public static void init(Context context) {
        gContext = context;
        initAsyncImageLoader();
    }

    public static Context getContext() {
        return gContext;
    }

    public static int dip2px(float dpValue) {
        final float scale = gContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void saveUserInfo(Context context, String username, String token) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LOGIN_USER_ID, username);
        editor.putString(LOGIN_TOKEN, token);
        editor.commit();
    }

    public static String getCachedUsername() {
        SharedPreferences sp = getSharedPreferences(gContext);
        return sp.getString(LOGIN_USER_ID, "");
    }

    public static String getCachedToken() {
        SharedPreferences sp = getSharedPreferences(gContext);
        return sp.getString(LOGIN_TOKEN, "");
    }

    public static void clearUserInfoCached() {
        SharedPreferences sp = getSharedPreferences(gContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LOGIN_USER_ID, "");
        editor.putString(LOGIN_TOKEN, "");
        editor.commit();
    }

    public static void loadUriImage(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage(uri, imageView, imageOptions);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        if (gsp == null) {
            gsp = context.getSharedPreferences(SHARED_INFO, Context.MODE_PRIVATE);
        }
        return gsp;
    }

    private static void initAsyncImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(gContext);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        ImageLoader.getInstance().init(config.build());

        imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.image_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }
}
