package rong.im.demo.util;

import android.content.Context;
import android.content.SharedPreferences;

public class AppUtil {

    public static final String SHARED_INFO = "RongDemo";
    public static final String LOGIN_USER_ID = "Login_UserId";
    public static final String LOGIN_TOKEN = "Login_Token";

    private static SharedPreferences gsp;
    private static Context gContext;

    public static void init(Context context) {
        gContext = context;
    }

    public static Context getContext() {
        return gContext;
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

    private static SharedPreferences getSharedPreferences(Context context) {
        if (gsp == null) {
            gsp = context.getSharedPreferences(SHARED_INFO, Context.MODE_PRIVATE);
        }
        return gsp;
    }
}
