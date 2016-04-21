package io.rong.live;

import android.view.SurfaceView;

public interface StreamPlayer {

    void setStreamListener(StreamListener listener);

    void play();

    void pause();

    void stop();

    void setUrl(String url);

    void setView(SurfaceView view);
}
