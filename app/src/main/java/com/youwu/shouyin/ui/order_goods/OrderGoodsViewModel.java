package com.youwu.shouyin.ui.order_goods;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyin.data.DemoRepository;
import com.youwu.shouyin.utils_view.RxToast;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 2022/03/30
 */

public class OrderGoodsViewModel extends BaseViewModel<DemoRepository> {


    //反结帐状态的绑定
    public ObservableField<Boolean> check_state = new ObservableField<>();

    //群组还是订单群组的绑定
    public ObservableField<String> group_state = new ObservableField<>("");


    //实收金额的绑定
    public ObservableField<String> paid_in_prick = new ObservableField<>("");

    //商品数量的绑定
    public ObservableField<String> goods_number = new ObservableField<>("");


    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public OrderGoodsViewModel(@NonNull Application application, DemoRepository repository) {
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

    //复用订单的点击事件
    public BindingCommand FYOrderOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);

        }
    });
    //再次提交的点击事件
    public BindingCommand ResubmitOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);

        }
    });
    //修改订单的点击事件
    public BindingCommand UpdateOrderOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
//            RxToast.normal("修改订单");
        }
    });
    //取消订单的点击事件
    public BindingCommand CancelOrderOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("取消订单");
        }
    });

    //新建订单的点击事件
    public BindingCommand NewOrderOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(NewOrderGoodsActivity.class);
        }
    });



    //打印的点击事件
    public BindingCommand DeClosingOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(5);
        }
    });





    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
