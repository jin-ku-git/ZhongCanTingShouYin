package com.youwu.shouyin.data.source.http.service;

import com.youwu.shouyin.entity.DemoEntity;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by goldze on 2017/6/15.
 */

public interface DemoApiService {
    @GET("action/apiv2/banner?catalog=1")
    Observable<BaseResponse<DemoEntity>> demoGet();

    @FormUrlEncoded
    @POST("action/apiv2/banner")
    Observable<BaseResponse<DemoEntity>> demoPost(@Field("catalog") String catalog);

    /**
     * 登录
     * @param tel 账号
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("auth/login")
    Observable<BaseBean<Object>> LOGIN(@Field("tel") String tel, @Field("password") String password);

    /**
     * 获取群组
     * @param store_id 门店id
     * @return
     */

    @GET("goods/goods_group")
    Observable<BaseBean<Object>> GOODS_GROUP(@Field("store_id") String store_id);

    /**
     * 获取商品
     * @param store_id 门店id
     * @param group_id 群组id
     * @return
     */

    @GET("goods/goods_list")
    Observable<BaseBean<Object>> GOODS_List(@Field("store_id") String store_id,@Field("group_id") String group_id);
}
