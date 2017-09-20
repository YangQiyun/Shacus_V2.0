package shacus.edu.seu.com.shacus.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import shacus.edu.seu.com.shacus.Data.Manager.PlaceholderManager;
import shacus.edu.seu.com.shacus.Data.Model.PhotographerModel;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.StatusCode;

/**
 * Created by Mind on 2017/9/10.
 */
public  class PlaceholderFragment extends android.support.v4.app.Fragment{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_ACTIVITY_TYPE="Activity_TYPE";
    private PlaceholderManager DataManager;
    private ListView listView;
    private SwipeRefreshLayout refreshLayout;
    private View mRootView;
    private LinearLayout invis;
    private int maxRecords = 400;
    private int bootCounter;
    private int last;
    private boolean isloading=false;
    private Handler mHandler=new Handler(new Handler.Callback(){

        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case StatusCode.WANT_TO_YUEPAI_SUCCESS:
                case StatusCode.REQUEST_YUEPAI_GRAPH_LIST_SUCCESS:
                case StatusCode.REQUEST_YUEPAI_MODEL_LIST_SUCCESS:
                    last = 6;
                    DataManager.getPersonAdapter().refresh(DataManager.getYuepailist());
                   // DataManager.getPersonAdapter().notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                    break;
                case StatusCode.REQUEST_YUEPAI_MORE_GRAPH_LIST_SUCCESS:
                case StatusCode.REQUEST_YUEPAI_MORE_MODEL_LIST_SUCCESS:
                    bootCounter+=msg.arg1;
                    last=msg.arg1;
                    List<PhotographerModel> addList;
                    addList= (List<PhotographerModel>) msg.obj;
                    DataManager.getPersonAdapter().add(addList);
                    DataManager.getPersonAdapter().notifyDataSetChanged();
                    isloading=false;
                    break;
            }
            return true;
        }
    });

    //获取activity调用是谁，摄影师招募会代号0，模特招募会代号1,我的订单时代号2

    public static PlaceholderFragment newInstance(int sectionNumber,int activity_type) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_ACTIVITY_TYPE,activity_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView= inflater.inflate(R.layout.fragment_activity_want_be_photograph, container, false);

        initView();
        initData();

        return mRootView;
    }

    private void initView(){
        listView = (ListView) mRootView.findViewById(R.id.person_list);
        refreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setEnabled(false);
        invis = (LinearLayout) mRootView.findViewById(R.id.invis);
    }

    private void initData(){
        DataManager=new PlaceholderManager(getActivity(),mHandler,getArguments().getInt(ARG_SECTION_NUMBER),getArguments().getInt(ARG_ACTIVITY_TYPE));
        listView.setAdapter(DataManager.getPersonAdapter());
        onScrollListener();
        onRefreshListener();
        invis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getPersonAdapter().notifyDataSetChanged();
                listView.setSelection(0);
            }
        });
    }

    private void onScrollListener(){
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                    /*if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        // 判断是否滚动到底部
                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                            doLoadmore();
                            //加载更多功能的代码
                        }
                    }*/
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount > totalItemCount - 2 && totalItemCount < maxRecords){
                    doLoadmore();
                }

                if (firstVisibleItem >= 1) {
                    invis.setVisibility(View.VISIBLE);
                } else {
                    invis.setVisibility(View.GONE);
                }

                if (firstVisibleItem == 0) {
                    refreshLayout.setEnabled(true);
                } else {
                    refreshLayout.setEnabled(false);
                }
            }
        });
    }

    private void doLoadmore(){
        if (bootCounter<6||isloading||DataManager.getPersonAdapter().getCount()==0||last<6)//如果数据小于五说明是初始化，不读加载更多
            return;
        isloading=true;
       DataManager.Loadmore(bootCounter);
    }

    private void onRefreshListener(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                bootCounter = 0;
                DataManager.doRefresh();
            }
        });
    }
}