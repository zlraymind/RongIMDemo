package io.rong.live;

public interface StreamListener {

    int ERROR_UNKNOWN = -1;
    int ERROR_SERVER_DIED = -100;

    void onError(int errorCode);

    void onPrepared();

    void onCompletion();
}
