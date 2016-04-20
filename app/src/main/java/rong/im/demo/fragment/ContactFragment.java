package rong.im.demo.fragment;

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

public class ContactFragment extends Fragment {

    ListView contactList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_contact, container, false);
        contactList = (ListView) root.findViewById(R.id.contact_list);
        contactList.setAdapter(new ContactListAdapter());
        return root;
    }

    private class ContactListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public ContactListAdapter() {
            mInflater = ContactFragment.this.getActivity().getLayoutInflater();
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
                view = mInflater.inflate(R.layout.contact_list_item, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) view.findViewById(R.id.image);
                holder.name = (TextView) view.findViewById(R.id.name);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            if (position == 0) {
                holder.image.setImageResource(R.mipmap.new_friend);
                holder.name.setText("新的朋友");
            }
            return view;
        }

        private class ViewHolder {
            ImageView image;
            TextView name;
        }
    }
}
