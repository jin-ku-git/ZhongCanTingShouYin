package com.youwu.shouyin.ui.order_goods;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youwu.shouyin.BR;
import com.youwu.shouyin.R;
import com.youwu.shouyin.app.AppViewModelFactory;
import com.youwu.shouyin.databinding.ActivityOrderGoodsBinding;
import com.youwu.shouyin.databinding.ActivitySalesDocumentBinding;
import com.youwu.shouyin.ui.main.bean.CommunityBean;
import com.youwu.shouyin.ui.money.SalesDocumentViewModel;
import com.youwu.shouyin.ui.money.adapter.SaleBillAdapter;
import com.youwu.shouyin.ui.money.adapter.SalesAdapter;
import com.youwu.shouyin.ui.money.bean.SaleBillBean;
import com.youwu.shouyin.ui.order_goods.adapter.ApplyGoodsAdapter;
import com.youwu.shouyin.ui.order_goods.adapter.OrderGoodsAdapter;
import com.youwu.shouyin.ui.order_goods.bean.OrderGoodsBean;
import com.youwu.shouyin.ui.order_goods.view.PlaceholderFragment;
import com.youwu.shouyin.utils_view.DividerItemDecorations;
import com.youwu.shouyin.utils_view.RxToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * ????????????
 * 2022/03/28
 */
public class OrderGoodsActivity extends BaseActivity<ActivityOrderGoodsBinding, OrderGoodsViewModel> {


    //????????????recyclerveiw????????????
    private OrderGoodsAdapter mOrderGoodsAdapter;
    //?????????goodsentity?????????????????????????????????
    private ArrayList<OrderGoodsBean> mSaleBillBeans = new ArrayList<>();

    //??????????????????recyclerveiw????????????
    private ApplyGoodsAdapter mApplyGoodsAdapter;
    //?????????goodsentity?????????????????????????????????
    private ArrayList<CommunityBean> mCommunityBeanList = new ArrayList<>();
    int pageNo=1;

    private List<String> tabs = new ArrayList<>();
    Bundle mBundle;

    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_order_goods;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public OrderGoodsViewModel initViewModel() {
        //??????????????????ViewModelFactory?????????ViewModel????????????????????????????????????????????????LoginViewModel(@NonNull Application application)????????????
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(OrderGoodsViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://????????????
                        mBundle = new Bundle();
                        mBundle.putSerializable("mCommunityBeanList", mCommunityBeanList);//????????????
                        startActivity(NewOrderGoodsActivity.class,mBundle);
                        break;
                    case 2:
                        mBundle = new Bundle();
                        mBundle.putSerializable("ShoppingEntityList", mCommunityBeanList);//????????????
                        mBundle.putString("goods_number", viewModel.goods_number.get());//????????????
                        mBundle.putString("goods_type", mCommunityBeanList.size()+"");//????????????
                        startActivity(ConfirmOrderActivity.class,mBundle);
                        break;
                    case 5://?????????
                        RxToast.normal("??????");
                        break;
                }
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();

        for (int i=0;i<10;i++){
            tabs.add("??????"+i);
        }
        viewModel.group_state.set("??????");
        initTab();

        binding.switchBtnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.switchBtnStatus.isChecked()) {
                   viewModel.group_state.set("??????");
//                    binding.switchBtnStatus.setBackColor(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.main_color)));
                } else {
                    viewModel.group_state.set("????????????");
//                    binding.switchBtnStatus.setBackColor(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.color_hui)));

                }
            }
        });

        //??????
        binding.scSmartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSaleBillBeans.clear();
                pageNo=1;
                initczjl();
                refreshLayout.finishRefresh(true);
            }
        });
        //??????
        binding.scSmartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                initczjl();
                refreshLayout.finishLoadMore(true);//????????????
            }
        });
        initczjl();


    }

    /**
     * ??????
     */
    private void initBinding(OrderGoodsBean orderGoodsBean) {

        viewModel.paid_in_prick.set(orderGoodsBean.getPaid_in_prick());//????????????

        viewModel.goods_number.set(orderGoodsBean.getGoods_num());//????????????
    }


    /**
     * ????????????????????????
     */
    private void initczjl() {

        for (int i=0;i<20;i++){
            ArrayList<CommunityBean> communityBeans = new ArrayList<>();
            OrderGoodsBean orderGoodsBean=new OrderGoodsBean();
            orderGoodsBean.setOrder_state("?????????"+i);//????????????
            orderGoodsBean.setOrder_state_num(1);//???????????????
            orderGoodsBean.setGoods_num("1"+i);//????????????
            orderGoodsBean.setCreate_time("2020.03.28 15:17:0"+i);//????????????

            orderGoodsBean.setPaid_in_prick("16"+i);//????????????

            orderGoodsBean.setGoods_list(communityBeans);

            for (int j=0;j<5;j++){
                CommunityBean communityBean=new CommunityBean();
                communityBean.setCom_name(i+"??????"+j);
                communityBean.setCom_number(i+j);
                communityBean.setGoods_purchase_price(i+"2"+j);
                communityBean.setCom_discount_price(i+"3"+j);
                communityBean.setTotal_price(i+"4"+j);
                communityBeans.add(communityBean);
            }
            mSaleBillBeans.add(orderGoodsBean);
        }
        initBinding(mSaleBillBeans.get(0));
        initRecyclerView();

        mCommunityBeanList=mSaleBillBeans.get(0).getGoods_list();
        initRecyclerViewTow();
    }

    /**
     * ????????????
     */
    private void initRecyclerView() {
        //??????adapter
        mOrderGoodsAdapter = new OrderGoodsAdapter(this, mSaleBillBeans);
        //???RecyclerView??????adapter
        binding.orderRecyclerview.setAdapter(mOrderGoodsAdapter);
        //??????layoutManager,?????????????????????????????????????????????grid??????????????????????????????

        //???????????????????????????????????????????????????????????????????????????
        binding.orderRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //??????item????????????
        if (binding.orderRecyclerview.getItemDecorationCount()==0) {
            binding.orderRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mOrderGoodsAdapter.setOnClickListener(new OrderGoodsAdapter.OnClickListener() {
            @Override
            public void onClick(OrderGoodsBean lists, int position) {
                mCommunityBeanList=lists.getGoods_list();
                initBinding(lists);
                initRecyclerViewTow();
            }
        });

    }

    /**
     * ??????????????????
     */
    private void initRecyclerViewTow() {
        //??????adapter
        mApplyGoodsAdapter = new ApplyGoodsAdapter(this, mCommunityBeanList);
        //???RecyclerView??????adapter
        binding.xqRecyclerview.setAdapter(mApplyGoodsAdapter);
        //??????layoutManager,?????????????????????????????????????????????grid??????????????????????????????

        //???????????????????????????????????????????????????????????????????????????
        binding.xqRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //??????item????????????
        if (binding.xqRecyclerview.getItemDecorationCount()==0) {
            binding.xqRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
    }


    //?????????tab
    private void initTab() {

        //??????TabLayout?????????
        binding.tab.setTabMode(TabLayout.MODE_SCROLLABLE);

        binding.viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        //??????ViewPager???TabLayout
        binding.tab.setupWithViewPager(binding.viewPager);


        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //0 ????????? 1 ????????? 2 ?????????
//                order_status= tab.getPosition();
//
//                group_id=tabs.get(order_status).getId();
//                presenter.getDetails(order_id,group_id);
//                RxToast.normal(""+group_id);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class TabAdapter extends FragmentPagerAdapter {
        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(tabs.get(position));
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        //????????????????????????
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }
    }


}
