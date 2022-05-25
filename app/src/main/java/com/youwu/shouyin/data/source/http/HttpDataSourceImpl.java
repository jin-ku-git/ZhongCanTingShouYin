package com.youwu.shouyin.data.source.http;

import com.youwu.shouyin.data.source.HttpDataSource;
import com.youwu.shouyin.data.source.http.service.DemoApiService;
import com.youwu.shouyin.entity.DemoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private DemoApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(DemoApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(DemoApiService apiService) {
        this.apiService = apiService;
    }


    @Override
    public Observable<BaseResponse<DemoEntity>> demoGet() {
        return apiService.demoGet();
    }

    @Override
    public Observable<BaseResponse<DemoEntity>> demoPost(String catalog) {
        return apiService.demoPost(catalog);
    }

    /**
     * 登录
     * @param tel
     * @param password
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> LOGIN(String tel, String password) {
        return apiService.LOGIN(tel,password);
    }
    /**
     * 获取商品群组
     * @param store_id 门店群组
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GOODS_GROUP(String store_id) {
        return apiService.GOODS_GROUP();
    }
    /**
     * 获取商品群组
     * @param store_id 门店群组
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GOODS_List(String store_id,String group_id) {
        return apiService.GOODS_List(store_id,group_id);
    }
}
