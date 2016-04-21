package rong.im.demo.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.live.LiveClient;
import io.rong.live.StreamListener;
import rong.im.demo.R;

public class LiveChatRoomActivity extends AppCompatActivity {

    private static final String TAG = "LiveChatRoomActivity";

    private SurfaceView surfaceView;

    private LiveClient liveClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chatroom);
        initView();

        liveClient = new LiveClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        liveClient.joinLiveChatRoom("testLiveRoom", "rtmp://test.uplive.ksyun.com/live/androidtest", surfaceView, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
        RongIMClient.getInstance().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                Log.d(TAG, "onReceived SentTime = " + message.getSentTime());
                return false;
            }
        });

        liveClient.setStreamListener(new StreamListener() {
            @Override
            public void onError(int errorCode) {
                Log.d(TAG, "onError errorCode = " + errorCode);
            }

            @Override
            public void onPrepared() {
                Log.d(TAG, "onPrepared");
            }

            @Override
            public void onCompletion() {
                Log.d(TAG, "onCompletion");
            }
        });

        ConversationFragment fragment = new ConversationFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath("chatroom")
                .appendQueryParameter("targetId", "testLiveRoom").build();

        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.test_con, fragment);
        transaction.commit();
    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.title);
        ImageView back = (ImageView) findViewById(R.id.back);
        surfaceView = (SurfaceView) findViewById(R.id.live_surface);
        Button play = (Button) findViewById(R.id.play);
        Button pause = (Button) findViewById(R.id.pause);
        Button stop = (Button) findViewById(R.id.stop);

        title.setText("直播聊天室");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveClient.startPlay();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveClient.pausePlay();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveClient.stopPlay();
            }
        });
    }
}
