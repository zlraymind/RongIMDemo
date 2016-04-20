package rong.im.demo.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.live.KSYPlayer;
import io.rong.live.LiveClient;
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
        liveClient.joinLiveChatRoom("testLiveRoom", "rtmp://test.uplive.ksyun.com/live/androidtest", surfaceView);
        RongIMClient.getInstance().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                Log.d("RYM", "onReceived SentTime = " + message.getSentTime());
                return false;
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

        title.setText("直播聊天室");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
