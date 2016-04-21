package io.rong.live;

import android.content.Context;
import android.view.SurfaceView;

import io.rong.imlib.RongIMClient;

public class LiveClient {

    private StreamPlayer mStreamPlayer;

    public void joinLiveChatRoom(String targetId, String url, SurfaceView view, RongIMClient.OperationCallback callback) {
        loadStreamPlayer(view.getContext());
        RongIMClient.getInstance().joinChatRoom(targetId, 10, callback);

        mStreamPlayer.setUrl(url);
        mStreamPlayer.setView(view);
    }

    public void setStreamListener(StreamListener listener) {
        mStreamPlayer.setStreamListener(listener);
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

    private void loadStreamPlayer(Context context) {
        mStreamPlayer = new KSYPlayer(context);
    }
}
