package rong.im.demo.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.listener.CloudCodeListener;
import rong.im.demo.model.UserInfo;

public class BmobUtil {

    private static final String TAG = "BmobUtil";

    public static void addUserToServer(UserInfo info, String password, Context context, final Handler handler) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("username", info.username);
            jsonObj.put("password", password);
            jsonObj.put("nickname", info.nickname);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
        ace.callEndpoint(context, "addUser", jsonObj, new CloudCodeListener() {
            @Override
            public void onSuccess(Object object) {
                Log.d(TAG, "callEndpoint->addUserToServer: response = " + object.toString());

                // parser result
                Message msg = Message.obtain();
                String objectId = null;
                try {
                    JSONObject result = new JSONObject(object.toString());
                    objectId = result.getString("objectId");
                    msg.obj = objectId;
                    msg.what = Const.REQUEST_SUCCESS;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    try {
                        JSONObject result = new JSONObject(object.toString());
                        int code = result.getInt("code");
                        msg.what = Const.REQUEST_FAILED;
                        msg.arg1 = code;
                        handler.sendMessage(msg);
                    } catch (JSONException exp) {
                    }
                }
            }

            @Override
            public void onFailure(int code, String message) {
                Message msg = Message.obtain();
                msg.what = Const.REQUEST_FAILED;
                msg.arg1 = code;
                handler.sendMessage(msg);
            }
        });
    }

    public static void uploadPortraitToServer(String path) {

    }

    public static void updatePortraitToServer() {

    }
}
