package com.youwu.shouyin.ui.vip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyin.data.DemoRepository;
import com.youwu.shouyin.ui.money.CouponPushActivity;
import com.youwu.shouyin.utils_view.RxToast;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 2022/03/23
 */

public class SouSuoVipViewModel extends BaseViewModel<DemoRepository> {
    //用户名手机号的绑定
    public ObservableField<String> vip_tel = new ObservableField<>("");
    //用户名的绑定
    public ObservableField<String> vip_name = new ObservableField<>("");
    //用户名余额的绑定
    public ObservableField<String> vip_money = new ObservableField<>("");

    //用户名信息展示的绑定
    public ObservableField<Boolean> vip_details_state = new ObservableField<>();

    //用户名余额的绑定
    public ObservableField<Integer> type_state = new ObservableField<>();

    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public SouSuoVipViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
        //从本地取得数据绑定到View层

    }


    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           finish();
        }
    });

    //搜索VIP的点击事件
    public BindingCommand SouSuoOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
            RxToast.normal("搜索手机号:"+vip_tel.get());

        }
    });
    //推送优惠券的点击事件
    public BindingCommand PushDisOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            startActivity(CouponPushActivity.class);

        }
    });
    //充值的点击事件
    public BindingCommand RechargeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);

        }
    });
    //选择会员的点击事件
    public BindingCommand ChoiceOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
//            RxToast.normal("选择会员");

        }
    });



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
