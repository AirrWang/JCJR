package com.ql.jcjr.utils.holder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.ql.jcjr.R;
import com.ql.jcjr.activity.NoviceExclusiveActivity;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.entity.HomeDataEntity;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.StringUtils;

import java.util.List;

/**
 * Created by wf on 2018/6/25.
 */

public class BidShowView implements Holder<HomeDataEntity.ResultBean.ResultBeanTwo.BidBean> {

    private TextView tv_annualized_rate;
    private TextView tv_term;
    private TextView tv_diffrent_bid;
    private TextView tv_limit_people;
    private Button btn_bid;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        View view= LayoutInflater.from(context).inflate(R.layout.item_bid_show,null);
        tv_annualized_rate = (TextView) view.findViewById(R.id.tv_annualized_rate);
        tv_term = (TextView) view.findViewById(R.id.tv_term);
        tv_diffrent_bid = (TextView) view.findViewById(R.id.tv_diffrent_bid);
        tv_limit_people = (TextView) view.findViewById(R.id.tv_limit_people);
        btn_bid = (Button) view.findViewById(R.id.btn_bid);

        return view;
    }

    @Override
    public void UpdateUI(final Context context, int position, final HomeDataEntity.ResultBean.ResultBeanTwo.BidBean data) {
        tv_annualized_rate.setText(data.getApr());
        tv_annualized_rate.setTypeface(JcbApplication.getPingFangBoldTypeFace());
        tv_term.setText(data.getTime_limit_day()+"天");
        tv_diffrent_bid.setText("限购额度");
        int money= data.getMost_account();
        if (money<=10000) {
            tv_limit_people.setText(money + "元");
        }else {
            money=money/10000;
            tv_limit_people.setText(money + "万元");
        }
        tv_limit_people.setTypeface(JcbApplication.getPingFangBoldTypeFace());
        tv_term.setTypeface(JcbApplication.getPingFangBoldTypeFace());
        btn_bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoviceExclusiveActivity.class);
                intent.putExtra("bid_id", data.getId());
                intent.putExtra("bid_title", data.getName());
                context.startActivity(intent);
            }
        });
    }

}
