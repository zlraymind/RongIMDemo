package rong.im.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import rong.im.demo.R;
import rong.im.demo.fragment.ContactFragment;
import rong.im.demo.itemview.ItemView;
import rong.im.demo.itemview.NewFriendItem;

public class NewFriendActivity extends AppCompatActivity {

    private static final String TAG = "NewFriendActivity";

    private View findFriend;
    private ListView newFriendList;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        initView();
    }

    private void initView() {
        findFriend = findViewById(R.id.find_new_friend);
        newFriendList = (ListView) findViewById(R.id.new_friend_list);
        back = (ImageView) findViewById(R.id.back);

        findFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewFriendActivity.this, FindFriendActivity.class);
                NewFriendActivity.this.startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class FriendListAdapter extends BaseAdapter {
        private List<ItemView> itemList;

        private LayoutInflater inflater;

        public FriendListAdapter() {
            inflater = NewFriendActivity.this.getLayoutInflater();
        }

        public void setItemList(List<ItemView> list) {
            itemList = list;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = itemList.get(position).getView(parent);
            } else {
                itemList.get(position).getTag();
            }


            return view;
        }

        private int getFirstFriendPosition() {
            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i).getTag().equals(NewFriendItem.TAG)) {
                    return i;
                }
            }
            return 0;
        }

        private class ViewHolder {
            View header;
            TextView initial;
            ImageView image;
            TextView name;
        }
    }
}
