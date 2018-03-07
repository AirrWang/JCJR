package com.ql.jcjr.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;

import java.io.File;

/**
 * ClassName: GlideUtil
 * Description:
 * Author: Administrator
 * Date: Created on 202017/8/30.
 */

public class GlideUtil {

    public static void displayPic(Context context, String url, int srcId, ImageView view) {
        Glide
                .with(context)
                .load(url)
                .dontAnimate()
                .placeholder(srcId)
                .error(srcId)
                .into(view);
    }
    public static void displayPic(Context context, File file, int srcId, ImageView view) {
        Glide
                .with(context)
                .load(file)
                .dontAnimate()
                .placeholder(srcId)
                .error(srcId)
                .into(view);
    }

    public static void displayNoCachePic(Context context, String url, int srcId, ImageView view) {
        Glide
                .with(context)
                .load(url)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .placeholder(srcId)
                .error(srcId)
                .into(view);
    }
}
