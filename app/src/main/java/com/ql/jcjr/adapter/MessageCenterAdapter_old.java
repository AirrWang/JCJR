package com.ql.jcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.MessageCenterEntity;
import com.ql.jcjr.utils.UrlUtil;

import java.util.List;


/**
 * ClassName: CarStoreListAdapter
 * Description:
 * Author: Administrator
 * Date: Created on 202017/6/19.
 */

public class MessageCenterAdapter_old extends BaseAdapter {

    private Context mContext;
    private List<MessageCenterEntity.ResultBean> mList;

    public MessageCenterAdapter_old(Context context, List<MessageCenterEntity.ResultBean> list) {
        mList = list;
        mContext = context;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_center_message_old, null);
            viewHolder = new ViewHolder();
            ViewUtils.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final MessageCenterEntity.ResultBean resultBean = mList.get(i);

        viewHolder.mTvTime.setText(resultBean.getAddtime());
        viewHolder.mTvMessageTitle.setText(resultBean.getName());
        viewHolder.mTvDate.setText(resultBean.getAddtime());
        viewHolder.mTvContent.setText(resultBean.getInstr());

        viewHolder.mLlDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlUtil.showHtmlPage(mContext,"消息详情", RequestURL.MESSAGE_DETAIL_URL + resultBean.getId());
            }
        });
        return view;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_time)
        public TextView mTvTime;
        @ViewInject(R.id.tv_message_title)
        public TextView mTvMessageTitle;
        @ViewInject(R.id.tv_date)
        public TextView mTvDate;
        @ViewInject(R.id.tv_content)
        public TextView mTvContent;
        @ViewInject(R.id.ll_detail)
        public LinearLayout mLlDetail;
    }
}
