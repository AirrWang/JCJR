package com.ql.jcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.MessageActEntity;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.UrlUtil;

import java.util.List;


/**
 * ClassName: CarStoreListAdapter
 * Description:
 * Author: Administrator
 * Date: Created on 202017/6/19.
 */

public class MessageActAdapter extends BaseAdapter {

    private Context mContext;
    private List<MessageActEntity.ResultBean> mList;
    private int msgType;

    public MessageActAdapter(Context context, List<MessageActEntity.ResultBean> list, int msgType) {
        mList = list;
        mContext = context;
        this.msgType = msgType;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_message_act, null);
            viewHolder = new ViewHolder();
            ViewUtils.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final MessageActEntity.ResultBean resultBean = mList.get(i);

        viewHolder.mTvTime.setText(resultBean.getAddtime());
        if(msgType == 0){
            GlideUtil.displayPic(mContext, resultBean.getLitpic(), -1, viewHolder.mIvMessagePic);
            viewHolder.mTvContent.setVisibility(View.GONE);
        }
        else{
            viewHolder.mIvMessagePic.setVisibility(View.GONE);
            viewHolder.mTvContent.setText(resultBean.getContent());
        }
        viewHolder.mTvMessageTitle.setText(resultBean.getName());

        viewHolder.mLlDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UrlUtil.showHtmlPage(mContext,"消息详情", RequestURL.MESSAGE_DETAIL_URL + resultBean.getId());
                if(msgType==0){
                    UrlUtil.showHtmlPage(mContext,"精选活动", resultBean.getId());
                }
                else{
                    UrlUtil.showHtmlPage(mContext,"公告详情", RequestURL.NOTICE_DETAILS_URL + resultBean.getId());
                }
            }
        });
        return view;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_time)
        public TextView mTvTime;
        @ViewInject(R.id.iv_message_pic)
        public ImageView mIvMessagePic;
        @ViewInject(R.id.tv_message_title)
        public TextView mTvMessageTitle;
        @ViewInject(R.id.tv_message_content)
        public TextView mTvContent;
        @ViewInject(R.id.ll_detail)
        public LinearLayout mLlDetail;
    }
}
