package shacus.edu.seu.com.shacus.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import shacus.edu.seu.com.shacus.Activity.ForumActivity;
import shacus.edu.seu.com.shacus.Adapter.ForumListAdapter;
import shacus.edu.seu.com.shacus.Data.Manager.ForumItemManager;
import shacus.edu.seu.com.shacus.Data.Model.ForumItemModel;
import shacus.edu.seu.com.shacus.R;

import static android.widget.Toast.LENGTH_SHORT;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_MINE_FORUM_LIST_ERROR;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_MINE_FORUM_LIST_SUCCESS;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_MINE_MORE_FORUM_LIST_ERROR;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_MINE_MORE_FORUM_LIST_SUCCESS;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.WANT_MINE_FORUM_LIST;

/**
 * Created by Administrator on 2017/9/14.
 */

public class ForumMineFragment extends BaseFragment implements AbsListView.OnScrollListener{
    private ForumItemManager forumItemManager;
    private int maxRecords = 400;
    private boolean isLoading = false;
    private int bootCounter = 0;
    private int model = 0;

    private ListView mineLv;
    private List<ForumItemModel> flist=new ArrayList<ForumItemModel>();
    ForumListAdapter adapter;

    //设置下拉刷新效果
    PullRefreshLayout pullRefreshLayout;

    //设置上拉加载效果
    public View loadmoreView;
    public int last_index;
    public int total_index;

    private Handler mHander = new Handler(new Handler.Callback(){

        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case REQUEST_MINE_FORUM_LIST_SUCCESS:
                    Log.d("handle","mine new");
                    flist= (List<ForumItemModel>) msg.obj;
                    adapter.refresh(flist);
                    adapter.notifyDataSetChanged();
                    pullRefreshLayout.setRefreshing(false);
                    bootCounter=msg.arg1;
                    break;
                case REQUEST_MINE_MORE_FORUM_LIST_SUCCESS:
                    Log.d("handle","mine more");
                    bootCounter+=msg.arg1;
                    List<ForumItemModel> addlist = (List<ForumItemModel>)msg.obj;
                    adapter.add(addlist);
                    adapter.notifyDataSetChanged();
                    loadComplete();//刷新结束
                    break;
                case REQUEST_MINE_FORUM_LIST_ERROR:
                    Toast.makeText(getActivity(),"刷新失败@^@",LENGTH_SHORT).show();
                    break;
                case REQUEST_MINE_MORE_FORUM_LIST_ERROR:
                    Toast.makeText(getActivity(), "加载失败@^@", LENGTH_SHORT).show();
                    break;
            }
            Log.d("Hander","mHander");
            return true;
        }
    });
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_forum_mine, container, false);

        Log.d("in mine fragment","onCreateView");

        mineLv = findViewById(R.id.mineListView);
        forumItemManager = new ForumItemManager(getActivity(),mHander,WANT_MINE_FORUM_LIST);
        adapter = new ForumListAdapter(getActivity(), flist);

        //设置上拉加载效果
        loadmoreView = inflater.inflate(R.layout.load_more, null);//获得刷新视图
        loadmoreView.setVisibility(View.GONE);//设置刷新视图默认情况下是不可见的
        mineLv.addFooterView(loadmoreView,null,false);
        mineLv.setOnScrollListener(this);

        mineLv.setAdapter(adapter);
        //条目点击事件
        //communityLv.setAdapter(forumItemManager.getForumListAdapter());
        mineLv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int listid = adapter.getItem(position).getFCid();
                Intent i = new Intent(getActivity(), ForumActivity.class);
                i.putExtra("listid",listid);
                startActivity(i);
            }
        });

        //设置下拉刷新效果
        pullRefreshLayout = findViewById(R.id.swipeRefreshLayout2);
        // listen refresh event
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                forumItemManager.doRefresh();
            }
        });

        // refresh complete
        pullRefreshLayout.setRefreshing(false);
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
//        pullRefreshLayout.setColorSchemeColors(int []);
//        pullRefreshLayout.setColor(int);

        return mRootView;
    }

    public int getBootCounter() {
        return bootCounter;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(last_index == total_index && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE))
        {
            //表示此时需要显示刷新视图界面进行新数据的加载(要等滑动停止)
            if(!isLoading)
            {
                //不处于加载状态的话对其进行加载
                isLoading = true;
                //设置刷新界面可见
                loadmoreView.setVisibility(View.VISIBLE);
                Log.e("loadmoreViewisLoading","VISIBLE");
                onLoad();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        last_index = firstVisibleItem+visibleItemCount;
        total_index = totalItemCount;
    }

    /**
     * 刷新加载
     */
    public void onLoad()
    {
        try {
            //模拟耗时操作
            Log.e("loadmoreViewonLoading","VISIBLE");
            forumItemManager.Loadmore(flist.get(bootCounter-1).getFCid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 加载完成
     */
    public void loadComplete()
    {
        loadmoreView.setVisibility(View.GONE);//设置刷新界面不可见
        isLoading = false;//设置正在刷新标志位false
        getActivity().invalidateOptionsMenu();
        mineLv.removeFooterView(loadmoreView);//如果是最后一页的话，则将其从ListView中移出
        Toast.makeText(getActivity(), "没有更多啦#>o<#", LENGTH_SHORT).show();
    }
}
