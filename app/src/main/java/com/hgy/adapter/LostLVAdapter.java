package com.hgy.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hgy.R;
import com.hgy.db.Lost;

import java.util.List;

/**
 * Created by asusnb on 2017/8/31.
 */

public class LostLVAdapter extends BaseAdapter {
    private Activity activity;
    private List<Lost> list;

    public LostLVAdapter(Activity activity, List<Lost> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (view==null){
            view=View.inflate(activity, R.layout.item_lost_found,null);
            holder=new ViewHolder();
            holder.title= (TextView) view.findViewById(R.id.item_title);
            holder.description= (TextView) view.findViewById(R.id.item_description);
            holder.phone= (TextView) view.findViewById(R.id.item_phone);
            holder.data= (TextView) view.findViewById(R.id.item_data);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        holder.title.setText(list.get(i).getTitle());
        holder.description.setText(list.get(i).getDescribe());
        holder.phone.setText(list.get(i).getPhone());
        holder.data.setText(list.get(i).getCreatedAt());
        return view;
    }

    class ViewHolder{
        TextView title;
        TextView description;
        TextView phone;
        TextView data;
    }
}
