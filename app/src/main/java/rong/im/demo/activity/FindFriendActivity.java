package rong.im.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import rong.im.demo.R;

public class FindFriendActivity extends AppCompatActivity {

    private static final String TAG = "FindFriendActivity";

    private ImageView back;
    private EditText searchEdit;
    private ImageView clearText;
    private SearchBtn searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        searchEdit = (EditText) findViewById(R.id.search_edit);
        clearText = (ImageView) findViewById(R.id.clear_text);
        searchBtn = new SearchBtn(findViewById(R.id.search_btn));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchBtn.setText(s.toString());
            }
        });
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("");
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "v = " + searchEdit.getText().toString());
            }
        });
    }

    private class SearchBtn {
        private View rootView;
        private TextView content;

        public SearchBtn(View root) {
            rootView = root;
            content = (TextView) root.findViewById(R.id.search_text);
        }

        public void setText(String text) {
            if (text.isEmpty()) {
                rootView.setVisibility(View.GONE);
            } else {
                rootView.setVisibility(View.VISIBLE);
                content.setText("搜索:" + text);
            }
        }

        public void setOnClickListener(View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
        }
    }
}
