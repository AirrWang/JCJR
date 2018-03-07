/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * 		Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.ql.jcjr.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ql.jcjr.R;

public class XListView extends ListView{

	private RelativeLayout mFooterView;

	public XListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		mFooterView = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.tip_load_all, null);
		int dividerHeight = getDividerHeight();
		int paddingBottom = getPaddingBottom();
		int viewTop = mFooterView.getPaddingTop();
		mFooterView.setPadding(0, viewTop-dividerHeight, 0, viewTop-paddingBottom);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		addFooterView(mFooterView);
		super.setAdapter(adapter);
		removeFooterView(mFooterView);
	}

	public void addFooterView(){
		addFooterView(mFooterView);
	}
	
	public void removeFooterView(){
		if(getFooterViewsCount()>0){
			removeFooterView(mFooterView);
		}
	}


}
