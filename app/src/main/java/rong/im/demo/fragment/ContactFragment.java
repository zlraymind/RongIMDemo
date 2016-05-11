package rong.im.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import rong.im.demo.R;
import rong.im.demo.activity.FindFriendActivity;
import rong.im.demo.activity.NewFriendActivity;
import rong.im.demo.model.User;
import rong.im.demo.widget.ContactItemView;

public class ContactFragment extends Fragment {

    ListView contactList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_contact, container, false);
        contactList = (ListView) root.findViewById(R.id.contact_list);
        contactList.setAdapter(new ContactListAdapter());
        return root;
    }

    private class ContactListAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public ContactListAdapter() {
            inflater = ContactFragment.this.getActivity().getLayoutInflater();
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ContactItemView itemView;
            if (convertView == null) {
                itemView = new ContactItemView(ContactFragment.this.getContext());
                itemView.setTag(itemView);
            } else {
                itemView = (ContactItemView) convertView.getTag();
            }
            itemView.setInfoMode(ContactItemView.SIMPLE);
            itemView.setNickname("新的朋友");
            itemView.setPortrait(R.mipmap.new_friend);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ContactFragment.this.getActivity(), NewFriendActivity.class);
                    ContactFragment.this.startActivity(intent);
                }
            });
            return itemView;
        }
    }
}
