package io.rong.live;

import android.content.Context;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ksyun.media.player.KSYMediaPlayer;

import java.io.IOException;

public class KSYPlayer implements StreamPlayer {

    private SurfaceHolder mSurfaceHolder;
    private KSYMediaPlayer mKSYMediaPlayer;

    public KSYPlayer(Context context) {
        mKSYMediaPlayer = new KSYMediaPlayer.Builder(context).build();
        mKSYMediaPlayer.setScreenOnWhilePlaying(true);
        mKSYMediaPlayer.setBufferTimeMax(5);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void play() {
        mKSYMediaPlayer.start();
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setUrl(String url) {
        try {
            mKSYMediaPlayer.setDataSource(url);
            mKSYMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setView(SurfaceView view) {
        mSurfaceHolder = view.getHolder();
        mSurfaceHolder.addCallback(mSurfaceCallback);
    }

    private final SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {

        private Surface mSurface = null;

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (mKSYMediaPlayer != null) {
                final Surface newSurface = holder.getSurface();
                mKSYMediaPlayer.setDisplay(holder);
                mKSYMediaPlayer.setScreenOnWhilePlaying(true);
                if (mSurface != newSurface) {
                    mSurface = newSurface;
                    mKSYMediaPlayer.setSurface(mSurface);
                }
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mKSYMediaPlayer != null) {
                mSurface = null;
            }
        }
    };
}
