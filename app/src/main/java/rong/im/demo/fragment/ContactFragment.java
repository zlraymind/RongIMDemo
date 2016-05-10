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
import rong.im.demo.activity.NewFriendActivity;

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
            ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.contact_list_item, parent, false);
                holder = new ViewHolder();
                holder.header = view.findViewById(R.id.header);
                holder.body = view.findViewById(R.id.body);
                holder.initial = (TextView) view.findViewById(R.id.initial);
                holder.image = (ImageView) view.findViewById(R.id.toolbar_find_icon);
                holder.name = (TextView) view.findViewById(R.id.name);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            if (position == 0) {
                holder.body.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ContactFragment.this.getActivity(), NewFriendActivity.class);
                        ContactFragment.this.startActivity(intent);
                    }
                });
                holder.image.setImageResource(R.mipmap.new_friend);
                holder.name.setText("新的朋友");
            }
            return view;
        }

        private class ViewHolder {
            View header;
            View body;
            TextView initial;
            ImageView image;
            TextView name;
        }
    }
}
