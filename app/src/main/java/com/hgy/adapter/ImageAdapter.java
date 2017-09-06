package com.hgy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hgy.R;
import com.hgy.util.ImageLoader;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
        private List<String> mImgPaths;
        private String mDirPath;
        private LayoutInflater mInflater;
        public ImageAdapter(Context context, List<String> mDatas, String dirPath){
            this.mDirPath=dirPath;
            this.mImgPaths=mDatas;
            this.mInflater= LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mImgPaths.size();
        }

        @Override
        public Object getItem(int i) {
            return mImgPaths.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder=null;
            if (view==null){
                view=mInflater.inflate(R.layout.item_grideview_selected_image,viewGroup,false);
                holder=new ViewHolder();
                holder.imageView= (ImageView) view.findViewById(R.id.image);
                view.setTag(holder);
            }else{
                holder= (ViewHolder) view.getTag();
            }
            Log.e("TAG",mImgPaths.get(i));
            holder.imageView.setImageResource(R.mipmap.picture_no);
            ImageLoader.getInstance().loadImage(mDirPath+"/"+mImgPaths.get(i),holder.imageView);
            return view;
        }

        class ViewHolder{
            ImageView imageView;
        }
    }