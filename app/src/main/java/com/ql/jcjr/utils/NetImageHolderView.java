package com.ql.jcjr.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.ql.jcjr.R;

/**
 * Created by wf on 2018/6/25.
 */

public class NetImageHolderView implements Holder<String> {

    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        GlideUtil.displayPic(context,data,R.color.c_page_bg,imageView);
    }

}
