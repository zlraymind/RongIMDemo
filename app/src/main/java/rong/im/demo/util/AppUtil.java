package rong.im.demo.util;

import android.content.Context;
import android.content.SharedPreferences;

public class AppUtil {

    public static final String SHARED_INFO = "RongDemo";
    public static final String LOGIN_USERID = "Login_UserId";
    public static final String LOGIN_TOKEN = "Login_Token";

    private static SharedPreferences gsp;

    private static SharedPreferences getSharedPreferences(Context context) {
        if (gsp == null) {
            gsp = context.getSharedPreferences(SHARED_INFO, Context.MODE_PRIVATE);
        }
        return gsp;
    }

    public static void saveUserInfo(Context context, String username, String token) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LOGIN_USERID, username);
        editor.putString(LOGIN_TOKEN, token);
        editor.commit();
    }

    public static String getSavedUsername(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(LOGIN_USERID, "");
    }

    public static String getSavedToken(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(LOGIN_TOKEN, "");
    }

    public static void clearUserInfo(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LOGIN_USERID, "");
        editor.putString(LOGIN_TOKEN, "");
        editor.commit();
    }

}
