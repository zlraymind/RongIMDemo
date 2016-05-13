package rong.im.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import rong.im.demo.R;
import rong.im.demo.model.User;

public class AppUtil {

    public static final String SHARED_INFO = "RongDemo";
    public static final String LOGIN_USERNAME = "Login_Username";
    public static final String LOGIN_PASSWORD_MD5 = "Login_Password_MD5";

    private static SharedPreferences gsp;
    private static Context gContext;
    private static DisplayImageOptions imageOptions;
    private static User currentUser;

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

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static User getCurrentUser() {
        return RemoteDBUtil.getCurrentUser();
    }

    public static void saveLoginInfo(String username, String password_md5) {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LOGIN_USERNAME, username);
        editor.putString(LOGIN_PASSWORD_MD5, password_md5);
        editor.commit();
    }

    public static String getCachedUsername() {
        SharedPreferences sp = getSharedPreferences();
        return sp.getString(LOGIN_USERNAME, "");
    }

    public static String getCachedPasswordMD5() {
        SharedPreferences sp = getSharedPreferences();
        return sp.getString(LOGIN_PASSWORD_MD5, "");
    }

    public static void clearUserInfoCached() {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LOGIN_USERNAME, "");
        editor.putString(LOGIN_PASSWORD_MD5, "");
        editor.commit();
    }

    public static void loadRemoteImage(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage(uri, imageView, imageOptions);
    }

    private static SharedPreferences getSharedPreferences() {
        if (gsp == null) {
            gsp = gContext.getSharedPreferences(SHARED_INFO, Context.MODE_PRIVATE);
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
