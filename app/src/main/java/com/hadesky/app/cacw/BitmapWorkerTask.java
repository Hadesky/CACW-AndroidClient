package com.hadesky.app.cacw;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by 45517 on 2015/8/2.
 */
public class BitmapWorkerTask extends AsyncTask<Integer,Bitmap,Bitmap> {
    private final WeakReference imageViewReference;
    private int resId = 0;
    private final Resources mResources;
    private int requireWidth = 100;
    private int requireHeight = 100;

    public BitmapWorkerTask(ImageView imageView,Resources resources) {
        imageViewReference = new WeakReference<>(imageView);
        mResources = resources;
    }

    @Override
    protected Bitmap doInBackground(Integer[] params) {
        resId = params[0];
        return DecodeBitmap.decodeSampledBitmapFromResource(mResources, resId, requireWidth, requireHeight);
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (bitmap != null) {
            final ImageView imageView = (ImageView) imageViewReference.get();
            final BitmapWorkerTask task = getBitmapWorkerTask(imageView);

            if (this == task) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public static boolean cancelPotentialWork(int resId, ImageView imageView) {
        final BitmapWorkerTask task = getBitmapWorkerTask(imageView);

        if (task != null) {
            int data = task.getResId();
            if (data == 0 || data != resId) {
                task.cancel(true);
            }else return false;
        }
        return true;
    }


    public int getResId() {
        return resId;
    }

    public static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> BitmapWorkerTaskReference;
        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            BitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return BitmapWorkerTaskReference.get();
        }
    }

    public void setRequireWidth(int requireWidth) {
        this.requireWidth = requireWidth;
    }

    public void setRequireHeight(int requireHeight) {
        this.requireHeight = requireHeight;
    }
}
