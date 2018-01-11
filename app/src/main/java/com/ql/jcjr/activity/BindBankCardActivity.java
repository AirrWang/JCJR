package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.freeme.swipemenu.SwipeMenu;
import com.freeme.swipemenu.SwipeMenuCreator;
import com.freeme.swipemenu.SwipeMenuItem;
import com.freeme.swipemenu.SwipeMenuListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.BankCardListAdapter;
import com.ql.jcjr.adapter.BankListAdapter;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.BankCardData;
import com.ql.jcjr.entity.BankListEntity;
import com.ql.jcjr.entity.BankNameEntity;
import com.ql.jcjr.entity.CheckBankEntity;
import com.ql.jcjr.entity.CityEntity;
import com.ql.jcjr.entity.ProvinceEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.ActionSheet;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.ImageTextHorizontalBar;
import com.ql.jcjr.view.wheelview.lib.ArrayWheelAdapter;
import com.ql.jcjr.view.wheelview.lib.OnWheelScrollListener;
import com.ql.jcjr.view.wheelview.lib.WheelView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BindBankCardActivity extends BaseActivity {

    @ViewInject(R.id.ab_header)
    private ActionBar mActionBar;
    @ViewInject(R.id.ll_first)
    private LinearLayout mLlFirst;
    @ViewInject(R.id.tv_real_name)
    private TextView mTvRealName;
    @ViewInject(R.id.et_card_num)
    private EditText mEtCardNum;
    @ViewInject(R.id.et_bank_branch)
    private EditText mEtBranch;
    @ViewInject(R.id.tv_tel)
    private TextView mTvTel;
    @ViewInject(R.id.et_tel)
    private EditText mEtTel;
    @ViewInject(R.id.ithb_bank)
    private ImageTextHorizontalBar mIthbBank;
    @ViewInject(R.id.ll_bank)
    private LinearLayout mLlBank;
//    @ViewInject(R.id.civ_bank_logo)
//    private CircleImageView mCivBankLogo;
    @ViewInject(R.id.tv_bank_name)
    private TextView mTvBankName;
    @ViewInject(R.id.tv_place)
    private TextView tvPlace;
    @ViewInject(R.id.lv_bank_card)
    private SwipeMenuListView mLvBankCard;

    private Context mContext;
    private String mRealName;
    private String mBankName;
    private String mBankId;

    private String provinceName;
    private String cityName;
    private String provinceId;
    private String cityId = "";

    private WheelView provinceWheelView;
    private WheelView cityWheelView;
    private ArrayWheelAdapter cityAdapter;
    private List<ProvinceEntity.ResultBean> provinceList;
    private ArrayList<String> provinceArrayList = new ArrayList<>();
    private List<CityEntity.ResultBean> cityList;
    private ArrayList<String> cityArrayList = new ArrayList<>();

    //银行列表
    private List<BankListEntity.ResultBean> bankList = new ArrayList<>();

    private List<BankCardData> mBankCardList = new ArrayList<>();
    private BankCardListAdapter mBankCardListAdapter;

    private static final int ID_MENU_DELETE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bank_card);
        mContext = this;
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        mLlFirst.setVisibility(View.GONE);
        mLvBankCard.setVisibility(View.GONE);

        mEtCardNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    //前往判断卡号
                    checkBankName();
                }
            }
        });
//        mRealName = getIntent().getStringExtra("real_name");
//        mTvRealName.setText(mRealName);
        if(UserData.getInstance().getRealName().length()==0){
            CommonToast.showShiMingDialogNoCancel(mContext);
        }
        mTvRealName.setText(UserData.getInstance().getRealName());
//        mTvTel.setText(UserData.getInstance().getPhoneNumber());
//        mEtTel.setText(UserData.getInstance().getPhoneNumber());
        initListView();

        checkBank();

        Map<String, String> datas = new HashMap<String, String>();
        MobclickAgent.onEventValue(this, "bind_card", datas, 3);
    }

    private void initListView() {
        mLvBankCard.setMenuCreator(addMenuItem());
        mBankCardListAdapter = new BankCardListAdapter(mContext, mBankCardList);
        mLvBankCard.setAdapter(mBankCardListAdapter);

        mLvBankCard.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (menu.getMenuItem(index).getId()) {
                    case ID_MENU_DELETE:
                        LogUtil.i("position = " + position);
                        CommonToast.showHintDialog(mContext, "请联系客服人工修改银行卡！");
                        break;
                }
                return false;// false : close the menu; true : not close the menu
            }
        });
    }

    private SwipeMenuCreator addMenuItem() {
        return new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                int itemSize = (int)getResources().getDimension(R.dimen.dimen_136px);
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
                deleteItem.setId(ID_MENU_DELETE);
                deleteItem.setBackground(R.drawable.bg_delete_gradient);
                deleteItem.setWidth(itemSize);
                deleteItem.setTitle("删除");
                deleteItem.setIcon(R.drawable.ic_bank_card_delete);
                menu.addMenuItem(deleteItem);
            }
        };
    }

    private void checkBank() {
        SenderResultModel resultModel = ParamsManager.senderCheckBank();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("是否绑定银行卡 " + responeJson);
                        CheckBankEntity entity = GsonParser.getParsedObj(responeJson, CheckBankEntity.class);
                        CheckBankEntity.ResultBean resultBean = entity.getResult();
                        String status = resultBean.getStatus();
                        switch (status){
                            case Global.STATUS_PASS:
                                setResult(RESULT_OK);
                                mActionBar.setTitle("我的银行卡");
                                BankCardData data = new BankCardData();
                                data.setBankname(resultBean.getBankname());
                                data.setBankno(resultBean.getBankno());
                                data.setBranch(resultBean.getBranch());
                                data.setImgUrl(resultBean.getImgUrl());
                                mBankCardList.add(data);
                                mBankCardListAdapter.notifyDataSetChanged();

                                mLlFirst.setVisibility(View.GONE);
                                mLvBankCard.setVisibility(View.VISIBLE);
                                break;

                            case Global.STATUS_UN_PASS:
                                mActionBar.setTitle("绑定银行卡");
                                mLlFirst.setVisibility(View.VISIBLE);
                                mLvBankCard.setVisibility(View.GONE);
                                //获取银行信息
                                getBankList(false);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("是否绑定银行卡 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    //获取银行列表
    private void getBankList(final boolean showDialog) {
        if(bankList.size()==0){
            SenderResultModel resultModel = ParamsManager.senderGetBankList();

            HttpRequestManager.httpRequestService(resultModel,
                    new HttpSenderController.ViewSenderCallback() {

                        @Override
                        public void onSuccess(String responeJson) {
                            LogUtil.i("获取银行列表 " + responeJson);
                            BankListEntity entity = GsonParser.getParsedObj(responeJson, BankListEntity.class);
                            bankList.addAll(entity.getResult());
                            if(showDialog){
                                showBankListDialog();
                            }
                        }

                        @Override
                        public void onFailure(ResponseEntity entity) {
                            LogUtil.i("获取银行列表 " + entity.errorInfo);
                            CommonToast.showHintDialog(mContext, entity.errorInfo);
                        }

                    }, mContext);
        }
        else{
            if(showDialog){
                showBankListDialog();
            }
        }
    }

    /**
     * 银行列表
     */
    private void showBankListDialog() {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.bank_lilst_dialog, null);

        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);

        ListView lvBank = (ListView) view.findViewById(R.id.lv_bank_list);
        BankListAdapter bankAdapter = new BankListAdapter(mContext, bankList);
        lvBank.setAdapter(bankAdapter);

        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_BOTTOM);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lvBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBankName = bankList.get(position).getName();
                mBankId = bankList.get(position).getId();
//                mIthbBank.setTitleText(bankList.get(position).getName());
//                GlideUtil.displayPic(mContext, bankList.get(position).getImgUrl(), -1, mCivBankLogo);
                mTvBankName.setText(mBankName);
                dialog.dismiss();
            }
        });
    }

    //获取银行名字
    private void checkBankName() {
        LogUtil.i("checkBankName");
        String bankNum = mEtCardNum.getText().toString().trim();
        if(bankNum.length()>0){
            SenderResultModel resultModel = ParamsManager.senderGetBankName(bankNum);

            HttpRequestManager.httpRequestService(resultModel,
                    new HttpSenderController.ViewSenderCallback() {

                        @Override
                        public void onSuccess(String responeJson) {
                            LogUtil.i("获取银行名字 " + responeJson);
                            BankNameEntity entity = GsonParser.getParsedObj(responeJson, BankNameEntity.class);
                            String bankName = entity.getResult().getBank();
                            if(bankName.indexOf("中国") != -1 && !bankName.equals("中国银行")){
                                bankName = bankName.replace("中国","");
                            }
                            if(bankName.indexOf("股份有限公司") != -1){
                                bankName = bankName.replace("股份有限公司","");
                            }
                            LogUtil.i("bankName:"+bankName);
                            //广发银行股份有限公司
                            //平安银行股份有限公司

                            String fullName = null;
                            int length = bankList.size();
                            for(int i=0;i<length;i++){
                                if(bankList.get(i).getName().indexOf(bankName) != -1){
                                    fullName = bankList.get(i).getName();
                                        mBankName = bankList.get(i).getName();
                                        mBankId = bankList.get(i).getId();
//                                        GlideUtil.displayPic(mContext, bankList.get(i).getImgUrl(), -1, mCivBankLogo);
                                        mTvBankName.setText(mBankName);
                                        break;

                                }
                            }
                            LogUtil.i("fullName:"+fullName);
                            if (fullName==null){
                                CommonToast.showHintDialog(mContext, "暂不支持该银行");
                            }
                        }

                        @Override
                        public void onFailure(ResponseEntity entity) {
                            LogUtil.i("获取银行名字 " + entity.errorInfo);
                            CommonToast.showHintDialog(mContext, entity.errorInfo);
                        }

                    }, mContext);
        }
    }

    private boolean checkInfo() {
        if(StringUtils.isBlank(mEtTel.getText().toString().trim())) {
            CommonToast.showHintDialog(mContext,"请输入手机号码！");
            return false;
        }
        if(StringUtils.isBlank(mBankId)) {
            CommonToast.showHintDialog(mContext,"请选择银行！");
            return false;
        }
        if(StringUtils.isBlank(mEtCardNum.getText().toString())) {
            CommonToast.showHintDialog(mContext,"请输入银行卡卡号！");
            return false;
        }
        if(StringUtils.isBlank(mEtBranch.getText().toString())) {
            CommonToast.showHintDialog(mContext,"请输入支行名称！");
            return false;
        }

        return true;
    }

    /**
     * 获取省
     */
    private void getProvince() {
        SenderResultModel resultModel = ParamsManager.senderGetprovince();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("获取省 " + responeJson);
                        ProvinceEntity entity = GsonParser.getParsedObj(responeJson, ProvinceEntity.class);
                        provinceList = entity.getResult();
                        provinceArrayList.clear();
                        for (ProvinceEntity.ResultBean item : provinceList) {
                            provinceArrayList.add(item.getName());
                        }

                        getCity(provinceList.get(0).getId(), true);
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("获取省 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    /**
     * 获取市
     */
    private void getCity(String provinceId , final boolean start) {
        SenderResultModel resultModel = ParamsManager.senderGetCity(provinceId);
        resultModel.isShowLoadding = false;
        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("获取市 " + responeJson);
                        CityEntity cityEntity = GsonParser.getParsedObj(responeJson, CityEntity.class);
                        cityList = cityEntity.getResult();

                        cityArrayList.clear();
                        for (CityEntity.ResultBean item : cityList) {
                            cityArrayList.add(item.getName());
                        }

                        if (start) {
                            showSelectPlaceDialog();
                        } else if (cityWheelView != null) {
                            cityAdapter.setItems(cityArrayList);
                            cityWheelView.setAdapter(cityAdapter);
                        }
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("获取市 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    private void showSelectPlaceDialog() {
        final ActionSheet dialog = new ActionSheet(this, ActionSheet.GRAVITY_BOTTOM);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.select_place_dialog, null);

        provinceWheelView = (WheelView) view.findViewById(R.id.wv_province);
        cityWheelView = (WheelView) view.findViewById(R.id.wv_city);

        provinceWheelView.setAdapter(new ArrayWheelAdapter(provinceArrayList));
        provinceWheelView.addScrollingListener(provinceScrollListener);

        cityAdapter = new ArrayWheelAdapter(cityArrayList);
        cityWheelView.setAdapter(cityAdapter);

        TextView tvSave = (TextView) view.findViewById(R.id.tv_city_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int provinceIndex = provinceWheelView.getCurrentItem();
                int cityIndex = cityWheelView.getCurrentItem();
                provinceName = provinceArrayList.get(provinceIndex);
                provinceId = provinceList.get(provinceIndex).getId();

                if(cityList != null && cityList.size() != 0) {
                    cityName = cityArrayList.get(cityIndex);
                    cityId = cityList.get(cityIndex).getId();
                    tvPlace.setText(provinceName + "，" + cityName);
                }else {
                    tvPlace.setText(provinceName);
                }
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    OnWheelScrollListener provinceScrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int idex = wheel.getCurrentItem();
            String provinceCode = provinceList.get(idex).getId();
            getCity(provinceCode, false);
        }
    };

    //绑定银行卡
    private void bindBankCard(Bundle bundle) {
        SenderResultModel resultModel = ParamsManager.senderBindBank(bundle);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("绑定银行卡 " + responeJson);
                        checkBank();
                }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("绑定银行卡 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    @OnClick({R.id.btn_left, R.id.ithb_bank, R.id.btn_bind, R.id.ll_place, R.id.ll_bank})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.ll_bank:
                if(mEtCardNum.hasFocus()){
//                    mEtCardNum.clearFocus();
                    mEtBranch.requestFocus();
                }
                else{
//                    getBankList(true);
                    Intent intent = new Intent(BindBankCardActivity.this, BankListActivity.class);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.btn_bind:
                if(checkInfo()) {
                    Map<String, String> datas = new HashMap<String, String>();
                    MobclickAgent.onEventValue(this, "kick_bind_card", datas, 4);

                    Bundle bundle = new Bundle();
                    bundle.putString("real_name", UserData.getInstance().getRealName());
                    bundle.putString("card_num", mEtCardNum.getText().toString());
                    bundle.putString("mobile", mEtTel.getText().toString().trim());
                    bundle.putString("bank_id", mBankId);
                    bundle.putString("branch_name", mEtBranch.getText().toString());
                    bundle.putString("province_id", provinceId);
                    bundle.putString("city_id", cityId);
                    bindBankCard(bundle);
                }
                break;
            case R.id.ll_place:
                getProvince();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String id = data.getStringExtra("bank_id");
                String name = data.getStringExtra("bank_name");
                mBankName = name;
                mBankId = id;
                mTvBankName.setText(mBankName);
            }
        }
    }
}
