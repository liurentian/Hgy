package com.hgy.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by asusnb on 2017/9/6.
 */

public class ImageLoader {

    private static ImageLoader mInstance;
    /**
     * 缓存的重要对象
     */
    private LruCache<String,Bitmap> mLruCache;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static int DEAFULT_COUNT=3;  //默认线程池数量
    /**
     *队列的调度方式
     */
    private Type mType=Type.LIFO;
    /**
     * 任务队列 线程池根据队列的调度方式，去任务队列取任务
     */
    private LinkedList<Runnable> mTaskQueue;
    /**
     * 后台轮询线程
     */
    private Thread   mPoolThread;
    private Handler  mPoolThreadHandler;
    /**
     * UI 线程的Handler
     */
    private Handler mUIHandler;
    private Semaphore mSemaphorePoolThreadHandler=new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;


    private ImageLoader(int threadCount,Type type){
        init(threadCount,type);
    }

    public static ImageLoader getInstance(){
        if (mInstance==null){
            synchronized (ImageLoader.class){
                if (mInstance==null){
                    mInstance=new ImageLoader(DEAFULT_COUNT,Type.LIFO);
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     * 1.开启后台轮询线程 从线程任务队列取任务执行
     * 2.初始化 LruCache 缓存 为当前可用内存的1/8
     * 3.初始化 线程池、线程任务队列
     * @param threadCount
     * @param type
     */
    private void init(int threadCount, Type type) {
        //1.开启后台轮询线程 从线程任务队列取任务执行
        mPoolThread=new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                mPoolThreadHandler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        // 线程池 去取出 一个任务进行执行
                        mThreadPool.execute(getTask());
                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mSemaphorePoolThreadHandler.release();
                Looper.loop();

            }
        };
        mPoolThread.start();
        //2.初始化 LruCache 缓存 为当前可用内存的1/8
        int MaxMemory= (int) Runtime.getRuntime().maxMemory();
        int cacheMemory=MaxMemory/8;
        mLruCache=new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getHeight() * value.getRowBytes();
            }
        };
        //3.初始化线程池、线程队列
        mThreadPool= Executors.newFixedThreadPool(threadCount);
        mTaskQueue=new LinkedList<>();
        mType=type;
        //线程池 信号量限制
        mSemaphoreThreadPool=new Semaphore(threadCount);
    }
    /**
     * 把 图片加载到 imageView 上
     * 0. 懒加载 开启 ui handler 把拿到的图片展示到 ImageView 上
     * 1. 首先去 LruCache 中去拿 bitmap 如果没有 则新的添加任务
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView){
        imageView.setTag(path);
        mUIHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //获取得到的图片，为imageView 设置图片
                ImageBeanHolder holder= (ImageBeanHolder) msg.obj;
                ImageView imageView = holder.imageView;
                Bitmap bitmap = holder.bitmap;
                String path = holder.path;
                if (imageView.getTag().toString().equals(path)){
                    imageView.setImageBitmap(bitmap);
                }
            }
        };
        //根据 path 去缓存中获取 bitmap
        Bitmap bm=getBitmapFromLruCache(path);
        if (bm!=null){
            refreshBitmap(path, imageView, bm);
        }else {
            addTasks(new Runnable() {
                @Override
                public void run() {
                    // 加载图片
                    // 图片压缩
                    // 1.获得图片需要显示的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    // 2.压缩图片
                    Bitmap bm=decodeSampleBitmapFromPath(path,imageSize.width,imageSize.height);
                    // 把图片缓存到内存中
                    addBitmapToLruCache(path,bm);

                    refreshBitmap(path, imageView, bm);

                    mSemaphoreThreadPool.release();
                }
            });
        }

    }
    /**
     * 把图片缓存到内存中
     * @param path
     * @param bm
     */
    private void addBitmapToLruCache(String path, Bitmap bm) {
        if (getBitmapFromLruCache(path) == null){
            if (bm!=null){
                mLruCache.put(path,bm);
            }
        }
    }
    /**
     * 根据图片要显示的宽和高对图片进行压缩
     * @param path
     * @param width
     * @param height
     * @return
     */
    private Bitmap decodeSampleBitmapFromPath(String path, int width, int height) {
        // 获得图片的宽和高 并不把图片加载到内存中
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);

        options.inSampleSize=caculateInSampleSize(options,width,height);

        // 使用得到的 inSampleSize 再次解析图片
        options.inJustDecodeBounds=false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }
    /**
     * 根据需求的宽和高以及图片实际的宽高计算 SampleSize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width=options.outWidth;
        int height=options.outHeight;

        int inSampleSize=1;

        if (width>reqWidth || height> reqHeight){
            int widthRadio=Math.round(width*1.0f/reqWidth);
            int heightRadio=Math.round(height*1.0f/reqHeight);

            inSampleSize=Math.max(widthRadio,heightRadio);
        }
        return inSampleSize;
    }

    /**
     * 得到要显示的图片的 大小
     * @param imageView
     */
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize=new ImageSize();

        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        int width=imageView.getWidth();  //获取imageView 的实际宽度
        if (width <= 0){
            width=lp.width; //获取 imageView 在layout 中的声明宽度
        }
        if (width <= 0){
            width= imageView.getMaxWidth();  //检查最大值
        }
        if (width <= 0){
            width=displayMetrics.widthPixels;  //屏幕的宽
        }

        int height=imageView.getHeight();  //获取imageView 的实际高度
        if (height <= 0){
            height=lp.height; //获取 imageView 在layout 中的声明高度
        }
        if (height <= 0){
            height= imageView.getMaxHeight();  //检查最大值
        }
        if (height <= 0){
            height=displayMetrics.heightPixels;  //屏幕的高
        }

        imageSize.width=width;
        imageSize.height=height;
        return imageSize;
    }


    private void refreshBitmap(String path, ImageView imageView, Bitmap bm) {
        Message message=Message.obtain();
        ImageBeanHolder holder=new ImageBeanHolder();
        holder.bitmap=bm;
        holder.path=path;
        holder.imageView=imageView;
        message.obj=holder;
        mUIHandler.sendMessage(message);
    }
    /**
     * 添加一个 任务到线程池
     * @param runnable
     */
    private synchronized void addTasks(Runnable runnable) {
        mTaskQueue.add(runnable);
        try{
            if (mPoolThreadHandler ==null ){
                mSemaphorePoolThreadHandler.acquire();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        mPoolThreadHandler.sendEmptyMessage(0x003);
    }
    /**
     * 从任务队列取出一个方法
     * @return
     */
    private Runnable getTask() {
        if (mType == Type.FIFO){
            return mTaskQueue.removeFirst();
        }else if(mType == Type.LIFO){
            return mTaskQueue.removeLast();
        }
        return null;
    }

    private class ImageBeanHolder{
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }
    /**
     * 根据 path 去缓存中获取 bitmap
     * @param path
     * @return
     */
    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }

    private class ImageSize{
        int width;
        int height;
    }
    public enum Type{
        LIFO,FIFO
    }
}
