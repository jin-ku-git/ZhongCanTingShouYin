package com.youwu.shouyin.app;

import android.annotation.SuppressLint;
import android.app.Application;

import com.youwu.shouyin.data.DemoRepository;
import com.youwu.shouyin.ui.handover.HandoverViewModel;
import com.youwu.shouyin.ui.handover.SaleGoodsListViewModel;
import com.youwu.shouyin.ui.login.LoginViewModel;
import com.youwu.shouyin.ui.main.MainViewModel;
import com.youwu.shouyin.ui.money.CouponPushViewModel;
import com.youwu.shouyin.ui.money.SalesDocumentViewModel;
import com.youwu.shouyin.ui.money.CashierViewModel;
import com.youwu.shouyin.ui.money.RechargeRecordViewModel;
import com.youwu.shouyin.ui.money.VipRechargeViewModel;
import com.youwu.shouyin.ui.network.NetWorkViewModel;
import com.youwu.shouyin.ui.order_goods.ConfirmOrderViewModel;
import com.youwu.shouyin.ui.order_goods.NewOrderGoodsViewModel;
import com.youwu.shouyin.ui.order_goods.OrderGoodsViewModel;
import com.youwu.shouyin.ui.order_goods.OrderSettlementViewModel;
import com.youwu.shouyin.ui.set_up.SetUpViewModel;
import com.youwu.shouyin.ui.vip.AddVipViewModel;
import com.youwu.shouyin.ui.vip.SouSuoVipViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by goldze on 2019/3/26.
 */
public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile AppViewModelFactory INSTANCE;
    private final Application mApplication;
    private final DemoRepository mRepository;

    public static AppViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (AppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppViewModelFactory(application, Injection.provideDemoRepository());
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private AppViewModelFactory(Application application, DemoRepository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NetWorkViewModel.class)) {
            return (T) new NetWorkViewModel(mApplication, mRepository);
        } else if (modelClass.isAssignableFrom(LoginViewModel.class)) {//??????
            return (T) new LoginViewModel(mApplication, mRepository);
        } else if (modelClass.isAssignableFrom(MainViewModel.class)) {//??????
            return (T) new MainViewModel(mApplication, mRepository);
        } else if (modelClass.isAssignableFrom(SouSuoVipViewModel.class)) {//??????VIP
            return (T) new SouSuoVipViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(SetUpViewModel.class)) {//????????????
            return (T) new SetUpViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(RechargeRecordViewModel.class)) {//????????????
            return (T) new RechargeRecordViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(AddVipViewModel.class)) {//????????????
            return (T) new AddVipViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(HandoverViewModel.class)) {//?????????
            return (T) new HandoverViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(SaleGoodsListViewModel.class)) {//??????????????????
            return (T) new SaleGoodsListViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(CashierViewModel.class)) {//??????
            return (T) new CashierViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(VipRechargeViewModel.class)) {//????????????
            return (T) new VipRechargeViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(SalesDocumentViewModel.class)) {//????????????
            return (T) new SalesDocumentViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(OrderGoodsViewModel.class)) {//????????????
            return (T) new OrderGoodsViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(NewOrderGoodsViewModel.class)) {//????????????
            return (T) new NewOrderGoodsViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(ConfirmOrderViewModel.class)) {//????????????
            return (T) new ConfirmOrderViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(OrderSettlementViewModel.class)) {//????????????
            return (T) new OrderSettlementViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(CouponPushViewModel.class)) {//???????????????
            return (T) new CouponPushViewModel(mApplication, mRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
