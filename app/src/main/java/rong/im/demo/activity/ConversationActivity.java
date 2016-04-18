package rong.im.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import rong.im.demo.R;

public class ConversationActivity extends AppCompatActivity {

    private static final String TAG = "ConversationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        initView();

    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.title);
        ImageView back = (ImageView) findViewById(R.id.back);

        title.setText(getIntent().getData().getQueryParameter("targetId"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
