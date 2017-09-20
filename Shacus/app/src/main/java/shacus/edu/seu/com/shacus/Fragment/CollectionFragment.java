package shacus.edu.seu.com.shacus.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.List;

import shacus.edu.seu.com.shacus.Activity.ShowBigimageActivity;
import shacus.edu.seu.com.shacus.Data.Manager.CollectionManager;
import shacus.edu.seu.com.shacus.Data.Model.CollectionModel;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.StatusCode;

;

/**
 * Created by Mind on 2017/9/5.
 */
public class CollectionFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener,StatusCode{
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final String TAG = "DynamicFragment";
    private CollectionManager DataManager;
    private int CurrentCounter;//当前加载的item数量
    private int TotalCounter=CurrentCounter;//最终无法再加载item的数量
    private boolean isErr=false; //加载请求是否成功
    private android.os.Handler Handler=new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REFLASH_COLLECTION_SUCCESS:
                    List<CollectionModel> list= (List<CollectionModel>) msg.obj;
                    DataManager.getCollectionAdapter().setNewData(list);
                    break;
                case StatusCode.REFLASH_COLLECTION_FAILURE:
                    break;
                case 404:
                    Toast.makeText(getContext(),"网络错误",Toast.LENGTH_SHORT);
                    break;
            }
            return true;
        }
    });


    public static CollectionFragment newInstance(int value ){
        Bundle args=new Bundle();
        args.putInt(TAG,value);
        CollectionFragment fragment=new CollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView= inflater.inflate(R.layout.fragment_yuepai_dynamic,container,false);
        initView();
        initAdapter();
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
                    ((yuepaiFragment)(CollectionFragment.this.getParentFragment())).setFloatingActionMenu(View.VISIBLE);
                }
                else{
                    ((yuepaiFragment)(CollectionFragment.this.getParentFragment())).setFloatingActionMenu(View.INVISIBLE);
                }
            }
        });
    }

    void initAdapter(){
        DataManager=new CollectionManager(mActivity,Handler);
        DataManager.getCollectionAdapter().setOnLoadMoreListener(this,mRecyclerView);
        DataManager.getCollectionAdapter().openLoadAnimation(BaseQuickAdapter.SCALEIN);
        final GridLayoutManager manager = new GridLayoutManager(mActivity, 2);
        mRecyclerView.setLayoutManager(manager);
        DataManager.getCollectionAdapter().setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                switch (position % 3) {
                    case 0:
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                }
                return 2;
            }
        });
        mRecyclerView.setAdapter(DataManager.getCollectionAdapter());
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                //showImageViewPager(position, DataManager.getPictureUrlList(), DataManager.getUploadImgUrlList(), "local", "upload");
                Intent intent=new Intent(mActivity,ShowBigimageActivity.class);
                Bundle temp=new Bundle();
                temp.putStringArrayList(ShowBigimageActivity.LIST,DataManager.getCollectionAdapter().getData().get(position).getUClurl());
                intent.putExtras(temp);
                getContext().startActivity(intent);
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
        DataManager.getCollectionAdapter().loadMoreEnd(false);
//        Toast.makeText(mActivity,"加载更多",Toast.LENGTH_LONG);
//        // DataManager.Loadmore();
//        //loadMoreEnd() 没有更多数据
//        //loadMoreEnd(true) 下面不会又提示
//        //loadMoreFail() 加载失败点我重试
//        //loadMoreComplete() 正在加载中
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
