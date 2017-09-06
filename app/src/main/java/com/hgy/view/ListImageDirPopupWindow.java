package com.hgy.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hgy.R;
import com.hgy.db.FolderBean;
import com.hgy.util.ImageLoader;

import java.util.List;

/**
 * Created by asusnb on 2017/9/6.
 */

public class ListImageDirPopupWindow extends PopupWindow {
    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;
    private List<FolderBean> mDatas;

    private mDirSelectedListener selectedListener;
    public void setSelectedListener(mDirSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }
    public interface mDirSelectedListener{
        void selected(FolderBean folderBean);
    }
    public ListImageDirPopupWindow(Context context, List<FolderBean> mDatas) {
        super(context);
        calWidthAndHeight(context);
        mConvertView= LayoutInflater.from(context).inflate(R.layout.popup_window,null);

        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE){
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        this.mDatas = mDatas;
        initView(context);
        initEvent();
    }

    private void initView(Context context) {
        mListView= (ListView) mConvertView.findViewById(R.id.id_list_dir);
        ListDirAdapter adapter=new ListDirAdapter(context,mDatas);
        mListView.setAdapter(adapter);

    }
    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (selectedListener != null){
                    selectedListener.selected(mDatas.get(i));
                }
            }
        });
    }
    /**
     * 计算 popupWindow 的宽度和高度
     * @param context
     */
    private void calWidthAndHeight(Context context) {
        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mWidth=outMetrics.widthPixels;
        mHeight= (int) (outMetrics.heightPixels*0.7);
    }


    class ListDirAdapter extends ArrayAdapter<FolderBean>{
        private LayoutInflater mInflater;
        private List<FolderBean> mDates;


        public ListDirAdapter(@NonNull Context context,List<FolderBean> objects) {
            super(context, 0,objects);
            mInflater=LayoutInflater.from(context);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder=null;
            if (convertView==null){
                convertView=mInflater.inflate(R.layout.item_popwindow,parent,false);
                holder=new ViewHolder();
                holder.mImg= (ImageView) convertView.findViewById(R.id.id_dir_item_image);
                holder.mDirName= (TextView) convertView.findViewById(R.id.id_dir_item_name);
                holder.mDirCount= (TextView) convertView.findViewById(R.id.id_dir_item_count);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }

            FolderBean bean=getItem(position);
            holder.mImg.setImageResource(R.mipmap.picture_no);
            ImageLoader.getInstance().loadImage(bean.getFirstImgPath(),holder.mImg);
            holder.mDirName.setText(bean.getName());
            holder.mDirCount.setText("("+bean.getCount()+")");

            return convertView;

        }

        private class ViewHolder{
            ImageView mImg;
            TextView mDirName;
            TextView mDirCount;
        }
    }
}
