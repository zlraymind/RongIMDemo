package rong.im.demo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rong.im.demo.R;
import rong.im.demo.model.User;
import rong.im.demo.util.BmobUtil;
import rong.im.demo.util.Const;
import rong.im.demo.widget.ContactItemView;
import rong.im.demo.widget.WaitingDialog;
import rong.im.demo.widget.WarringDialog;

public class FindFriendActivity extends AppCompatActivity implements Handler.Callback {

    private static final String TAG = "FindFriendActivity";

    private ImageView back;
    private EditText searchEdit;
    private ImageView clearText;
    private SearchBtn searchBtn;
    private ListView friendListView;
    private WaitingDialog waitingDialog;

    private Handler handler = new Handler(this);

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
        friendListView = (ListView) findViewById(R.id.friend_listview);

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
                if (s.toString().isEmpty()) {
                    searchBtn.setVisibility(View.GONE);
                    searchBtn.setText("");
                    friendListView.setVisibility(View.GONE);
                } else {
                    searchBtn.setText(s.toString());
                    searchBtn.setVisibility(View.VISIBLE);
                    friendListView.setVisibility(View.GONE);
                }
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
                BmobUtil.queryLoginUser(searchEdit.getText().toString(), handler);
                waitingDialog = new WaitingDialog(FindFriendActivity.this);
                waitingDialog.setText("正在查找联系人...");
                waitingDialog.show();
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        waitingDialog.dismiss();
        if (msg.what == Const.REQUEST_SUCCESS) {
            showSearchResult((ArrayList<User>) msg.obj);
        } else if (msg.what == Const.REQUEST_FAILED) {
            Toast.makeText(FindFriendActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void showSearchResult(ArrayList<User> userList) {
        if (userList.size() == 0) {
            WarringDialog dialog = new WarringDialog(this);
            dialog.setBody("用户不存在");
            dialog.show();
            return;
        } else {
            searchBtn.setVisibility(View.GONE);
            friendListView.setVisibility(View.VISIBLE);
            FriendListAdapter adapter = new FriendListAdapter(userList);
            friendListView.setAdapter(adapter);
        }
    }

    private class SearchBtn {
        private View rootView;
        private TextView content;

        public SearchBtn(View root) {
            rootView = root;
            content = (TextView) root.findViewById(R.id.search_text);
        }

        public void setText(String text) {
            if (!text.isEmpty()) {
                SpannableStringBuilder builder = new SpannableStringBuilder("搜索:" + text);
                builder.setSpan(new ForegroundColorSpan(Color.GREEN), 3, 3 + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                content.setText(builder);
            }
        }

        public void setVisibility(int visibility) {
            rootView.setVisibility(visibility);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
        }
    }

    private class FriendListAdapter extends BaseAdapter {
        private ArrayList<User> userList;

        public FriendListAdapter(ArrayList<User> list) {
            userList = list;
        }

        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ContactItemView itemView;
            if (convertView == null) {
                itemView = new ContactItemView(FindFriendActivity.this);
                itemView.setTag(itemView);
            } else {
                itemView = (ContactItemView) convertView.getTag();
            }
            itemView.setInfoMode(ContactItemView.DETAIL);
            User user = userList.get(position);
            itemView.setSearchText(searchEdit.getText().toString());
            itemView.setUserInfo(user);
            return itemView;
        }
    }
}
