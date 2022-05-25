package com.youwu.shouyin.data.source;

import com.youwu.shouyin.entity.DemoEntity;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {


    Observable<BaseResponse<DemoEntity>> demoGet();

    Observable<BaseResponse<DemoEntity>> demoPost(String catalog);

    //登录
    Observable<BaseBean<Object>> LOGIN(String name, String password);
    //获取商品群组
    Observable<BaseBean<Object>> GOODS_GROUP(String store_id);
    //获取商品
    Observable<BaseBean<Object>> GOODS_List(String store_id,String group_id);
}
