package com.example.fatih.runforlife;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Fatih on 21.04.2017.
 */

public class UserActivitiesAdapter extends BaseAdapter {


    private List<Activities> mList;
    private LayoutInflater mInfilater;
    private Context context;

    public UserActivitiesAdapter(Activity activitiy, List<Activities> mList) {
        this.mList = mList;
        this.mInfilater = (LayoutInflater)activitiy.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = activitiy;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView;
        listView=mInfilater.inflate(R.layout.satir_layout,null);
        TextView nameTxt=(TextView)listView.findViewById(R.id.messageTextView);
        TextView totalRoute=(TextView)listView.findViewById(R.id.nameTextView);

        Activities act=mList.get(position);

        nameTxt.setText(act.getTotalTime());
        totalRoute.setText(act.getTotalRoute());
        return  listView;
    }
}
