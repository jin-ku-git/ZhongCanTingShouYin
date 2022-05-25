package com.youwu.shouyin.ui.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youwu.shouyin.R;
import com.youwu.shouyin.ui.main.bean.CommunityBean;
import com.youwu.shouyin.ui.main.bean.WMOrderBean;
import com.youwu.shouyin.utils_view.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 小程序订单订货适配器
 */
public class WMOrderAdapter extends RecyclerView.Adapter<WMOrderAdapter.myViewHodler> {
    private Context context;
    private List<WMOrderBean> mList;
    private int currentIndex = 0;

    //创建构造函数
    public WMOrderAdapter(Context context, List<WMOrderBean> goodsEntityList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.mList = goodsEntityList;//实体类数据ArrayList
    }

    /**
     * 创建viewhodler，相当于listview中getview中的创建view和viewhodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public myViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建自定义布局
        View itemView = View.inflate(context, R.layout.item_wm_order_layout, null);
        return new myViewHodler(itemView);
    }

    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final myViewHodler holder, @SuppressLint("RecyclerView") final int position) {

        //根据点击位置绑定数据
        WMOrderBean data = mList.get(position);



          /*
         1.把内部RecyclerView的adapter和集合数据通过holder缓存
         2.使内部RecyclerView的adapter提供设置position的方法
         */
        holder.list.clear();
        holder.list.addAll(mList.get(position).getGoods_list());
        if (holder.mRvAdapter == null) {
            holder.mRvAdapter = new CommodityAdapter(context, holder.list, position);
            GridLayoutManager layoutManage = new GridLayoutManager(context, 1);
            holder.rvItemItem.setLayoutManager(layoutManage);
            holder.rvItemItem.addItemDecoration(new GridSpacingItemDecoration(1, 0, false));
            holder.rvItemItem.setAdapter(holder.mRvAdapter);
//            holder.mdianpu_biaoti.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent intent = new Intent(mContext, SearchActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("lay_option", mList.get(position).getTitle());
//                    intent.putExtras(bundle);
//                    mContext.startActivity(intent);
//
//                }
//            });
        } else {
            holder.mRvAdapter.setPosition(position);
            holder.mRvAdapter.notifyDataSetChanged();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentIndex(position);

                if(mClickListener!=null){
                    mClickListener.onClick(mList.get(position),position);
                }
            }
        });


    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        notifyDataSetChanged();
    }

    /**
     * 得到总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mList.size();
    }

    //自定义viewhodler
    class myViewHodler extends RecyclerView.ViewHolder {

        private TextView order_state,order_time;//订单状态，订单时间
        private TextView goods_num;//商品数量
        private View mView;

        RecyclerView rvItemItem;

        LinearLayout mdianpu_biaoti;


        private CommodityAdapter mRvAdapter;
        private List<CommunityBean> list = new ArrayList<>();

        public myViewHodler(View itemView) {
            super(itemView);

            order_state = (TextView) itemView.findViewById(R.id.order_state);
            order_time = (TextView) itemView.findViewById(R.id.order_time);
            goods_num = (TextView) itemView.findViewById(R.id.goods_num);
            rvItemItem = itemView.findViewById(R.id.CommodityRecycle);

            mView = (View) itemView.findViewById(R.id.view);



            //点击事件放在adapter中使用，也可以写个接口在activity中调用
            //方法一：在adapter中设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //可以选择直接在本位置直接写业务处理
                    //Toast.makeText(context,"点击了xxx",Toast.LENGTH_SHORT).show();
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, mList.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });
        }

        public void bindData(WMOrderBean saleBillBean, int position, int currentIndex) {
            if (position == currentIndex) {

                mView.setVisibility(View.VISIBLE);

            } else {

                mView.setVisibility(View.GONE);
            }
//            mItemGoodsName.setText(();
        }

    }


    //删除的监听的回调
    public interface OnClickListener {
        void onClick(WMOrderBean lists, int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        mClickListener = listener;
    }

    private OnClickListener mClickListener;



    /**
     * 设置item的监听事件的接口
     */
    public interface OnItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         *
         * @param view 点击的item的视图
         * @param data 点击的item的数据
         */
        public void OnItemClick(View view, WMOrderBean data, int position);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}