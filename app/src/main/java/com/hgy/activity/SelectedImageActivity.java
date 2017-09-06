package com.hgy.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hgy.R;
import com.hgy.adapter.ImageAdapter;
import com.hgy.base.BaseActivity;
import com.hgy.db.FolderBean;
import com.hgy.view.ListImageDirPopupWindow;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by asusnb on 2017/9/6.
 */

public class SelectedImageActivity extends BaseActivity {
    private GridView mGridView;
    private LinearLayout mLinearLayout;
    private TextView mDirName;
    private ImageView ivBack;
    private ImageView mDirSelector;
    private ImageAdapter mAdapter;

    private List<String> mImgs;
    private File mCurrentDir;
    private List<FolderBean> folderBeanList=new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private int mMaxCount;
    private ListImageDirPopupWindow mPopupWindow;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dataToView();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_image);
        initView();
        checkMyPermission();
        initEvent();
    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.id_gradeView);
        mLinearLayout = (LinearLayout) findViewById(R.id.id_ly_top);
        ivBack = (ImageView) findViewById(R.id.back);
        mDirName = (TextView) findViewById(R.id.title);
        mDirSelector = (ImageView) findViewById(R.id.titlePic);
    }

    private void dataToView() {
        mProgressDialog.dismiss();
        if (mCurrentDir == null){
            Toast.makeText(this,"未扫描到任何图片",Toast.LENGTH_SHORT).show();
            return;
        }
        mImgs = Arrays.asList(mCurrentDir.list());
        mAdapter = new ImageAdapter(this,mImgs,mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(mAdapter);
        mDirName.setText(mCurrentDir.getName());
        initPopWindow();

    }
    /**
     * 初始化 PopWindow
     */
    private void initPopWindow() {
        mPopupWindow=new ListImageDirPopupWindow(this,folderBeanList);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        mPopupWindow.setSelectedListener(new ListImageDirPopupWindow.mDirSelectedListener() {
            @Override
            public void selected(FolderBean folderBean) {
                mPopupWindow.dismiss();
                mCurrentDir=new File(folderBean.getDir());
                mImgs = Arrays.asList(mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        if (s.endsWith(".jpg") ||
                                s.endsWith(".jpeg") ||
                                s.endsWith("png")){
                            return true;
                        }
                        return false;
                    }
                }));

                mAdapter = new ImageAdapter(SelectedImageActivity.this,mImgs,mCurrentDir.getAbsolutePath());
                mGridView.setAdapter(mAdapter);
                mDirName.setText(mCurrentDir.getName());
            }
        });
    }

    /**
     * 内容区域 变亮
     */
    private void lightOn() {
        mGridView.setAlpha(1.0f);
    }

    /**
     * 内容区域 变暗
     */
    private void lightOff() {
        mGridView.setAlpha(0.3f);
    }

    private void initData() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)){
            Toast.makeText(this,"当前存储卡不可用",Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressDialog = ProgressDialog.show(this,null,"正在加载....");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Log.e("TAG", String.valueOf(mImgUri));
                ContentResolver resolver=SelectedImageActivity.this.getContentResolver();
                Cursor cursor = resolver.query(mImgUri, null, MediaStore.Images.Media.MIME_TYPE
                                + " = ? or " + MediaStore.Images.Media.MIME_TYPE
                                + " = ? ", new String[]
                                {"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                Set<String> mDirPath=new HashSet<String>();
                while(cursor.moveToNext()){
                    String path=cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile=new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath=parentFile.getAbsolutePath();
                    FolderBean folderBean=null;
                    if (mDirPath.contains(dirPath)){
                        continue;
                    }else{
                        mDirPath.add(dirPath);
                        folderBean=new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);
                    }
                    if (parentFile.list() == null)
                        continue;
                    int picSize=parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File file, String s) {
                            if (s.endsWith(".jpg") ||
                                    s.endsWith(".jpeg") ||
                                    s.endsWith("png")){
                                return true;
                            }
                            return false;
                        }
                    }).length;
                    folderBean.setCount(picSize);
                    folderBeanList.add(folderBean);
                    if (picSize > mMaxCount){
                        mMaxCount=picSize;
                        mCurrentDir=parentFile;
                    }
                }
                cursor.close();
                mHandler.sendEmptyMessage(0x123);
            }
        }).start();
    }

    private void initEvent() {
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.showAsDropDown(mLinearLayout,0,0);
                lightOff();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String path = mImgs.get(i);
            }
        });
    }

    private void checkMyPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},000);
                return;
            }else{
                initData();
            }
        } else {
            initData();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==000){
            if (grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                initData();
            }else{
                Toast.makeText(SelectedImageActivity.this, "权限拒接", Toast.LENGTH_SHORT)
                        .show();
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
