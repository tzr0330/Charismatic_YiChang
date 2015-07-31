package com.amlogic.tzr.charismatic_yichang.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amlogic.tzr.charismatic_yichang.ApplicationController;
import com.amlogic.tzr.charismatic_yichang.Tool.BitmapUtil;
import com.amlogic.tzr.charismatic_yichang.Tool.ScreenUtil;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2015/7/25.
 */
public class ImageCompressTransformation implements Transformation{
    @Override
    public Bitmap transform(Bitmap image) {
//        int oldWidth=source.getWidth();
//        int oldHeight=source.getHeight();
//        int reqWidth= ScreenUtil.getScreenWidth(ApplicationController.getInstance())*3/4;
//        int reqHeight= Math.round((float) oldHeight*reqWidth / (float) oldWidth);
//        Bitmap result = Bitmap.createBitmap()
//        if (result != source) {
//            source.recycle();
//        }
//        return result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024)
        {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 设置图片的宽为屏幕的宽度
        float ww = ScreenUtil.getScreenWidth(ApplicationController.getInstance());
        float hh = ww*h/w;

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww)
        {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh)
        {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inJustDecodeBounds = false;
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        if (isBm != null)
        {
            try
            {
                isBm.close();
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (image != null && !image.isRecycled())
        {
            image.recycle();
            image = null;
        }
        return BitmapUtil.compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    @Override
    public String key() {
        return  "ImageCompressTransformation()";
    }
}
