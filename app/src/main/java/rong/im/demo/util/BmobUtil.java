package rong.im.demo.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import rong.im.demo.model.LoginUser;

public class BmobUtil {

    private static final String TAG = "BmobUtil";

    public static void checkUserFromServer(String username, final Context context, final Handler handler) {
        BmobQuery<LoginUser> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        query.findObjects(context, new FindListener<LoginUser>() {
            @Override
            public void onSuccess(List<LoginUser> object) {
                Log.d(TAG, "checkUserFromServer onSuccess");
                handler.sendEmptyMessage(Const.REQUEST_SUCCESS);
            }

            @Override
            public void onError(int code, String msg) {
                Log.e(TAG, "checkUserFromServer onFailure code = " + code + "; msg = " + msg);
                Message message = Message.obtain();
                message.what = Const.REQUEST_FAILED;
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }

    public static void uploadPortraitToServer(String picPath, final Context context, final Handler handler) {
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(context, new UploadFileListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "uploadPortraitToServer onSuccess");
                Message message = Message.obtain();
                message.what = Const.REQUEST_SUCCESS;
                message.obj = bmobFile.getFileUrl(context);
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.e(TAG, "uploadPortraitToServer onFailure code = " + code + "; msg = " + msg);
                Message message = Message.obtain();
                message.what = Const.REQUEST_FAILED;
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }

    public static void addUserToServer(LoginUser user, final Context context, final Handler handler) {
        user.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "addUserToServer onSuccess");
                handler.sendEmptyMessage(Const.REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.e(TAG, "addUserToServer onFailure code = " + code + "; msg = " + msg);
                Message message = Message.obtain();
                message.what = Const.REQUEST_FAILED;
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }

    public static void loginToServer(LoginUser user, final Context context, final Handler handler) {
        user.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "loginToServer onSuccess");
                handler.sendEmptyMessage(Const.REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.e(TAG, "loginToServer onFailure code = " + code + "; msg = " + msg);
                Message message = Message.obtain();
                message.what = Const.REQUEST_FAILED;
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }
}
