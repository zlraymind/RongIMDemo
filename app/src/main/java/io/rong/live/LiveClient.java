package io.rong.live;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;

import io.rong.imlib.RongIMClient;

public class LiveClient {

    private StreamPlayer mStreamPlayer;

    public void joinLiveChatRoom(String targetId, String url, SurfaceView view) {

        createStreamPlayer(view.getContext());
        RongIMClient.getInstance().joinChatRoom(targetId, 10, new RongIMClient.OperationCallback() {

            @Override
            public void onSuccess() {
                Log.d("RYM", "joinLiveChatRoom onSuccess");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d("RYM", "joinLiveChatRoom onError errorCode = " + errorCode);
            }
        });

        mStreamPlayer.setUrl(url);
        mStreamPlayer.setView(view);
    }

    public void startPlay() {
        if (mStreamPlayer != null) {
            mStreamPlayer.play();
        }
    }

    public void pausePlay() {
        if (mStreamPlayer != null) {
            mStreamPlayer.pause();
        }
    }

    public void stopPlay() {
        if (mStreamPlayer != null) {
            mStreamPlayer.stop();
        }
    }

    private void createStreamPlayer(Context context) {
        mStreamPlayer = new KSYPlayer(context);
    }
}
