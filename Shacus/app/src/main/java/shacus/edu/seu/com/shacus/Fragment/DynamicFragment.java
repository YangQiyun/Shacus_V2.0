package shacus.edu.seu.com.shacus.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.List;

import shacus.edu.seu.com.shacus.Data.Manager.DynamicManager;
import shacus.edu.seu.com.shacus.Data.Model.DynamicModel;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.StatusCode;

;

/**
 * Created by Mind on 2017/9/5.
 */
public class DynamicFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener,StatusCode{
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final String TAG = "DynamicFragment";
    private DynamicManager DataManager;
    private int CurrentCounter;//当前加载的item数量
    private int TotalCounter=CurrentCounter;//最终无法再加载item的数量
    private boolean isErr=false; //加载请求是否成功
    private android.os.Handler Handler=new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REFLASH_TREND_SUCCESS:
                    List<DynamicModel> list= (List<DynamicModel>) msg.obj;
                    CurrentCounter=list.size();
                    TotalCounter=CurrentCounter+1;
                    DataManager.getmDynamicAdapter().setNewData(list);
                    mSwipeRefreshLayout.setRefreshing(false);
                    DataManager.getmDynamicAdapter().setEnableLoadMore(true);
                    break;
                case StatusCode.REFLASH_TREND_FAILURE:
                    mSwipeRefreshLayout.setRefreshing(false);
                    Log.d(TAG, "handleMessage: 下拉失败");
                    break;
                case StatusCode.MORE_TREND_SUCCESS:
                    Log.d(TAG, "handleMessage: "+"加载更过");
                    List<DynamicModel> list2= (List<DynamicModel>) msg.obj;
                    CurrentCounter+=list2.size();
                    TotalCounter=CurrentCounter+1;
                    DataManager.getmDynamicAdapter().addData(list2);
                    //DataManager.getmDynamicAdapter().setEnableLoadMore(true);
                    break;
                case StatusCode.MORE_TREND_FAILURE:
                    Log.d(TAG, "handleMessage: 无更多");
                    TotalCounter=CurrentCounter;
                    break;
                case 110:
                case StatusCode.TREND_USRID_FAILURE:
                    isErr=true;
                    break;

            }
            return true;
        }
    });


    public static DynamicFragment newInstance(int value ){
        Bundle args=new Bundle();
        args.putInt(TAG,value);
        DynamicFragment fragment=new DynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView= inflater.inflate(R.layout.fragment_yuepai_dynamic,container,false);
        initView();
        initAdapter();
        CurrentCounter=DataManager.getinitnumber();
        TotalCounter=CurrentCounter+1;
        return mRootView;
    }


    void initView(){
        mSwipeRefreshLayout= findViewById(R.id.swipeLayout);
        mRecyclerView=findViewById(R.id.fgdynamic_recyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRootView.getContext()));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy<0) {//上滑
                    ((yuepaiFragment)(DynamicFragment.this.getParentFragment())).setFloatingActionMenu(View.VISIBLE);
                }
                else{
                    ((yuepaiFragment)(DynamicFragment.this.getParentFragment())).setFloatingActionMenu(View.INVISIBLE);
                }
            }
        });
    }

    void initAdapter(){
        DataManager=new DynamicManager(mActivity,Handler);
        DataManager.getmDynamicAdapter().setOnLoadMoreListener(this,mRecyclerView);
        DataManager.getmDynamicAdapter().openLoadAnimation(BaseQuickAdapter.SCALEIN);

        mRecyclerView.setAdapter(DataManager.getmDynamicAdapter());
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {

            }
        });

    }


    @Override
    public void onRefresh() {
        DataManager.doRefresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }


    //下拉到最后一个item的时候会调用这个函数
    @Override
    public void onLoadMoreRequested() {
      //  DataManager.Loadmore();
        DataManager.getmDynamicAdapter().loadMoreEnd(false);
        //loadMoreEnd() 没有更多数据
        //loadMoreEnd(true) 下面不会又提示
        //loadMoreFail() 加载失败点我重试
        //loadMoreComplete() 正在加载中
//        if(isErr){
//            DataManager.getmDynamicAdapter().loadMoreFail();
//            isErr=false;
//        }
//        else
//        if(CurrentCounter<TotalCounter){
//            DataManager.Loadmore();
//
//        }
//        else {
//            Log.d(TAG, "onLoadMoreRequested: 无更多");
//            DataManager.getmDynamicAdapter().loadMoreEnd();
//        }
    }
}
