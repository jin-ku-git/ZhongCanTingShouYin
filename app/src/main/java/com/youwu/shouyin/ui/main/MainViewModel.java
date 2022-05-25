package com.youwu.shouyin.ui.main;

import android.app.Application;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.youwu.shouyin.data.DemoRepository;
import com.youwu.shouyin.ui.main.bean.GroupBean;
import com.youwu.shouyin.ui.vip.SouSuoVipActivity;
import com.youwu.shouyin.utils_view.RxToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

import static com.youwu.shouyin.app.AppApplication.toPrettyFormat;
import static me.goldze.mvvmhabit.base.BaseActivity.finishAllActivity;

/**
 * 2022/03/21
 */

public class MainViewModel extends BaseViewModel<DemoRepository> {


    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    //搜索页面的显示和隐藏
    public ObservableField<Boolean> sou_suo_bool = new ObservableField<>();
    //vip信息的显示和隐藏
    public ObservableField<Boolean> vip_bool = new ObservableField<>();

    //用户名的绑定
    public ObservableField<String> vip_name = new ObservableField<>("");
    //用户名余额的绑定
    public ObservableField<String> vip_money = new ObservableField<>("");
    //应付价格的绑定
    public ObservableField<String> paid_in = new ObservableField<>("");
    //优惠金额的绑定
    public ObservableField<String> discount_prick = new ObservableField<>("");

    //商品群组列表
    public SingleLiveEvent<ArrayList<GroupBean>> groupList = new SingleLiveEvent<>();

    public MainViewModel(@NonNull Application application, DemoRepository repository) {
        super(application,repository);
    }
    //添加会员的点击事件
    public BindingCommand AddVipOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
        }
    });
    //交接班的点击事件
    public BindingCommand HandoverOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
        }
    });
    //销售单据的点击事件
    public BindingCommand SalesDocumentOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(4);
        }
    });
    //申请订货的点击事件
    public BindingCommand OrderGoodsOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(5);
        }
    });
    //更多的点击事件
    public BindingCommand moreOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(6);
        }
    });
    //选择VIP的点击事件
    public BindingCommand choiceVipOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(7);
        }
    });
    //搜索的点击事件
    public BindingCommand SouSuoOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(8);
            sou_suo_bool.set(true);
        }
    });
    //取消搜索的点击事件
    public BindingCommand cancelSouSuoOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(9);
            sou_suo_bool.set(false);
        }
    });

    //清除按钮的点击事件
    public BindingCommand clearOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(10);
        }
    });
    //选择优惠券的点击事件
    public BindingCommand CouponOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(11);
        }
    });
    //点击vip信息的点击事件
    public BindingCommand VipOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(12);
        }
    });

    //收银的点击事件
    public BindingCommand CashierOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(13);
        }
    });

    //订单的点击事件
    public BindingCommand takeFoodOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(14);
        }
    });


    /**
     * 获取群组
     * @param store_id 门店id
     */
    public void getGoodsGroup(String store_id){

        model.GOODS_GROUP(store_id)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> response) {
                        if (response.isOk()){
                            String submitJsonData = new Gson().toJson(response.data);

                            groupList.setValue((ArrayList<GroupBean>) response.data);

                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });
    }

    /**
     * 获取商品
     * @param store_id 门店id
     * @param group_id 群组id
     */
    public void getGoodsGroup(String store_id,String group_id){

        model.GOODS_List(store_id,group_id)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> response) {
                        if (response.isOk()){
                            String submitJsonData = new Gson().toJson(response.data);

                            groupList.setValue((ArrayList<GroupBean>) response.data);

                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });
    }
}
