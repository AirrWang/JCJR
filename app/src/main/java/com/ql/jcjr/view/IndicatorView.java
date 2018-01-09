package com.ql.jcjr.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.IndicatorViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuchao.
 * <p/>
 * 带有指示的view
 * 使用说明：
 * 1.如果直接是静态显示则直接调用setPagerViewList方法即可。
 * 2.如果是需要自动切换的，需要在布局文件中增加设置该can_play属性，
 * 然后在onResume方法中startAutoPlay()，在onPause方法中stopAutoPlay()
 * 3.
 */
public class IndicatorView extends FrameLayout {
    private final int RUNNING = 1;
    private final int STOP = 2;
    private Handler autoHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RUNNING: {
                    break;
                }
                case STOP: {
                    stopAutoPlay();
                    break;
                }
            }
            invalidateAllView();
        }
    };
    private ViewPager viewPager;
    private LinearLayout dotLayout;
    private List<View> pagerViewList;
    private List<ImageView> dotViewList;
    private IndicatorViewPagerAdapter pagerAdapter;
    private ViewPagerChangerListener pagerChangedListener;
    private ImageView dotView;
    private RelativeLayout dotRelativeLayout;
    private Context context;
    private int id;
    private int normalDotRes = R.drawable.dot_cc_16px;
    private int selectedDotRes = R.drawable.dot_24b279_15px;
    private ScheduledExecutorService autoService;
    private int currentAutoIndex = -1;
    private int preState = 0;
    private boolean canAutoPlay = false;
    private int dotLayoutBottomMargin;
    private int dotLayoutTopMargin;
    private int indicatorWidth;
    private int indicatorHeight;

    private boolean isDots_alignParentBottom = false;
    private boolean isDots_alignParentTop = false;
    private boolean isDots_alignParentLeft = false;
    private boolean isDots_alignParentRight = false;

    private int dotsMarginBottom = 0;
    private int dotsMargintop = 0;
    private int dotsMarginLeft = 0;
    private int dotsMarginRight = 0;

    private final int dotLayoutMarginDefRes = R.dimen.dot_layout_margin;
    private AttributeSet attrs;
    private IndicatorOnPageChangeListener pageChangeListener;

    public IndicatorView(Context context) {
        super(context);
        initLayout(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context, attrs);
    }

    private void initLayout(Context context, AttributeSet attrs) {
        this.context = context;
        this.attrs = attrs;
        pagerViewList = new ArrayList<View>();
        dotViewList = new ArrayList<ImageView>();
        //从xml文件中读取设置的dotlayout的属性
        getAttrsFromXml();
        //设置根布局的参数
        LayoutParams rootParams = new LayoutParams(indicatorWidth, indicatorHeight);
        this.setLayoutParams(rootParams);
        //加载ViewPager
        viewPager = new ViewPager(context);
        LayoutParams viewPagerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        viewPager.setLayoutParams(viewPagerParams);
        viewPager.setPageMargin(-50);
        viewPager.setHorizontalFadingEdgeEnabled(true);
        viewPager.setFadingEdgeLength(getResources().getDimensionPixelOffset(R.dimen.dot_offset));
        this.addView(viewPager);
        //将小圆点的最外层的RelativeLayout加入到view中
        dotRelativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams dotOutLayoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);


        dotRelativeLayout.setLayoutParams(dotOutLayoutParams);
        this.addView(dotRelativeLayout);

        //通过设置dotLayout在RelativeLayout的底部位置来控制小圆点的布局
        RelativeLayout.LayoutParams dotLayoutParams = getDotLayoutParams();

        //加载小圆点的布局文件
        dotLayout = new LinearLayout(context);//小圆点外层的布局文件

        dotLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        dotLayout.setLayoutParams(dotLayoutParams);
        dotRelativeLayout.addView(dotLayout);
        //设置ViewPager的Adapter
        pagerChangedListener = new ViewPagerChangerListener();
        viewPager.addOnPageChangeListener(pagerChangedListener);
        pagerAdapter = new IndicatorViewPagerAdapter(pagerViewList);
        viewPager.setAdapter(pagerAdapter);

        setCanAutoPlay(canAutoPlay);
    }

    /**
     * 获取当前view的id
     */
    public int getViewPagerId() {
        return id;
    }

    /**
     * 设置当前view的id
     */
    public void setViewPagerId(int id) {
        this.id = id;
    }

    /**
     * 设置PageChange的监听
     */
    public void setOnPageChangeListener(IndicatorOnPageChangeListener listener) {
        this.pageChangeListener = listener;
    }

    /**
     * 设置viewpager的view集合
     */
    public void setPagerViewList(List<View> list) {
        resetIndicatorView();
        this.pagerViewList.addAll(list);
        //增加小红点
        for (int i = 0; i < pagerViewList.size(); i++) {
            addImageIndicator(i);
        }
        invalidate();
    }

    /**
     * 设置小圆点的背景图片
     */
    public void setDotDrawableRes(int normal, int selected) {
        this.normalDotRes = normal;
        this.selectedDotRes = selected;
    }

    public void setCanAutoPlay(boolean isCan) {
        this.canAutoPlay = isCan;
    }

    /**
     * 更新ViewPager的适配器
     */
    @Override
    public void invalidate() {
        //根据dotviewList的个数确定是不是显示小红点的布局。当只有一个的时候，不加小红点
        if (dotViewList == null || dotViewList.size() < 2) {
            dotLayout.setVisibility(View.GONE);
            this.dotLayoutBottomMargin = 0;
        } else {
            dotLayout.setVisibility(View.VISIBLE);
            getAttrsFromXml();
        }
        ViewGroup parent = (ViewGroup) viewPager.getParent();
        if (parent == null) {
            viewPager = new ViewPager(context);
            this.addView(viewPager, 0);
            viewPager.setOnPageChangeListener(pagerChangedListener);
            viewPager.setAdapter(pagerAdapter);
        }
        pagerAdapter.notifyDataSetChanged();
        super.invalidate();
    }

    /**
     * 从xml文件中读取设置的dotLayout的属性
     */
    private void getAttrsFromXml() {
        if (attrs == null) {
            return;
        }
        TypedArray typed = this.context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        if (typed == null) {
            return;
        }
        if (typed.hasValue(R.styleable.IndicatorView_dotmargin_bottom)) {
            dotLayoutBottomMargin = typed.getDimensionPixelSize(R.styleable.IndicatorView_dotmargin_bottom,
                    (int)getResources().getDimension(R.dimen.dot_margin_bottom));
        }
        if (typed.hasValue(R.styleable.IndicatorView_dotmargin_top)) {
            dotLayoutTopMargin = typed.getDimensionPixelSize(R.styleable.IndicatorView_dotmargin_bottom,
                    (int)getResources().getDimension(R.dimen.dot_margin_bottom));
        }
        if (typed.hasValue(R.styleable.IndicatorView_can_play)) {
            canAutoPlay = typed.getBoolean(R.styleable.IndicatorView_can_play, false);
        }

        if (typed.hasValue(R.styleable.IndicatorView_dots_layout_alignParentBottom)) {
            isDots_alignParentBottom = typed.getBoolean(R.styleable.IndicatorView_dots_layout_alignParentBottom, false);
        }
        if (typed.hasValue(R.styleable.IndicatorView_dots_layout_alignParentTop)) {
            isDots_alignParentTop = typed.getBoolean(R.styleable.IndicatorView_dots_layout_alignParentTop, false);
        }
        if (typed.hasValue(R.styleable.IndicatorView_dots_layout_alignParentLeft)) {
            isDots_alignParentLeft = typed.getBoolean(R.styleable.IndicatorView_dots_layout_alignParentLeft, false);
        }
        if (typed.hasValue(R.styleable.IndicatorView_dots_layout_alignParentRight)) {
            isDots_alignParentRight = typed.getBoolean(R.styleable.IndicatorView_dots_layout_alignParentRight, false);
        }

        if (typed.hasValue(R.styleable.IndicatorView_dots_layout_marginBottom)) {
            dotsMarginBottom = typed.getDimensionPixelSize(R.styleable.IndicatorView_dots_layout_alignParentBottom,
                    (int)getResources().getDimension(R.dimen.dot_margin_bottom));
        }

        if (typed.hasValue(R.styleable.IndicatorView_dots_layout_marginTop)) {
            dotsMargintop = typed.getDimensionPixelSize(R.styleable.IndicatorView_dots_layout_marginTop, 0);
        }
        if (typed.hasValue(R.styleable.IndicatorView_dots_layout_marginLeft)) {
            dotsMarginLeft = typed.getDimensionPixelSize(R.styleable.IndicatorView_dots_layout_marginLeft, 0);
        }
        if (typed.hasValue(R.styleable.IndicatorView_dots_layout_marginRight)) {
            dotsMarginRight = typed.getDimensionPixelSize(R.styleable.IndicatorView_dots_layout_marginRight, 0);
        }

        TypedArray typeAttrs = this.context.obtainStyledAttributes(attrs, new int[]{android.R.attr.layout_height
                , android.R.attr.layout_width});
        indicatorHeight = typeAttrs.getLayoutDimension(0, LayoutParams.WRAP_CONTENT);
        indicatorWidth = typeAttrs.getLayoutDimension(1, LayoutParams.MATCH_PARENT);

    }

    /**
     * 获取小圆点dotLayout布局的布局参数
     */
    private RelativeLayout.LayoutParams getDotLayoutParams() {
        RelativeLayout.LayoutParams dotLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT
                , LayoutParams.WRAP_CONTENT);
        if (isDots_alignParentRight) {
            dotLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            dotLayoutParams.rightMargin = dotsMarginRight;
        }
        if (isDots_alignParentLeft) {
            dotLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            dotLayoutParams.leftMargin = dotsMarginLeft;
        }
        if (!isDots_alignParentLeft & !isDots_alignParentRight) {
            dotLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        }

        if (isDots_alignParentBottom) {
            dotLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            dotLayoutParams.bottomMargin = dotsMarginBottom;
        }
        if (isDots_alignParentTop) {
            dotLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            dotLayoutParams.topMargin = dotsMargintop;
        }

        if (!isDots_alignParentBottom & !isDots_alignParentTop) {
            dotLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            dotLayoutParams.topMargin = dotsMargintop;
        }

        return dotLayoutParams;
    }

    /**
     * 将view进行还原为初始状态
     */
    private void resetIndicatorView() {
        //将所有的小红点移除
        dotLayout.removeAllViews();
        dotViewList.clear();
        //将view的list清空
        pagerViewList.clear();
        //pagerAdapter.notifyDataSetChanged();
        currentAutoIndex = -1;//自动播放的position为初始值
        this.removeView(viewPager);
    }

    /**
     * 设置view为第几页
     */
    public void setCurrentItem(int item) {
        viewPager.setCurrentItem(item, true);
    }

    /**
     * 更新选中的小红点
     */
    private void refreshDotIndicator(int position) {

        if (dotViewList == null || dotViewList.size() < 2) {
            dotLayout.setVisibility(View.GONE);
        } else {
            dotLayout.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < dotViewList.size(); i++) {
            //不是当前选中的pager，其小圆点设置为未选中的状态
            if (position == i) {
                dotViewList.get(i).setImageResource(selectedDotRes);
            } else {
                dotViewList.get(i).setImageResource(normalDotRes);
            }
        }
    }

    /**
     * 通过代码设置dotLayout的下边距
     *
     * @param dotBottom 要求传入的是dimen值
     */
    public void setDotBottomMargin(int dotBottom) {
        this.dotLayoutBottomMargin = dotBottom;
    }

    /**
     * 设置单个小圆点
     */
    private void addImageIndicator(int position) {
        dotView = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,

                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dot_offset);

        if (dotLayoutBottomMargin <= 0) {
            params.bottomMargin = 0;
        } else {
            params.bottomMargin = dotLayoutBottomMargin;
        }
        if (dotLayoutTopMargin <= 0) {
            params.topMargin = 0;
        } else {
            params.topMargin = dotLayoutTopMargin;
        }

        dotView.setLayoutParams(params);
        if (position == 0) {
            dotView.setImageResource(selectedDotRes);
        } else {
            dotView.setImageResource(normalDotRes);
        }
        dotLayout.addView(dotView); //将小圆点加到dotView布局当中

        dotViewList.add(dotView); //将小圆点加到小圆点的Imageview的list中，在进行切换的时候进行设置背景色
    }

    public void startAutoPlay() {
        if (!canAutoPlay) {
            return;
        }
        stopAutoPlay();
        if (autoService == null) {
            autoService = Executors.newSingleThreadScheduledExecutor();
        }
        autoService.scheduleAtFixedRate(new AutoTask(), 2, 2, TimeUnit.SECONDS);
    }

    public void stopAutoPlay() {
        if (autoService != null) {
            autoService.shutdown();
            autoService = null;
        }
    }

    private void invalidateAllView() {
        viewPager.setCurrentItem(currentAutoIndex, true);
        refreshDotIndicator(currentAutoIndex);
        invalidate();
    }

    public class ViewPagerChangerListener implements OnPageChangeListener {

        public ViewPagerChangerListener() {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            currentAutoIndex = position;  //若是手动滑动的时候也要改变自动播放的index
            refreshDotIndicator(position);
        }

        /**
         * @param （0，1，2）.arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
         *                     int SCROLL_STATE_IDLE = 0; SCROLL_STATE_DRAGGING = 1;int SCROLL_STATE_SETTLING = 2;
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING: {  // 正在滑动
                    if (canAutoPlay) {
                        stopAutoPlay();
                    }
                    break;
                }
                case ViewPager.SCROLL_STATE_IDLE: //默示什么都没做
                case ViewPager.SCROLL_STATE_SETTLING: { //默示滑动完毕
                    if (autoService == null && canAutoPlay) {
                        startAutoPlay();
                    }
                    break;
                }
            }
            if (preState == ViewPager.SCROLL_STATE_DRAGGING && state == ViewPager.SCROLL_STATE_IDLE
                    && (currentAutoIndex == 0 || currentAutoIndex == -1)) {
                //滑动到最后一个的时候的监听事件
                if (pageChangeListener != null) {
                    pageChangeListener.onPageScrollBegin();
                    preState = state;
                    return;
                }
                viewPager.setCurrentItem(pagerAdapter.getCount() - 1, true);
            } else if (preState == ViewPager.SCROLL_STATE_DRAGGING && state == ViewPager.SCROLL_STATE_IDLE
                    && currentAutoIndex == (pagerAdapter.getCount() - 1)) {
                //滑动到最后一个的时候的监听事件
                if (pageChangeListener != null) {
                    pageChangeListener.onPageScrollEnd();
                    preState = state;
                    return;
                }
                viewPager.setCurrentItem(0, true);
            }
            preState = state;
        }
    }

    public interface IndicatorOnPageChangeListener {
        //滑动到最后一个的时候的监听事件
        void onPageScrollEnd();

        //滑动到最后一个的时候的监听事件
        void onPageScrollBegin();
    }

    private class AutoTask implements Runnable {
        public void run() {
            if (pagerViewList == null || pagerViewList.size() == 0) {
                return;
            }
            currentAutoIndex = (currentAutoIndex + 1) % pagerViewList.size();
            if (pagerViewList.size() == 1) {
                Message msg = autoHandler.obtainMessage();
                msg.what = STOP;
                autoHandler.sendMessage(msg);
                return;
            }
            Message msg = autoHandler.obtainMessage();
            msg.what = RUNNING;
            autoHandler.sendMessage(msg);
        }
    }

    /**
     * 解决ViewPager子View滑动事件冲突
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /// 通知父控件此事件你就不要拦截了，由我子view自己进行处理即可
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
