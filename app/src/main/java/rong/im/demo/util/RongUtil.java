package rong.im.demo.util;

import android.os.Handler;
import android.os.Message;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class RongUtil {

    public static void connectIMServer(String token, final Handler handler) {
        connectIMServer(token, handler, 0);
    }

    public static void connectIMServer(String token, final Handler handler, final long delayMillis) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Message message = Message.obtain();
                message.what = Const.REQUEST_FAILED;
                message.obj = "RongIM.connect onTokenIncorrect";
                handler.sendMessage(message);
            }

            @Override
            public void onSuccess(String userId) {
                handler.sendEmptyMessageDelayed(Const.REQUEST_SUCCESS, delayMillis);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Message message = Message.obtain();
                message.what = Const.REQUEST_FAILED;
                message.obj = "RongIM.connect onError code = " + errorCode;
                handler.sendMessage(message);
            }
        });
    }
}
