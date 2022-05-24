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
 * 申请订货
 * 2022/03/28
 */
public class OrderGoodsActivity extends BaseActivity<ActivityOrderGoodsBinding, OrderGoodsViewModel> {


    //订单记录recyclerveiw的适配器
    private OrderGoodsAdapter mOrderGoodsAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<OrderGoodsBean> mSaleBillBeans = new ArrayList<>();

    //订货记录详情recyclerveiw的适配器
    private ApplyGoodsAdapter mApplyGoodsAdapter;
    //定义以goodsentity实体类为对象的数据集合
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
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(OrderGoodsViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://复用订单
                        mBundle = new Bundle();
                        mBundle.putSerializable("mCommunityBeanList", mCommunityBeanList);//订货列表
                        startActivity(NewOrderGoodsActivity.class,mBundle);
                        break;
                    case 2:
                        mBundle = new Bundle();
                        mBundle.putSerializable("ShoppingEntityList", mCommunityBeanList);//订货列表
                        mBundle.putString("goods_number", viewModel.goods_number.get());//订货数量
                        mBundle.putString("goods_type", mCommunityBeanList.size()+"");//订货种类
                        startActivity(ConfirmOrderActivity.class,mBundle);
                        break;
                    case 5://反结帐
                        RxToast.normal("打印");
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
            tabs.add("测试"+i);
        }
        viewModel.group_state.set("群组");
        initTab();

        binding.switchBtnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.switchBtnStatus.isChecked()) {
                   viewModel.group_state.set("群组");
//                    binding.switchBtnStatus.setBackColor(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.main_color)));
                } else {
                    viewModel.group_state.set("门店群组");
//                    binding.switchBtnStatus.setBackColor(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.color_hui)));

                }
            }
        });

        //刷新
        binding.scSmartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSaleBillBeans.clear();
                pageNo=1;
                initczjl();
                refreshLayout.finishRefresh(true);
            }
        });
        //加载
        binding.scSmartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                initczjl();
                refreshLayout.finishLoadMore(true);//加载完成
            }
        });
        initczjl();


    }

    /**
     * 绑定
     */
    private void initBinding(OrderGoodsBean orderGoodsBean) {

        viewModel.paid_in_prick.set(orderGoodsBean.getPaid_in_prick());//实收金额

        viewModel.goods_number.set(orderGoodsBean.getGoods_num());//商品数量
    }


    /**
     * 模拟充值记录数据
     */
    private void initczjl() {

        for (int i=0;i<20;i++){
            ArrayList<CommunityBean> communityBeans = new ArrayList<>();
            OrderGoodsBean orderGoodsBean=new OrderGoodsBean();
            orderGoodsBean.setOrder_state("待配货"+i);//订单状态
            orderGoodsBean.setOrder_state_num(1);//订单状态码
            orderGoodsBean.setGoods_num("1"+i);//商品价格
            orderGoodsBean.setCreate_time("2020.03.28 15:17:0"+i);//创建时间

            orderGoodsBean.setPaid_in_prick("16"+i);//实收金额

            orderGoodsBean.setGoods_list(communityBeans);

            for (int j=0;j<5;j++){
                CommunityBean communityBean=new CommunityBean();
                communityBean.setCom_name(i+"白菜"+j);
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
     * 门店记录
     */
    private void initRecyclerView() {
        //创建adapter
        mOrderGoodsAdapter = new OrderGoodsAdapter(this, mSaleBillBeans);
        //给RecyclerView设置adapter
        binding.orderRecyclerview.setAdapter(mOrderGoodsAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.orderRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
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
     * 订单详情记录
     */
    private void initRecyclerViewTow() {
        //创建adapter
        mApplyGoodsAdapter = new ApplyGoodsAdapter(this, mCommunityBeanList);
        //给RecyclerView设置adapter
        binding.xqRecyclerview.setAdapter(mApplyGoodsAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.xqRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.xqRecyclerview.getItemDecorationCount()==0) {
            binding.xqRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
    }


    //初始化tab
    private void initTab() {

        //设置TabLayout的模式
        binding.tab.setTabMode(TabLayout.MODE_SCROLLABLE);

        binding.viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        //关联ViewPager和TabLayout
        binding.tab.setupWithViewPager(binding.viewPager);


        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //0 待存货 1 待取货 2 已完成
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

        //显示标签上的文字
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }
    }


}
