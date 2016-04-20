package io.rong.live;

import android.view.SurfaceView;

public interface StreamPlayer {

    public void setListener();

    public void play();

    public void pause();

    public void stop();

    public void setUrl(String url);

    public void setView(SurfaceView view);
}
